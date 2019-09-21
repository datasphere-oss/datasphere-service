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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.datasphere.server.domain.DSSDomain;
import com.datasphere.server.domain.user.CachedUserService;
import com.datasphere.server.domain.user.UserProfile;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

/**
 * Created by aladin on 2019. 1. 5..
 */
@Entity
@Table(name = "user_group_member", indexes = {
    @Index(name = "i_user_member_id", columnList = "member_id")
})
public class GroupMember implements DSSDomain<Long> {

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
  @GenericGenerator(name = "native", strategy = "native")
  @Column(name = "id")
  Long id;

  @Column(name = "member_id")
  @NotNull
  @JsonProperty(access = WRITE_ONLY)
  String memberId;

  @Column(name = "member_name")
  @JsonProperty(access = READ_ONLY)
  String memberName;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  @JoinColumn(name = "group_id")
  @JsonBackReference
  Group group;

  @JsonIgnore
  UserProfile profile;

  public GroupMember() {
    // Empty Constructor
  }

  public GroupMember(String memberId, String memberName) {
    this.memberId = memberId;
    this.memberName = memberName;
  }

  @Override
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getMemberId() {
    return memberId;
  }

  public void setMemberId(String memberId) {
    this.memberId = memberId;
  }

  public String getMemberName() {
    return memberName;
  }

  public void setMemberName(String memberName) {
    this.memberName = memberName;
  }

  public Group getGroup() {
    return group;
  }

  public void setGroup(Group group) {
    this.group = group;
  }

  public UserProfile getProfile(CachedUserService service) {
    if(profile == null) {
      profile = service.findUserProfile(this.memberId);
    }
    return profile;
  }
}
