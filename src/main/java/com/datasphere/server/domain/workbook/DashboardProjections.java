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
import java.util.Set;

import com.datasphere.server.common.BaseProjections;
import com.datasphere.server.domain.datasource.DataSourceAlias;
import com.datasphere.server.domain.user.UserProfile;
import com.datasphere.server.domain.workbook.widget.Widget;

/**
 * Created by kyungtaak on 2016. 11. 3..
 */
public class DashboardProjections extends BaseProjections {

  @Projection(name = "default", types = {DashBoard.class})
  public interface DefaultProjection {

    String getId();

    String getName();

    String getDescription();

    String getImageUrl();

    String getTag();

    DateTime getCreatedTime();

    @Value("#{@cachedUserService.findUserProfile(target.createdBy)}")
    UserProfile getCreatedBy();

    @Value("#{@cachedUserService.findUserProfile(target.modifiedBy)}")
    UserProfile getModifiedBy();

    DateTime getModifiedTime();
  }

  /**
   *
   */
  @Projection(name = "forListView", types = {DashBoard.class})
  public interface ForListViewProjection {

    String getId();

    String getName();

    String getDescription();

    String getImageUrl();

    String getTag();

    Boolean getHiding();

    Integer getSeq();

    DateTime getCreatedTime();

    @Value("#{@cachedUserService.findUserProfile(target.createdBy)}")
    UserProfile getCreatedBy();

    @Value("#{@cachedUserService.findUserProfile(target.modifiedBy)}")
    UserProfile getModifiedBy();

    DateTime getModifiedTime();

    @Value("#{T(com.datasphere.server.util.ProjectionUtils).toListResource(@projectionFactory, T(com.datasphere.server.domain.datasource.DataSourceProjections$ForDetailProjection), @dashBoardService.backingDataSource(target.dataSources, target.workBook))}")
    Object getDataSources();
  }

  /**
   *
   */
  @Projection(name = "forTreeView", types = {DashBoard.class})
  public interface ForTreeViewProjection {
    String getId();

    String getName();

    String getDescription();

    String getImageUrl();
  }

  /**
   *
   */
  @Projection(name = "forLeftListView", types = {DashBoard.class})
  public interface ForLeftListViewProjection {

    String getId();

    String getName();

    String getDescription();

    String getImageUrl();

    Boolean getHiding();

    Integer getSeq();
  }

  /**
   * 페이지 UI 내 워크북 Conf가 필요
   */
  @Projection(name = "forDetailView", types = {DashBoard.class})
  public interface ForDetailViewProjection {

    String getId();

    String getName();

    String getDescription();

    @Value("#{target.configuration == null ? null : @objectMapper.readValue(target.configuration, T(java.lang.Object))}")
    Object getConfiguration();

    String getImageUrl();

    String getTag();

    Integer getSeq();

    String getTemporaryId();

    //@Value("#{T(com.datasphere.server.util.ProjectionUtils).toListResource(@projectionFactory, T(com.datasphere.server.domain.datasource.DataSourceProjections$ForDetailProjection), target.dataSources)}")
    @Value("#{T(com.datasphere.server.util.ProjectionUtils).toListResource(@projectionFactory, T(com.datasphere.server.domain.datasource.DataSourceProjections$ForDetailProjection), @dashBoardService.backingDataSource(target.dataSources, target.workBook))}")
    Object getDataSources();

//    @Value("#{T(com.datasphere.server.util.ProjectionUtils).toListResource(@projectionFactory, T(com.datasphere.server.domain.workbook.widget.WidgetProjections$ForDetailViewProjection), target.widgets)}")
    Set<Widget> getWidgets();

    @Value("#{@dataSourceAliasRepository.findByDashBoardId(target.id)}")
    List<DataSourceAlias> getAliases();

    DateTime getCreatedTime();

    @Value("#{@cachedUserService.findUserProfile(target.createdBy)}")
    UserProfile getCreatedBy();

    @Value("#{@cachedUserService.findUserProfile(target.modifiedBy)}")
    UserProfile getModifiedBy();

    DateTime getModifiedTime();
  }

}
