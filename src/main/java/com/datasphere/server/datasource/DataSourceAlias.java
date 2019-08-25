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

package com.datasphere.server.datasource;

import com.google.common.collect.Maps;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import app.metatron.discovery.common.GlobalObjectMapper;
import app.metatron.discovery.common.KeepAsJsonDeserialzier;
import app.metatron.discovery.common.entity.Spec;
import app.metatron.discovery.domain.AbstractHistoryEntity;
import app.metatron.discovery.domain.MetatronDomain;

@Entity
@Table(name = "datasource_alias")
public class DataSourceAlias extends AbstractHistoryEntity implements MetatronDomain<Long> {
  /**
   * ID
   */
  @Column(name = "id")
  @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
  @GenericGenerator(name = "native", strategy = "native")
  @Id
  private Long id;

  /**
   * Datasource Id
   */
  @Column(name = "alias_ds_id")
  @NotBlank
  private String dataSourceId;

  /**
   * Bashboard Id
   */
  @Column(name = "alias_db_id")
  @NotBlank
  private String dashBoardId;

  /**
   * alias field name
   */
  @Column(name = "alias_target_field")
  @NotBlank
  private String fieldName;

  /**
   * alias field name
   */
  @Column(name = "alias_field_name")
  private String nameAlias;

  /**
   * 데이터 소스 필드 값 Alias : {"key": "value", ...}
   */
  @Column(name = "alias_field_value", length = 65535, columnDefinition = "TEXT")
  @Basic(fetch = FetchType.LAZY)
  @Spec(target = Map.class)
  @JsonRawValue
  @JsonDeserialize(using = KeepAsJsonDeserialzier.class)
  private String valueAlias;

  public DataSourceAlias() {
  }

  public Map<String, String> getValueAliasMap() {

    if(StringUtils.isEmpty(valueAlias)) {
      return Maps.newHashMap();
    }

    return GlobalObjectMapper.readValue(valueAlias, Map.class);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDataSourceId() {
    return dataSourceId;
  }

  public void setDataSourceId(String dataSourceId) {
    this.dataSourceId = dataSourceId;
  }

  public String getDashBoardId() {
    return dashBoardId;
  }

  public void setDashBoardId(String dashBoardId) {
    this.dashBoardId = dashBoardId;
  }

  public String getFieldName() {
    return fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getNameAlias() {
    return nameAlias;
  }

  public void setNameAlias(String nameAlias) {
    this.nameAlias = nameAlias;
  }

  public String getValueAlias() {
    return valueAlias;
  }

  public void setValueAlias(String valueAlias) {
    this.valueAlias = valueAlias;
  }

}
