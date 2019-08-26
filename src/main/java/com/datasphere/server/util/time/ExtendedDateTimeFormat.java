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

package com.datasphere.server.util.time;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.regex.Pattern;

/**
 * http://higher-state.blogspot.com/2013/04/extended-joda-datetime-formatter-to.html
 */
public class ExtendedDateTimeFormat extends DateTimeFormat {

  public static final Pattern PATTERN_PATH_PARAM = Pattern.compile("^([^Q]*)(|Q{1,4})([^Q]*)$");

  public static DateTimeFormatter forPattern(String pattern) {
    return DateTimeFormat.forPattern(pattern);
  }
}
