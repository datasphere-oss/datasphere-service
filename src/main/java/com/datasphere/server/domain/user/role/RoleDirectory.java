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

package com.datasphere.server.domain.user.role;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;

import com.datasphere.server.domain.user.DirectoryProfile;
import com.datasphere.server.domain.user.UserProfile;

@Entity
@Table(name = "role_directory")
public class RoleDirectory {

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
  @GenericGenerator(name = "native", strategy = "native")
  @Column(name = "id")
  Long id;

  @Column(name = "directory_id")
  String directoryId;

  @Column(name = "directory_name")
  String directoryName;

  @Column(name = "directory_type")
  @Enumerated(EnumType.STRING)
  DirectoryProfile.Type type;

  @Column(name = "created_time")
  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  DateTime createdTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "role_id")
  @JsonBackReference
  @RestResource(exported = false)
  Role role;

  @Transient
  DirectoryProfile profile;

  public RoleDirectory() {
  }

  public RoleDirectory(Role role, DirectoryProfile profile) {
    this.role = role;
    this.profile = profile;
    if(profile instanceof UserProfile) {
      this.type = DirectoryProfile.Type.USER;
    } else {
      this.type = DirectoryProfile.Type.GROUP;
    }
    this.directoryId = profile.getId();
    this.directoryName = profile.getName();
  }

  @PrePersist
  public void prePersist() {
    createdTime = DateTime.now();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDirectoryId() {
    return directoryId;
  }

  public void setDirectoryId(String directoryId) {
    this.directoryId = directoryId;
  }

  public String getDirectoryName() {
    return directoryName;
  }

  public void setDirectoryName(String directoryName) {
    this.directoryName = directoryName;
  }

  public DirectoryProfile.Type getType() {
    return type;
  }

  public void setType(DirectoryProfile.Type type) {
    this.type = type;
  }

  public DateTime getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(DateTime createdTime) {
    this.createdTime = createdTime;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public DirectoryProfile getProfile() {
    return profile;
  }

  public void setProfile(DirectoryProfile profile) {
    this.profile = profile;
  }
}
