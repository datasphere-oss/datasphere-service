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

package com.datasphere.server.user.role;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.apache.commons.lang3.StringUtils;

import com.datasphere.server.domain.user.DirectoryProfile;

public class RoleDirectoryPredicate {

  /**
   * Role을 지정한 Directory(user/group) 정보
   */
  public static Predicate searchRoleDirectoryList(Role role, DirectoryProfile.Type type,
                                                  String nameContains) {

    BooleanBuilder builder = new BooleanBuilder();
    QRoleDirectory roleDirectory = QRoleDirectory.roleDirectory;

    if(role != null) {
      builder.and(roleDirectory.role.eq(role));
    }

    if(type != null) {
      builder.and(roleDirectory.type.eq(type));
    }

    if (StringUtils.isNotEmpty(nameContains)) {
      builder.and(roleDirectory.directoryName.containsIgnoreCase(nameContains));
    }

    return builder;
  }

  public static Predicate searchUserSystemRoleList(String username) {

    BooleanBuilder builder = new BooleanBuilder();
    QRole role = QRole.role;

    builder.and(role.scope.eq(Role.RoleScope.GLOBAL));

    return builder;
  }

  /**
   * RoleSet 명 중복 조회 조건
   */
  public static Predicate searchDuplicatedName(String name) {

    BooleanBuilder builder = new BooleanBuilder();
    QRole role = QRole.role;

    builder = builder.and(role.name.eq(name));

    return builder;
  }

  /**
   * 워크스페이스 Role이 존재하고, 지정된 RoleSet에 포함이 되는 조건
   */
  public static Predicate searchWorkspaceRoleInRoleSet(String roleName, RoleSet roleSet) {
    BooleanBuilder builder = new BooleanBuilder();
    QRole role = QRole.role;

    builder.and(role.scope.eq(Role.RoleScope.WORKSPACE))
           .and(role.name.eq(roleName))
           .and(role.roleSet.eq(roleSet));

    return builder;
  }

}
