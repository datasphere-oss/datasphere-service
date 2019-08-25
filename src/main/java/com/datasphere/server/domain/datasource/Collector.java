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

package app.metatron.discovery.domain.datasource;

import java.io.Serializable;

/**
 * Status of collector
 */
public class Collector implements Serializable{
  public static enum Status{
    RUNNING,
    STOPPED,
    UNINSTALL
  }

  Status staus;

  private String ipAddress;

  //사용자 정보와 연계해야 한다.

  //Datasource 연계 정보를 포함해야 한다.


  public Status getStaus() {
    return staus;
  }

  public void setStaus(Status staus) {
    this.staus = staus;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }
}
