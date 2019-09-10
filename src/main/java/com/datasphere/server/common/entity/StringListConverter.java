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

package com.datasphere.server.common.entity;

import com.google.common.collect.Lists;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.StringJoiner;

import javax.persistence.AttributeConverter;

/**
 * Created by aladin on 2019. 5. 10..
 */
public class StringListConverter implements AttributeConverter<List<String>, String> {

  private static final String SEP = ",";

  @Override
  public String convertToDatabaseColumn(List<String> objects) {

    if(CollectionUtils.isEmpty(objects)) {
      return null;
    }

    StringJoiner joiner = new StringJoiner(SEP);
    for(String str : objects) {
      joiner.add(str);
    }

    return joiner.toString();
  }

  @Override
  public List<String> convertToEntityAttribute(String s) {

    if(StringUtils.isEmpty(s)) {
      return null;
    }

    return Lists.newArrayList(StringUtils.split(SEP));
  }
}
