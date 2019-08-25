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

package com.datasphere.server.common;

import java.io.Serializable;

public class ProgressResponse implements Serializable {

  Integer progress;

  String message;

  Object results;

  public ProgressResponse(Integer progress, String message) {
    this.progress = progress;
    this.message = message;
  }

  public ProgressResponse(Integer progress, Enum<?> enumMessage) {
    this.progress = progress;
    this.message = enumMessage.toString();
  }

  public ProgressResponse(Integer progress, Enum<?> enumMessage, Object results) {
    this.progress = progress;
    this.message = enumMessage.toString();
    this.results = results;
  }

  public Integer getProgress() {
    return progress;
  }

  public void setProgress(Integer progress) {
    this.progress = progress;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Object getResults() {
    return results;
  }

  public void setResults(Object results) {
    this.results = results;
  }

  @Override
  public String toString() {
    return "ProgressResponse{" +
        "progress=" + progress +
        ", message='" + message + '\'' +
        ", results=" + results +
        '}';
  }
}
