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

package com.datasphere.server.domain.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datasphere.server.common.entity.DomainType;
import com.datasphere.server.common.exception.DSSException;
import com.datasphere.server.datasource.DataSourceRepository;
import com.datasphere.server.domain.notebook.NotebookRepository;
import com.datasphere.server.domain.user.role.RoleRepository;
import com.datasphere.server.domain.workbench.WorkbenchRepository;
import com.datasphere.server.domain.workbook.DashboardRepository;
import com.datasphere.server.domain.workbook.WorkBookRepository;
import com.datasphere.server.domain.workspace.WorkspaceRepository;

@Component
public class ContextDomainRepositoryFactory {

  private static Logger LOGGER = LoggerFactory.getLogger(ContextDomainRepositoryFactory.class);

  @Autowired
  DataSourceRepository dataSourceRepository;

  @Autowired
  WorkspaceRepository workspaceRepository;

  @Autowired
  WorkBookRepository workBookRepository;

  @Autowired
  DashboardRepository dashBoardRepository;

  @Autowired
  WorkbenchRepository workbenchRepository;

  @Autowired
  NotebookRepository noteBookRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  ContextRepository contextRepository;

  public ContextDomainRepository getDomainRepository(DomainType type) throws DSSException {
    switch (type) {
      case DATASOURCE:
        return dataSourceRepository;
      case WORKSPACE:
        return workspaceRepository;
      case GROUP:
        return roleRepository;
      default:
        // 임시 Exception 정의, 추후 Exception 정의 필요.
        throw new DSSException("Not supported repository.");
    }
  }
}
