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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.HandlerMapping;

public class ControllerUtil {

//	public static String getUserId(final HttpServletRequest request) {
//		Principal principal = request.getUserPrincipal();
//		if (principal != null) {
//			return principal.getName();
//		} else {
//			return "anonymous";
//		}
//	}

	public static String getUserId(final HttpServletRequest request) {
		return SecurityUtil.getUserName();
	}

	public static String getScopeId(final HttpServletRequest request) {
		String scopeId = null;

		Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		if (pathVariables.containsKey("scope")) {
			scopeId = (String) pathVariables.get("scope");
		}

		if (request.getHeader("X-Scope") != null) {
			scopeId = request.getHeader("X-Scope");
		}

		return scopeId;
	}
}
