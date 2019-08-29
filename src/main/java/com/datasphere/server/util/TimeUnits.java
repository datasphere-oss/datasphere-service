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

package com.datasphere.server.util;

/**
 * Created by aladin on 2019. 8. 31..
 */
public enum TimeUnits {

  MILLISECOND(1L),
  SECOND(1000L),
  MINUTE(60 * 1000L),
  HOUR(60 * 60 * 1000L),
  DAY(24 * 60 * 60 * 1000L),
  WEEK(7 * 24 * 60 * 60 * 1000L),
  MONTH(31 * 24 * 60 * 60 * 1000L),
  QUARTER(3 * 31 * 24 * 60 * 60 * 1000L),
  YEAR(365 * 24 * 60 * 60 * 1000L);

  long mils;

  TimeUnits(long mils) {
    this.mils = mils;
  }

  public long getMils() {
    return this.mils;
  }
}
