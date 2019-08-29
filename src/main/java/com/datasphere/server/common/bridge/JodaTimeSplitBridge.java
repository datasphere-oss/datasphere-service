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

package com.datasphere.server.common.bridge;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.TwoWayFieldBridge;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * Created by aladin on 2019. 1. 4..
 * http://in.relation.to/2015/07/24/hibernate-search-complex-type-query/
 */
public class JodaTimeSplitBridge implements TwoWayFieldBridge {

  /**
   * Set year, month and day in separate fields
   */
  @Override
  public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
    DateTime datetime = (DateTime) value;

    if(name.endsWith("year")) {
      luceneOptions.addFieldToDocument(
          name, String.valueOf(datetime.getYear()), document
      );
    } else if(name.endsWith("month")) {
      luceneOptions.addFieldToDocument(
          name, String.valueOf(datetime.getMonthOfYear()), document
      );
    } else if(name.endsWith("day")) {
      luceneOptions.addFieldToDocument(
          name, String.valueOf(datetime.getDayOfMonth()), document
      );
    } else if(name.endsWith("ymd")) {
      luceneOptions.addFieldToDocument(
          name, datetime.toString("yyyy-MM-dd"), document
      );
    } else if(name.endsWith("mils")) {
      luceneOptions.addFieldToDocument(
          name, String.format("%013d", datetime.getMillis()), document
      );
    }

  }

  @Override
  public Object get(String name, Document document) {
    IndexableField fieldymd = document.getField(name + ".ymd");
    DateTime value = DateTime.parse(fieldymd.stringValue(), DateTimeFormat.forPattern("yyyy-MM-dd"));
    return String.valueOf(value);
  }

  @Override
  public String objectToString(Object date) {
    DateTime datetime = (DateTime) date;
    return datetime.toString("yyyy-MM-dd");
  }
}
