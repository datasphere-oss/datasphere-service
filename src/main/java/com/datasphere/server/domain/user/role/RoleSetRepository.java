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

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Set;

/**
 * Created by aladin on 2019. 1. 7..
 */
@RepositoryRestResource(path = "rolesets", itemResourceRel = "roleSet", collectionResourceRel = "roleSets",
    excerptProjection = RoleSetProjections.DefaultRoleProjection.class)
public interface RoleSetRepository extends JpaRepository<RoleSet, String>, QuerydslPredicateExecutor<RoleSet> {

  RoleSet findByName(String name);

  @Query("SELECT distinct p.name " +
      "FROM RoleSet rs INNER JOIN rs.roles r INNER JOIN r.permissions p " +
      "WHERE rs IN :roleSets AND r.name IN :roleNames")
  Set<String> getPermissionsByRoleSetAndRoleName(@Param("roleSets") List<RoleSet> roleSets,
                                                 @Param("roleNames") List<String> roleNames);

}
