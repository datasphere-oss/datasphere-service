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

package com.datasphere.server.domain.datalineage;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.datasphere.server.common.entity.SearchParamValidator;
import com.datasphere.server.common.exception.BadRequestException;

@RepositoryRestController
public class DataLineageController {

  private static Logger LOGGER = LoggerFactory.getLogger(DataLineageController.class);

  @Autowired
  DataLineageRepository dataLineageRepository;

  @Autowired
  DataLineageWorkFlowRepository dataLineageWorkFlowRepository;

  @Autowired
  DataLineageService dataLineageService;

  @Autowired
  PagedResourcesAssembler pagedResourcesAssembler;

  @RequestMapping(path = "/datalineages", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<?> dataLineageList(
          @RequestParam(required = true) String scope,
          @RequestParam(required = false) String keyword,
          @RequestParam(value = "from", required = false)
          @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) DateTime from,
          @RequestParam(value = "to", required = false)
          @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) DateTime to,
          Pageable pageable) throws BadRequestException {

    LOGGER.debug("keyword : {}", keyword);
    LOGGER.debug("scopes : {}", scope);
    LOGGER.debug("from : {}", from);
    LOGGER.debug("to : {}", to);
    LOGGER.debug("pageable : {}", pageable);

    // Validate SearchScope
    DataLineage.SearchScope searchScopeType = SearchParamValidator
            .enumUpperValue(DataLineage.SearchScope.class, scope, "scope");

    SearchParamValidator.range(null, from, to);

    Page<DataLineageDto> dataLineageDtoList = null;

    switch (searchScopeType){
      case TABLE:
        dataLineageDtoList = dataLineageRepository.getTableList(keyword, pageable);
        break;
      case COLUMN:
        dataLineageDtoList = dataLineageRepository.getColumnList(keyword, pageable);
        break;
      case SQL:
        dataLineageDtoList = dataLineageRepository.getSQLList(keyword, pageable);
        break;
      case WORKFLOW:
        dataLineageDtoList = dataLineageRepository.getWorkflowList(keyword, pageable);
        break;
    }

    return ResponseEntity.ok(this.pagedResourcesAssembler.toResource(dataLineageDtoList));
  }

  @RequestMapping(path = "/datalineages/lineages", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<?> dataLineageFromTableListQueryString(
          @RequestParam(value = "databaseName") String dbName,
          @RequestParam(value = "tableName") String tableName,
          @RequestParam(value = "from", required = false)
          @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) DateTime from,
          @RequestParam(value = "to", required = false)
          @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) DateTime to,
          @RequestParam(value = "direction", required = true) String direction) throws BadRequestException {
    LOGGER.debug("dbName : {}", dbName);
    LOGGER.debug("tableName : {}", tableName);
    LOGGER.debug("from : {}", from);
    LOGGER.debug("to : {}", to);
    LOGGER.debug("direction : {}", direction);

    // Validate DataLineageLink Direction
    DataLineageLink.Direction directionType = SearchParamValidator
            .enumUpperValue(DataLineageLink.Direction.class, direction, "direction");

    DataLineageLink dataLineageLink = dataLineageService.getLinkedEntity(dbName, tableName, directionType, from, to);
    return ResponseEntity.ok(dataLineageLink);
  }

  @RequestMapping(path = "/datalineages/lineages/link", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<?> dataLineageAllFromTableList(
          @RequestParam(value = "databaseName") String dbName,
          @RequestParam(value = "tableName") String tableName,
          @RequestParam(value = "from", required = false)
          @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) DateTime from,
          @RequestParam(value = "to", required = false)
          @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) DateTime to,
          @RequestParam(value = "depth", required = false, defaultValue = "9999") int depth) {
    LOGGER.debug("dbName : {}", dbName);
    LOGGER.debug("tableName : {}", tableName);
    LOGGER.debug("from : {}", from);
    LOGGER.debug("to : {}", to);
    LOGGER.debug("depth : {}", depth);

    List<DataLineageLink> dataLineageLinks = dataLineageService.getLinkedEntityByDepth(dbName, tableName, from, to, depth);
    List<DataLineageLinkedEdge> dataLineageLinkedEdges = new ArrayList<>();

    Long linkCnt = 1L;
    Map<String, String> queryMap = new HashMap<>();
    for(DataLineageLink dataLineageLink : dataLineageLinks){
      for(DataLineageLinkedEdge dataLineageLinkedEdge : dataLineageLink.getEdges()){
        //Link Use index as depth (starting at 1)
        dataLineageLinkedEdge.setDepth(linkCnt);

        //SqlQuery Delete duplicates
        String path = dataLineageLinkedEdge.getPath();
        boolean pathStored = false;
        for(String storedPath : queryMap.values()){
          if(storedPath.equals(path)){
            pathStored = true;
            break;
          }
        }

        //In case of duplicate path, delete contents, first appearing path is added to Map
        if(pathStored){
          dataLineageLinkedEdge.setPath("");
        } else {
          queryMap.put(path, path);
        }

        dataLineageLinkedEdges.add(dataLineageLinkedEdge);
      }
      linkCnt++;
    }
    return ResponseEntity.ok(dataLineageLinkedEdges);
  }

  @RequestMapping(path = "/datalineages/sql/{datalineageId}", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<?> dataLineageFromSqlList(
          @PathVariable(value = "datalineageId") Long datalineageId,
          @RequestParam(value = "from", required = false)
          @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) DateTime from,
          @RequestParam(value = "to", required = false)
          @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) DateTime to,
          @RequestParam(value = "direction") String direction) throws BadRequestException {
    LOGGER.debug("datalineageId : {}", datalineageId);
    LOGGER.debug("direction : {}", direction);

    // Validate DataLineageLink Direction
    DataLineageLink.Direction directionType = SearchParamValidator
            .enumUpperValue(DataLineageLink.Direction.class, direction, "direction");

    DataLineageLink dataLineageLink = dataLineageService.getLinkedEntity(datalineageId, directionType, from, to);
    return ResponseEntity.ok(dataLineageLink);
  }

  @RequestMapping(path = "/datalineages/tables/information", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<?> tableInformation(
          @RequestParam(value = "databaseName") String dbName,
          @RequestParam(value = "tableName") String tableName) {
    LOGGER.debug("dbName : {}", dbName);
    LOGGER.debug("tableName : {}", tableName);
    if(StringUtils.isNotEmpty(dbName)){
      HashMap<String, Object> tableInformation = dataLineageService.getTableInformation(dbName, tableName);
      return ResponseEntity.ok(tableInformation);
    } else {
      return ResponseEntity.ok().build();
    }
  }

  @RequestMapping(path = "/datalineages/workflows/{workflowId}/information", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<?> workflowInformation(@PathVariable Long workflowId) {
    LOGGER.debug("workflowId : {}", workflowId);
    Optional<DataLineageWorkFlow> workFlow = dataLineageWorkFlowRepository.findById(workflowId);
    return ResponseEntity.ok(workFlow.get());
  }

}
