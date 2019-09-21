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

import com.google.common.collect.Lists;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import com.datasphere.server.common.exception.BadRequestException;
import com.datasphere.server.domain.workspace.WorkspaceMemberRepository;
import com.datasphere.server.domain.workspace.WorkspaceRepository;

@Component
@Transactional(readOnly = true)
public class RoleSetService {

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PermissionRepository permRepository;

  @Autowired
  RoleSetRepository roleSetRepository;

  @Autowired
  WorkspaceRepository workspaceRepository;

  @Autowired
  WorkspaceMemberRepository workspaceMemberRepository;

  @Transactional
  public RoleSet createRoleSet(RoleSet roleSet) {
    // Checked for duplicate name
    if(checkDuplicatedName(roleSet.getName())) {
      throw new DuplicatedRoleSetNameException(roleSet.getName());
    }

    // Default Roles must exist one by one if Role exists in RoleSet
    if(!existDefaultRoleSet(roleSet)) {
      throw new IllegalArgumentException("One default role must be specified.");
    }

    List<Role> roles = roleSet.getRoles();
    int seq = 1;
    for (Role role : roles) {
      role.setRoleSet(roleSet);
      role.setSeq(seq++);
      role.setScope(Role.RoleScope.WORKSPACE);
      if(CollectionUtils.isNotEmpty(role.getPermissionNames())) {
        role.setPermissions(permRepository.findByNameInAndDomain(role.getPermissionNames(),
                                                                 Permission.DomainType.WORKSPACE));
      }
    }

    return roleSetRepository.save(roleSet);
  }

  @Transactional
  public RoleSet copyRoleSet(RoleSet originalRoleSet, String name) {

    RoleSet copiedRoleSet = originalRoleSet.copyOf();

    // If a rollset name is specified during copying, error handling is performed if the name is duplicated.
    // Auto assign and do numbering if duplicate
    if(StringUtils.isNotEmpty(name)) {
      copiedRoleSet.setName(name);
    } else {
      if(checkDuplicatedName(copiedRoleSet.getName())) {
        String candidateName = copiedRoleSet.getName() + "_";
        long count = roleSetRepository.count(RoleSetPredicate.searchDuplicatedName(candidateName, true));
        copiedRoleSet.setName(candidateName + (++count));
      }
    }

    return createRoleSet(copiedRoleSet);
  }

  @Transactional
  public RoleSet updateRoleSet(RoleSet roleSet, RoleSet persistRoleSet) {

    if(!persistRoleSet.getName().equals(roleSet.getName())) {
      if (checkDuplicatedName(roleSet.name)) {
        throw new DuplicatedRoleSetNameException(roleSet.getName());
      }
      persistRoleSet.setName(roleSet.getName());
    }

    persistRoleSet.setDescription(roleSet.getDescription());

    if(!existDefaultRoleSet(roleSet)) {
      throw new IllegalArgumentException("One default role must be specified.");
    }

    List<String> fromRoleNames = persistRoleSet.getRoleNames();
    List<String> toRoleNames = roleSet.getRoleNames();

    roleRepository.deleteRoleInRoleSet(persistRoleSet);

    List<Role> roles = Lists.newArrayList();
    int seq = 1;
    for (Role role : roleSet.getRoles()) {
      role.setRoleSet(persistRoleSet);
      role.setScope(Role.RoleScope.WORKSPACE);
      role.setSeq(seq++);
      if(CollectionUtils.isNotEmpty(role.getPermissionNames())) {
        role.setPermissions(permRepository.findByNameInAndDomain(role.getPermissionNames(),
                                                                 Permission.DomainType.WORKSPACE));
      }
      roles.add(role);
    }

    persistRoleSet.setRoles(roles);

    roleSetRepository.saveAndFlush(persistRoleSet);

    Map<String, String> mapper = roleSet.getMapper();

    List<String> linkedWorkspaceIds = workspaceRepository.findIdByRoleSetId(persistRoleSet.getId());
    if(CollectionUtils.isNotEmpty(linkedWorkspaceIds)) {
      Role defaultRole = persistRoleSet.getDefaultRole();
      if (MapUtils.isNotEmpty(mapper)) {
        if (!fromRoleNames.containsAll(mapper.keySet())) {
          throw new BadRequestException("Invalid mapper property: from Name(" + fromRoleNames + "), mapper key(" + mapper.keySet() + ")");
        }

        for (String fromRoleName : mapper.keySet()) {
          String toRoleName = mapper.get(fromRoleName);
          workspaceMemberRepository.updateMultiMemberRoleInWorkspaces(linkedWorkspaceIds, fromRoleName,
                                                                      StringUtils.isEmpty(toRoleName) ? defaultRole.getName() : toRoleName);
        }
      } else {
        if (!CollectionUtils.isEqualCollection(fromRoleNames, toRoleNames)) {
          for (String fromRoleName : fromRoleNames) {
            workspaceMemberRepository.updateMultiMemberRoleInWorkspaces(linkedWorkspaceIds, fromRoleName, defaultRole.getName());
          }
        }
      }
    }

    return persistRoleSet;
  }

  public boolean checkDuplicatedName(String name) {
    return roleSetRepository.exists(RoleSetPredicate.searchDuplicatedName(name, false));
  }

  public boolean existDefaultRoleSet(RoleSet roleSet) {
    return CollectionUtils.isNotEmpty(roleSet.getRoles()) && roleSet.getDefaultRole() != null;
  }

  public RoleSet getDefaultRoleSet() {
    return roleSetRepository.findById(RoleSet.ROLESET_ID_DEFAULT).get();
  }

}
