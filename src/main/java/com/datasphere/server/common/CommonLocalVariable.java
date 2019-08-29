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

/**
 * Created by aladin on 2019. 11. 20..
 */
public class CommonLocalVariable {

  private static final ThreadLocal<LocalVariable> managementsThreadLocal =
      new ThreadLocal<LocalVariable>() {
        protected LocalVariable initialValue() {
          return new LocalVariable();
        }
      };

  public static LocalVariable getLocalVariable() {
    return managementsThreadLocal.get();
  }

  public static void remove() {
    managementsThreadLocal.remove();
  }

  public static void setQueryId(String queryId) {
    LocalVariable localVariable = getLocalVariable();
    localVariable.setQueryId(queryId);
  }

  public static void generateQueryId() {
    LocalVariable localVariable = getLocalVariable();
    localVariable.setQueryId(IdGenerator.queryId());
  }

  public static String getQueryId() {
    return getLocalVariable().getQueryId();
  }

  public static class LocalVariable {

    /**
     * Query Id
     */
    String queryId;

    public String getQueryId() {
      return queryId == null ? "UNKNOWN" : queryId;
    }

    public void setQueryId(String queryId) {
      this.queryId = queryId;
    }
  }
}
