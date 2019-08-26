/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.datasphere.server.domain.mdm.source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.datasphere.server.domain.dataconnection.DataConnectionRepository;
import com.datasphere.server.domain.datasource.DataSource;
import com.datasphere.server.domain.datasource.DataSourceProjections;
import com.datasphere.server.domain.datasource.DataSourceRepository;
import com.datasphere.server.domain.mdm.Metadata;
import com.datasphere.server.domain.mdm.MetadataController;
import com.datasphere.server.domain.workbook.DashboardRepository;
import com.datasphere.server.util.ProjectionUtils;

@Component
@Transactional(readOnly = true)
public class MetaSourceService {

  private static Logger LOGGER = LoggerFactory.getLogger(MetadataController.class);

  @Autowired
  MetadataSourceRepository metadataSourceRepository;

  @Autowired
  DataSourceRepository dataSourceRepository;

  @Autowired
  DataConnectionRepository dataConnectionRepository;

  @Autowired
  DashboardRepository dashboardRepository;

  @Autowired
  ProjectionFactory projectionFactory;

  public List<MetadataSource> findMetadataSourcesBySourceId(String type, String sourceId) {
    return null;
  }

  /**
   * Get Metadata source by source id
   *
   * @param type
   * @param sourceId
   * @return
   */
  public Object getSourcesBySourceId(Metadata.SourceType type, String sourceId) {
    switch (type) {
      case ENGINE:
        return dataSourceRepository.findOne(sourceId);
      case JDBC:
        return dataConnectionRepository.findOne(sourceId);
    }
    return null;
  }

  /**
   * Gets sources by source id with projection.
   *
   * @param type     the type
   * @param sourceId the source id
   * @return the sources by source id with projection
   */
  public Object getSourcesBySourceIdWithProjection(Metadata.SourceType type, String sourceId) {
    Object source = null;
    switch (type) {
      case ENGINE:
        DataSource dataSource = dataSourceRepository.findOne(sourceId);
        return ProjectionUtils.toResource(projectionFactory, DataSourceProjections.ForDetailProjection.class, dataSource);
      case JDBC:
        source = dataConnectionRepository.findOne(sourceId);
        break;
    }
    return source;
  }
}
