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

package com.datasphere.server.query.druid.queries;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;

import com.datasphere.server.common.datasource.LogicalType;
import com.datasphere.server.domain.workbook.configurations.datasource.DataSource;
import com.datasphere.server.domain.workbook.configurations.field.Field;
import com.datasphere.server.domain.workbook.configurations.field.UserDefinedField;
import com.datasphere.server.domain.workbook.configurations.filter.Filter;
import com.datasphere.server.query.druid.AbstractQueryBuilder;
import com.datasphere.server.query.druid.Granularity;
import com.datasphere.server.query.druid.aggregations.DoubleMaxAggregation;
import com.datasphere.server.query.druid.aggregations.DoubleMinAggregation;
import com.datasphere.server.query.druid.aggregations.EnvelopeAggregation;
import com.datasphere.server.query.druid.filters.AndFilter;
import com.datasphere.server.query.druid.funtions.ShapeFromWktFunc;
import com.datasphere.server.query.druid.granularities.SimpleGranularity;
import com.datasphere.server.query.druid.postaggregations.ExprPostAggregator;
import com.datasphere.server.query.druid.virtualcolumns.ExprVirtualColumn;

/**
 * Created by aladin on 2016. 7. 5..
 */
public class TimeseriesQueryBuilder extends AbstractQueryBuilder {

  private AndFilter filter = new AndFilter();

  private Granularity granularity;

  private Set<String> outputColumns = Sets.newLinkedHashSet();

  private List<String> intervals = Lists.newArrayList();

  public TimeseriesQueryBuilder(DataSource dataSource) {
    super(dataSource);
  }

  public TimeseriesQueryBuilder initVirtualColumns(List<UserDefinedField> userDefinedFields) {

    setVirtualColumns(userDefinedFields);

    return this;
  }

  public TimeseriesQueryBuilder fields(List<Field> reqFields) {

    return this;
  }

  public TimeseriesQueryBuilder filters(List<Filter> reqFilters) {

    extractPartitions(reqFilters);

    setFilters(filter, reqFilters, intervals);

    return this;
  }

  public TimeseriesQueryBuilder geoBoundary(String geometry, boolean isShape) {

    String upperCornerColumn = "__upperCorner";
    String lowerCornerColumn = "__lowerCorner";
    outputColumns.add(upperCornerColumn);
    outputColumns.add(lowerCornerColumn);

    if (isShape) {
      String shapeColumn = "__geo_shape";
      virtualColumns.put(shapeColumn, new ExprVirtualColumn(new ShapeFromWktFunc(geometry).toExpression(), shapeColumn));

      String envelopColumn = "__geo_envelop";
      aggregations.add(new EnvelopeAggregation(envelopColumn, shapeColumn));

      postAggregations.add(new ExprPostAggregator(upperCornerColumn + " = concat(\"" + envelopColumn + "\"[0], ' ', \"" + envelopColumn + "\"[3])"));
      postAggregations.add(new ExprPostAggregator(lowerCornerColumn + " = concat(\"" + envelopColumn + "\"[1], ' ', \"" + envelopColumn + "\"[2])"));

    } else {
      String latColumn = geometry + "." + LogicalType.GEO_POINT.getGeoPointKeys().get(0);
      String lonColumn = geometry + "." + LogicalType.GEO_POINT.getGeoPointKeys().get(1);

      String maxLatColumn = "__max_lat";
      String minLatColumn = "__min_lat";
      String maxLonColumn = "__max_lon";
      String minLonColumn = "__min_lon";

      aggregations.add(new DoubleMaxAggregation(maxLatColumn, latColumn));
      aggregations.add(new DoubleMinAggregation(minLatColumn, latColumn));
      aggregations.add(new DoubleMaxAggregation(maxLonColumn, lonColumn));
      aggregations.add(new DoubleMinAggregation(minLonColumn, lonColumn));

      postAggregations.add(new ExprPostAggregator(upperCornerColumn + " = concat(" + minLonColumn + ", ' ', " + maxLatColumn + ")"));
      postAggregations.add(new ExprPostAggregator(lowerCornerColumn + " = concat(" + maxLonColumn + ", ' ', " + minLatColumn + ")"));

    }

    return this;
  }

  @Override
  public TimeseriesQuery build() {

    TimeseriesQuery timeseriesQuery = new TimeseriesQuery();

    timeseriesQuery.setDataSource(getDataSourceSpec(dataSource));
    timeseriesQuery.setAggregations(aggregations);
    timeseriesQuery.setPostAggregations(postAggregations);

    timeseriesQuery.setOutputColumns(outputColumns);

    if (CollectionUtils.isEmpty(filter.getFields())) {
      timeseriesQuery.setFilter(null);
    } else {
      timeseriesQuery.setFilter(filter);
    }

    if (virtualColumns != null) {
      // 먼저, 사용하지 않는 사용자 정의 컬럼 삭제
      for (String removeColumnName : unUsedVirtualColumnName) {
        virtualColumns.remove(removeColumnName);
      }
      timeseriesQuery.setVirtualColumns(Lists.newArrayList(virtualColumns.values()));
    }

    if (granularity != null) {
      timeseriesQuery.setGranularity(granularity);
    } else {
      timeseriesQuery.setGranularity(new SimpleGranularity("all"));
    }

    if (CollectionUtils.isEmpty(intervals)) {
      timeseriesQuery.setIntervals(DEFAULT_INTERVALS);
    } else {
      timeseriesQuery.setIntervals(intervals);
    }

    if (StringUtils.isNotEmpty(queryId)) {
      addQueryId(queryId);
    }

    timeseriesQuery.setContext(context);

    return timeseriesQuery;

  }


}
