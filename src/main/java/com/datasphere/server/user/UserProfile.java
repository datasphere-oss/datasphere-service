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

package com.datasphere.server.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.security.Principal;

/**
 * Created by aladin on 2019. 5. 18..
 */
@JsonTypeName("user")
public class UserProfile implements DirectoryProfile {

  public static final String UNKNOWN_USERNAME = "Unknown user";
  // 用户名称
  String username;
  // 用户全名
  String fullName;
  // 电子邮箱
  String email;
  // 图片路径
  String imageUrl;

  public UserProfile() {
    // Empty Constructor
  }

  public UserProfile(String username, String fullName, String email) {
    this.username = username;
    this.fullName = fullName;
    this.email = email;
  }

  @JsonIgnore
  @Override
  public String getId() {
    return username;
  }

  @JsonIgnore
  @Override
  public String getName() {
    return fullName;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public static UserProfile getProfile(User user) {

    if(user == null) {
      return null;
    }

    UserProfile userProfile = new UserProfile();
    userProfile.setUsername(user.getUsername());
    userProfile.setFullName(user.getFullName());
    userProfile.setEmail(user.getEmail());
    userProfile.setImageUrl(user.getImageUrl());

    return userProfile;
  }

  public static UserProfile getProfile(Principal principal) {

    if(principal != null && principal instanceof User) {
      return getProfile((User) principal);
    }

    return null;
  }


}
