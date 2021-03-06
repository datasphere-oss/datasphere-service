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


import com.fasterxml.jackson.annotation.JsonTypeName;

import com.datasphere.server.domain.workbook.configurations.datasource.DataSource;
import com.datasphere.server.query.druid.Query;

@JsonTypeName("timeBoundary")
public class TimeBoundaryQuery extends Query {

  String bound;

  public TimeBoundaryQuery() {
  }

  public String getBound() {
    return bound;
  }

  public void setBound(String bound) {
    this.bound = bound;
  }

  public static TimeBoundaryQueryBuilder builder(DataSource dataSource) {
    return new TimeBoundaryQueryBuilder(dataSource);
  }
}
