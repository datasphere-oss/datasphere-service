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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;
import java.util.Map;

import com.datasphere.server.domain.workbook.configurations.datasource.DataSource;
import com.datasphere.server.query.druid.Dimension;
import com.datasphere.server.query.druid.Filter;
import com.datasphere.server.query.druid.Granularity;
import com.datasphere.server.query.druid.Query;
import com.datasphere.server.query.druid.SearchQuerySpec;
import com.datasphere.server.query.druid.serializers.GranularitySerializer;
import com.datasphere.server.query.druid.sorts.SearchHitSort;
import com.datasphere.server.query.druid.virtualcolumns.VirtualColumn;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonTypeName("search")
public class SearchQuery extends Query {

  Granularity granularity;

  @JsonInclude(Include.NON_NULL)
  Filter filter;

  List<String> intervals;

  List<Dimension> searchDimensions;

  List<VirtualColumn> virtualColumns;

  SearchQuerySpec query;

  SearchHitSort sort;

  Integer limit;

  Map<String, Object> context;

  public SearchQuery() {
    super();
  }

  @JsonSerialize(using = GranularitySerializer.class, typing = JsonSerialize.Typing.DYNAMIC)
  public Granularity getGranularity() {
    return granularity;
  }

  public void setGranularity(Granularity granularity) {
    this.granularity = granularity;
  }

  public List<String> getIntervals() {
    return intervals;
  }

  public void setIntervals(List<String> intervals) {
    this.intervals = intervals;
  }

  public Filter getFilter() {
    return filter;
  }

  public void setFilter(Filter filter) {
    this.filter = filter;
  }

  public List<Dimension> getSearchDimensions() {
    return searchDimensions;
  }

  public void setSearchDimensions(List<Dimension> searchDimensions) {
    this.searchDimensions = searchDimensions;
  }

  public List<VirtualColumn> getVirtualColumns() {
    return virtualColumns;
  }

  public void setVirtualColumns(List<VirtualColumn> virtualColumns) {
    this.virtualColumns = virtualColumns;
  }

  public SearchQuerySpec getQuery() {
    return query;
  }

  public void setQuery(SearchQuerySpec query) {
    this.query = query;
  }

  public SearchHitSort getSort() {
    return sort;
  }

  public void setSort(SearchHitSort sort) {
    this.sort = sort;
  }

  public Integer getLimit() {
    return limit;
  }

  public void setLimit(Integer limit) {
    this.limit = limit;
  }

  public Map<String, Object> getContext() {
    return context;
  }

  public void setContext(Map<String, Object> context) {
    this.context = context;
  }

  public static SearchQueryBuilder builder(DataSource dataSource) {
    return new SearchQueryBuilder(dataSource);
  }

}
