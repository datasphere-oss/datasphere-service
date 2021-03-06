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

package com.datasphere.server.query.druid.virtualcolumns;

/**
 * Created by aladin on 2019. 7. 2..
 */
public class MapVirtualColumn extends VirtualColumn {

  String keyDimension;
  String valueDimension;

  public MapVirtualColumn() {
  }

  public MapVirtualColumn(String keyDimension, String valueDimension, String outputName) {
    super(outputName);
    this.keyDimension = keyDimension;
    this.valueDimension = valueDimension;
  }

  public String getKeyDimension() {
    return keyDimension;
  }

  public void setKeyDimension(String keyDimension) {
    this.keyDimension = keyDimension;
  }

  public String getValueDimension() {
    return valueDimension;
  }

  public void setValueDimension(String valueDimension) {
    this.valueDimension = valueDimension;
  }
}
