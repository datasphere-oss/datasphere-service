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

package com.datasphere.server.domain.workbench;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.datasphere.server.common.exception.BadRequestException;
import com.datasphere.server.common.exception.ResourceNotFoundException;
import com.datasphere.server.connections.jdbc.accessor.JdbcAccessor;
import com.datasphere.server.domain.dataconnection.DataConnection;
import com.datasphere.server.domain.dataconnection.DataConnectionHelper;
import com.datasphere.server.domain.dataconnection.accessor.HiveDataAccessor;
import com.datasphere.server.domain.dataconnection.dialect.HiveDialect;
import com.datasphere.server.domain.workbench.dto.ImportFile;
import com.datasphere.server.domain.workbench.hive.WorkbenchHiveService;
import com.datasphere.server.domain.workbench.util.WorkbenchDataSource;
import com.datasphere.server.domain.workbench.util.WorkbenchDataSourceManager;
import com.datasphere.server.domain.workspace.Workspace;
import com.datasphere.server.util.HibernateUtils;

@RepositoryRestController
public class WorkbenchController {

  @Autowired
  WorkbenchRepository workbenchRepository;

  @Autowired
  PagedResourcesAssembler pagedResourcesAssembler;

  @Autowired
  WorkbenchHiveService workbenchHiveService;

  @Autowired
  WorkbenchDataSourceManager workbenchDataSourceManager;

  @RequestMapping(value = "/workbenchs/{id}/navigation", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public ResponseEntity<?> listNavigation(@PathVariable("id") String id,
                                          @RequestParam(required=false) String namePattern,
                                          Pageable pageable,
                                          PersistentEntityResourceAssembler resourceAssembler) {

    Workbench workbench = workbenchRepository.findById(id).get();
    if(workbench == null){
      throw new ResourceNotFoundException("Workbench(" + id + ")");
    }
    Workspace workspace = workbench.getWorkspace();
    if(workspace == null){
      throw new ResourceNotFoundException("Workspace");
    }
    Page<Workbench> workbenches = workbenchRepository.findByWorkspaceIdAndNameIgnoreCaseContaining(workspace.getId(),
            StringUtils.defaultString(namePattern, ""), pageable);
    return ResponseEntity.ok(this.pagedResourcesAssembler.toResource(workbenches, resourceAssembler));
  }

  @RequestMapping(value = "/workbenchs/connection", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<?> currentConnection() {
    Map<String, WorkbenchDataSource> dataSourceMap = workbenchDataSourceManager.getCurrentConnections();
    return ResponseEntity.ok(dataSourceMap);
  }

  @RequestMapping(value = "/workbenchs/{id}/import", method = RequestMethod.POST)
  @ResponseBody
  public ResponseEntity<?> importFileToPersonalDatabase(@PathVariable("id") String id,
                                                        @RequestBody ImportFile importFile) {
    Workbench workbench = workbenchRepository.findById(id).get();

    if(workbench == null) {
      throw new ResourceNotFoundException("Workbench(" + id + ")");
    }

    DataConnection dataConnection = HibernateUtils.unproxy(workbench.getDataConnection());

    JdbcAccessor jdbcDataAccessor = DataConnectionHelper.getAccessor(dataConnection);

    if((jdbcDataAccessor instanceof HiveDataAccessor) == false ||
        HiveDialect.isSupportSaveAsHiveTable(dataConnection) == false) {
      throw new BadRequestException("Only Hive Connection supported save as hive table is allowed.");
    }

    workbenchHiveService.importFileToPersonalDatabase(dataConnection, importFile);

    return ResponseEntity.noContent().build();
  }

}
