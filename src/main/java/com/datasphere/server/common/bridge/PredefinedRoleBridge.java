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

import static com.datasphere.server.domain.user.role.Role.PREDEFINED_GROUP_ADMIN;
import static com.datasphere.server.domain.user.role.Role.PREDEFINED_GROUP_SUPER;
import static com.datasphere.server.domain.user.role.Role.PREDEFINED_GROUP_USER;

/**
 * Created by aladin on 2019. 2. 13..
 */
public class PredefinedRoleBridge implements TwoWayFieldBridge {

  @Override
  public Object get(String name, Document document) {
    IndexableField field = document.getField(name);
    return field.stringValue();
  }

  @Override
  public String objectToString(Object object) {
    return object.toString();
  }

  @Override
  public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {

    String indexValue;
    if(name.endsWith("name.predefined")) {
      if (PREDEFINED_GROUP_ADMIN.equals(value)) {
        indexValue = "!0001";
      } else if (PREDEFINED_GROUP_SUPER.equals(value)) {
        indexValue = "!0002";
      } else if (PREDEFINED_GROUP_USER.equals(value)) {
        indexValue = "!0003";
      } else {
        indexValue = "!0004";
      }
    } else {
      indexValue = String.valueOf(value);
    }

    luceneOptions.addFieldToDocument(name, indexValue, document);

  }
}
