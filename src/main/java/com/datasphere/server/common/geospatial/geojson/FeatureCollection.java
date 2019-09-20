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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class FeatureCollection implements GeoJson {

  Crs crs;

  List<Feature> features = Lists.newArrayList();

  Map<String, MinMaxRange> valueRange = Maps.newLinkedHashMap();

  public FeatureCollection() {
  }

  public FeatureCollection(List<Feature> features) {
    this.crs = Crs.DEFAULT;
    this.features = features;
  }

  public void addFeature(Feature feature) {
    features.add(feature);
  }

  public void addMinMaxValues(String field, MinMaxRange minMaxRange) {
    valueRange.put(field, minMaxRange);
  }

  public Crs getCrs() {
    if (crs == null) {
      return Crs.DEFAULT;
    }
    return crs;
  }

  public List<Feature> getFeatures() {
    return features;
  }

  public Map<String, MinMaxRange> getValueRange() {
    return valueRange;
  }

  public static class MinMaxRange implements Serializable {

    Double minValue;

    Double maxValue;

    public MinMaxRange() {
    }

    public MinMaxRange(Double minValue, Double maxValue) {
      this.minValue = minValue;
      this.maxValue = maxValue;
    }

    public Double getMinValue() {
      return minValue;
    }

    public Double getMaxValue() {
      return maxValue;
    }
  }
}
