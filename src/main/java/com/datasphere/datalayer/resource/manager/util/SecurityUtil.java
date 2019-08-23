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

package com.datasphere.datalayer.resource.manager.util;

import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {

	private static final Logger _log = LoggerFactory.getLogger(SecurityUtil.class);

	private static final String ANONYMOUS = "anonymous";

	public static String getUserName() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		String username = ANONYMOUS;

		if (null != authentication) {
			if (authentication.getPrincipal() instanceof UserDetails) {
				UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
				username = springSecurityUser.getUsername();

			} else if (authentication.getPrincipal() instanceof String) {
				username = (String) authentication.getPrincipal();

			} else {
				_log.debug("no user detail");
			}
		} else {
			_log.debug("not authenticated");
		}

		_log.debug("username " + username);

		return username;
	}

	public static Set<String> getUserRoles() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		Set<String> roles = new HashSet<>();

		if (null != authentication) {
			authentication.getAuthorities()
					.forEach(e -> roles.add(e.getAuthority()));
		}
		return roles;
	}
}