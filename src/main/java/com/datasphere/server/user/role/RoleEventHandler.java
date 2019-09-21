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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.prepost.PreAuthorize;

import com.datasphere.server.domain.context.ContextService;

/**
 * Created by aladin on 2019. 5. 14..
 */
@RepositoryEventHandler(Role.class)
public class RoleEventHandler {

  @Autowired
  ContextService contextService;

  @Autowired
  RoleRepository roleRepository;

  @HandleBeforeCreate
  @PreAuthorize("hasAnyAuthority('PERM_SYSTEM_MANAGE_USER')")
  public void handleBeforeCreate(Role role) {

    // 중복 Role 명 체크
    Long count;
    switch (role.getScope()) {
      case WORKSPACE:
        count = roleRepository.countByRoleSetAndName(role.getRoleSet(), role.getName());
        break;
      default:
        count = roleRepository.countByScopeAndName(role.getScope(), role.getName());
    }

    if(count > 0) {
      throw new DuplicatedRoleNameException(role.getScope(), role.getName());
    }

  }

  @HandleAfterCreate
  public void handleAfterCreate(Role role) {
    // Context 정보 저장, ID 가 지정후 생성 필요
    contextService.saveContextFromDomain(role);
  }

  @HandleBeforeSave
  @PreAuthorize("hasAnyAuthority('PERM_SYSTEM_MANAGE_USER')")
  public void handleBeforeSave(Role role) {
    // Context 정보 저장
    contextService.saveContextFromDomain(role);
  }

  @HandleBeforeDelete
  @PreAuthorize("hasAnyAuthority('PERM_SYSTEM_MANAGE_USER')")
  public void handleBeforeDelete(Role role) {
    // Context 정보 삭제
    contextService.removeContextFromDomain(role);
  }

}
