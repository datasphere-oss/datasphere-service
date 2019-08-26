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

package com.datasphere.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.SortableField;
import org.hibernate.search.annotations.Store;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import com.datasphere.server.common.bridge.JodaTimeSplitBridge;
import com.datasphere.server.util.AuthUtils;

/**
 * Created by kyungtaak on 2016. 1. 6..
 */
@MappedSuperclass
public abstract class AbstractHistoryEntity {

  private static final long serialVersionUID = 1L;

  @Column(name = "version")
  @Version
  protected long version;

  @Column(name = "created_by")
  @Field(index = Index.YES, analyze = Analyze.NO, store = Store.NO)
  protected String createdBy;

  @Column(name = "created_time")
  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  @FieldBridge(impl = JodaTimeSplitBridge.class)
  @Fields({
      @Field(name = "createdTime.year", index = Index.YES, analyze = Analyze.NO, store = Store.NO),
      @Field(name = "createdTime.month", index = Index.YES, analyze = Analyze.NO, store = Store.NO),
      @Field(name = "createdTime.day", index = Index.YES, analyze = Analyze.NO, store = Store.NO),
      @Field(name = "createdTime.ymd", index = Index.YES, analyze = Analyze.NO, store = Store.NO),
      @Field(name = "createdTime.mils", index = Index.YES, analyze = Analyze.NO, store = Store.NO),
  })
  @SortableField(forField = "createdTime.mils")
  protected DateTime createdTime;

  @Column(name = "modified_by")
  @Field(index = Index.YES, analyze = Analyze.NO, store = Store.NO)
  protected String modifiedBy;

  @Column(name = "modified_time")
  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  @FieldBridge(impl = JodaTimeSplitBridge.class)
  @Fields({
      @Field(name = "modifiedTime.year", index = Index.YES, analyze = Analyze.NO, store = Store.NO),
      @Field(name = "modifiedTime.month", index = Index.YES, analyze = Analyze.NO, store = Store.NO),
      @Field(name = "modifiedTime.day", index = Index.YES, analyze = Analyze.NO, store = Store.NO),
      @Field(name = "modifiedTime.ymd", index = Index.YES, analyze = Analyze.NO, store = Store.NO),
      @Field(name = "modifiedTime.mils", index = Index.YES, analyze = Analyze.NO, store = Store.NO),
  })
  @SortableField(forField = "modifiedTime.mils")
  protected DateTime modifiedTime;

  public AbstractHistoryEntity() {
    // empty constructor
  }

  public AbstractHistoryEntity(String createdBy) {
    this.createdBy = createdBy;
  }

  @PrePersist
  public void prePersist() {
    createdBy = AuthUtils.getAuthUserName();
    modifiedBy = AuthUtils.getAuthUserName();
    createdTime = DateTime.now();
    modifiedTime = createdTime;
  }

  @PreUpdate
  public void preUpdate() {
    modifiedBy = AuthUtils.getAuthUserName();
    modifiedTime = DateTime.now();
  }

  @JsonIgnore
  public long getVersion() {
    return version;
  }

  public void setVersion(long version) {
    this.version = version;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public String getModifiedBy() {
    return modifiedBy;
  }

  public void setModifiedBy(String modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

  public DateTime getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(DateTime createdTime) {
    this.createdTime = createdTime;
  }

  public DateTime getModifiedTime() {
    return modifiedTime;
  }

  public void setModifiedTime(DateTime modifiedTime) {
    this.modifiedTime = modifiedTime;
  }
}
