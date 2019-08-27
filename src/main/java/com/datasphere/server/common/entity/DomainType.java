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

package com.datasphere.server.common.entity;

import com.datasphere.server.common.BaseProjections;
import com.datasphere.server.domain.MetatronDomain;
import com.datasphere.server.domain.context.ContextEntity;
import com.datasphere.server.datasource.DataSource;
import com.datasphere.server.datasource.DataSourceProjections;
import com.datasphere.server.domain.mdm.Metadata;
import com.datasphere.server.domain.notebook.Notebook;
import com.datasphere.server.domain.user.role.Role;
import com.datasphere.server.domain.user.role.RoleProjections;
import com.datasphere.server.domain.workbench.Workbench;
import com.datasphere.server.domain.workbook.DashBoard;
import com.datasphere.server.domain.workbook.DashboardProjections;
import com.datasphere.server.domain.workbook.WorkBook;
import com.datasphere.server.domain.workspace.BookProjections;
import com.datasphere.server.domain.workspace.Workspace;
import com.datasphere.server.domain.workspace.WorkspaceProjections;

/**
 * Metatron 내 도메인 타입 정의
 */
public enum  DomainType {

  COMMON, METADATA, DATASOURCE, WORKSPACE, WORKBOOK, DASHBOARD, WORKBENCH, NOTEBOOK, GROUP;

  public BaseProjections getProjection() {

    switch (this) {
      case DATASOURCE:
        return new DataSourceProjections();
      case WORKSPACE:
        return new WorkspaceProjections();
      case WORKBOOK:
      case WORKBENCH:
      case NOTEBOOK:
        return new BookProjections();
      case DASHBOARD:
        return new DashboardProjections();
      case GROUP:
        return new RoleProjections();
    }

    return new BaseProjections();
  }

  public static DomainType getType(ContextEntity entity) {
    if(entity instanceof DataSource) {
      return DATASOURCE;
    } else if(entity instanceof Workspace) {
      return WORKSPACE;
    } else if(entity instanceof WorkBook) {
      return WORKBOOK;
    } else if(entity instanceof DashBoard) {
      return DASHBOARD;
    } else if(entity instanceof Workbench) {
      return WORKBENCH;
    } else if(entity instanceof Notebook) {
      return NOTEBOOK;
    } else if(entity instanceof Role) {
      return GROUP;
    } else {
      return COMMON;
    }
  }

  public static DomainType getType(MetatronDomain domain) {
    if(domain instanceof DataSource) {
      return DATASOURCE;
    } else if(domain instanceof Metadata) {
      return METADATA;
    } else if(domain instanceof Workspace) {
      return WORKSPACE;
    } else if(domain instanceof WorkBook) {
      return WORKBOOK;
    } else if(domain instanceof DashBoard) {
      return DASHBOARD;
    } else if(domain instanceof Workbench) {
      return WORKBENCH;
    } else if(domain instanceof Notebook) {
      return NOTEBOOK;
    } else if(domain instanceof Role) {
      return GROUP;
    } else {
      return COMMON;
    }
  }
}
