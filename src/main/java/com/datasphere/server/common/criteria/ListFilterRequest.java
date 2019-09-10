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

package com.datasphere.server.common.criteria;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import com.datasphere.server.common.entity.SearchParamValidator;
import com.datasphere.server.common.exception.BadRequestException;

/**
 *
 */
public abstract class ListFilterRequest {
  List<String> createdBy;
  List<String> modifiedBy;
  DateTime createdTimeFrom;
  DateTime createdTimeTo;
  DateTime modifiedTimeFrom;
  DateTime modifiedTimeTo;
  String containsText;

  public List<String> getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(List<String> createdBy) {
    this.createdBy = createdBy;
  }

  public List<String> getModifiedBy() {
    return modifiedBy;
  }

  public void setModifiedBy(List<String> modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

  public DateTime getCreatedTimeFrom() {
    return createdTimeFrom;
  }

  public void setCreatedTimeFrom(DateTime createdTimeFrom) {
    this.createdTimeFrom = createdTimeFrom;
  }

  public DateTime getCreatedTimeTo() {
    return createdTimeTo;
  }

  public void setCreatedTimeTo(DateTime createdTimeTo) {
    this.createdTimeTo = createdTimeTo;
  }

  public DateTime getModifiedTimeFrom() {
    return modifiedTimeFrom;
  }

  public void setModifiedTimeFrom(DateTime modifiedTimeFrom) {
    this.modifiedTimeFrom = modifiedTimeFrom;
  }

  public DateTime getModifiedTimeTo() {
    return modifiedTimeTo;
  }

  public void setModifiedTimeTo(DateTime modifiedTimeTo) {
    this.modifiedTimeTo = modifiedTimeTo;
  }

  public String getContainsText() {
    return containsText;
  }

  public void setContainsText(String containsText) {
    this.containsText = containsText;
  }

  public <E extends Enum<E>> List<E> getEnumList(List<String> enumStrList, Class<E> enumClass, String propName) throws BadRequestException{
    List<E> enumList = null;
    if(enumStrList != null && !enumStrList.isEmpty()){
      enumList = new ArrayList<>();
      for(String enumStr : enumStrList){
        enumList.add(SearchParamValidator.enumUpperValue(enumClass, enumStr, propName));
      }
    }
    return enumList;
  }
}
