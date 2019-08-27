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
