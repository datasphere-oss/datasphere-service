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

package com.datasphere.datalayer.resource.manager.controller;

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

import com.datasphere.datalayer.resource.manager.common.NoSuchConsumerException;
import com.datasphere.datalayer.resource.manager.dto.BuilderDTO;
import com.datasphere.datalayer.resource.manager.model.ConsumerBuilder;
import com.datasphere.datalayer.resource.manager.service.ConsumerService;
import com.datasphere.datalayer.resource.manager.util.ControllerUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(value = "/builders")
public class BuilderController {

    private final static Logger _log = LoggerFactory.getLogger(BuilderController.class);

    @Value("${scopes.default}")
    private String defaultScope;

    @Autowired
    private ConsumerService consumerService;

    /*
     * List w/scope
     */

    @GetMapping(value = "/api/c/{scope}/builders", produces = "application/json")
    @ApiOperation(value = "List available consumer builders")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public List<BuilderDTO> list(
            @ApiParam(value = "Scope", defaultValue = "default") @PathVariable("scope") Optional<String> scope,
            @RequestParam("type") Optional<String> type,
            HttpServletRequest request, HttpServletResponse response,
            Pageable pageable) {

        String scopeId = scope.orElse(defaultScope);
        String userId = ControllerUtil.getUserId(request);

        _log.debug("list builders by " + userId + " for scope " + scopeId);

        List<BuilderDTO> results = new ArrayList<>();
        if (type.isPresent()) {
            List<ConsumerBuilder> builders = consumerService.listBuilders(scopeId, userId, type.get());
            for (ConsumerBuilder cb : builders) {
                results.add(BuilderDTO.fromBuilder(cb));
            }

        } else {
            Map<String, List<ConsumerBuilder>> map = consumerService.listBuilders(scopeId, userId);
            for (String t : map.keySet()) {
                for (ConsumerBuilder cb : map.get(t)) {
                    results.add(BuilderDTO.fromBuilder(cb));
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

    @GetMapping(value = "/api/c/{scope}/builders/{id}", produces = "application/json")
    @ApiOperation(value = "Fetch a specific consumer builder by id")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @ResponseBody
    public BuilderDTO get(
            @ApiParam(value = "Scope", defaultValue = "default") @PathVariable("scope") Optional<String> scope,
            @PathVariable("id") String id,
            HttpServletRequest request, HttpServletResponse response)
            throws NoSuchConsumerException {

        String scopeId = scope.orElse(defaultScope);
        String userId = ControllerUtil.getUserId(request);

        _log.debug("get builder " + id + " by " + userId + " for scope " + scopeId);

        ConsumerBuilder cb = consumerService.getBuilder(scopeId, userId, id);

        return BuilderDTO.fromBuilder(cb);
    }

    /*
     * Types w/scope
     */

    @GetMapping(value = "/api/c/{scope}/builders/types", produces = "application/json")
    @ApiOperation(value = "List available consumer types")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    public List<String> types(
            @ApiParam(value = "Scope", defaultValue = "default") @PathVariable("scope") Optional<String> scope,
            HttpServletRequest request, HttpServletResponse response,
            Pageable pageable) {

        String scopeId = scope.orElse(defaultScope);
        String userId = ControllerUtil.getUserId(request);

        _log.debug("list types by " + userId + " for scope " + scopeId);

        List<String> results = consumerService.listTypes(scopeId, userId);

        // add total count as header
        response.setHeader("X-Total-Count", String.valueOf(results.size()));

        return results;
    }

    /*
     * List
     */

    @GetMapping(value = "/api/builders", produces = "application/json")
    @ApiOperation(value = "List available consumer builders")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token"),
            @ApiImplicitParam(name = "X-Scope", value = "Scope", required = false, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "default", defaultValue = "default")
    })
    public List<BuilderDTO> list(
            @RequestParam("type") Optional<String> type,
            HttpServletRequest request, HttpServletResponse response,
            Pageable pageable) {

        Optional<String> scopeId = Optional.ofNullable(ControllerUtil.getScopeId(request));
        return list(scopeId, type, request, response, pageable);
    }

    /*
     * Get
     */

    @GetMapping(value = "/api/builders/{id}", produces = "application/json")
    @ApiOperation(value = "Fetch a specific consumer builder by id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token"),
            @ApiImplicitParam(name = "X-Scope", value = "Scope", required = false, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "default", defaultValue = "default")
    })
    @ResponseBody
    public BuilderDTO get(
            @PathVariable("id") String id,
            HttpServletRequest request, HttpServletResponse response)
            throws NoSuchConsumerException {

        Optional<String> scopeId = Optional.ofNullable(ControllerUtil.getScopeId(request));
        return get(scopeId, id, request, response);
    }

    /*
     * Type
     */

    @GetMapping(value = "/api/builders/types", produces = "application/json")
    @ApiOperation(value = "List available consumer types")
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

    @ExceptionHandler(NoSuchConsumerException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String notFound(NoSuchConsumerException ex) {
        return ex.getMessage();
    }

    /*
     * Helper
     */
}