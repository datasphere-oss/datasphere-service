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
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.DenyAllPermissionEvaluator;
import org.springframework.security.core.Authentication;

public class PermissionEvaluatorManager implements PermissionEvaluator {

    private final static Logger _log = LoggerFactory.getLogger(PermissionEvaluatorManager.class);

    private final static PermissionEvaluator denyAll = new DenyAllPermissionEvaluator();

    // store permission evaluators map with [ENTITY] key
    private final Map<String, PermissionEvaluator> permissionEvaluators;

    @Value("${permissions.enabled}")
    private boolean enabled;

    public PermissionEvaluatorManager(Map<String, PermissionEvaluator> permissionEvaluators) {
        this.permissionEvaluators = permissionEvaluators;

        _log.debug("available evaluators for " + this.permissionEvaluators.keySet().toString());

    }

    @Override
    public boolean hasPermission(
            Authentication authentication, Object targetDomainObject, Object permission) {

        if (!enabled) {
            // permission system disabled, permit all
            return true;
        }

        // fetch specific permissionEvaluator by looking object class
        String className = targetDomainObject.getClass().getSimpleName().toUpperCase();

        _log.debug("hasPermission for " + className + ":" + permission.toString());
        PermissionEvaluator permissionEvaluator = permissionEvaluators.get(className);

        // deny all unknown
        if (permissionEvaluator == null) {
            permissionEvaluator = denyAll;
        }

        return permissionEvaluator.hasPermission(authentication, targetDomainObject, permission);
    }

    @Override
    public boolean hasPermission(
            Authentication authentication, Serializable targetId, String targetType,
            Object permission) {

        if (!enabled) {
            // permission system disabled, permit all
            return true;
        }

        // fetch specific permissionEvaluator by looking object class
        String className = targetType.toUpperCase();

        _log.debug("hasPermission for " + targetType + ":" + permission.toString());
        PermissionEvaluator permissionEvaluator = permissionEvaluators.get(className);

        // deny all unknown
        if (permissionEvaluator == null) {
            permissionEvaluator = denyAll;
        }

        return permissionEvaluator.hasPermission(authentication, targetId, targetType, permission);
    }
}