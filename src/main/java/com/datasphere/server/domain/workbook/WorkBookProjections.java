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

package com.datasphere.server.domain.workbook;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.util.List;
import java.util.Map;

import com.datasphere.server.domain.user.UserProfile;

/**
 * Created by kyungtaak on 2016. 11. 3..
 */
public class WorkBookProjections {

  @Projection(name = "default", types = { WorkBook.class })
  public interface DefaultProjection {

    String getId();

    String getName();

    String getType();

    String getDescription();

    String getFolderId();

    DateTime getCreatedTime();

    @Value("#{@cachedUserService.findUserProfile(target.createdBy)}")
    UserProfile getCreatedBy();

    @Value("#{@cachedUserService.findUserProfile(target.modifiedBy)}")
    UserProfile getModifiedBy();

    DateTime getModifiedTime();

    @Value("#{target.getWorkspace().getId()}")
    String getWorkspaceId();

  }

  @Projection(name = "forDetailView", types = { WorkBook.class })
  public interface ForDetailViewProjection {

    String getId();

    String getName();

    String getType();

    String getDescription();

    String getFolderId();

    //@Value("#{T(com.datasphere.server.util.ProjectionUtils).toListResource(@projectionFactory, T(com.datasphere.server.datasource.DataSourceProjections$ForSimpleViewProjection), @dashboardRepository.findAllDataSourceInDashboard(target.id))}")
    @Value("#{T(com.datasphere.server.util.ProjectionUtils).toListResource(@projectionFactory, T(com.datasphere.server.datasource.DataSourceProjections$ForSimpleViewProjection), @dashBoardService.backingDataSource(target))}")
    Object getDataSource();

    @Value("#{T(com.datasphere.server.util.ProjectionUtils).toListResource(@projectionFactory, T(com.datasphere.server.domain.workbook.DashboardProjections$ForLeftListViewProjection), target.dashBoards)}")
    Object getDashBoards();

    @Value("#{@commentRepository.countByDomainTypeAndDomainId(T(com.datasphere.server.common.entity.DomainType).WORKBOOK, target.id)}")
    Long getCountOfComments();

    DateTime getCreatedTime();

    @Value("#{@cachedUserService.findUserProfile(target.createdBy)}")
    UserProfile getCreatedBy();

    @Value("#{@cachedUserService.findUserProfile(target.modifiedBy)}")
    UserProfile getModifiedBy();

    DateTime getModifiedTime();

    @Value("#{target.getWorkspace().getId()}")
    String getWorkspaceId();

  }

  @Projection(name = "forTreeView", types = { WorkBook.class })
  public interface ForTreeViewProjection {

    String getId();

    String getName();

    String getType();

    String getDescription();

    @Value("#{@bookTreeService.findBookHierarchies(target.id)}")
    List<Map<String, String>> getHierarchies();

    @Value("#{T(com.datasphere.server.util.ProjectionUtils).toListResource(@projectionFactory, T(com.datasphere.server.domain.workbook.DashboardProjections$ForLeftListViewProjection), target.dashBoards)}")
    Object getDashBoards();

  }

}
