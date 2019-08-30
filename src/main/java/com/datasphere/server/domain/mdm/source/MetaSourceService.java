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

package com.datasphere.server.domain.mdm.source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.datasphere.server.domain.dataconnection.DataConnectionRepository;
import com.datasphere.server.datasource.DataSource;
import com.datasphere.server.datasource.DataSourceProjections;
import com.datasphere.server.datasource.DataSourceRepository;
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
        return dataSourceRepository.findById(sourceId);
      case JDBC:
        return dataConnectionRepository.findById(sourceId);
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
        DataSource dataSource = dataSourceRepository.findById(sourceId).get();
        return ProjectionUtils.toResource(projectionFactory, DataSourceProjections.ForDetailProjection.class, dataSource);
      case JDBC:
        source = dataConnectionRepository.findById(sourceId);
        break;
    }
    return source;
  }
}
