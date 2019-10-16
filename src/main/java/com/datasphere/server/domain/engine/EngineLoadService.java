/*
 * Copyright 2019, Huahuidata, Inc.
 * DataSphere is licensed under the Mulan PSL v1.
 * You can use this software according to the terms and conditions of the Mulan PSL v1.
 * You may obtain a copy of Mulan PSL v1 at:
 * http://license.coscl.org.cn/MulanPSL
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v1 for more details.
 */

package com.datasphere.server.domain.engine;

import static com.datasphere.engine.datasource.DataSource.DataSourceType.VOLATILITY;
import static com.datasphere.engine.datasource.DataSourceTemporary.LoadStatus.ENABLE;
import static com.datasphere.engine.datasource.DataSourceTemporary.LoadStatus.FAIL;
import static com.datasphere.engine.datasource.Field.FIELD_NAME_CURRENT_TIMESTAMP;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;

import com.datasphere.engine.datasource.DataSource;
import com.datasphere.engine.datasource.DataSourceIngestionException;
import com.datasphere.engine.datasource.DataSourceRepository;
import com.datasphere.engine.datasource.DataSourceTemporary;
import com.datasphere.engine.datasource.DataSourceTemporaryRepository;
import com.datasphere.engine.datasource.Field;
import com.datasphere.engine.datasource.connection.jdbc.JdbcConnectionService;
import com.datasphere.engine.datasource.connections.DataConnection;
import com.datasphere.engine.datasource.ingestion.IngestionInfo;
import com.datasphere.engine.datasource.ingestion.LocalFileIngestionInfo;
import com.datasphere.engine.datasource.ingestion.jdbc.LinkIngestionInfo;
import com.datasphere.server.common.GlobalObjectMapper;
import com.datasphere.server.common.ProgressResponse;
import com.datasphere.server.common.datasource.DataType;
import com.datasphere.server.common.datasource.LogicalType;
import com.datasphere.server.common.fileloader.FileLoaderFactory;
import com.datasphere.server.common.fileloader.FileLoaderProperties;
import com.datasphere.server.domain.engine.model.SegmentMetaDataResponse;
import com.datasphere.server.domain.workbook.configurations.filter.Filter;
import com.datasphere.server.domain.workbook.configurations.format.TemporaryTimeFormat;
import com.datasphere.server.spec.druid.ingestion.BulkLoadSpec;
import com.datasphere.server.spec.druid.ingestion.BulkLoadSpecBuilder;
import com.datasphere.server.util.PolarisUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;

@Component
public class EngineLoadService {

  private static final Logger LOGGER = LoggerFactory.getLogger(EngineLoadService.class);

  public static final String TOPIC_LOAD_PROGRESS = "/topic/datasources/%s/progress";

  @Autowired
  DruidEngineMetaRepository engineMetaRepository;

  @Autowired
  EngineQueryService queryService;

  @Autowired
  DruidEngineRepository engineRepository;

  @Autowired
  DataSourceRepository dataSourceRepository;

  @Autowired
  DataSourceTemporaryRepository temporaryRepository;

  @Autowired
  JdbcConnectionService jdbcConnectionService;

  @Autowired
  SimpMessageSendingOperations messagingTemplate;

  @Autowired
  EngineProperties engineProperties;

  @Autowired
  FileLoaderFactory fileLoaderFactory;

  @Value("${polaris.datasource.ingestion.retries.delay:3}")
  private Long delay;

  @Value("${polaris.datasource.ingestion.retries.maxDelay:60}")
  private Long maxDelay;

  @Value("${polaris.datasource.ingestion.retries.maxDuration:3600}")
  private Long maxDuration;

  /**
   * 엔진내 BulkLoad Timeout 시간, 기본값 10 분
   */
  @Value("${polaris.engine.timeout.bulk:900}")
  Integer timeout;

  @Value("${polaris.load.maxrow:1000000}")
  Integer maxRow;

  public EngineLoadService() {
  }

  public void sendTopic(String topicUri, ProgressResponse progressResponse) {
    LOGGER.debug("Send Progress Topic : {}, {}", topicUri, progressResponse);
    messagingTemplate.convertAndSend(topicUri,
                                     GlobalObjectMapper.writeValueAsString(progressResponse));

  }

  @Transactional
  public DataSourceTemporary load(DataSource dataSource, List<Filter> filters) {
    return load(dataSource, filters, false, null);
  }

  @Transactional
  public DataSourceTemporary load(DataSource dataSource, List<Filter> filters, boolean async, String temporaryId) {

    if (async) { // delay for subscribe time.
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
      }
    }

    String sendTopicUri = String.format(TOPIC_LOAD_PROGRESS, temporaryId);

    //if reserved field name...add postfix
    if(Field.RESERVED_FIELDS.length > 0){
      for(Field field : dataSource.getFields()){
        final String compareFieldName = field.getName();
        boolean reservedFieldMatched = Arrays.stream(Field.RESERVED_FIELDS)
                                             .anyMatch(reserved -> reserved.equals(compareFieldName));

        if(reservedFieldMatched){
          String newFieldName = compareFieldName;
          boolean fieldNameDuplicated = true;
          while(fieldNameDuplicated){
            newFieldName = newFieldName + "_";
            final String compareField = newFieldName;
            fieldNameDuplicated = dataSource.getFields()
                                            .stream()
                                            .anyMatch(legacyField -> legacyField.getName().equals(compareField));
          }
          field.setName(newFieldName);
          // #1920 Change buildspec column to originalname
          field.setOriginalName(newFieldName);
        }
      }
    }

    boolean isVoatile = false;

    if (StringUtils.isEmpty(dataSource.getId()) && dataSource.getDsType() == VOLATILITY) {
      isVoatile = true;
      dataSourceRepository.saveAndFlush(dataSource);
    }

    // temporary 가 기존에 존재하는지 체크
    DataSourceTemporary temporary = null;
    if (StringUtils.isEmpty(temporaryId)) {
      temporaryId = PolarisUtils.randomUUID(DataSourceTemporary.ID_PREFIX, false);
    } else {
      temporary = temporaryRepository.findById(temporaryId).get();
      if (temporary != null && temporary.getStatus() == ENABLE) {
        temporary.reloadExpiredTime();
        return temporaryRepository.save(temporary);
      }
    }

    // 엔진에서 사용할 데이터 소스 이름 지정
    String engineName = null;
    if (temporary == null) {
      engineName = dataSource.getEngineName() + '_' + PolarisUtils.randomString(5);
    } else {
      engineName = temporary.getName();
    }

    LOGGER.debug("Start to load temporary datasource.");

    String tempFile = "";
    sendTopic(sendTopicUri, new ProgressResponse(0, "START_LOAD_TEMP_DATASOURCE"));

    // Validate timestamp role of fields
    validateTimestampRole(dataSource);

    IngestionInfo info = dataSource.getIngestionInfo();
    Integer expired = null;
    if(info instanceof LocalFileIngestionInfo){
      LocalFileIngestionInfo localFileIngestionInfo = (LocalFileIngestionInfo) info;
      //set expired 600 sec
      expired = 600;

      tempFile = localFileIngestionInfo.getPath();
      LOGGER.debug("load file : {}", tempFile);

    } else if(info instanceof LinkIngestionInfo){

      // JDBC 로 부터 File 을 Import 함
      Preconditions.checkNotNull(info);
      expired = ((LinkIngestionInfo) info).getExpired();
      DataConnection connection = Preconditions.checkNotNull(dataSource.getJdbcConnectionForIngestion(), "Connection info. required");

      List<String> tempResultFile;
      sendTopic(sendTopicUri, new ProgressResponse(5, "PROGRESS_GET_DATA_FROM_LINK_DATASOURCE"));
      try {
        tempResultFile = jdbcConnectionService
                .selectQueryToCsv(connection,
                        (LinkIngestionInfo) info,
                        engineProperties.getQuery().getLocalBaseDir(),
                        engineName,
                        dataSource.getFields(), filters, maxRow);
      } catch (Exception e) {
        sendTopic(sendTopicUri, new ProgressResponse(-1, "FAIL_TO_LOAD_LINK_DATASOURCE"));
        throw new DataSourceIngestionException("Fail to create temporary file : " + e.getMessage());
      }

      if (CollectionUtils.isEmpty(tempResultFile)) {
        sendTopic(sendTopicUri, new ProgressResponse(-1, "FAIL_TO_LOAD_LINK_DATASOURCE"));
        throw new DataSourceIngestionException("Fail to create temporary file ");
      }

      tempFile = tempResultFile.get(0);
    }


    LOGGER.debug("Created result file from source : {}", tempFile);

    // Send Remote Broker
    String remoteFile = copyLocalToRemoteBroker(tempFile);
    LOGGER.debug("Send result file to broker : {}", remoteFile);

    sendTopic(sendTopicUri, new ProgressResponse(10, "COMPLETE_GET_DATA_FROM_LINK_DATASOURCE"));

    // Bulk load from result file.
    Map<String, Object> paramMap = Maps.newHashMap();
    paramMap.put("async", async);
    paramMap.put("temporary", false);

    Map<String, Object> properties = Maps.newHashMap();
    properties.put("assertLoaded", true);
    properties.put("waitTimeout", 10000);

    BulkLoadSpec spec = new BulkLoadSpecBuilder(dataSource)
        .name(engineName)
        .path(Lists.newArrayList(remoteFile))
        .tuningConfig(info.getTuningOptions())
        .properties(properties)
        .build();

    String specStr = GlobalObjectMapper.writeValueAsString(spec);

    LOGGER.info("Start to load to druid, async: {}, Spec: {}", async, specStr);
    Map<String, Object> result = engineRepository.load(specStr, paramMap, Map.class)
                                                 .orElseThrow(() -> new DataSourceIngestionException("Result empty"));

    LOGGER.info("Successfully load. Result is {}", result);

    if (temporary == null) {
      temporary =
          new DataSourceTemporary(temporaryId, engineName, dataSource.getId(),
                                  (String) result.get("queryId"), expired,
                                  GlobalObjectMapper.writeListValueAsString(filters, Filter.class),
                                  isVoatile, async);
    }

    temporaryRepository.saveAndFlush(temporary);

    SegmentMetaDataResponse segmentMetaDataResponse = checkExistLoadDataSource(engineName);

    if (segmentMetaDataResponse == null) {
      temporary.setStatus(FAIL);
      sendTopic(sendTopicUri, new ProgressResponse(-1, "TIMEOUT_LOAD_TEMP_DATASOURCE"));
      throw new DataSourceIngestionException("An error occurred while registering the temporary data source");
    }

    temporary.setStatus(ENABLE);

    // Reset from the moment the loading is completed
    temporary.reloadExpiredTime();

    sendTopic(sendTopicUri, new ProgressResponse(100, "COMPLETE_LOAD_TEMP_DATASOURCE"));

    return temporaryRepository.saveAndFlush(temporary);

  }

  public void validateTimestampRole(DataSource dataSource) {
    // Check Timestamp Field
    if (!dataSource.existTimestampField()) {
      // find timestamp field in datasource
      List<Field> timeFields = dataSource.getFields().stream()
                                         .filter(field -> field.getLogicalType() == LogicalType.TIMESTAMP)
                                         .collect(Collectors.toList());

    // Time Field Processing
	// If there is no timestamp type field, it passes current timestamp,
	// If there is, select the first timestamp type field
	  Field timeField;
      if (timeFields == null || timeFields.size() == 0) {
        timeField = new Field(FIELD_NAME_CURRENT_TIMESTAMP, DataType.TIMESTAMP, Field.FieldRole.TIMESTAMP, 0L);
        timeField.setFormat(GlobalObjectMapper.writeValueAsString(new TemporaryTimeFormat()));
        dataSource.addField(timeField);
      } else {
        timeField = timeFields.get(0);
        timeField.setRole(Field.FieldRole.TIMESTAMP);
        timeField.setSeq(0L);
      }
    }
  }

  public void deleteLoadDataSource(String datasourceName) {
    engineMetaRepository.purgeDataSource(datasourceName);
  }

  public List<String> findLoadDataSourceNames() {
    return engineMetaRepository.findLoadDataSources();
  }

  public List<DataSourceTemporary> findTemporaryByDataSources(String dataSourceId) {

    List<DataSourceTemporary> temporaries = temporaryRepository.findAll();
    if (CollectionUtils.isEmpty(temporaries)) {
      return temporaries;
    }

    final List<String> listDataSource = findLoadDataSourceNames();

    return temporaries.stream()
                      .filter(temp -> listDataSource.contains(temp.getName()))
                      .collect(Collectors.toList());
  }

  public SegmentMetaDataResponse checkExistLoadDataSource(String dataSourceName) {
    // @formatter:off
    RetryPolicy retryPolicy = new RetryPolicy()
        .retryOn(ResourceAccessException.class)
        .retryOn(Exception.class)
        .retryIf(segmentResult -> segmentResult == null)
        .withBackoff(delay, maxDelay, TimeUnit.SECONDS)
        .withMaxDuration(maxDuration, TimeUnit.SECONDS);
		// @formatter:on

    Callable<SegmentMetaDataResponse> callable = () -> queryService.segmentMetadata(dataSourceName);

    // @formatter:off
    SegmentMetaDataResponse segmentMetaDataResponse = Failsafe.with(retryPolicy)
            .onRetriesExceeded((o, throwable) -> {
              throw new DataSourceIngestionException("Retries exceed for checking temporary datasource : " + dataSourceName);
            })
            .onComplete((o, throwable, ctx) -> {
              if(ctx != null) {
                LOGGER.debug("Completed checking temporary datasource({}). {} tries. Take time {} seconds.", dataSourceName, ctx.getExecutions(), ctx.getElapsedTime().toSeconds());
              }
            })
            .get(callable);
    // @formatter:on

    return segmentMetaDataResponse;
  }

  /**
   * Broker node 에 위치한 서버에 파일 업로드, Local 일 경우 Empty
   *
   * @return Dedicated worker host, if success.
   */
  private String copyLocalToRemoteBroker(String uploadFileName) {

    FileLoaderProperties properties = engineProperties.getQuery().getLoader();
    if (properties == null) {
      return uploadFileName;
    }

    List<String> remotePaths = fileLoaderFactory.put(properties, uploadFileName);

    return remotePaths.get(0);

  }
}
