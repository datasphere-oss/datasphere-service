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

package com.datasphere.server.connections.jdbc.accessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;


public class StageDataAccessor extends DSSDataAccessor {

  private static final Logger LOGGER = LoggerFactory.getLogger(StageDataAccessor.class);

  @Override
  public Map<String, Object> getDatabases(String catalog, String schemaPattern, Integer pageSize, Integer pageNumber) {
    Map<String, Object> databaseMap = super.getDatabases(catalog, schemaPattern, pageSize, pageNumber);

    List<String> databaseNames = (List) databaseMap.get("databases");

    // TODO : temporary measures
    List<String> filteredDatabaseNames = databaseNames.stream()
                                 .filter(s -> !s.startsWith("FLOW_"))
                                 .collect(toList());

    int databaseCount = filteredDatabaseNames.size();
    databaseMap.put("databases", filteredDatabaseNames);
    databaseMap.put("page", createPageInfoMap(databaseCount, databaseCount, 0));
    return databaseMap;
  }
}
