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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import com.datasphere.server.common.datasource.DataType;
import com.datasphere.server.domain.workbook.configurations.Limit;
import com.datasphere.server.domain.workbook.configurations.datasource.DataSource;
import com.datasphere.server.domain.workbook.configurations.field.DimensionField;
import com.datasphere.server.domain.workbook.configurations.field.Field;
import com.datasphere.server.domain.workbook.configurations.field.MeasureField;
import com.datasphere.server.domain.workbook.configurations.field.UserDefinedField;
import com.datasphere.server.query.druid.AbstractQueryBuilder;
import com.datasphere.server.query.druid.Aggregation;
import com.datasphere.server.query.druid.Dimension;
import com.datasphere.server.query.druid.Granularity;
import com.datasphere.server.query.druid.aggregations.LongSumAggregation;
import com.datasphere.server.query.druid.dimensions.DefaultDimension;
import com.datasphere.server.query.druid.filters.AndFilter;
import com.datasphere.server.query.druid.granularities.SimpleGranularity;
import com.datasphere.server.query.druid.limits.OrderByColumn;
import com.datasphere.server.query.druid.virtualcolumns.ExprVirtualColumn;
import com.datasphere.server.query.druid.virtualcolumns.IndexVirtualColumn;
import com.datasphere.server.query.druid.virtualcolumns.VirtualColumn;

/**
 * Created by aladin on 2016. 7. 5..
 */
public class TopNQueryBuilder extends AbstractQueryBuilder {

  private AndFilter filter = new AndFilter();

  private Dimension dimension;

  private Granularity granularity;

  private com.datasphere.server.query.druid.Limit limitSpec;

  private List<OrderByColumn> orderByColumns = Lists.newArrayList();

  private List<String> intervals = Lists.newArrayList();

  public TopNQueryBuilder(DataSource dataSource) {
    super(dataSource);
  }

  public TopNQueryBuilder initVirtualColumns(List<UserDefinedField> userDefinedFields) {

    setVirtualColumns(userDefinedFields);

    return this;
  }

  public TopNQueryBuilder fields(Field reqField) {

    String fieldName = reqField.getColunm();
    String aliasName = reqField.getAlias();

    if (reqField instanceof DimensionField) {

      // for virtual column
      if (virtualColumns.containsKey(fieldName)) {
        VirtualColumn column = virtualColumns.get(fieldName);
        if(column instanceof IndexVirtualColumn) {
          IndexVirtualColumn indexVirtualColumn = (IndexVirtualColumn) column;
          dimension = new DefaultDimension(indexVirtualColumn.getKeyDimension(), aliasName);
        } else if(column instanceof ExprVirtualColumn) {
          dimension = new DefaultDimension(fieldName, aliasName);
        }
        unUsedVirtualColumnName.remove(fieldName);
      } else {
        dimension = new DefaultDimension(fieldName, aliasName);
      }

    }
    else if (reqField instanceof MeasureField) {

      com.datasphere.server.datasource.Field datasourceField = this.metaFieldMap.get(fieldName);

      if( datasourceField.getType() == DataType.MAP ){

        String keyFieldName = "";
        for( com.datasphere.server.datasource.Field curField : datasourceField.getMappedField() ){
          if( curField.getRole() == com.datasphere.server.datasource.Field.FieldRole.DIMENSION ){
            keyFieldName = curField.getName();
            break;
          }
        }

        if( !keyFieldName.isEmpty() ){
          dimension = new DefaultDimension(keyFieldName, aliasName);
        }
      }

    }

    return this;
  }

  public TopNQueryBuilder filters(List<com.datasphere.server.domain.workbook.configurations.filter.Filter> reqFilters) {

    extractPartitions(reqFilters);

    setFilters(filter, reqFilters, intervals);

    return this;
  }


  public TopNQueryBuilder limit(Limit reqLimit) {
    return this;
  }

  @Override
  public TopNQuery build() {

    TopNQuery topNQuery = new TopNQuery();

    topNQuery.setDataSource(getDataSourceSpec(dataSource));
    topNQuery.setDimension(dimension);


    List<Aggregation> aggregations = Lists.newArrayList();
    aggregations.add(new LongSumAggregation("count", "count"));
    topNQuery.setAggregations(aggregations);


    String metric = "count";
    topNQuery.setMetric( metric );


    if (CollectionUtils.isEmpty(filter.getFields())) {
      topNQuery.setFilter(null);
    } else {
      topNQuery.setFilter(filter);
    }

    if (!virtualColumns.isEmpty()) {
      // 먼저, 사용하지 않는 사용자 정의 컬럼 삭제
      for (String removeColumnName : unUsedVirtualColumnName) {
        virtualColumns.remove(removeColumnName);
      }
      topNQuery.setVirtualColumns(Lists.newArrayList(virtualColumns.values()));
    }


    topNQuery.setThreshold( 10000 );

    if ( granularity != null ){
      topNQuery.setGranularity( granularity );
    } else {
      topNQuery.setGranularity(new SimpleGranularity("all"));
    }

    if (CollectionUtils.isEmpty(intervals)) {
      topNQuery.setIntervals(DEFAULT_INTERVALS);
    } else {
      topNQuery.setIntervals(intervals);
    }

    if(StringUtils.isNotEmpty(queryId)) {
      addQueryId(queryId);
    }

    if(context != null) {
      topNQuery.setContext(context);
    }

    return topNQuery;

  }



}
