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

package com.datasphere.datalayer.resource.manager.security;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.datasphere.datalayer.resource.manager.model.Resource;
import com.datasphere.datalayer.resource.manager.service.ResourceLocalService;

@Component
public class ResourcePermissionEvaluator implements PermissionEvaluator {
	private final static Logger _log = LoggerFactory.getLogger(ResourcePermissionEvaluator.class);

	@Autowired
	ScopePermissionEvaluator scopePermission;

	@Autowired
	ResourceLocalService resourceService;

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		if (targetDomainObject != null && Resource.class.isAssignableFrom(targetDomainObject.getClass())) {
			Resource res = (Resource) targetDomainObject;

			_log.debug("hasPermission for resource " + res.getId() + ":" + permission);

			// delegate to ScopePermission
			return scopePermission.hasPermission(authentication, res.getScopeId(), "scope", permission);
		}

		return false;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		try {
			long id = Long.parseLong(targetId.toString());
			Resource res = resourceService.fetch(id);

			return hasPermission(authentication, res, permission);

		} catch (Exception ex) {
			_log.error(ex.getMessage());
			return false;
		}

	}

}
