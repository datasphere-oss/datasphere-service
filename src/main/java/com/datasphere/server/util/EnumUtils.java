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

import java.util.List;

public class EnumUtils extends org.apache.commons.lang3.EnumUtils {

  public static <E extends Enum<E>> boolean isValidUpperCaseEnum(final Class<E> enumClass, final String enumName) {
    if (enumName == null) {
      return false;
    }
    try {
      Enum.valueOf(enumClass, enumName.toUpperCase());
      return true;
    } catch (final IllegalArgumentException ex) {
      return false;
    }
  }

  public static <E extends Enum<E>> List<String> getEnumValues(final Class<E> enumClass) {
    List<String> enumValue = Lists.newArrayList();
    for (final Enum e: enumClass.getEnumConstants()) {
      enumValue.add(e.name());
    }
    return enumValue;
  }

  public static <E extends Enum<E>> E getEnum(final Class<E> enumClass, final String enumName, final E defaultValue) {
    if (enumName == null) {
      return defaultValue;
    }
    try {
      return Enum.valueOf(enumClass, enumName);
    } catch (final IllegalArgumentException ex) {
      return defaultValue;
    }
  }

  public static <E extends Enum<E>> E getUpperCaseEnum(final Class<E> enumClass, final String enumName) {
    if (enumName == null) {
      return null;
    }
    try {
      return Enum.valueOf(enumClass, enumName.toUpperCase());
    } catch (final IllegalArgumentException ex) {
      return null;
    }
  }

  public static <E extends Enum<E>> E getUpperCaseEnum(final Class<E> enumClass, final String enumName, final E defaultValue) {

    if (enumName == null) {
      return defaultValue;
    }
    try {
      return Enum.valueOf(enumClass, enumName.toUpperCase());
    } catch (final IllegalArgumentException ex) {
      return defaultValue;
    }
  }

  public static <E extends Enum<E>> E getCaseEnum(final Class<E> enumClass, final String enumName, final E defaultValue) {

    if (enumName == null) {
      return defaultValue;
    }

    String caseIgnoreName = null;
    if(isValidEnum(enumClass, enumName)) {
      caseIgnoreName = enumName;
    } else if(isValidEnum(enumClass, enumName.toUpperCase())) {
      caseIgnoreName = enumName.toUpperCase();
    } else if(isValidEnum(enumClass, enumName.toLowerCase())) {
      caseIgnoreName = enumName.toLowerCase();
    } else {
      return defaultValue;
    }

    return Enum.valueOf(enumClass, caseIgnoreName);
  }
}
