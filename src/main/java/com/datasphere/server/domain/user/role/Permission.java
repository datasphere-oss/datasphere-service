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

import com.google.common.collect.Sets;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.datasphere.server.domain.AbstractHistoryEntity;
import com.datasphere.server.domain.MetatronDomain;

import static javax.persistence.CascadeType.MERGE;

/**
 * Created by kyungtaak on 2016. 1. 7..
 */
@Entity
@Table(name = "permissions")
public class Permission extends AbstractHistoryEntity implements MetatronDomain<Long>, GrantedAuthority {

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
  @GenericGenerator(name = "native", strategy = "native")
  @Column(name = "id")
  Long id;

  @Column(name = "perm_name")
  String name;

  @Column(name = "perm_domain")
  @Enumerated(EnumType.STRING)
  DomainType domain;

  @ManyToMany(mappedBy = "permissions", cascade = {MERGE})
  @JsonBackReference
  Set<Role> roles = Sets.newHashSet();

  public Permission() {
    // Empty Constructor
  }

  public Permission(String name) {
    this.name = name;
    this.domain = DomainType.SYSTEM;
  }

  public Permission(String name, DomainType domain) {
    this.name = name;
    this.domain = domain;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public DomainType getDomain() {
    return domain;
  }

  public void setDomain(DomainType domain) {
    this.domain = domain;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  @Override
  public String toString() {
    return "Permission{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
  }

  @JsonIgnore
  @Override
  public String getAuthority() {
    return getName();
  }

  public enum DomainType {
    SYSTEM, WORKSPACE
  }
}
