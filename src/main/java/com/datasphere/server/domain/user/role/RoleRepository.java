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
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;

import com.datasphere.server.domain.context.ContextDomainRepository;

/**
 * Created by kyungtaak on 2016. 1. 7..
 */
@RepositoryRestResource(path = "roles", itemResourceRel = "role", collectionResourceRel = "roles",
    excerptProjection = RoleProjections.DefaultRoleProjection.class)
public interface RoleRepository extends JpaRepository<Role, String>, QueryDslPredicateExecutor<Role>,
                                        RoleSearchRepository, RoleRepositoryExtends, ContextDomainRepository {

  @RestResource(path = "keyword")
  @Query("select r from Role r where r.id= :q")  // fake!! http://stackoverflow.com/questions/25201306/implementing-custom-methods-of-spring-data-repository-and-exposing-them-through
  Page<Role> searchByKeyword(@Param("q") String keywords, Pageable pageable);

  @RestResource(path = "query")
  @Query("select r from Role r where r.id= :q")  // fake!!
  Page<Role> searchByQuery(@Param("q") String query, Pageable pageable);

  @RestResource(path = "names")
  Role findByName(@Param("name") String name);

  @RestResource(path = "scopes")
  Page<Role> findByScope(@Param("scope") Role.RoleScope scope, Pageable pageable);

  @RestResource(exported = false)
  Long countByScopeAndName(Role.RoleScope scope, String name);

  @RestResource(exported = false)
  Long countByRoleSetAndName(RoleSet roleSet, String name);

  @RestResource(exported = false)
  Role findByScopeAndName(@Param("scope") Role.RoleScope scope, @Param("name") String name);

  @RestResource(exported = false)
  @Query("SELECT role FROM Role role JOIN FETCH role.permissions perms WHERE role.name = :name AND role.scope = :scope")
  Role findByNameAndScopeWithPermissions(@Param("name") String name, @Param("scope") Role.RoleScope scope);

  @Transactional
  @Modifying(clearAutomatically = true)
  @Query("DELETE FROM Role role WHERE role.roleSet = :roleSet")
  void deleteRoleInRoleSet(@Param("roleSet") RoleSet roleSet);

}
