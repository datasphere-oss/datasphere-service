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

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import com.datasphere.server.common.exception.BadRequestException;
import com.datasphere.server.util.EnumUtils;

public class SearchParamValidator {

  public static <E extends Enum<E>> E enumUpperValue(final Class<E> enumClass, final String enumName, final String propertyName) throws BadRequestException {
    if(StringUtils.isNotEmpty(enumName)
        && !EnumUtils.isValidUpperCaseEnum(enumClass, enumName)) {
      throw new BadRequestException("Invalid '" + propertyName + "' parameter. Choose "
                                        + EnumUtils.getEnumValues(enumClass));
    }
    return EnumUtils.getUpperCaseEnum(enumClass, enumName);
  }

  public static void range(String searchDateBy, DateTime from, DateTime to) throws BadRequestException {

    // Validate searchDateBy
    if(StringUtils.isNotEmpty(searchDateBy)
        && !("CREATED".equalsIgnoreCase(searchDateBy) || "MODIFIED".equalsIgnoreCase(searchDateBy))) {
      throw new BadRequestException("Invalid 'searchDateBy' parameter. Choose [CREATED, MODIFIED]");
    }

    if(from != null && to != null && from.isAfter(to)) {
      throw new BadRequestException("Invalid range 'to' must be greater then 'from'");
    }
  }

  public static <T> T checkNull(T param, String paramName) throws BadRequestException {

    if(param == null) {
      throw new BadRequestException(paramName + " required.");
    }

    return param;
  }

}
