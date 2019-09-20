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

package com.datasphere.server.common.geospatial.geojson;

import java.util.HashMap;
import java.util.Map;

public class Crs {

  public final static Crs WGS84 = Crs.of("urn:ogc:def:crs:EPSG::4326");

  public final static Crs EPSG4326 = WGS84;

  public final static Crs DEFAULT = WGS84;

  private String type;

  private Map<String, Object> properties;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Map<String, Object> getProperties() {
    return properties;
  }

  public void setProperties(Map<String, Object> properties) {
    this.properties = properties;
  }

  /**
   * create a name style CRS
   *
   * @param uri name string
   * @return name style crs
   */
  public static Crs of(final String uri) {
    Crs crs = new Crs();
    crs.setType("name");
    crs.setProperties(new HashMap<String, Object>() {
      {
        put("name", uri);
      }
    });
    return crs;
  }

  public static String getName(Crs crs) {
    if (crs == null || crs.getProperties() == null || crs.getProperties().get("name") == null) {
      return null;
    }
    return crs.getProperties().get("name").toString();
  }
}
