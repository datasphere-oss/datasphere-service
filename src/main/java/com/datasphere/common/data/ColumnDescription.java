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

package com.datasphere.common.data;

import java.util.Map;

/**
 * The type Column description.
 */
public class ColumnDescription {
  /**
   * The Logical Name.
   */
  String name;
  /**
   * The Logical Type.
   */
  String type;
  /**
   * The Physical name.
   */
  String physicalName;
  /**
   * The Physical type.
   */
  String physicalType;
  /**
   * The Format.
   */
  Map<String, Object> format;
  /**
   * The Additionals.
   */
  Map<String, Object> additionals;

  /**
   * Instantiates a new Column description.
   *
   * @param name         the name
   * @param type         the type
   * @param physicalName the physical name
   * @param physicalType the physical type
   * @param format       the format
   * @param additionals  the additionals
   */
  public ColumnDescription(String name, String type, String physicalName, String physicalType, Map<String, Object> format, Map<String, Object> additionals) {
    this.name = name;
    this.type = type;
    this.physicalName = physicalName;
    this.physicalType = physicalType;
    this.format = format;
    this.additionals = additionals;
  }

  /**
   * Gets name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets name.
   *
   * @param name the name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets type.
   *
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * Sets type.
   *
   * @param type the type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Gets physical name.
   *
   * @return the physical name
   */
  public String getPhysicalName() {
    return physicalName;
  }

  /**
   * Sets physical name.
   *
   * @param physicalName the physical name
   */
  public void setPhysicalName(String physicalName) {
    this.physicalName = physicalName;
  }

  /**
   * Gets physical type.
   *
   * @return the physical type
   */
  public String getPhysicalType() {
    return physicalType;
  }

  /**
   * Sets physical type.
   *
   * @param physicalType the physical type
   */
  public void setPhysicalType(String physicalType) {
    this.physicalType = physicalType;
  }

  /**
   * Gets format.
   *
   * @return the format
   */
  public Map<String, Object> getFormat() {
    return format;
  }

  /**
   * Sets format.
   *
   * @param format the format
   */
  public void setFormat(Map<String, Object> format) {
    this.format = format;
  }

  /**
   * Gets additionals.
   *
   * @return the additionals
   */
  public Map<String, Object> getAdditionals() {
    return additionals;
  }

  /**
   * Sets additionals.
   *
   * @param additionals the additionals
   */
  public void setAdditionals(Map<String, Object> additionals) {
    this.additionals = additionals;
  }
}
