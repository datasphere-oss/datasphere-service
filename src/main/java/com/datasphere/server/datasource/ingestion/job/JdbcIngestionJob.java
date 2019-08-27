/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specic language governing permissions and
 * limitations under the License.
 */

/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specic language governing permissions and
 * limitations under the License.
 */

package com.datasphere.server.datasource.ingestion.job;

import static com.datasphere.server.datasource.DataSourceErrorCodes.INGESTION_COMMON_ERROR;
import static com.datasphere.server.datasource.DataSourceErrorCodes.INGESTION_JDBC_EMPTY_RESULT_ERROR;
import static com.datasphere.server.datasource.DataSourceErrorCodes.INGESTION_JDBC_FETCH_RESULT_ERROR;
import static com.datasphere.server.datasource.DataSourceErrorCodes.INGESTION_JDBC_QUERY_EXECUTION_ERROR;
import static com.datasphere.server.datasource.ingestion.jdbc.BatchIngestionInfo.IngestionScope.INCREMENTAL;

import java.io.File;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datasphere.server.connections.jdbc.exception.JdbcDataConnectionErrorCodes;
import com.datasphere.server.connections.jdbc.exception.JdbcDataConnectionException;
import com.datasphere.server.datasource.DataSource;
import com.datasphere.server.datasource.DataSourceIngestionException;
import com.datasphere.server.datasource.DataSourceSummary;
import com.datasphere.server.datasource.connection.jdbc.JdbcConnectionService;
import com.datasphere.server.datasource.ingestion.IngestionHistory;
import com.datasphere.server.datasource.ingestion.IngestionOption;
import com.datasphere.server.datasource.ingestion.file.CsvFileFormat;
import com.datasphere.server.datasource.ingestion.jdbc.BatchIngestionInfo;
import com.datasphere.server.datasource.ingestion.jdbc.JdbcIngestionInfo;
import com.datasphere.server.domain.dataconnection.DataConnection;
import com.datasphere.server.spec.druid.ingestion.BatchIndex;
import com.datasphere.server.spec.druid.ingestion.Index;
import com.datasphere.server.spec.druid.ingestion.IngestionSpec;
import com.datasphere.server.spec.druid.ingestion.IngestionSpecBuilder;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class JdbcIngestionJob extends AbstractIngestionJob implements IngestionJob {

  private static final Logger LOGGER = LoggerFactory.getLogger(JdbcIngestionJob.class);

  private JdbcConnectionService jdbcConnectionService;

  private JdbcIngestionInfo ingestionInfo;

  private String srcFilePath;

  private String loadFileName;

  private Index indexSpec;

  public JdbcIngestionJob(DataSource dataSource, IngestionHistory ingestionHistory) {
    super(dataSource, ingestionHistory);
    ingestionInfo = dataSource.getIngestionInfoByType();
    ingestionInfo.setFormat(new CsvFileFormat());
  }

  public void setJdbcConnectionService(JdbcConnectionService jdbcConnectionService) {
    this.jdbcConnectionService = jdbcConnectionService;
  }

  @Override
  public void preparation() {

    DataConnection connection = Preconditions.checkNotNull(dataSource.getJdbcConnectionForIngestion(), "Required connection info.");

    // Select 문을 가지고 CSV 파일로 변환
    List<String> csvFiles = null;

    if (connection.getImplementor().equals("MYSQL")
        || connection.getImplementor().equals("HIVE")
        || connection.getImplementor().equals("PRESTO")) {
      if (ingestionInfo.getDatabase() != null && ingestionInfo.getDataType() == JdbcIngestionInfo.DataType.QUERY) {
        connection.setDatabase(ingestionInfo.getDatabase());
      }
    }

    try {
      if (ingestionInfo instanceof BatchIngestionInfo
          && ((BatchIngestionInfo) ingestionInfo).getRange() == INCREMENTAL) {

        DataSourceSummary summary = dataSource.getSummary();

        csvFiles = jdbcConnectionService.selectIncrementalQueryToCsv(
            connection,
            ingestionInfo,
            dataSource.getEngineName(),
            summary == null ? null : summary.getIngestionMaxTime(),
            dataSource.getFields()
        );
      } else {
        csvFiles = jdbcConnectionService.selectQueryToCsv(
            connection,
            ingestionInfo,
            dataSource.getEngineName(),
            dataSource.getFields(),
            null
        );
      }
    } catch (JdbcDataConnectionException ce) {
      if (ce.getCode() == JdbcDataConnectionErrorCodes.INVALID_QUERY_ERROR_CODE) {
        try {
			throw new DataSourceIngestionException(INGESTION_JDBC_QUERY_EXECUTION_ERROR, ce);
		} catch (DataSourceIngestionException e) {
			e.printStackTrace();
		}
      } else if (ce.getCode() == JdbcDataConnectionErrorCodes.CSV_IO_ERROR_CODE) {
        try {
			throw new DataSourceIngestionException(INGESTION_JDBC_FETCH_RESULT_ERROR, ce);
		} catch (DataSourceIngestionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      } else {
        try {
			throw new DataSourceIngestionException(INGESTION_COMMON_ERROR, ce);
		} catch (DataSourceIngestionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }
    } catch (Exception e) {
      try {
		throw new DataSourceIngestionException(INGESTION_COMMON_ERROR, e);
	} catch (DataSourceIngestionException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
    }

    if (CollectionUtils.isEmpty(csvFiles)) {
      try {
		throw new DataSourceIngestionException(INGESTION_JDBC_EMPTY_RESULT_ERROR, "Empty result of query.");
	} catch (DataSourceIngestionException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }

    File tempFile = new File(csvFiles.get(0));
    if (!tempFile.canRead()) {
      try {
		throw new DataSourceIngestionException(INGESTION_JDBC_FETCH_RESULT_ERROR, "Temporary file for ingestion are not available.");
	} catch (DataSourceIngestionException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }

    srcFilePath = tempFile.getAbsolutePath();
    loadFileName = tempFile.getName();
  }

  @Override
  public void loadToEngine() {
    loadFileToEngine(Lists.newArrayList(srcFilePath), Lists.newArrayList(loadFileName));
  }

  @Override
  public void buildSpec() {
    IngestionSpec spec = new IngestionSpecBuilder()
        .dataSchema(dataSource)
        .batchTuningConfig(ingestionOptionService.findTuningOptionMap(IngestionOption.IngestionType.BATCH,
                                                                      ingestionInfo.getTuningOptions()))
        .localIoConfig(engineProperties.getIngestion().getBaseDir(), loadFileName)
        .build();

    indexSpec = new BatchIndex(spec, dedicatedWorker);
  }

  @Override
  public String process() {
    String taskId = null;
	try {
		taskId = doIngestion(indexSpec);
	} catch (DataSourceIngestionException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    LOGGER.info("Successfully creating task : {}", ingestionHistory);
    return taskId;
  }

}

