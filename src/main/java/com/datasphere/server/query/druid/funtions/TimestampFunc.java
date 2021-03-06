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

package com.datasphere.server.query.druid.funtions;

import org.apache.commons.lang3.StringUtils;

import com.datasphere.server.util.PolarisUtils;

public class TimestampFunc {

  private static final String FUNC_NAME = "timestamp";

  String field;

  String format;

  String timezone;

  String locale;

  public TimestampFunc(String field, String format, String timezone, String locale) {

    this.field = field;

    this.format = StringUtils.isEmpty(format) ? "yyyy-MM-dd HH:mm:ss" : format;
    this.timezone = StringUtils.isEmpty(timezone) ? "UTC" : timezone;
    this.locale = StringUtils.isEmpty(locale) ? "en" : locale;
  }

  public String toExpression() {
    StringBuilder sb = new StringBuilder();
    sb.append(FUNC_NAME).append("(\"");
    sb.append(field);
    sb.append("\",").append("format=").append("'").append(PolarisUtils.escapeTimeFormatChars(format)).append("'");
    sb.append(",").append("locale=").append("'").append(locale).append("'");
    sb.append(",").append("timezone=").append("'").append(timezone).append("'");
    sb.append(")");
    return sb.toString();
  }
}
