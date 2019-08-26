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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The interface GroupMember repository.
 */
@RepositoryRestResource(exported = false, itemResourceRel = "member", collectionResourceRel = "members",
                      excerptProjection = GroupMemberProjections.DefaultProjection.class)
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long>,
    QueryDslPredicateExecutor<GroupMember> {

  @Transactional
  @Modifying
  @Query("UPDATE GroupMember gm SET gm.memberName = :memberName WHERE gm.memberId = :memberId")
  void updateMemberName(@Param("memberId") String memberId,
                        @Param("memberName") String memberName);

  @Transactional
  @Modifying
  @Query("DELETE FROM GroupMember wm WHERE wm.memberId IN (:memberIds)")
  void deleteByMemberIds(@Param("memberIds") List<String> memberIds);

  GroupMember findByGroupAndMemberId(Group group, String memberId);

  List<GroupMember> findByMemberId(String memberId);

  List<GroupMember> findByGroupId(String groupId);

  @Query("SELECT DISTINCT gm FROM GroupMember gm LEFT JOIN gm.group gr WHERE gr.id IN (:groupIds)")
  List<GroupMember> findByGroupIds(@Param("groupIds") List<String> groupIds);

  Page<GroupMember> findByGroup(Group group, Pageable pageable);

}
