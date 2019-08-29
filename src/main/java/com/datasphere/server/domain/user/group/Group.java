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

package com.datasphere.server.domain.user.group;

import com.google.common.collect.Lists;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.SortableField;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.bridge.builtin.BooleanBridge;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PostPersist;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.datasphere.server.common.GlobalObjectMapper;
import com.datasphere.server.common.KeepAsJsonDeserialzier;
import com.datasphere.server.common.bridge.PredefinedRoleBridge;
import com.datasphere.server.domain.AbstractHistoryEntity;
import com.datasphere.server.domain.MetatronDomain;
import com.datasphere.server.domain.context.ContextEntity;

/**
 * Created by aladin on 2019. 1. 5..
 */
@Entity
@Table(name = "user_group")
@Indexed
public class Group extends AbstractHistoryEntity implements MetatronDomain<String>, ContextEntity {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "id")
  String id;

  @Column(name = "group_name")
  @FieldBridge(impl = PredefinedRoleBridge.class)
  @Fields({
      @Field(name = "name", analyze = Analyze.YES, store = Store.YES),
      @Field(name = "name.sort", analyze = Analyze.NO, store = Store.NO)
  })
  @SortableField(forField = "name.sort")
  @NotBlank
  @Size(max = 150)
  String name;

  @Column(name = "group_desc", length = 1000)
  @Size(max = 900)
  String description;


  @Column(name = "group_predefined")
  @Field(analyze = Analyze.NO, store = Store.YES)
  @FieldBridge(impl = BooleanBridge.class)
  @SortableField
  Boolean predefined = false;

  /**
   * 기본 그룹 여부
   */
  @Column(name = "group_default")
  Boolean defaultGroup;

  /**
   * 화면내에서 수정할 수 없도록 구성하는 플래그
   */
  @Column(name = "group_read_only")
  Boolean readOnly;

  @Column(name = "group_member_count")
//  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  Integer memberCount = 0;

  @OneToMany(mappedBy = "group", cascade = { CascadeType.ALL }, orphanRemoval = true)
  @RestResource(exported = false)
  @BatchSize(size = 100)
  List<GroupMember> members = Lists.newArrayList();

  /**
   * Spring data rest 제약으로 인한 Dummy Property.
   *  - Transient 어노테이션 구성시 HandleBeforeSave 에서 인식 못하는 문제 발생
   */
  @Column(name = "group_contexts", length = 10000)
  @JsonRawValue
  @JsonDeserialize(using = KeepAsJsonDeserialzier.class)
  String contexts;

  public Group() {
    // Empty Constructor
  }

  public Group(String name) {
    this.name = name;
  }

  /**
   * Group 에 포함된 Member의 수를 저장해둠
   */
  @PostPersist
  public void postPersist() {
    this.memberCount = this.members.size();
  }

  public void addGroupMember(GroupMember member) {
    if(members == null) {
      members = Lists.newArrayList();
    }
    member.setGroup(this);
    members.add(member);

    memberCount++;
  }

  public void removeGroupMember(GroupMember member) {
    if(members == null) {
      members = Lists.newArrayList();
    }
    member.setGroup(null);
    members.remove(member);

    memberCount--;
  }

  @Override
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean getPredefined() {
    return predefined;
  }

  public void setPredefined(Boolean predefined) {
    this.predefined = predefined;
  }

  public Boolean getDefaultGroup() {
    return defaultGroup;
  }

  public void setDefaultGroup(Boolean defaultGroup) {
    this.defaultGroup = defaultGroup;
  }

  public Boolean getReadOnly() {
    return readOnly;
  }

  public void setReadOnly(Boolean readOnly) {
    this.readOnly = readOnly;
  }

  public Integer getMemberCount() {
    return memberCount;
  }

  public void setMemberCount(Integer memberCount) {
    this.memberCount = memberCount;
  }

  public List<GroupMember> getMembers() {
    return members;
  }

  public void setMembers(List<GroupMember> members) {
    this.members = members;
  }

  public String getContexts() {
    return contexts;
  }

  public void setContexts(String contexts) {
    this.contexts = contexts;
  }

  @Override
  public Map<String, String> getContextMap() {
    if(StringUtils.isEmpty(this.contexts)) {
      return null;
    }

    return GlobalObjectMapper.readValue(this.contexts, Map.class);
  }
}
