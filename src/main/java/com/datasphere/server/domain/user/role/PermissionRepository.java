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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Set;

/**
 * Created by kyungtaak on 2016. 1. 7..
 */
@RepositoryRestResource(path = "permissions",
    itemResourceRel = "permission", collectionResourceRel = "permissions",
    excerptProjection = PermissionProjections.DefaultRoleProjection.class)
public interface PermissionRepository extends JpaRepository<Permission, Long> {

  @RestResource(path = "names")
  Permission findByName(String name);

  @RestResource(path = "domains")
  Page<Permission> findByDomain(Permission.DomainType domain, Pageable pageable);

  @Override
  @RestResource(exported = false)
  Page<Permission> findAll(Pageable pageable);

  @Override
  @RestResource(exported = false)
  List<Permission> findAll();

  @Override
  @RestResource(exported = false)
  void delete(Long aLong);

  @Override
  @RestResource(exported = false)
  void delete(Permission entity);

  @Override
  @RestResource(exported = false)
  Permission save(Permission entity);

  @RestResource(exported = false)
  @Query("SELECT perm FROM Permission perm JOIN perm.roles roles WHERE roles.name = :name AND roles.scope = :scope")
  Set<Permission> findByRoleNameAndScope(@Param("name") String name, @Param("scope") Role.RoleScope scope);

  @RestResource(exported = false)
  Set<Permission> findByNameInAndDomain(List<String> names, Permission.DomainType domain);

  @RestResource(exported = false)
  @Query("SELECT distinct perm.name FROM Permission perm JOIN perm.roles roles WHERE roles.name IN :name")
  Set<String> findPermissionNameByRoleNames(@Param("name") List<String> name);
}
