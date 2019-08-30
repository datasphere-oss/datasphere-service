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

package com.datasphere.server.datasource;

import static com.datasphere.server.datasource.DataSource.ConnectionType.ENGINE;
import static com.datasphere.server.datasource.DataSource.SourceType.FILE;
import static com.datasphere.server.datasource.DataSource.Status.PREPARING;
import static com.datasphere.server.datasource.DataSourceErrorCodes.INGESTION_COMMON_ERROR;
import static com.datasphere.server.datasource.DataSourceErrorCodes.INGESTION_ENGINE_GET_TASK_LOG_ERROR;
import static com.datasphere.server.datasource.DataSourceTemporary.ID_PREFIX;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.datasphere.engine.common.exception.MetatronException;
import com.datasphere.server.common.CommonLocalVariable;
import com.datasphere.server.common.MetatronProperties;
import com.datasphere.server.common.criteria.ListCriterion;
import com.datasphere.server.common.criteria.ListFilter;
import com.datasphere.server.common.datasource.DataType;
import com.datasphere.server.common.datasource.LogicalType;
import com.datasphere.server.common.entity.SearchParamValidator;
import com.datasphere.server.common.exception.BadRequestException;
import com.datasphere.server.common.exception.ResourceNotFoundException;
import com.datasphere.server.datasource.connection.jdbc.JdbcConnectionService;
import com.datasphere.server.datasource.data.DataSourceValidator;
import com.datasphere.server.datasource.data.SearchQueryRequest;
import com.datasphere.server.datasource.data.result.ObjectResultFormat;
import com.datasphere.server.datasource.format.DateTimeFormatChecker;
import com.datasphere.server.datasource.ingestion.IngestionDataResultResponse;
import com.datasphere.server.datasource.ingestion.IngestionHistory;
import com.datasphere.server.datasource.ingestion.IngestionHistoryRepository;
import com.datasphere.server.datasource.ingestion.IngestionInfo;
import com.datasphere.server.datasource.ingestion.IngestionOption;
import com.datasphere.server.datasource.ingestion.IngestionOptionProjections;
import com.datasphere.server.datasource.ingestion.IngestionOptionService;
import com.datasphere.server.datasource.ingestion.LocalFileIngestionInfo;
import com.datasphere.server.datasource.ingestion.ReingestionRequest;
import com.datasphere.server.datasource.ingestion.job.IngestionJobRunner;
import com.datasphere.server.domain.CollectionPatch;
import com.datasphere.server.domain.dataconnection.DataConnectionRepository;
import com.datasphere.server.domain.engine.EngineIngestionService;
import com.datasphere.server.domain.engine.EngineLoadService;
import com.datasphere.server.domain.engine.EngineQueryService;
import com.datasphere.server.domain.engine.model.SegmentMetaDataResponse;
import com.datasphere.server.domain.mdm.MetadataService;
import com.datasphere.server.domain.workbench.WorkbenchProperties;
import com.datasphere.server.domain.workbook.configurations.Limit;
import com.datasphere.server.domain.workbook.configurations.datasource.DefaultDataSource;
import com.datasphere.server.domain.workbook.configurations.filter.Filter;
import com.datasphere.server.domain.workbook.configurations.filter.IntervalFilter;
import com.datasphere.server.util.AuthUtils;
import com.datasphere.server.util.CommonsCsvProcessor;
import com.datasphere.server.util.ExcelProcessor;
import com.datasphere.server.util.PolarisUtils;
import com.datasphere.server.util.ProjectionUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.univocity.parsers.common.TextParsingException;

@RepositoryRestController
public class DataSourceController {

  private static Logger LOGGER = LoggerFactory.getLogger(DataSourceController.class);

  @Autowired
  DataSourceService dataSourceService;

  @Autowired
  MetadataService metadataService;

  @Autowired
  JdbcConnectionService jdbcConnectionService;

  @Autowired
  EngineLoadService engineLoadService;

  @Autowired
  EngineIngestionService engineIngestionService;

  @Autowired
  EngineQueryService engineQueryService;

  @Autowired
  IngestionOptionService ingestionOptionService;

  @Autowired
  DataSourceValidator dataSourceValidator;

  @Autowired
  IngestionJobRunner jobRunner;

  @Autowired
  IngestionHistoryRepository ingestionHistoryRepository;

  @Autowired
  DataSourceRepository dataSourceRepository;

  @Autowired
  DataSourceTemporaryRepository temporaryRepository;

  @Autowired
  DataConnectionRepository dataConnectionRepository;

  @Autowired
  PagedResourcesAssembler pagedResourcesAssembler;

  @Autowired
  ProjectionFactory projectionFactory;

  @Autowired
  DateTimeFormatChecker dateTimeFormatChecker;

  @Autowired
  WorkbenchProperties workbenchProperties;

  @Autowired
  MetatronProperties metatronProperties;

  DataSourceProjections dataSourceProjections = new DataSourceProjections();

  public DataSourceController() {
  }

  /**
   * Create temporary data sources based on stored Linked data source information
   *
   * @param filters Essential Filter Information Specified
   * @param async   Asynchronous Processing
   */
  @RequestMapping(value = "/datasources/{dataSourceId}/temporary", method = RequestMethod.POST)
  public ResponseEntity<?> createDataSourceTemporary(@PathVariable("dataSourceId") String dataSourceId,
                                                     @RequestBody(required = false) List<Filter> filters,
                                                     @RequestParam(value = "async", required = false) boolean async) {

    DataSource dataSource = dataSourceRepository.findByIdIncludeConnection(dataSourceId);
    if (dataSource == null) {
      throw new ResourceNotFoundException(dataSourceId);
    }

    if (dataSource.getConnType() != DataSource.ConnectionType.LINK) {
      throw new BadRequestException("Invalid connection type. Only use 'LINK' type.");
    }

    // Check the filter information to see if you have created a temporary data source
    List<DataSourceTemporary> temporaries = dataSourceService.getMatchedTemporaries(dataSourceId, filters);

    String tempoaryId = null;
    if (CollectionUtils.isNotEmpty(temporaries)) {
      DataSourceTemporary temporary = temporaries.get(0);
      LOGGER.info("Already created temporary datasource : {}", temporary.getName());
      if (temporary.getStatus() == DataSourceTemporary.LoadStatus.ENABLE) {
        // Return information after rebalancing the Expired Time
        temporary.setNextExpireTime(DateTime.now().plusSeconds(temporary.getExpired()));
        temporaryRepository.save(temporary);

        return ResponseEntity.ok(temporary);

      } else if (temporary.getStatus() == DataSourceTemporary.LoadStatus.PREPARING) {
        // Return after passing information about progress
        Map<String, Object> responseMap = Maps.newHashMap();
        responseMap.put("id", temporary.getId());
        responseMap.put("progressTopic", String.format(EngineLoadService.TOPIC_LOAD_PROGRESS, temporary.getId()));

        return ResponseEntity.ok(responseMap);

      } else if (temporary.getStatus() == DataSourceTemporary.LoadStatus.DISABLE) {
        // Reload process
        tempoaryId = temporary.getId();
      }
    }

    return handleTemporaryDatasource(dataSource, tempoaryId, filters, async);
  }

  /**
   * Create a temporary data source with the specified data source information. (Used by WorkBench)
   */
  @RequestMapping(value = "/datasources/temporary", method = RequestMethod.POST)
  public ResponseEntity<?> createDataSourceTemporary(@RequestBody Resource<DataSource> dataSourceResource,
                                                     @RequestParam(value = "async", required = false) boolean async) {

    DataSource dataSource = dataSourceResource.getContent();

    Preconditions.checkNotNull(dataSource.getIngestionInfo(),
                               "Required ingestion information.");

    String csvBaseDir = workbenchProperties.getTempCSVPath();
    if (!csvBaseDir.endsWith(File.separator)) {
      csvBaseDir = csvBaseDir + File.separator;
    }

    LocalFileIngestionInfo ingestionInfo = (LocalFileIngestionInfo) dataSource.getIngestionInfo();
    ingestionInfo.setPath(csvBaseDir + ingestionInfo.getPath());

    return handleTemporaryDatasource(dataSource, null, null, async);
  }

  /**
   * Query list of temporary data sources created through Link data source information
   */
  @RequestMapping(value = "/datasources/{dataSourceId}/temporaries/{temporaryId}/reload", method = RequestMethod.POST)
  public ResponseEntity<?> reloadTemporaryDataSources(@PathVariable("dataSourceId") String dataSourceId,
                                                      @PathVariable("temporaryId") String temporaryId,
                                                      @RequestParam(value = "async", required = false) boolean async) {

    DataSource dataSource = dataSourceRepository.findById(dataSourceId).get();
    if (dataSource == null) {
      throw new ResourceNotFoundException(dataSourceId);
    }

    if (dataSource.getConnType() != DataSource.ConnectionType.LINK) {
      throw new BadRequestException("Invalid connection type. Only use 'LINK' type.");
    }

    DataSourceTemporary temporary = temporaryRepository.findById(temporaryId).get();
    if (temporary == null || dataSourceId.equals(temporary.getDataSourceId())) {
      throw new ResourceNotFoundException(temporaryId);
    }

    return handleTemporaryDatasource(dataSource, temporary.getId(), temporary.getFilterList(), async);
  }

  /**
   * Create a temporary data source.
   */
  private ResponseEntity<?> handleTemporaryDatasource(DataSource dataSource, String temporaryId, List<Filter> filters, boolean async) {
    // Generate Temporary ID
    final String tempTargetId = StringUtils.isNotEmpty(temporaryId) ?
        temporaryId : PolarisUtils.randomUUID(ID_PREFIX, false);

    if (async) {
      LOGGER.debug("Start async process : {}", temporaryId);
      ThreadFactory factory = new ThreadFactoryBuilder()
          .setNameFormat("BulkLoad-" + temporaryId + "-%s")
          .setDaemon(true)
          .build();

      // FIXME: Consider changing to a dedicated thread pool!
      ExecutorService service = Executors.newSingleThreadExecutor(factory);
      service.submit(() ->
                         engineLoadService.load(dataSource, filters, async, tempTargetId)
      );

      Map<String, Object> responseMap = Maps.newHashMap();
      responseMap.put("id", tempTargetId);
      responseMap.put("progressTopic", String.format(EngineLoadService.TOPIC_LOAD_PROGRESS, tempTargetId));

      return ResponseEntity.created(URI.create("")).body(responseMap);
    } else {
      return ResponseEntity.created(URI.create(""))
                           .body(engineLoadService.load(dataSource, filters, async, tempTargetId));
    }
  }

  /**
   * Get the details of a data source. If a temporary data source exists
 * @throws Exception 
   */
  @Transactional(readOnly = true)
  @RequestMapping(value = "/datasources/{dataSourceId}", method = RequestMethod.GET)
  public ResponseEntity<?> findDataSources(@PathVariable("dataSourceId") String dataSourceId,
                                           @RequestParam(value = "includeUnloadedField", required = false) Boolean includeUnloadedField,
                                           PersistentEntityResourceAssembler resourceAssembler) throws Exception {

    DataSource resultDataSource = dataSourceService.findDataSourceIncludeTemporary(dataSourceId, includeUnloadedField);

    return ResponseEntity.ok(resourceAssembler.toResource(resultDataSource));
  }

  @Transactional(readOnly = true)
  @RequestMapping(value = "/datasources/{ids}/multiple", method = RequestMethod.GET)
  public ResponseEntity<?> findMultipleDataSources(@PathVariable("ids") List<String> ids,
                                                   @RequestParam(value = "includeUnloadedField", required = false) Boolean includeUnloadedField,
                                                   @RequestParam(value = "projection", required = false, defaultValue = "default") String projection) throws Exception {

    List results = dataSourceService.findMultipleDataSourceIncludeTemporary(ids, includeUnloadedField);

    return ResponseEntity.ok(ProjectionUtils.toListResource(projectionFactory,
                                                            dataSourceProjections.getProjectionByName(projection),
                                                            results));
  }

  /**
   * Query list of temporary data sources created through Link data source information
   */
  @RequestMapping(value = "/datasources/{dataSourceId}/temporaries", method = RequestMethod.GET)
  public ResponseEntity<?> findTemporaryDataSources(@PathVariable("dataSourceId") String dataSourceId) {

    DataSource dataSource = dataSourceRepository.findById(dataSourceId).get();
    if (dataSource == null) {
      throw new ResourceNotFoundException(dataSourceId);
    }

    if (dataSource.getConnType() != DataSource.ConnectionType.LINK) {
      throw new BadRequestException("Invalid connection type. Only use 'LINK' type.");
    }

    return ResponseEntity.ok(engineLoadService.findTemporaryByDataSources(dataSourceId));
  }


  /**
   * Query the list of data sources.
   *
   * @param type       DataSourceType
   * @param connection ConnectionType
   * @param source     SourceType
   * @param status     Status
   */
  @RequestMapping(value = "/datasources", method = RequestMethod.GET)
  public ResponseEntity<?> findDataSources(@RequestParam(value = "dsType", required = false) String type,
                                           @RequestParam(value = "connType", required = false) String connection,
                                           @RequestParam(value = "srcType", required = false) String source,
                                           @RequestParam(value = "status", required = false) String status,
                                           @RequestParam(value = "published", required = false) Boolean published,
                                           @RequestParam(value = "nameContains", required = false) String nameContains,
                                           @RequestParam(value = "linkedMetadata", required = false) Boolean linkedMetadata,
                                           @RequestParam(value = "searchDateBy", required = false) String searchDateBy,
                                           @RequestParam(value = "from", required = false)
                                           @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) DateTime from,
                                           @RequestParam(value = "to", required = false)
                                           @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) DateTime to,
                                           Pageable pageable, PersistentEntityResourceAssembler resourceAssembler) {

    // Validate DataSourceType
    DataSource.DataSourceType dataSourceType = SearchParamValidator
        .enumUpperValue(DataSource.DataSourceType.class, type, "dsType");

    // Validate ConnectionType
    DataSource.ConnectionType connectionType = SearchParamValidator
        .enumUpperValue(DataSource.ConnectionType.class, connection, "connType");

    // Validate SourceType
    DataSource.SourceType sourceType = SearchParamValidator
        .enumUpperValue(DataSource.SourceType.class, source, "srcType");

    // Validate status
    DataSource.Status statusType = SearchParamValidator
        .enumUpperValue(DataSource.Status.class, status, "status");

    // Validate searchByTime
    SearchParamValidator.range(searchDateBy, from, to);

    // Get Predicate
    //    Predicate searchPredicated = DataSourcePredicate
    //        .searchList(dataSourceType, connectionType, sourceType, statusType,
    //                    published, nameContains, searchDateBy, from, to);

    // Default sort condition settings
    if (pageable.getSort() == null || !pageable.getSort().iterator().hasNext()) {
      pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                                 new Sort(Sort.Direction.ASC, "createdTime", "name"));
    }

    // Find by predicated
    // Page<DataSource> dataSources = dataSourceRepository.findAll(searchPredicated, pageable);
    Page<DataSource> dataSources = dataSourceRepository.findDataSources(dataSourceType, connectionType, sourceType, statusType,
                                                                        published, nameContains, linkedMetadata, searchDateBy, from, to, pageable);

    return ResponseEntity.ok(this.pagedResourcesAssembler.toResource(dataSources, resourceAssembler));
  }

  /**
   * Perform data queries in data sources, mainly in NoteBook modules
   */
  @RequestMapping(path = "/datasources/{id}/data", method = RequestMethod.POST)
  public @ResponseBody
  ResponseEntity<?> getDataFromDatasource(@PathVariable("id") String id,
                                          @RequestParam(value = "limit", required = false) Integer limit,
                                          @RequestParam(value = "intervals", required = false) List<String> intervals,
                                          @RequestBody(required = false) SearchQueryRequest request) {

    DataSource dataSource = dataSourceRepository.findById(id).get();
    if (dataSource == null) {
      throw new ResourceNotFoundException(id);
    }

    if (request == null) {
      request = new SearchQueryRequest();
    }

    DefaultDataSource defaultDataSource = new DefaultDataSource(dataSource.getEngineName());
    defaultDataSource.setMetaDataSource(dataSource);

    if (request.getResultFormat() == null) {
      ObjectResultFormat resultFormat = new ObjectResultFormat();
      resultFormat.setRequest(request);
      request.setResultFormat(resultFormat);
    }
    request.setDataSource(defaultDataSource);

    if (CollectionUtils.isEmpty(request.getProjections())) {
      request.setProjections(new ArrayList<>());
    }

    // Data limit processing can be checked up to 1 million cases
    if (request.getLimits() == null) {
      if (limit == null) {
        limit = 1000;
      } else if (limit > 1000000) {
        limit = 1000000;
      }
      request.setLimits(new Limit(limit));
    }

    if (CollectionUtils.isNotEmpty(intervals)) {
      List<Field> timestampFields = dataSource.getFieldByRole(Field.FieldRole.TIMESTAMP);

      IntervalFilter intervalFilter = new IntervalFilter(timestampFields.get(0).getName(), intervals);
      request.addFilters(intervalFilter);
    }

    return ResponseEntity.ok(engineQueryService.search(request));
  }

  /**
   * Add data in data source
   */
  @RequestMapping(path = "/datasources/{id}/data", method = {RequestMethod.PATCH, RequestMethod.PUT})
  public @ResponseBody
  ResponseEntity<?> appendDataSource(
      @PathVariable("id") String id,
      @RequestParam(value = "once", required = false) Boolean singleMode,
      @RequestBody IngestionInfo ingestionInfo) {

    DataSource dataSource = dataSourceRepository.findById(id).get();
    if (dataSource == null) {
      throw new ResourceNotFoundException(id);
    }

    // TODO: Restrictions required if restrictions are required compared to existing loading operations
    dataSource.setIngestionInfo(ingestionInfo);

    // Existing Loading Work ShutDown
    if (BooleanUtils.isTrue(singleMode)) {
      engineIngestionService.shutDownIngestionTask(dataSource.getId());
    }

    ThreadFactory factory = new ThreadFactoryBuilder()
        .setNameFormat("ingestion-append-" + dataSource.getId() + "-%s")
        .setDaemon(true)
        .build();
    ExecutorService service = Executors.newSingleThreadExecutor(factory);
    service.submit(() -> jobRunner.ingestion(dataSource));

    return ResponseEntity.noContent().build();

  }


  /**
   * Modifies field information in the data source.
   */
  @RequestMapping(path = "/datasources/{id}/fields", method = RequestMethod.PATCH)
  public @ResponseBody
  ResponseEntity<?> patchFieldsInDataSource(
      @PathVariable("id") String id, @RequestBody List<CollectionPatch> patches) {

    DataSource dataSource = dataSourceRepository.findById(id).get();
    if (dataSource == null) {
      throw new ResourceNotFoundException(id);
    }

    Map<Long, Field> fieldMap = dataSource.getFieldMap();
    for (CollectionPatch patch : patches) {
      Long fieldId = patch.getLongValue("id");
      switch (patch.getOp()) {
        case ADD:
          dataSource.getFields().add(new Field(patch));
          LOGGER.debug("Add field in datasource({})", dataSource.getId());
          break;
        case REPLACE:
          if (fieldMap.containsKey(fieldId)) {
            Field field = fieldMap.get(fieldId);
            field.updateField(patch);
            LOGGER.debug("Updated field in datasource({}) : {}", dataSource.getId(), fieldId);
          }
          break;
        case REMOVE:
          if (fieldMap.containsKey(fieldId)) {
            dataSource.getFields().remove(fieldMap.get(fieldId));
            LOGGER.debug("Deleted field in datasource({}) : {}", dataSource.getId(), fieldId);
          }
          break;
        default:
          break;
      }
    }

    dataSourceRepository.saveAndFlush(dataSource);
    metadataService.updateFromDataSource(dataSource, true);

    return ResponseEntity.noContent().build();
  }

  /**
   * Synchronize and add values ​​that exist in the Engine Datasource but do not exist in the Metatron Datasource field.
   */
  @RequestMapping(path = "/datasources/{id}/fields/sync", method = RequestMethod.PATCH)
  public ResponseEntity<?> synchronizeFieldsInDataSource(@PathVariable("id") final String id) {
    final DataSource dataSource = dataSourceRepository.findById(id).get();
    if (dataSource == null) {
      throw new ResourceNotFoundException(id);
    }

    List<Field> candidateFields = getCandidateFieldsFromEngine(dataSource.getEngineName());
    dataSource.synchronizeFields(candidateFields);
    dataSourceRepository.save(dataSource);

    return ResponseEntity.noContent().build();
  }

  private List<Field> getCandidateFieldsFromEngine(String engineName) {
    SegmentMetaDataResponse segmentMetaData = engineQueryService.segmentMetadata(engineName);

    if (segmentMetaData == null || segmentMetaData.getColumns() == null) {
      return new ArrayList<>();
    } else {
      return segmentMetaData.getColumns().entrySet().stream()
                            .filter(entry -> ((entry.getKey().equals("__time") || entry.getKey().equals("count")) == false))
                            .map(entry -> {
                              SegmentMetaDataResponse.ColumnInfo value = entry.getValue();

                              Field field = new Field();
                              field.setName(entry.getKey());
                              field.setType(value.getType().startsWith("dimension.") ? DataType.STRING : DataType.INTEGER);
                              field.setRole(value.getType().startsWith("dimension.") ? Field.FieldRole.DIMENSION : Field.FieldRole.MEASURE);

                              return field;
                            })
                            .collect(Collectors.toList());
    }
  }

  /**
   * Register the data source loaded on the engine, not through metatron.
   */
  @RequestMapping(value = "/datasources/import/{engineSourceName}", method = RequestMethod.POST)
  public ResponseEntity<?> importEngineDataSources(@PathVariable("engineSourceName") String engineName,
                                                   @RequestBody(required = false) DataSource dataSource) {

    DataSource importedDataSource = dataSourceService
        .importEngineDataSource(engineName, dataSource == null ? new DataSource() : dataSource);

    return ResponseEntity.created(URI.create("")).body(importedDataSource);
  }

  /**
   * Passing list of unregistered data sources within the engine
   */
  @RequestMapping(value = "/datasources/import/datasources", method = RequestMethod.GET)
  public ResponseEntity<?> importAvailableEngineDataSources() {

    return ResponseEntity.ok(dataSourceService.findImportAvailableEngineDataSource());
  }

  /**
   * Check the schema information of the data source loaded into the engine.
   */
  @RequestMapping(value = "/datasources/import/{engineSourceName}/preview", method = RequestMethod.GET)
  public ResponseEntity<?> findEngineDataSourcesInfo(@PathVariable("engineSourceName") String engineName,
                                                     @RequestParam(value = "withData", required = false) boolean withData,
                                                     @RequestParam(value = "limit", required = false) Integer limit) {

    Map<String, Object> resultMap = Maps.newLinkedHashMap();

    SegmentMetaDataResponse segmentMetaData = engineQueryService.segmentMetadata(engineName);
    List<Field> convertedField = segmentMetaData.getConvertedField(null);
    segmentMetaData.setFields(convertedField);

    resultMap.put("meta", segmentMetaData);

    if (withData) {
      if (limit == null || limit < 0) {
        limit = 100;
      }
      SearchQueryRequest queryRequest = new SearchQueryRequest();
      DefaultDataSource dataSource = new DefaultDataSource(engineName);
      dataSource.setMetaDataSource(new DataSource("Raw Data",
                                                  convertedField.toArray(new Field[segmentMetaData.getFields().size()])));

      queryRequest.setDataSource(dataSource);
      queryRequest.setLimits(new Limit(limit));
      queryRequest.setResultFormat(new ObjectResultFormat(DataSource.ConnectionType.ENGINE));

      resultMap.put("data", engineQueryService.preview(queryRequest));
    }

    return ResponseEntity.ok(resultMap);
  }

  /**
   * Get list of ingestion history by status
   */
  @RequestMapping(value = {"/datasources/{id}/ingestion/histories", "/datasources/{id}/histories"},
      method = RequestMethod.GET)
  public ResponseEntity<?> findIngestionHistorys(@PathVariable("id") String dataSourceId,
                                                 @RequestParam(name = "status", required = false) String status,
                                                 Pageable pageable) {

    IngestionHistory.IngestionStatus ingestionStatus = SearchParamValidator.enumUpperValue(
        IngestionHistory.IngestionStatus.class, status, "status");

    if (dataSourceRepository.findById(dataSourceId) == null) {
      return ResponseEntity.notFound().build();
    }

    Page<IngestionHistory> results;
    if (ingestionStatus == null) {
      results = ingestionHistoryRepository.findByDataSourceIdOrderByModifiedTimeDesc(dataSourceId, pageable);
    } else {
      results = ingestionHistoryRepository.findByDataSourceIdAndStatusOrderByModifiedTimeDesc(dataSourceId, ingestionStatus, pageable);
    }

    return ResponseEntity.ok(pagedResourcesAssembler.toResource(results));
  }

  /**
   * Get ingestion history
   */
  @RequestMapping(value = "/datasources/{dataSourceId}/histories/{historyId}", method = RequestMethod.GET)
  public ResponseEntity<?> findIngestionHistory(@PathVariable("dataSourceId") String dataSourceId,
                                                @PathVariable("historyId") Long historyId) {

    if (dataSourceRepository.findById(dataSourceId) == null) {
      throw new ResourceNotFoundException(dataSourceId);
    }

    IngestionHistory history = ingestionHistoryRepository.findById(historyId).get();
    if (history == null) {
      throw new ResourceNotFoundException(historyId);
    }

    return ResponseEntity.ok(history);
  }

  /**
   * Get ingestion log (= engine task log)
   */
  @RequestMapping(value = "/datasources/{dataSourceId}/histories/{historyId}/log", method = RequestMethod.GET)
  public ResponseEntity<?> findIngestionHistoryLog(@PathVariable("dataSourceId") String dataSourceId,
                                                   @PathVariable("historyId") Long historyId,
                                                   @RequestParam(value = "offset", required = false) Integer offset) {

    if (dataSourceRepository.findById(dataSourceId) == null) {
      throw new ResourceNotFoundException(dataSourceId);
    }

    IngestionHistory history = ingestionHistoryRepository.findById(historyId).get();
    if (history == null) {
      throw new ResourceNotFoundException(historyId);
    }

    String taskId = history.getIngestionId();

    EngineIngestionService.EngineTaskLog taskLog;
    try {
      taskLog = engineIngestionService.getIngestionTaskLog(taskId, offset);
    } catch (MetatronException e) {
      throw new DataSourceIngestionException(INGESTION_ENGINE_GET_TASK_LOG_ERROR, "Task log on engine not founded", e);
    } catch (Exception e) {
      throw new DataSourceIngestionException(INGESTION_COMMON_ERROR, e);
    }

    return ResponseEntity.ok(taskLog);
  }

  /**
   * Stop ingestion task
   */
  @RequestMapping(value = "/datasources/{dataSourceId}/histories/{historyId}/stop", method = RequestMethod.POST)
  public ResponseEntity<?> stopIngestionJob(@PathVariable("dataSourceId") String dataSourceId,
                                            @PathVariable("historyId") Long historyId) {

    if (dataSourceRepository.findById(dataSourceId) == null) {
      throw new ResourceNotFoundException(dataSourceId);
    }

    IngestionHistory history = ingestionHistoryRepository.findById(historyId).get();
    if (history == null) {
      throw new ResourceNotFoundException(historyId);
    }

    boolean result = engineIngestionService.shutDownIngestionTask(history);
    Map<String, Object> results = Maps.newLinkedHashMap();
    results.put("result", result);

    return ResponseEntity.ok(results);
  }

  /**
   * Reset (real time) ingestion task
   */
  @RequestMapping(value = "/datasources/{dataSourceId}/histories/{historyId}/reset", method = RequestMethod.POST)
  public ResponseEntity<?> resetRealTimeIngestionJob(@PathVariable("dataSourceId") String dataSourceId,
                                                     @PathVariable("historyId") Long historyId) {

    if (dataSourceRepository.findById(dataSourceId) == null) {
      throw new ResourceNotFoundException(dataSourceId);
    }

    IngestionHistory history = ingestionHistoryRepository.findById(historyId).get();
    if (history == null) {
      throw new ResourceNotFoundException(historyId);
    }

    boolean result = engineIngestionService.resetRealTimeIngestionTask(history);
    Map<String, Object> results = Maps.newLinkedHashMap();
    results.put("result", result);

    return ResponseEntity.ok(results);
  }

  /**
   * Get list fo option for datasource ingestion
   */
  @RequestMapping(value = "/datasources/ingestion/options", method = RequestMethod.GET)
  public ResponseEntity<?> findIngestionOptions(@RequestParam(value = "type", required = false) String type,
                                                @RequestParam(value = "ingestionType", required = false) String ingestionType) {

    // Validate type
    IngestionOption.OptionType optionEnumType = SearchParamValidator
        .enumUpperValue(IngestionOption.OptionType.class, type, "type");

    // Validate ingestionType
    IngestionOption.IngestionType ingestionEnumType = SearchParamValidator
        .enumUpperValue(IngestionOption.IngestionType.class, ingestionType, "ingestionType");

    List resultOptions = ingestionOptionService.findIngestionOptions(optionEnumType, ingestionEnumType);

    return ResponseEntity.ok(ProjectionUtils.toListResource(projectionFactory,
                                                            IngestionOptionProjections.DefaultProjection.class,
                                                            resultOptions));
  }


  /**
   * Check the validity of the datetime format.
   */
  @RequestMapping(value = "/datasources/validation/datetime", method = RequestMethod.POST)
  public ResponseEntity<?> checkDateTimeFormat(@RequestBody TimeFormatCheckRequest request) {

    if (StringUtils.isEmpty(request.getFormat())) {
      return ResponseEntity.ok(new TimeFormatCheckResponse(dateTimeFormatChecker.findPattern(request)));
    } else {
      return ResponseEntity.ok(new TimeFormatCheckResponse(dateTimeFormatChecker.checkTimeFormat(request)));
    }

  }

  /**
   * Check the validity of a cron expression
   */
  @RequestMapping(value = "/datasources/validation/cron", method = RequestMethod.POST)
  public ResponseEntity<?> checkCronExpression(@RequestParam String expr,
                                               @RequestParam(value = "timeZone", required = false, defaultValue = "UTC") String timeZone,
                                               @RequestParam(value = "count", required = false, defaultValue = "5") int count) {

    CronExpression cronExpression = null;
    try {
      cronExpression = new CronExpression(expr);
    } catch (ParseException e) {
      return ResponseEntity.ok(new CronValidationResponse(false, e.getMessage()));
    }

    DateTimeZone zone = DateTimeZone.forID(timeZone);

    cronExpression.setTimeZone(zone.toTimeZone());
    LOGGER.debug("Timezone summary : {}", cronExpression.getExpressionSummary());

    List<String> afterTimes = Lists.newArrayList();
    DateTime time = DateTime.now();
    for (int i = 0; i < count; i++) {
      time = new DateTime(cronExpression.getNextValidTimeAfter(time.toDate()));
      afterTimes.add(time.withZone(zone).toString());
    }

    return ResponseEntity.ok(new CronValidationResponse(true, afterTimes));
  }

  /**
   * Check the validity of a cron expression
   */
  @RequestMapping(value = "/datasources/validation/wkt", method = RequestMethod.POST)
  public ResponseEntity<?> checkWktType(@RequestBody WktCheckRequest wktCheckRequest) {

    LogicalType type = wktCheckRequest.getGeoType();

    WKTReader wktReader = new WKTReader();
    Geometry geometry = null;

    boolean valid = true;
    String message = null;
    LogicalType suggestType = null;
    for (String value : wktCheckRequest.getValues()) {
      try {
        geometry = wktReader.read(value);

        LogicalType parsedType = findGeoType(geometry, type).orElseThrow(
            () -> new RuntimeException("ERROR_NOT_SUPPORT_WKT_TYPE")
        );

        if (type != parsedType) {
          suggestType = parsedType;
          throw new RuntimeException("ERROR_NOT_MATCHED_WKT_TYPE");
        }

      } catch (org.locationtech.jts.io.ParseException e) {
        LOGGER.debug("WKT Parse error ({}), Invalid WKT String : {}", e.getMessage(), value);
        valid = false;
        message = "ERROR_PARSE_WKT";
        break;
      } catch (Exception ex) {
        LOGGER.debug("WKT validation error ({}), Invalid WKT String : {}", ex.getMessage(), value);
        valid = false;
        message = ex.getMessage();
        break;
      }
    }

    Map<String, Object> resultResponse = Maps.newLinkedHashMap();
    resultResponse.put("valid", valid);
    resultResponse.put("suggestType", suggestType);
    resultResponse.put("message", message);

    return ResponseEntity.ok(resultResponse);
  }

  private Optional<LogicalType> findGeoType(Geometry geometry, LogicalType type) {

    Class<? extends Geometry> c = geometry.getClass();
    LogicalType logicalType = null;
    if (c.equals(Point.class)) {
      logicalType = LogicalType.GEO_POINT;
    } else if (c.equals(LineString.class)) {
      logicalType = LogicalType.GEO_LINE;
    } else if (c.equals(Polygon.class)) {
      logicalType = LogicalType.GEO_POLYGON;
    } else if (c.equals(MultiLineString.class)) {
      if (type != null && type == LogicalType.GEO_POLYGON) { // It can be a polygon.
        logicalType = LogicalType.GEO_POLYGON;
      } else {
        logicalType = LogicalType.GEO_LINE;
      }
    } else if (c.equals(MultiPolygon.class)) {
      logicalType = LogicalType.GEO_POLYGON;
    }

    return Optional.ofNullable(logicalType);
  }

  @RequestMapping(value = "/datasources/duration/{duration}", method = RequestMethod.GET, produces = "application/json")
  public ResponseEntity<?> test(@PathVariable("duration") Long duration) {

    CommonLocalVariable.setQueryId("test-query");
    try {
      Thread.sleep(duration * 1000L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    return ResponseEntity.ok(duration + " OK!");

  }

  /**
   * Excel file upload
   *
   * @return Upload success, file key, sheet name list
   */
  @RequestMapping(value = "/datasources/file/upload", method = RequestMethod.POST, produces = "application/json")
  public
  @ResponseBody
  ResponseEntity<?> uploadFileForIngestion(@RequestParam("file") MultipartFile file) {

    String fileName = file.getOriginalFilename();

    String extensionType = FilenameUtils.getExtension(fileName).toLowerCase();

    if (StringUtils.isEmpty(extensionType) || !extensionType.matches("xlsx|xls|csv")) {
      throw new BadRequestException("Not supported file type : " + extensionType);
    }

    String tempFileName = "TEMP_FILE_" + UUID.randomUUID().toString() + "." + extensionType;
    String tempFilePath = System.getProperty("java.io.tmpdir") + File.separator + tempFileName;

    Map<String, Object> responseMap = Maps.newHashMap();
    responseMap.put("filekey", tempFileName);
    responseMap.put("filePath", tempFilePath);

    try {
      File tempFile = new File(tempFilePath);
      file.transferTo(tempFile);

      if ("xlsx".equals(extensionType) || "xls".equals(extensionType)) {
        responseMap.put("sheets", new ExcelProcessor(tempFile).getSheetNames());
      }
    } catch (IOException e) {
      LOGGER.error("Failed to upload file : {}", e.getMessage());
      throw new DataSourceIngestionException("Fail to upload file.", e.getCause());
    }

    return ResponseEntity.ok(responseMap);
  }

  /**
   * View uploaded file sheet contents
   */
  @RequestMapping(value = "/datasources/file/{fileKey}/data", method = RequestMethod.GET, produces = "application/json")
  public @ResponseBody
  ResponseEntity<?> getPreviewFromFile(@PathVariable(value = "fileKey") String fileKey,
                                       @RequestParam(value = "sheet", required = false) String sheetName,
                                       @RequestParam(value = "lineSep", required = false, defaultValue = "\n") String lineSep,
                                       @RequestParam(value = "delimiter", required = false, defaultValue = ",") String delimiter,
                                       @RequestParam(value = "limit", required = false, defaultValue = "100") int limit,
                                       @RequestParam(value = "firstHeaderRow", required = false, defaultValue = "true") boolean firstHeaderRow) {

    IngestionDataResultResponse resultResponse = null;

    try {
      String filePath = System.getProperty("java.io.tmpdir") + File.separator + fileKey;
      File tempFile = new File(filePath);
      String extensionType = FilenameUtils.getExtension(fileKey);

      if (!tempFile.exists()) {
        throw new BadRequestException("Invalid temporary file name.");
      }

      if ("xlsx".equals(extensionType) || "xls".equals(extensionType)) {
        resultResponse = new ExcelProcessor(tempFile).getSheetData(sheetName, limit, firstHeaderRow);
      } else if ("csv".equals(extensionType)) {
        CommonsCsvProcessor commonsCsvProcessor = new CommonsCsvProcessor("file://" + tempFile)
            .totalCount()
            .maxRowCount(Integer.valueOf(limit).longValue())
            .withHeader(firstHeaderRow)
            .parse(delimiter);

        resultResponse = commonsCsvProcessor.ingestionDataResultResponse();

        //        CsvProcessor csvProcessor = new CsvProcessor(tempFile);
        //        csvProcessor.setCsvMaxCharsPerColumn(metatronProperties.getCsvMaxCharsPerColumn());
        //        resultResponse = csvProcessor.getData(lineSep, delimiter, limit, firstHeaderRow);
      } else {
        throw new BadRequestException("Invalid temporary file.");
      }

    } catch (TextParsingException e) {
      LOGGER.error("Failed to parse csv file ({}) : {}", fileKey, e.getMessage());
      throw new DataSourceIngestionException("Fail to parse csv file. \n" +
                                                 "Line Index : " + e.getLineIndex() + ",\n" +
                                                 "Column Index : " + e.getColumnIndex() + ",\n" +
                                                 "Char Index : " + e.getCharIndex()
          , e.getCause());
    } catch (Exception e) {
      LOGGER.error("Failed to parse file ({}) : {}", fileKey, e.getMessage());
      throw new DataSourceIngestionException("Fail to parse file.", e.getCause());
    }

    return ResponseEntity.ok(resultResponse);
  }

  @RequestMapping(value = "/datasources/filter", method = RequestMethod.POST)
  public @ResponseBody
  ResponseEntity<?> filterDataSources(@RequestBody DataSourceFilterRequest request,
                                      Pageable pageable,
                                      PersistentEntityResourceAssembler resourceAssembler) {

    List<String> statuses = request == null ? null : request.getStatus();
    List<String> workspaces = request == null ? null : request.getWorkspace();
    List<String> createdBys = request == null ? null : request.getCreatedBy();
    List<String> userGroups = request == null ? null : request.getUserGroup();
    List<String> dataSourceTypes = request == null ? null : request.getDataSourceType();
    List<String> sourceTypes = request == null ? null : request.getSourceType();
    List<String> connectionTypes = request == null ? null : request.getConnectionType();
    DateTime createdTimeFrom = request == null ? null : request.getCreatedTimeFrom();
    DateTime createdTimeTo = request == null ? null : request.getCreatedTimeTo();
    DateTime modifiedTimeFrom = request == null ? null : request.getModifiedTimeFrom();
    DateTime modifiedTimeTo = request == null ? null : request.getModifiedTimeTo();
    String containsText = request == null ? null : request.getContainsText();
    List<Boolean> published = request == null ? null : request.getPublished();

    LOGGER.debug("Parameter (status) : {}", statuses);
    LOGGER.debug("Parameter (workspace) : {}", workspaces);
    LOGGER.debug("Parameter (createdBy) : {}", createdBys);
    LOGGER.debug("Parameter (userGroup) : {}", userGroups);
    LOGGER.debug("Parameter (dataSourceType) : {}", dataSourceTypes);
    LOGGER.debug("Parameter (sourceType) : {}", sourceTypes);
    LOGGER.debug("Parameter (connectionType) : {}", connectionTypes);
    LOGGER.debug("Parameter (createdTimeFrom) : {}", createdTimeFrom);
    LOGGER.debug("Parameter (createdTimeTo) : {}", createdTimeTo);
    LOGGER.debug("Parameter (modifiedTimeFrom) : {}", modifiedTimeFrom);
    LOGGER.debug("Parameter (modifiedTimeTo) : {}", modifiedTimeTo);
    LOGGER.debug("Parameter (containsText) : {}", containsText);
    LOGGER.debug("Parameter (published) : {}", published);


    // Validate status
    List<DataSource.Status> statusEnumList
        = request.getEnumList(statuses, DataSource.Status.class, "status");

    // Validate DataSourceType
    List<DataSource.DataSourceType> dataSourceTypeEnumList
        = request.getEnumList(dataSourceTypes, DataSource.DataSourceType.class, "dataSourceType");

    // Validate ConnectionType
    List<DataSource.ConnectionType> connectionTypeEnumList
        = request.getEnumList(connectionTypes, DataSource.ConnectionType.class, "connectionType");

    // Validate SourceType
    List<DataSource.SourceType> sourceTypeEnumList
        = request.getEnumList(sourceTypes, DataSource.SourceType.class, "sourceType");

    // Validate createdTimeFrom, createdTimeTo
    SearchParamValidator.range(null, createdTimeFrom, createdTimeTo);

    // Validate modifiedTimeFrom, modifiedTimeTo
    SearchParamValidator.range(null, modifiedTimeFrom, modifiedTimeTo);

    // Setting the bone alignment condition
    if (pageable.getSort() == null || !pageable.getSort().iterator().hasNext()) {
      pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                                 new Sort(Sort.Direction.DESC, "createdTime", "name"));
    }

    Page<DataSource> dataSources = dataSourceService.findDataSourceListByFilter(
        statusEnumList, workspaces, createdBys, userGroups, createdTimeFrom, createdTimeTo, modifiedTimeFrom,
        modifiedTimeTo, containsText, dataSourceTypeEnumList, sourceTypeEnumList, connectionTypeEnumList, published, pageable
    );

    return ResponseEntity.ok(this.pagedResourcesAssembler.toResource(dataSources, resourceAssembler));
  }

  @RequestMapping(value = "/datasources/criteria", method = RequestMethod.GET)
  public ResponseEntity<?> getCriteria() {
    List<ListCriterion> listCriteria = dataSourceService.getListCriterion();
    List<ListFilter> defaultFilter = dataSourceService.getDefaultFilter();

    HashMap<String, Object> response = new HashMap<>();
    response.put("criteria", listCriteria);
    response.put("defaultFilters", defaultFilter);

    return ResponseEntity.ok(response);
  }

  @RequestMapping(value = "/datasources/criteria/{criterionKey}", method = RequestMethod.GET)
  public ResponseEntity<?> getCriterionDetail(@PathVariable(value = "criterionKey") String criterionKey) {

    DataSourceListCriterionKey criterionKeyEnum = DataSourceListCriterionKey.valueOf(criterionKey);

    if (criterionKeyEnum == null) {
      throw new ResourceNotFoundException("Criterion(" + criterionKey + ") is not founded.");
    }

    ListCriterion criterion = dataSourceService.getListCriterionByKey(criterionKeyEnum);
    return ResponseEntity.ok(criterion);
  }

  @RequestMapping(path = "/datasources/{id}/append", method = {RequestMethod.PATCH, RequestMethod.PUT})
  public @ResponseBody
  ResponseEntity<?> appendReingestionDataSource(
      @PathVariable("id") String id,
      @RequestBody ReingestionRequest reingestionRequest) {

    DataSource dataSource = dataSourceRepository.findById(id).get();
    if (dataSource == null) {
      throw new ResourceNotFoundException(id);
    }

    if (reingestionRequest.getIngestionInfo() instanceof LocalFileIngestionInfo) {
      IngestionInfo ingestionInfo = reingestionRequest.getIngestionInfo();
      LocalFileIngestionInfo localFileIngestionInfo = (LocalFileIngestionInfo) dataSource.getIngestionInfo();
      localFileIngestionInfo.setPath(((LocalFileIngestionInfo) ingestionInfo).getPath());
      localFileIngestionInfo.setUploadFileName(((LocalFileIngestionInfo) ingestionInfo).getUploadFileName());
      localFileIngestionInfo.setRemoveFirstRow(((LocalFileIngestionInfo) ingestionInfo).getRemoveFirstRow());
      localFileIngestionInfo.setFormat(ingestionInfo.getFormat());
      if (ingestionInfo.getIntervals() != null) {
        localFileIngestionInfo.setIntervals(ingestionInfo.getIntervals());
      }
      dataSource.setIngestionInfo(localFileIngestionInfo);
    }

    if (!reingestionRequest.getPatches().isEmpty()) {
      for (CollectionPatch patch : reingestionRequest.getPatches()) {
        dataSource.getFields().add(new Field(patch));
        LOGGER.debug("Add field in datasource({})", dataSource.getId());
      }

      metadataService.updateFromDataSource(dataSource, true);
    }

    // Status change
    dataSource.setStatus(PREPARING);
    dataSourceRepository.saveAndFlush(dataSource);

    LOGGER.debug("Re-Ingestion append dataSource : {} ", dataSource.toString());

    ThreadFactory factory = new ThreadFactoryBuilder()
        .setNameFormat("ingestion-append-" + dataSource.getId() + "-%s")
        .setDaemon(true)
        .build();
    ExecutorService service = Executors.newSingleThreadExecutor(factory);
    service.submit(() -> jobRunner.ingestion(dataSource));

    return ResponseEntity.noContent().build();
  }

  @RequestMapping(path = "/datasources/{id}/overwrite", method = {RequestMethod.PATCH, RequestMethod.PUT})
  public @ResponseBody
  ResponseEntity<?> overwriteReingestionDataSource(
      @PathVariable("id") String id,
      @RequestBody DataSource paramDataSource) {

    DataSource dataSource = dataSourceRepository.findById(id).get();
    if (dataSource == null) {
      throw new ResourceNotFoundException(id);
    }

    if (StringUtils.isEmpty(dataSource.getOwnerId())) {
      dataSource.setOwnerId(AuthUtils.getAuthUserName());
    }

    if (dataSource.getConnType() == ENGINE) {
      if (dataSource.getSrcType() == FILE) {
        dataSource.setEngineName(dataSourceService.convertName(dataSource.getName()));
        dataSource.setIncludeGeo(dataSource.existGeoField()); // mark datasource include geo column
        dataSource.setStatus(PREPARING);
        dataSource.setFields(paramDataSource.getFields());
        dataSource.setIngestionInfo(paramDataSource.getIngestionInfo());
        dataSource.setName(paramDataSource.getName());
        dataSource.setDescription(paramDataSource.getDescription());
      }
    }

    LOGGER.debug("Re-Ingestion overwrite dataSource : {} ", dataSource.toString());

    dataSourceRepository.saveAndFlush(dataSource);
    metadataService.updateFromDataSource(dataSource, true);

    engineIngestionService.purgeDataSource(id);

    ThreadFactory factory = new ThreadFactoryBuilder()
        .setNameFormat("ingestion-overwrite-" + dataSource.getId() + "-%s")
        .setDaemon(true)
        .build();
    ExecutorService service = Executors.newSingleThreadExecutor(factory);
    service.submit(() -> jobRunner.ingestion(dataSource));

    return ResponseEntity.noContent().build();
  }

  class TimeFormatCheckResponse {

    Boolean valid;
    String pattern;

    public TimeFormatCheckResponse(Boolean valid) {
      this.valid = valid;
    }

    public TimeFormatCheckResponse(String pattern) {
      this.pattern = pattern;
    }

    public Boolean getValid() {
      return valid;
    }

    public void setValid(Boolean valid) {
      this.valid = valid;
    }

    public String getPattern() {
      return pattern;
    }

    public void setPattern(String pattern) {
      this.pattern = pattern;
    }
  }
}
