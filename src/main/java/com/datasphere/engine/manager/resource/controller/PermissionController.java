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

package com.datasphere.engine.manager.resource.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.datasphere.engine.manager.resource.security.ScopePermissionEvaluator;
import com.datasphere.engine.manager.resource.util.ControllerUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(value = "/permissions")
public class PermissionController {

    private final static Logger _log = LoggerFactory.getLogger(PermissionController.class);

    @Value("${scopes.default}")
    private String defaultScope;

    @Value("${scopes.list}")
    private List<String> scopes;

    @Autowired
    private ScopePermissionEvaluator permissionEvaluator;

    /*
     * Permission list w/scope
     */
    @GetMapping(value = "/api/c/{scope}/permissions", produces = "application/json")
    @ApiOperation(value = "Fetch all permissions for the scope")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @ResponseBody
    public List<String> permissions(
            @ApiParam(value = "Scope", defaultValue = "default") @PathVariable("scope") Optional<String> scope,
            HttpServletRequest request, HttpServletResponse response) {

        String scopeId = scope.orElse(defaultScope);
        String userId = ControllerUtil.getUserId(request);

        _log.debug("get permissions for user " + userId + " for scope " + scopeId);

        List<String> permissions = new ArrayList<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            if (permissionEvaluator.isScopePermitted(scopeId)) {
                List<String> roles = permissionEvaluator.getScopeRoles(scopeId, auth);
                for (String role : roles) {
                    permissions.addAll(permissionEvaluator.roleToPermissions(role));
                }
            }
        }

        return permissions;
    }

    /*
     * Roles list w/scope
     */
    @GetMapping(value = "/api/c/{scope}/roles", produces = "application/json")
    @ApiOperation(value = "Fetch all roles for the scope")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @ResponseBody
    public List<String> roles(
            @ApiParam(value = "Scope", defaultValue = "default") @PathVariable("scope") Optional<String> scope,
            HttpServletRequest request, HttpServletResponse response) {

        String scopeId = scope.orElse(defaultScope);
        String userId = ControllerUtil.getUserId(request);

        _log.debug("get roles for user " + userId + " for scope " + scopeId);

        List<String> roles = new ArrayList<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            if (permissionEvaluator.isScopePermitted(scopeId)) {
                roles.addAll(permissionEvaluator.getScopeRoles(scopeId, auth));
            }
        }

        return roles;
    }

    /*
     * Scopes list
     */
    @GetMapping(value = "/api/scopes", produces = "application/json")
    @ApiOperation(value = "Fetch all scopes")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    })
    @ResponseBody
    public List<String> scopes(
            HttpServletRequest request, HttpServletResponse response) {

        if (scopes.isEmpty()) {
            scopes.add(defaultScope);
        }
        return scopes;
    }

    /*
     * Permissions list
     */
    @GetMapping(value = "/api/permissions", produces = "application/json")
    @ApiOperation(value = "Fetch all permissions for the scope")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token"),
            @ApiImplicitParam(name = "X-Scope", value = "Scope", required = false, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "default", defaultValue = "default")
    })
    @ResponseBody
    public List<String> permissions(
            HttpServletRequest request, HttpServletResponse response) {

        Optional<String> scopeId = Optional.ofNullable(ControllerUtil.getScopeId(request));
        return permissions(scopeId, request, response);
    }

    /*
     * Roles list
     */
    @GetMapping(value = "/api/roles", produces = "application/json")
    @ApiOperation(value = "Fetch all roles for the scope")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token"),
            @ApiImplicitParam(name = "X-Scope", value = "Scope", required = false, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "default", defaultValue = "default")
    })
    @ResponseBody
    public List<String> roles(
            HttpServletRequest request, HttpServletResponse response) {

        Optional<String> scopeId = Optional.ofNullable(ControllerUtil.getScopeId(request));
        return roles(scopeId, request, response);
    }
}
