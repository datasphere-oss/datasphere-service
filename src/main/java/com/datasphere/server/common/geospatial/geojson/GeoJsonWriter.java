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

import com.fasterxml.jackson.databind.JsonNode;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.datasphere.server.query.druid.AbstractQueryBuilder.GEOMETRY_COLUMN_NAME;

public class GeoJsonWriter {

  private static Logger LOGGER = LoggerFactory.getLogger(GeoJsonWriter.class);

  GeoJson geoJson;

  WKTReader wktReader;

  String idPrefix;

  int idx;

  boolean seekMinMaxField = true;

  Map<String, List<Double>> valueRanges = Maps.newLinkedHashMap();

  public GeoJsonWriter() {
    geoJson = new FeatureCollection();
    wktReader = new WKTReader();
    idPrefix = "f_" + RandomStringUtils.randomAlphanumeric(8) + "_";
  }

  public GeoJson write(JsonNode jsonNode) {
    if (jsonNode.isArray()) {
      FeatureCollection collection = new FeatureCollection();
      jsonNode.forEach(node -> collection.addFeature(writeFeature(node)));

      if (MapUtils.isNotEmpty(valueRanges)) {
        valueRanges.forEach((field, values) -> collection.addMinMaxValues(field, calculateMinMax(values)));
      }

      return collection;
    } else {
      return writeFeature(jsonNode);
    }
  }

  public Feature writeFeature(JsonNode jsonNode) {

    Feature feature = new Feature();
    feature.setId(idPrefix + idx++);

    Iterator<Map.Entry<String, JsonNode>> nodes = jsonNode.fields();

    while (nodes.hasNext()) {
      Map.Entry<String, JsonNode> entry = nodes.next();

      if (GEOMETRY_COLUMN_NAME.equals(entry.getKey())) {
        Geometry geometry = null;
        String wkt = entry.getValue().asText();
        try {
          geometry = wktReader.read(wkt);
        } catch (ParseException e) {
          LOGGER.warn("Fail to parse WKT : {}", wkt);
          continue;
        }
        feature.setGeometry(write(geometry));
      } else {
        String key = entry.getKey();
        JsonNode valueNode = entry.getValue();
        feature.addProperties(key, valueNode);
        if (seekMinMaxField) {
          if (entry.getValue().isNumber()) {
            valueRanges.put(key, Lists.newArrayList(valueNode.asDouble(0.0)));
          }
        } else {
          if (valueRanges.containsKey(key)) {
            valueRanges.get(key).add(valueNode.asDouble(0.0));
          }
        }
      }

    }

    seekMinMaxField = false;

    return feature;
  }

  public GeoJsonGeometry write(Geometry geometry) {
    Class<? extends Geometry> c = geometry.getClass();
    if (c.equals(Point.class)) {
      return convert((Point) geometry);
    } else if (c.equals(LineString.class)) {
      return convert((LineString) geometry);
    } else if (c.equals(Polygon.class)) {
      return convert((Polygon) geometry);
    } else if (c.equals(MultiPoint.class)) {
      return convert((MultiPoint) geometry);
    } else if (c.equals(MultiLineString.class)) {
      return convert((MultiLineString) geometry);
    } else if (c.equals(MultiPolygon.class)) {
      return convert((MultiPolygon) geometry);
    } else {
      throw new UnsupportedOperationException();
    }
  }

  private FeatureCollection.MinMaxRange calculateMinMax(List<Double> values) {

    Double minLimit = Double.NEGATIVE_INFINITY;
    Double maxLimit = Double.POSITIVE_INFINITY;
    Double min = maxLimit;
    Double max = minLimit;

    for (int i = 0; i < values.size(); i++) {
      Double target = values.get(i);

      if (target <= maxLimit && target >= minLimit) {
        if (target > max) {
          max = target;
        }
        if (target < min) {
          min = target;
        }
      }
    }

    return new FeatureCollection.MinMaxRange(min, max);
  }

  PointGeometry convert(Point point) {
    PointGeometry geometry = new PointGeometry(convert(point.getCoordinate()));
    return geometry;
  }

  MultiPointGeometry convert(MultiPoint multiPoint) {
    return new MultiPointGeometry(convert(multiPoint.getCoordinates()));
  }

  LineStringGeometry convert(LineString lineString) {
    return new LineStringGeometry(convert(lineString.getCoordinates()));
  }

  MultiLineStringGeometry convert(MultiLineString multiLineString) {
    int size = multiLineString.getNumGeometries();
    double[][][] lineStrings = new double[size][][];
    for (int i = 0; i < size; i++) {
      lineStrings[i] = convert(multiLineString.getGeometryN(i).getCoordinates());
    }
    return new MultiLineStringGeometry(lineStrings);
  }

  PolygonGeometry convert(Polygon polygon) {
    int size = polygon.getNumInteriorRing() + 1;
    double[][][] rings = new double[size][][];
    rings[0] = convert(polygon.getExteriorRing().getCoordinates());
    for (int i = 0; i < size - 1; i++) {
      rings[i + 1] = convert(polygon.getInteriorRingN(i).getCoordinates());
    }
    return new PolygonGeometry(rings);
  }

  MultiPolygonGeometry convert(MultiPolygon multiPolygon) {
    int size = multiPolygon.getNumGeometries();
    double[][][][] polygons = new double[size][][][];
    for (int i = 0; i < size; i++) {
      polygons[i] = convert((Polygon) multiPolygon.getGeometryN(i)).getCoordinates();
    }
    return new MultiPolygonGeometry(polygons);
  }

  double[] convert(Coordinate coordinate) {
    if (Double.isNaN(coordinate.getZ())) {
      return new double[]{coordinate.getX(), coordinate.getY()};
    } else {
      return new double[]{coordinate.getX(), coordinate.getY(), coordinate.getZ()};
    }
  }

  double[][] convert(Coordinate[] coordinates) {
    double[][] array = new double[coordinates.length][];
    for (int i = 0; i < coordinates.length; i++) {
      array[i] = convert(coordinates[i]);
    }
    return array;
  }
}
