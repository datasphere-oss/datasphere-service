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
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.datasphere.engine.manager.resource.common.NoSuchProviderException;
import com.datasphere.engine.manager.resource.dto.ProviderDTO;
import com.datasphere.engine.manager.resource.model.ResourceProvider;
import com.datasphere.engine.manager.resource.service.ProviderService;
import com.datasphere.engine.manager.resource.util.ControllerUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(value = "/providers")
public class ProviderController {

    private final static Logger _log = LoggerFactory.getLogger(ProviderController.class);

    @Value("${scopes.default}")
    private String defaultScope;

    @Autowired
    private ProviderService providerService;

    /*
     * List w/scope
     */

    @GetMapping(value = "/api/c/{scope}/providers", produces = "application/json")
    @ApiOperation(value = "List available resource providers")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public List<ProviderDTO> list(
            @ApiParam(value = "Scope", defaultValue = "default") @PathVariable("scope") Optional<String> scope,
            @RequestParam("type") Optional<String> type,
            HttpServletRequest request, HttpServletResponse response,
            Pageable pageable) {

        String scopeId = scope.orElse(defaultScope);
        String userId = ControllerUtil.getUserId(request);

        _log.debug("list providers by " + userId + " for scope " + scopeId);

        List<ProviderDTO> results = new ArrayList<>();
        if (type.isPresent()) {
            List<ResourceProvider> providers = providerService.list(scopeId, userId, type.get());
            for (ResourceProvider p : providers) {
                results.add(ProviderDTO.fromProvider(p));
            }
        } else {
            Map<String, List<ResourceProvider>> providers = providerService.list(scopeId, userId);
            for (String t : providers.keySet()) {
                for (ResourceProvider p : providers.get(t)) {
                    results.add(ProviderDTO.fromProvider(p));
                }
            }

        }

        // add total count as header
        response.setHeader("X-Total-Count", String.valueOf(results.size()));

        return results;
    }

    /*
     * Get w/scope
     */

    @GetMapping(value = "/api/c/{scope}/providers/{id}", produces = "application/json")
    @ApiOperation(value = "Fetch a specific resource provider by id")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @ResponseBody
    public ProviderDTO get(
            @ApiParam(value = "Scope", defaultValue = "default") @PathVariable("scope") Optional<String> scope,
            @PathVariable("id") String id,
            HttpServletRequest request, HttpServletResponse response)
            throws NoSuchProviderException {

        String scopeId = scope.orElse(defaultScope);
        String userId = ControllerUtil.getUserId(request);

        _log.debug("get provider " + id + " by " + userId + " for scope " + scopeId);

        ResourceProvider p = providerService.get(scopeId, userId, id);

        return ProviderDTO.fromProvider(p);
    }

    /*
     * Types w/scope
     */

    @GetMapping(value = "/api/c/{scope}/providers/types", produces = "application/json")
    @ApiOperation(value = "List available provider types")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public List<String> types(
            @ApiParam(value = "Scope", defaultValue = "default") @PathVariable("scope") Optional<String> scope,
            HttpServletRequest request, HttpServletResponse response,
            Pageable pageable) {

        String scopeId = scope.orElse(defaultScope);
        String userId = ControllerUtil.getUserId(request);

        _log.debug("list types by " + userId + " for scope " + scopeId);

        List<String> results = providerService.listTypes(scopeId, userId);

        // add total count as header
        response.setHeader("X-Total-Count", String.valueOf(results.size()));

        return results;
    }

    /*
     * List
     */

    @GetMapping(value = "/api/providers", produces = "application/json")
    @ApiOperation(value = "List available resource providers")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token"),
            @ApiImplicitParam(name = "X-Scope", value = "Scope", required = false, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "default", defaultValue = "default")
    })
    public List<ProviderDTO> list(
            @RequestParam("type") Optional<String> type,
            HttpServletRequest request, HttpServletResponse response,
            Pageable pageable) {

        Optional<String> scopeId = Optional.ofNullable(ControllerUtil.getScopeId(request));
        return list(scopeId, type, request, response, pageable);
    }

    /*
     * Get
     */

    @GetMapping(value = "/api/providers/{id}", produces = "application/json")
    @ApiOperation(value = "Fetch a specific resource provider by id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token"),
            @ApiImplicitParam(name = "X-Scope", value = "Scope", required = false, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "default", defaultValue = "default")
    })
    @ResponseBody
    public ProviderDTO get(
            @PathVariable("id") String id,
            HttpServletRequest request, HttpServletResponse response)
            throws NoSuchProviderException {

        Optional<String> scopeId = Optional.ofNullable(ControllerUtil.getScopeId(request));
        return get(scopeId, id, request, response);
    }

    /*
     * Type
     */

    @GetMapping(value = "/api/providers/types", produces = "application/json")
    @ApiOperation(value = "List available provider types")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token"),
            @ApiImplicitParam(name = "X-Scope", value = "Scope", required = false, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "default", defaultValue = "default")
    })
    public List<String> types(
            HttpServletRequest request, HttpServletResponse response,
            Pageable pageable) {

        Optional<String> scopeId = Optional.ofNullable(ControllerUtil.getScopeId(request));
        return types(scopeId, request, response, pageable);
    }

    /*
     * Exceptions
     */

    @ExceptionHandler(NoSuchProviderException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String notFound(NoSuchProviderException ex) {
        return ex.getMessage();
    }

    /*
     * Helper
     */

}
