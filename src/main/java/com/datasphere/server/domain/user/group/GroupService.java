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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import com.datasphere.server.domain.images.ImageRepository;
import com.datasphere.server.domain.user.UserRepository;
import com.datasphere.server.domain.user.role.RoleRepository;

@Component
public class GroupService {

  @Autowired
  GroupRepository groupRepository;

  @Autowired
  GroupMemberRepository groupMemberRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  ImageRepository imageRepository;

  public Group getDefaultGroup() {
    return groupRepository.findByPredefinedAndDefaultGroup(true, true);
  }

  public boolean checkDuplicatedName(String groupName) {
    return groupRepository.exists(GroupPredicate.searchDuplicatedName(groupName));
  }

  public void deleteGroupMember(String memberIds) {
    List<GroupMember> groupMembers = groupMemberRepository.findByMemberId(memberIds);
    for (GroupMember groupMember : groupMembers) {
      Group group = groupMember.getGroup();
      group.removeGroupMember(groupMember);
    }
  }

  /**
   * UserProjection 에서 사용
   *
   * @param username
   * @return
   */
  public List<Map<String, Object>> getJoinedGroupsForProjection(String username, boolean includeRole) {

    List<Group> groups = getJoinedGroups(username);

    List<Map<String,Object>> result = Lists.newArrayList();
    groups.forEach(group -> {
      Map<String,Object> groupMap = Maps.newHashMap();
      groupMap.put("id", group.getId());
      groupMap.put("name", group.getName());
      groupMap.put("predefined", group.getPredefined());
      if(includeRole) {
        groupMap.put("roleNames", roleRepository.findRoleNameByDirectoryId(group.getId()));
      }
      result.add(groupMap);
    });

    return result;
  }

  /**
   * 소속된 그룹 가져오기
   *
   * @param username
   * @return
   */
  public List<Group> getJoinedGroups(String username) {

    Iterable<Group> groups = groupRepository.findJoinedGroups(username);

    return Lists.newArrayList(groups);
  }

  /**
   *
   * @param user
   * @param groupNames
   * @param requiredGroup
   */
//  public void setJoinedGroups(User user, List<String> groupNames, boolean requiredGroup) {
//
//    Set<Role> joinRoles = Sets.newHashSet();
//    if (CollectionUtils.isNotEmpty(groupNames)) {
//      for (String name : groupNames) {
//        Role role = roleRepository.findByScopeAndName(Role.RoleScope.GLOBAL, name);
//        if (role != null) {
//          joinRoles.add(role);
//        }
//      }
//    }
//
//    // 선택한 그룹이 없을 경우 기본 그룹에 포함
//    if (joinRoles.isEmpty() && requiredGroup) {
//      joinRoles.add(roleRepository.findByScopeAndName(Role.RoleScope.GLOBAL, "SYSTEM_USER"));
//    }
//
//  }

}
