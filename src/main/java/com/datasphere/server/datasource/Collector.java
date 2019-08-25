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

package com.datasphere.server.datasource;

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

  //Must be linked to user information.

  //Datasource Must include linkage information.


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
