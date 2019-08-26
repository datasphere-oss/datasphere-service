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

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

public class GroupPredicate {

  /**
   * 그룹 정보 조회 조건
   *
   * @param nameContains
   * @param searchDateBy
   * @param from
   * @param to
   * @return
   */
  public static Predicate searchGroupList(String nameContains, String searchDateBy, DateTime from, DateTime to) {

    BooleanBuilder builder = new BooleanBuilder();
    QGroup group = QGroup.group;

    if(from != null && to != null) {
      if(StringUtils.isNotEmpty(searchDateBy) && "CREATED".equalsIgnoreCase(searchDateBy)) {
        builder.and(group.createdTime.between(from, to));
      } else {
        builder.and(group.modifiedTime.between(from, to));
      }
    }

    if(StringUtils.isNotEmpty(nameContains)) {
      builder.and(group.name.containsIgnoreCase(nameContains));
    }

    return builder;
  }

  /**
   * Group 명 중복 조회 조건
   *
   * @param name
   * @return
   */
  public static Predicate searchDuplicatedName(String name) {

    BooleanBuilder builder = new BooleanBuilder();
    QGroup group = QGroup.group;

    builder = builder.and(group.name.eq(name));

    return builder;
  }

  /**
   * 특정 사용자가 포함된 Group 조회
   *
   * @param username
   * @return
   */
  public static Predicate searchGroupByMemberId(String username) {

    BooleanBuilder builder = new BooleanBuilder();
    QGroup group = QGroup.group;

    builder = builder.and(group.members.any().memberId.eq(username));

    return builder;
  }

}
