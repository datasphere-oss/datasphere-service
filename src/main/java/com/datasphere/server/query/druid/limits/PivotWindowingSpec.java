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

package com.datasphere.server.query.druid.limits;

import javax.validation.constraints.NotNull;

/**
 *
 */
public class PivotWindowingSpec extends WindowingSpec {

  @NotNull
  PivotSpec pivotSpec;

  public PivotWindowingSpec() {
  }

  public PivotSpec getPivotSpec() {
    return pivotSpec;
  }

  public void setPivotSpec(PivotSpec pivotSpec) {
    this.pivotSpec = pivotSpec;
  }
}
