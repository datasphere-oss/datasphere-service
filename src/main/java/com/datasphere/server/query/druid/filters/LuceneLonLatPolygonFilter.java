/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specic language governing permissions and
 * limitations under the License.
 */
package com.datasphere.server.query.druid.filters;

import com.datasphere.server.domain.workbook.configurations.filter.SpatialShapeFilter;
import com.datasphere.server.query.druid.Filter;
import com.datasphere.server.query.druid.ShapeFormat;
import com.datasphere.server.query.druid.SpatialOperations;

public class LuceneLonLatPolygonFilter extends LuceneSpatialFilter implements Filter {

  public LuceneLonLatPolygonFilter() {
  }

  public LuceneLonLatPolygonFilter(String field,
                                   SpatialOperations operation,
                                   ShapeFormat shapeFormat,
                                   String shapeString) {

    super(field, operation, shapeFormat, shapeString);
  }

  public LuceneLonLatPolygonFilter(SpatialShapeFilter filter, boolean isPoint) {
    this.field = filter.getField();
    if (isPoint) this.field += ".coord";

    this.operation = SpatialOperations.WITHIN;
    this.shapeFormat = filter.getShapeFormat();
    this.shapeString = filter.getShapeString();
  }

  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public SpatialOperations getOperation() {
    return operation;
  }

  public ShapeFormat getShapeFormat() {
    return shapeFormat;
  }

  public String getShapeString() {
    return shapeString;
  }
}
