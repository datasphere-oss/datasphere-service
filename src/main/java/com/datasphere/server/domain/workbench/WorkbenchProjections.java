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

package com.datasphere.server.domain.workbench;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import org.springframework.stereotype.Component;

import java.util.Set;

import com.datasphere.engine.datasource.connections.DataConnectionProjections;
import com.datasphere.server.common.BaseProjections;
import com.datasphere.server.user.UserProfile;
import com.datasphere.server.domain.workspace.WorkspaceProjections;

/**
 * Created by aladin on 2019. 11. 29..
 */
@Component
public class WorkbenchProjections extends BaseProjections{

  @Projection(name = "workbenchDefault", types = { Workbench.class })
  public interface DefaultProjection {

    String getId();
    // 工作表类型
    String getType();
    // 工作表名称
    String getName();
    // 工作表描述
    String getDescription();
    // 工作表文件夹
    String getFolderId();
    // 数据库名称
    String getDatabaseName();
    // 用户资料
    @Value("#{@cachedUserService.findUserProfile(target.createdBy)}")
    UserProfile getCreatedBy();

    DateTime getCreatedTime();

    @Value("#{@cachedUserService.findUserProfile(target.modifiedBy)}")
    UserProfile getModifiedBy();

    DateTime getModifiedTime();
  }

  @Projection(name = "workbenchDetail", types = { Workbench.class })
  public interface DetailViewProjection {

    String getId();
    // 工作表类型
    String getType();
    // 工作表名称
    String getName();
    // 工作表描述
    String getDescription();
    // 工作表文件夹
    String getFolderId();
    // 工作表全局变量
    String getGlobalVar();
    // 获得工作空间
    @Value("#{T(com.datasphere.server.util.HibernateUtils).unproxy(target.workspace)}")
    WorkspaceProjections.HeaderViewProjection getWorkspace();
    // 获得数据连接
    @Value("#{T(com.datasphere.server.util.HibernateUtils).unproxy(target.dataConnection)}")
    DataConnectionProjections.defaultProjection getDataConnection();

    Set<QueryEditor> getQueryEditors();
    // 获得数据库名称
    String getDatabaseName();

    @Value("#{target.dataConnection.workspaces.contains(target.workspace) || target.dataConnection.published}")
    Boolean getValid();

    @Value("#{@cachedUserService.findUserProfile(target.createdBy)}")
    UserProfile getCreatedBy();

    DateTime getCreatedTime();

    @Value("#{@cachedUserService.findUserProfile(target.modifiedBy)}")
    UserProfile getModifiedBy();

    DateTime getModifiedTime();
  }
  // 工作表浏览
  @Projection(name = "workbenchNavigation", types = { Workbench.class })
  public interface WorkbenchNavigationProjection {

    String getId();
    // 工作表类型
    String getType();

    String getName();

    String getDescription();

    String getFolderId();

//    WorkspaceProjections.HeaderViewProjection getWorkspace();
    // 工作空间
    @Value("#{target.workspace.id}")
    String getWorkspaceId();
    // 工作空间名称
    @Value("#{target.workspace.name}")
    String getWorkspaceName();

    @Value("#{@cachedUserService.findUserProfile(target.createdBy)}")
    UserProfile getCreatedBy();
    // 创建时间
    DateTime getCreatedTime();

    @Value("#{@cachedUserService.findUserProfile(target.modifiedBy)}")
    UserProfile getModifiedBy();

    DateTime getModifiedTime();
  }

}
