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

package com.datasphere.server.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.BeanIds;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.datasphere.server.domain.user.role.RoleService;

/**
 * Spring Security Utilize in User Information Processing Service
 */
@Component(BeanIds.USER_DETAILS_SERVICE)
@Transactional(readOnly = true)
public class InnerUserServiceImpl implements UserDetailsService {

  private static final Logger LOGGER = LoggerFactory.getLogger(InnerUserServiceImpl.class);

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleService roleService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	// 通过用户名查询用户信息
    User user = userRepository.findByUsername(username);

    if (user == null) {
      throw new UsernameNotFoundException(username + " not found.");
    }

    user.setRoleService(roleService);

    // Preload credentials
    user.getAuthorities();

    LOGGER.debug("Load User info. : " + user);

    return user;
  }

}
