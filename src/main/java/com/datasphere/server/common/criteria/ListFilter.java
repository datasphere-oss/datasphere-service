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

/**
 *
 */
public class ListFilter {
  ListCriterionKey criterionKey;
  String filterKey;
  String filterSubKey;
  String filterName;
  String filterValue;
  String filterSubValue;


  public ListFilter(){

  }

  public ListFilter(ListCriterionKey criterionKey, String filterKey, String filterSubKey,
                              String filterValue, String filterSubValue, String filterName){
    this.criterionKey = criterionKey;
    this.filterKey = filterKey;
    this.filterSubKey = filterSubKey;
    this.filterValue = filterValue;
    this.filterSubValue = filterSubValue;
    this.filterName = filterName;
  }

  public ListFilter(ListCriterionKey criterionKey, String filterKey, String filterValue, String filterName){
    this.criterionKey = criterionKey;
    this.filterKey = filterKey;
    this.filterValue = filterValue;
    this.filterName = filterName;
  }

  public ListFilter(String filterKey, String filterValue, String filterName){
    this.filterKey = filterKey;
    this.filterValue = filterValue;
    this.filterName = filterName;
  }

  public ListCriterionKey getCriterionKey() {
    return criterionKey;
  }

  public void setCriterionKey(ListCriterionKey criterionKey) {
    this.criterionKey = criterionKey;
  }

  public String getFilterKey() {
    return filterKey;
  }

  public void setFilterKey(String filterKey) {
    this.filterKey = filterKey;
  }

  public String getFilterSubKey() {
    return filterSubKey;
  }

  public void setFilterSubKey(String filterSubKey) {
    this.filterSubKey = filterSubKey;
  }

  public String getFilterName() {
    return filterName;
  }

  public void setFilterName(String filterName) {
    this.filterName = filterName;
  }

  public String getFilterValue() {
    return filterValue;
  }

  public void setFilterValue(String filterValue) {
    this.filterValue = filterValue;
  }

  public String getFilterSubValue() {
    return filterSubValue;
  }

  public void setFilterSubValue(String filterSubValue) {
    this.filterSubValue = filterSubValue;
  }
}
