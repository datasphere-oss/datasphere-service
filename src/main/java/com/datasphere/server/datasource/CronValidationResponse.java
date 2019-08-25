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
import java.util.List;

public class CronValidationResponse implements Serializable {

  boolean valid;

  String error;

  List<String> nextTriggerTimes;

  public CronValidationResponse(boolean valid, String error) {
    this.valid = valid;
    this.error = error;
  }

  public CronValidationResponse(boolean valid, List<String> nextTriggerTimes) {
    this.valid = valid;
    this.nextTriggerTimes = nextTriggerTimes;
  }

  public boolean isValid() {
    return valid;
  }

  public String getError() {
    return error;
  }

  public List<String> getNextTriggerTimes() {
    return nextTriggerTimes;
  }
}
