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

package com.datasphere.server.common;

import com.google.common.collect.Maps;

import org.springframework.data.rest.core.config.Projection;

import java.util.Map;


public class BaseProjections {

  private final Map<String, Class> projectionsMap = Maps.newHashMap();

  public BaseProjections() {
  }

  private void setProjectionsMap() {
    Class[] types = this.getClass().getDeclaredClasses();
    for (Class pClass : types) {
      Projection projection = (Projection) pClass.getAnnotation(Projection.class);
      if(projection == null) {
        continue;
      }
      projectionsMap.put(projection.name(), pClass);
    }
  }

  public Class getProjectionByName(String name) {
    if(projectionsMap.isEmpty()) {
      setProjectionsMap();
    }

    if(!projectionsMap.containsKey(name)) {
      throw new BadRequestException("Invalid projection name. choose one of " + projectionsMap.keySet());
    }

    return projectionsMap.get(name);
  }
}
