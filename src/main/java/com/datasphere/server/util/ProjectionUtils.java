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

package com.datasphere.server.util;

import com.google.common.collect.Lists;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.projection.ProjectionFactory;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ProjectionUtils {

  public static <T> List<T> toListResource(ProjectionFactory factory,
                                     Class<T> projectionClass,
                                     List<Object> resources) {
    if(CollectionUtils.isEmpty(resources)) {
      return Lists.newArrayList();
    }

    return resources.stream()
             .map(resource -> factory.createProjection(projectionClass, resource))
             .collect(toList());
  }

  public static <T, E> Page<T> toPageResource(ProjectionFactory factory,
                                           Class<T> projectionClass,
                                           Page<E> resources) {

    return resources.map(resource -> factory.createProjection(projectionClass, resource));
  }

  public static <T> T toResource(ProjectionFactory factory,
                                    Class<T> projectionClass,
                                    Object resource) {
    if(resource == null) {
      return null;
    }

    return factory.createProjection(projectionClass, resource);
  }
}
