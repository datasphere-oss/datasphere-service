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

package com.datasphere.engine.manager.resource.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.datasphere.engine.manager.resource.SystemKeys;

@Component
public class ScopePermissionEvaluator implements PermissionEvaluator {
    private final static Logger _log = LoggerFactory.getLogger(ScopePermissionEvaluator.class);

    @Value("${scopes.enabled}")
    private boolean enabled;

    @Value("${scopes.list}")
    private List<String> scopes;

    @Value("${scopes.default}")
    private String defaultScope;

    @Value("${scopes.roles.mapping.admin}")
    private String roleAdminMapping;

    @Value("${scopes.roles.mapping.resourceAdmin}")
    private String roleResourceAdminMapping;

    @Value("${scopes.roles.mapping.consumerAdmin}")
    private String roleConsumerAdminMapping;

    @Value("${scopes.roles.mapping.user}")
    private String roleUserMapping;

    @PostConstruct
    public void init() {
        _log.debug("scopePermission enabled? " + enabled);

        if (scopes == null) {
            scopes = new ArrayList<>();
        }

        // add placeholder to scopes if empty
        if (scopes.isEmpty()) {
            scopes.add("*");
        }

        // always add default scope if defined
        if (!defaultScope.isEmpty() && !scopes.contains(defaultScope)) {
            scopes.add(defaultScope);
        }

        _log.debug("scopes: " + scopes.toString());

        // set default mappings
        if (roleAdminMapping.isEmpty()) {
            roleAdminMapping = SystemKeys.ROLE_ADMIN;
        }
        if (roleResourceAdminMapping.isEmpty()) {
            roleResourceAdminMapping = SystemKeys.ROLE_RESOURCE_ADMIN;
        }
        if (roleConsumerAdminMapping.isEmpty()) {
            roleConsumerAdminMapping = SystemKeys.ROLE_CONSUMER_ADMIN;
        }
        if (roleUserMapping.isEmpty()) {
            roleUserMapping = SystemKeys.ROLE_USER;
        }

        _log.debug("role mapping " + SystemKeys.ROLE_ADMIN + " to " + roleAdminMapping);
        _log.debug("role mapping " + SystemKeys.ROLE_RESOURCE_ADMIN + " to " + roleResourceAdminMapping);
        _log.debug("role mapping " + SystemKeys.ROLE_CONSUMER_ADMIN + " to " + roleConsumerAdminMapping);
        _log.debug("role mapping " + SystemKeys.ROLE_USER + " to " + roleUserMapping);

    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        // no scope object to check
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
            Object permission) {

        String scopeId = targetId.toString();
        String userId = authentication.getName();
        String action = permission.toString();

        for (GrantedAuthority ga : authentication.getAuthorities()) {
            _log.debug("user " + userId + " authority " + ga.toString());
        }

        boolean isPermitted = isScopePermitted(scopeId);
        _log.debug("user " + userId + " hasPermission scope " + scopeId + " permitted " + isPermitted);

        // check in Auth
        boolean hasPermission = false;

        // fetch ONLY scope roles
        List<GrantedAuthority> authorities = new ArrayList<>(authentication.getAuthorities());
        _log.debug("user " + userId + " authorities " + authorities.toString());

        
        Set<String> roles = new HashSet<>();
        roles.addAll(getScopeRoles(scopeId, authorities));

        // fetch all permissions related to any assigned role
        Set<String> permissions = new HashSet<>();
        for (String role : roles) {
            permissions.addAll(roleToPermissions(role));
        }

        _log.debug("user " + userId + " permissions " + permissions.toString());

        hasPermission = permissions.contains(action);

        _log.debug("user " + userId + " hasPermission for scope " + scopeId + ":" + action + " " + hasPermission);

        return (isPermitted && hasPermission);
    }

    /*
     * Helpers
     */
    public List<String> getScopeRoles(String scopeId, Authentication authentication) {
        List<GrantedAuthority> authorities = new ArrayList<>(authentication.getAuthorities());
        return getScopeRoles(scopeId, authorities);
    }

    public boolean isScopePermitted(String scopeId) {

        if (!defaultScope.isEmpty() && scopeId.equals(defaultScope)) {
            // default scope always enabled if defined
            return true;
        }

        if (enabled) {
            if (scopes.contains("*")) {
                return true;
            }
            return scopes.contains(scopeId);
        }

        return false;
    }

    private List<String> getScopeRoles(String scopeId, List<GrantedAuthority> authorities) {
        List<String> roles = new ArrayList<>();

        for (GrantedAuthority ga : authorities) {
            // support variable substitution with placeholder <scope>
            String auth = ga.getAuthority();
            if (auth != null) {
                // check against mappings
                if (auth.equals(roleAdminMapping.replace("<scope>", scopeId))) {
                    roles.add(SystemKeys.ROLE_ADMIN);
                }
                if (auth.equals(roleResourceAdminMapping.replace("<scope>", scopeId))) {
                    roles.add(SystemKeys.ROLE_RESOURCE_ADMIN);
                }
                if (auth.equals(roleConsumerAdminMapping.replace("<scope>", scopeId))) {
                    roles.add(SystemKeys.ROLE_CONSUMER_ADMIN);
                }
                if (auth.equals(roleUserMapping.replace("<scope>", scopeId))) {
                    roles.add(SystemKeys.ROLE_USER);
                }

            }
        }

        return roles;
    }

    public Set<String> roleToPermissions(String role) {
        // statically resolve roles => permission mapping
        // TODO refactor
        Set<String> permissions = new HashSet<String>();

        if (role.equals(SystemKeys.ROLE_ADMIN)) {
            permissions.addAll(Arrays.asList(
                    SystemKeys.PERMISSION_RESOURCE_CREATE,
                    SystemKeys.PERMISSION_RESOURCE_UPDATE,
                    SystemKeys.PERMISSION_RESOURCE_DELETE,
                    SystemKeys.PERMISSION_RESOURCE_CHECK,
                    SystemKeys.PERMISSION_RESOURCE_VIEW,

                    SystemKeys.PERMISSION_CONSUMER_CREATE,
                    SystemKeys.PERMISSION_CONSUMER_UPDATE,
                    SystemKeys.PERMISSION_CONSUMER_DELETE,
                    SystemKeys.PERMISSION_CONSUMER_VIEW));
        } else if (role.equals(SystemKeys.ROLE_RESOURCE_ADMIN)) {
            permissions.addAll(Arrays.asList(
                    SystemKeys.PERMISSION_RESOURCE_CREATE,
                    SystemKeys.PERMISSION_RESOURCE_UPDATE,
                    SystemKeys.PERMISSION_RESOURCE_DELETE,
                    SystemKeys.PERMISSION_RESOURCE_CHECK,
                    SystemKeys.PERMISSION_RESOURCE_VIEW,

                    SystemKeys.PERMISSION_CONSUMER_VIEW));
        } else if (role.equals(SystemKeys.ROLE_CONSUMER_ADMIN)) {
            permissions.addAll(Arrays.asList(
                    SystemKeys.PERMISSION_RESOURCE_CHECK,
                    SystemKeys.PERMISSION_RESOURCE_VIEW,

                    SystemKeys.PERMISSION_CONSUMER_CREATE,
                    SystemKeys.PERMISSION_CONSUMER_UPDATE,
                    SystemKeys.PERMISSION_CONSUMER_DELETE,
                    SystemKeys.PERMISSION_CONSUMER_VIEW));
        } else if (role.equals(SystemKeys.ROLE_USER)) {
            permissions.addAll(Arrays.asList(
                    SystemKeys.PERMISSION_RESOURCE_CHECK,
                    SystemKeys.PERMISSION_RESOURCE_VIEW,
                    SystemKeys.PERMISSION_CONSUMER_VIEW));
        }

        return permissions;
    }

}
