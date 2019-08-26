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

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

public class RoleSetPredicate {

  /**
   * RoleSet 정보 조회 조건
   *
   * @param nameContains
   * @param searchDateBy
   * @param from
   * @param to
   * @return
   */
  public static Predicate searchRoleSetList(RoleSet.RoleSetScope scope, String nameContains,
                                            String searchDateBy, DateTime from, DateTime to) {

    BooleanBuilder builder = new BooleanBuilder();
    QRoleSet roleSet = QRoleSet.roleSet;

    if(scope != null) {
      builder.and(roleSet.scope.eq(scope));
    }

    if(from != null && to != null) {
      if(StringUtils.isNotEmpty(searchDateBy) && "CREATED".equalsIgnoreCase(searchDateBy)) {
        builder.and(roleSet.createdTime.between(from, to));
      } else {
        builder.and(roleSet.modifiedTime.between(from, to));
      }
    }

    if(StringUtils.isNotEmpty(nameContains)) {
      builder.and(roleSet.name.containsIgnoreCase(nameContains));
    }

    return builder;
  }

  /**
   * RoleSet 명 중복 조회 조건
   *
   * @param name
   * @return
   */
  public static Predicate searchDuplicatedName(String name, boolean like) {

    BooleanBuilder builder = new BooleanBuilder();
    QRoleSet roleSet = QRoleSet.roleSet;

    if(like) {
      builder.and(roleSet.name.startsWith(name));
    } else {
      builder.and(roleSet.name.eq(name));
    }

    return builder;
  }

}
