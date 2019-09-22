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

package com.datasphere.server.user.group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.datasphere.server.common.domain.context.ContextDomainRepository;

import java.util.List;

/**
 * Created by aladin on 2019. 1. 7..
 */
@RepositoryRestResource(path = "groups", itemResourceRel = "group", collectionResourceRel = "groups",
    excerptProjection = GroupProjections.DefaultProjection.class)
public interface GroupRepository extends JpaRepository<Group, String>,
    QuerydslPredicateExecutor<Group>, GroupRepositoryExtends, ContextDomainRepository {

  Group findByName(String name);

  Group findByPredefinedAndDefaultGroup(Boolean predefined, Boolean defaultGroup);

  @Query("select g from Group g join g.members m where m.memberId = ?1")
  List<Group> findJoinedGroups(String memberId);
}
