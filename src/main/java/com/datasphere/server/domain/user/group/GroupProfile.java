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

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.datasphere.server.domain.user.DirectoryProfile;
import com.datasphere.server.domain.user.role.Permission;

/**
 * Created by kyungtaak on 2017. 1. 22..
 */
@JsonTypeName("group")
public class GroupProfile implements DirectoryProfile {

  public static final String UNKNOWN_GROUPNAME = "Unknown group";

  String id;

  String name;

  List<String> permissions;

  public GroupProfile() {
    // Empty Constructor
  }

  public GroupProfile(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public GroupProfile(String id, String name, String... permissions) {
    this(id, name);
    this.permissions = Lists.newArrayList(permissions);
  }

  public static GroupProfile getProfile(Group group) {
    if (group == null) {
      return null;
    }

    GroupProfile profile = new GroupProfile(group.getId(), group.getName());

    return profile;
  }

  @Override
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<String> permissions) {
    this.permissions = permissions;
  }

  public void setPermissions(Set<Permission> permissions) {
    this.permissions = permissions.stream()
                                  .map(permission -> permission.getName())
                                  .collect(Collectors.toList());
  }
}
