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

import com.google.common.collect.Lists;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import com.datasphere.server.domain.CollectionPatch;
import com.datasphere.server.domain.user.CachedUserService;
import com.datasphere.server.domain.user.DirectoryProfile;
import com.datasphere.server.domain.user.group.GroupRepository;
import com.datasphere.server.domain.workspace.Workspace;

@Component
public class RoleService {

  @Autowired
  CachedUserService userService;

  @Autowired
  RoleSetService roleSetService;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  GroupRepository groupRepository;

  @Autowired
  RoleDirectoryRepository roleDirectoryRepository;

  @Autowired
  PermissionRepository permRepository;

  public Role createRole(Role role) {
    // 이름이 중복인지 체크함
    if (checkDuplicatedName(role.getName())) {
      throw new DuplicatedRoleSetNameException(role.getName());
    }

    if (CollectionUtils.isNotEmpty(role.getPermissionNames())) {
      Permission.DomainType type = null;
      if (role.getScope() == Role.RoleScope.WORKSPACE) {
        type = Permission.DomainType.WORKSPACE;
      } else {
        type = Permission.DomainType.SYSTEM;
      }

      role.setPermissions(permRepository.findByNameInAndDomain(role.getPermissionNames(), type));
    }

    return roleRepository.save(role);
  }

  public Role copyRole(Role originalRole) {

    Role copiedRole = originalRole.copyOf();

    return createRole(copiedRole);
  }

  public Role updateRole(Role role, Role persistRole) {

    // 이름이 다른 경우 중복체크를 수행합니다
    if (!persistRole.getName().equals(role.getName())) {
      if (checkDuplicatedName(role.name)) {
        throw new DuplicatedRoleSetNameException(role.getName());
      }
      persistRole.setName(role.getName());
    }

    persistRole.setDescription(role.getDescription());

    if (CollectionUtils.isNotEmpty(role.getPermissionNames())) {
      Permission.DomainType type = null;
      if (role.getScope() == Role.RoleScope.WORKSPACE) {
        type = Permission.DomainType.WORKSPACE;
      } else {
        type = Permission.DomainType.SYSTEM;
      }

      role.setPermissions(permRepository.findByNameInAndDomain(role.getPermissionNames(), type));
    }

    return roleRepository.save(persistRole);
  }

  public void assignDirectory(Role role, List<CollectionPatch> patches) {

    for (CollectionPatch patch : patches) {
      String directoryId = patch.getValue("directoryId");
      String directoryType = patch.getValue("type");

      // 값 검증 (별도 메소드 처리 필요)
      if(StringUtils.isEmpty(directoryId) || StringUtils.isEmpty(directoryType)) {
        continue;
      }
      DirectoryProfile.Type type = DirectoryProfile.Type.valueOf(directoryType);
      if(type == null) {
        continue;
      }

      switch (patch.getOp()) {
        case ADD:
          DirectoryProfile profile = userService.findProfileByDirectoryType(directoryId, type);
          role.addDirectory(new RoleDirectory(role, profile));
          break;
        case REMOVE:
          RoleDirectory removeDirectory = roleDirectoryRepository.findByRoleAndTypeAndDirectoryId(role, type, directoryId);
          role.removeDirectory(removeDirectory);
          break;
        default:
          break;
      }
    }

    roleRepository.save(role);
  }

  public boolean checkDuplicatedName(String name) {
    return roleRepository.exists(RolePredicate.searchDuplicatedName(name));
  }

  public List<Role> getRolesByUsername(String username) {

    List<String> directoryIds = Lists.newArrayList(username);

    // 사용자가 포함된 그룹 조회
    List<String> groupIds = groupRepository.findGroupIdsByMemberId(username);
    if(CollectionUtils.isNotEmpty(groupIds)) {
      directoryIds.addAll(groupIds);
    }

    List<Role> resultRoles = roleRepository.findRoleByDirectoryId(
        directoryIds.toArray(new String[directoryIds.size()]));

    return resultRoles;
  }

  /**
   * UserProjection 내에서 활용
   * @param username
   * @return
   */
  public List<String> getRoleNamesByUsername(String username) {

    List<String> directoryIds = Lists.newArrayList(username);

    // 사용자가 포함된 그룹 조회
    List<String> groupIds = groupRepository.findGroupIdsByMemberId(username);
    if(CollectionUtils.isNotEmpty(groupIds)) {
      directoryIds.addAll(groupIds);
    }

    List<String> resultRoleNames = roleRepository.findRoleNameByDirectoryId(
        directoryIds.toArray(new String[directoryIds.size()]));

    return resultRoleNames;
  }

  public List<String> getRoleNamesByGroupId(String groupId) {

    List<String> directoryIds = Lists.newArrayList(groupId);

    List<String> resultRoleNames = roleRepository.findRoleNameByDirectoryId(
        directoryIds.toArray(new String[directoryIds.size()]));

    return resultRoleNames;
  }

  public List<String> getRoleName(Workspace workspace, String... permName) {
    List<RoleSet> roleSets = workspace.getRoleSets();

    if(CollectionUtils.isEmpty(roleSets)) {
      roleSets = Lists.newArrayList(roleSetService.getDefaultRoleSet());
    }

    return roleRepository.findRoleNamesByScopeAndPerm(Role.RoleScope.WORKSPACE, roleSets, permName);
  }

}
