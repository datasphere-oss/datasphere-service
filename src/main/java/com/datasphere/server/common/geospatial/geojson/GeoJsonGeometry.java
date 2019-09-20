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

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = PointGeometry.class, name = "Point"),
    @JsonSubTypes.Type(value = MultiPointGeometry.class, name = "MultiPoint"),
    @JsonSubTypes.Type(value = LineStringGeometry.class, name = "LineString"),
    @JsonSubTypes.Type(value = MultiLineStringGeometry.class, name = "MultiLineString"),
    @JsonSubTypes.Type(value = PolygonGeometry.class, name = "Polygon"),
    @JsonSubTypes.Type(value = MultiPolygonGeometry.class, name = "MultiPolygon")
})
public interface GeoJsonGeometry {
}
