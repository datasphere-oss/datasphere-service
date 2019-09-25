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

import com.google.common.collect.Sets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.SortableField;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.bridge.builtin.EnumBridge;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.datasphere.server.common.domain.AbstractHistoryEntity;
import com.datasphere.server.common.domain.DSSDomain;
import com.datasphere.server.user.role.Permission;
import com.datasphere.server.user.role.Role;
import com.datasphere.server.user.role.RoleService;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;
import static org.hibernate.search.annotations.Index.NO;

/**
 * User Model Definition <br/>
 * Implementing extension of User model (UserDetails) provided by Spring Security
 */
@Entity
@Table(name = "users", indexes = {
    @Index(name = "i_user_name", columnList = "user_name"),
    @Index(name = "i_user_email", columnList = "user_email"),
    @Index(name = "i_user_username_status", columnList = "user_name,user_status")
})
@Indexed
public class User extends AbstractHistoryEntity implements UserDetails, DSSDomain<String> {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "id")
  private String id;
  // 用户名
  @Column(name = "user_name", length = 50)
  @Field(analyze = Analyze.NO, store = Store.YES)
  @NotBlank
  private String username;
  // 用户密码
  @JsonProperty(access = WRITE_ONLY)
  @Column(name = "user_password", length = 200)
  private String password;
  // 用户全名
  @Column(name = "user_full_name")
  @Fields({
      @Field(analyze = Analyze.YES, store = Store.YES),
      @Field(name = "sortFullName", analyze = Analyze.NO, store = Store.NO, index = NO)
  })
  // 排序全名
  @SortableField(forField = "sortFullName")
  private String fullName;

  @Column(name = "user_email")
  @Email
  @Field(analyze = Analyze.NO, store = Store.YES)
  private String email;
  // 用户电话
  @Column(name = "user_tel")
  @Field(analyze = Analyze.YES, store = Store.YES)
  private String tel;

  // 用户状态
  @Column(name = "user_status")
  @Enumerated(EnumType.STRING)
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @Field(analyze = Analyze.NO, store = Store.YES)
  @FieldBridge(impl = EnumBridge.class)
  private Status status;
  // 用户状态消息
  @Column(name = "user_status_msg", length = 3000)
  private String statusMessage;

  @Column(name = "user_origin")
  private String userOrigin;

  /**
   * User Image Url
   */
  @Column(name = "user_image_Url")
  private String imageUrl;
  // 群组名称
  @Transient
  @JsonProperty(access = WRITE_ONLY)
  private List<String> groupNames;
  // 角色名称
  @Transient
  @JsonProperty(access = WRITE_ONLY)
  private List<String> roleNames;

  @Transient
  @JsonProperty(access = WRITE_ONLY)
  private Boolean passMailer = false;

  @Transient
  @JsonProperty(access = WRITE_ONLY)
  private String roleSetName;
  // 工作空间类型
  @Transient
  @JsonProperty(access = WRITE_ONLY)
  private String workspaceType;

  @Transient
  private RoleService roleService;

  public User() {
  }

  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }

  @JsonIgnore
  @Transient
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Set<GrantedAuthority> authorities = Sets.newHashSet();

    List<Role> roles = getRoles();
    Set<Permission> permissions = Sets.newHashSet();
    for (Role role : roles) {
      if(CollectionUtils.isEmpty(role.getPermissions())) {
        continue;
      }
      permissions.addAll(role.getPermissions());
    }

    authorities.addAll(roles);
    authorities.addAll(permissions);

    return authorities;
  }

  @JsonIgnore
  @Transient
  public List<Role> getRoles() {
    return roleService.getRolesByUsername(username);
  }

  @JsonIgnore
  @Transient
  public Set<Permission> getPermissions() {
    Set<Permission> permissions = Sets.newHashSet();

    List<Role> roles = getRoles();
    for (Role role : roles) {
      if(CollectionUtils.isEmpty(role.getPermissions())) {
        continue;
      }
      permissions.addAll(role.getPermissions());
    }

    return permissions;
  }

  public void setRoleService(RoleService roleService) {
    this.roleService = roleService;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
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

  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {
    this.tel = tel;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public String getStatusMessage() {
    return statusMessage;
  }

  public void setStatusMessage(String statusMessage) {
    this.statusMessage = statusMessage;
  }

  @Override
  @JsonIgnore
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  @JsonIgnore
  public boolean isAccountNonExpired() {
    return status == Status.EXPIRED ? false : true;
  }

  @Override
  @JsonIgnore
  public boolean isAccountNonLocked() {
    return status == Status.LOCKED ? false : true;
  }

  @Override
  public boolean isEnabled() {
    return status == Status.ACTIVATED ? true : false;
  }

  public List<String> getRoleNames() {
    return roleNames;
  }

  public void setRoleNames(List<String> roleNames) {
    this.roleNames = roleNames;
  }

  public List<String> getGroupNames() {
    return groupNames;
  }

  public void setGroupNames(List<String> groupNames) {
    this.groupNames = groupNames;
  }

  public Boolean getPassMailer() {
    return passMailer;
  }

  public void setPassMailer(Boolean passMailer) {
    this.passMailer = passMailer;
  }

  public String getRoleSetName() {
    return roleSetName;
  }

  public void setRoleSetName(String roleSetName) {
    this.roleSetName = roleSetName;
  }

  public String getWorkspaceType() {
    return workspaceType;
  }

  public void setWorkspaceType(String workspaceType) {
    this.workspaceType = workspaceType;
  }

  public String getUserOrigin() {
    return userOrigin;
  }

  public void setUserOrigin(String userOrigin) {
    this.userOrigin = userOrigin;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    User user = (User) o;

    return id != null ? id.equals(user.id) : user.id == null;
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "User{" +
            "id='" + id + '\'' +
            ", username='" + username + '\'' +
            ", fullName='" + fullName + '\'' +
            ", tel='" + tel + '\'' +
            ", status=" + status +
            ", imageUrl='" + imageUrl + '\'' +
            "} " + super.toString();
  }

  public enum Status {
    REJECTED,
    EXPIRED,
    LOCKED,
    DELETED,
    REQUESTED,
    ACTIVATED
  }

}
