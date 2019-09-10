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

package com.datasphere.server.common.datasource;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Common Logical Type
 */
public enum LogicalType {
  STRING,
  BOOLEAN,
  NUMBER,       // Integer or Double
  INTEGER,
  DOUBLE,
  TIMESTAMP,
  LNG,
  LNT,
  GEO_POINT,          // [lat(latitude),lon(longitude)] structure for GEO
  GEO_LINE,
  GEO_POLYGON,
  ARRAY,
  STRUCT,
  MAP_KEY,
  MAP_VALUE,
  IP_V4,         // IPv4 Address
  DISTRICT,      // District
  EMAIL,
  SEX,
  CREDIT_CARD,   // Credit card
  NIN,           // National Indentification Number (eq. SSN)
  POSTAL_CODE,
  PHONE_NUMBER,  // Phone Number
  URL,
  HTTP_CODE;

  public List<String> getGeoPointKeys() {
    return Lists.newArrayList("lat", "lon", "coord");
  }

  public boolean isGeoType() {
    if (this == GEO_POINT || this == GEO_LINE || this == GEO_POLYGON) {
      return true;
    }
    return false;
  }

  public boolean isShape() {
    if (this == GEO_LINE || this == GEO_POLYGON) {
      return true;
    }
    return false;
  }

  public boolean isPoint() {
    if (this == GEO_POINT) {
      return true;
    }
    return false;
  }

  public String toEngineMetricType() {
    switch (this) {
      case STRING:
        return "string";
      case INTEGER:
        return "long";
      case ARRAY:
        return "array.double";
      case DOUBLE:
      default:
        return "double";
    }
  }
}
