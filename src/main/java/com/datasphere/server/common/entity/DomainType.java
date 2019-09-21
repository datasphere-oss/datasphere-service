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

package com.datasphere.server.common.entity;

import com.datasphere.server.common.BaseProjections;
import com.datasphere.server.domain.DSSDomain;
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

  public static DomainType getType(DSSDomain domain) {
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
