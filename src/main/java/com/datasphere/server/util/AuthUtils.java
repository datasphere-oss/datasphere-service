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

package com.datasphere.server.util;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.datasphere.server.domain.user.User;


/**
 * Authentication Utility Class
 *
 */
public class AuthUtils {

  public static Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  /**
   * Get permissions of user
   *
   * @return permission list
   */
  public static List<String> getPermissions() {

    return getAuthentication()
                      .getAuthorities().stream()
                      .filter(auth -> auth.getAuthority().startsWith("PERM_"))
                      .map(auth -> auth.getAuthority())
                      .collect(Collectors.toList());
  }

  public static String getAuthUserName() {
    Authentication auth = getAuthentication();
    if (auth == null) {
      return "unknown";
    }

    return auth.getName();
  }

  public static void refreshAuth(User user) {
    Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
