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

package com.datasphere.server.query.druid.aggregations;

import com.fasterxml.jackson.annotation.JsonTypeName;

import com.datasphere.server.query.druid.Aggregation;
import com.datasphere.server.query.druid.Filter;

@JsonTypeName("filtered")
public class FilteredAggregation implements Aggregation{
  Filter filter;
  Aggregation aggregation;

  public Filter getFilter() {

    return filter;
  }

  public void setFilter(Filter filter) {
    this.filter = filter;
  }

  public Aggregation getAggregation() {
    return aggregation;
  }

  public void setAggregation(Aggregation aggregation) {
    this.aggregation = aggregation;
  }

  public String getName(){
    return aggregation.getName();
  }
}
