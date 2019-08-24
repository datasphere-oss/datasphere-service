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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.datasphere.engine.manager.resource.common.DuplicateNameException;
import com.datasphere.engine.manager.resource.common.InvalidNameException;
import com.datasphere.engine.manager.resource.common.NoSuchProviderException;
import com.datasphere.engine.manager.resource.common.NoSuchResourceException;
import com.datasphere.engine.manager.resource.common.ResourceProviderException;
import com.datasphere.engine.manager.resource.dto.ResourceDTO;
import com.datasphere.engine.manager.resource.model.Resource;
import com.datasphere.engine.manager.resource.service.ResourceService;
import com.datasphere.engine.manager.resource.util.ControllerUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(value = "/resources")
public class ResourceController {

    private final static Logger _log = LoggerFactory.getLogger(ResourceController.class);

    @Value("${scopes.default}")
    private String defaultScope;

    @Autowired
    private ResourceService resourceService;

    /*
     * Resource w/scope
     */
    @GetMapping(value = "/api/c/{scope}/resources/{id}", produces = "application/json")
    @ApiOperation(value = "Fetch a specific resource by id")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @ResponseBody
    public ResourceDTO get(
            @ApiParam(value = "Scope", defaultValue = "default") @PathVariable("scope") Optional<String> scope,
            @ApiParam(value = "Resource id", required = true) @PathVariable("id") long id,
            HttpServletRequest request, HttpServletResponse response)
            throws NoSuchResourceException {

        String scopeId = scope.orElse(defaultScope);
        String userId = ControllerUtil.getUserId(request);

        _log.debug("get resource " + String.valueOf(id) + " by " + userId + " for scope " + scopeId);

        // call exists to trigger 404, otherwise get() will
        // check permissions *before* checking existence
        resourceService.exists(scopeId, userId, id);

        Resource resource = resourceService.get(scopeId, userId, id);

        // include private fields on detail view
        return ResourceDTO.fromResource(resource, true);
    }

    @PostMapping(value = "/api/c/{scope}/resources", produces = "application/json")
    @ApiOperation(value = "Create a new resource")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @ResponseBody
    public ResourceDTO create(
            @ApiParam(value = "Scope", defaultValue = "default") @PathVariable("scope") Optional<String> scope,
            @ApiParam(value = "Resource json", required = true) @RequestBody ResourceDTO resource,
            HttpServletRequest request, HttpServletResponse response)
            throws NoSuchProviderException, ResourceProviderException, InvalidNameException, DuplicateNameException {

        String scopeId = scope.orElse(defaultScope);
        String userId = ControllerUtil.getUserId(request);

        // parse fields from post
        Map<String, Serializable> propertiesMap = Resource.propertiesFromValue(resource.getProperties());
        List<String> tags = new ArrayList<>(Arrays.asList(resource.getTags()));

        _log.debug("create resource by " + userId + " for scope " + scopeId);

        Resource result = resourceService.create(scopeId, userId,
                resource.getType(), resource.getProvider(), resource.getName(),
                propertiesMap, tags);

        // include private fields on create view
        return ResourceDTO.fromResource(result, true);

    }

    @PutMapping(value = "/api/c/{scope}/resources", produces = "application/json")
    @ApiOperation(value = "Add an existing resource")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @ResponseBody
    public ResourceDTO add(
            @ApiParam(value = "Scope", defaultValue = "default") @PathVariable("scope") Optional<String> scope,
            @ApiParam(value = "Resource json", required = true) @RequestBody ResourceDTO resource,
            HttpServletRequest request, HttpServletResponse response)
            throws NoSuchProviderException, ResourceProviderException {

        String scopeId = scope.orElse(defaultScope);
        String userId = ControllerUtil.getUserId(request);

        // parse fields from post
        Map<String, Serializable> propertiesMap = Resource.propertiesFromValue(resource.getProperties());
        List<String> tags = new ArrayList<>(Arrays.asList(resource.getTags()));

        _log.debug("add resource by " + userId + " for scope " + scopeId);

        Resource result = resourceService.add(scopeId, userId, resource.getType(), resource.getProvider(),
                resource.getUri(),
                propertiesMap, tags);

        // include private fields on create view
        return ResourceDTO.fromResource(result, true);

    }

    @PutMapping(value = "/api/c/{scope}/resources/{id}", produces = "application/json")
    @ApiOperation(value = "Update a specific resource")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @ResponseBody
    public ResourceDTO update(
            @ApiParam(value = "Scope", defaultValue = "default") @PathVariable("scope") Optional<String> scope,
            @ApiParam(value = "Resource id", required = true) @PathVariable("id") long id,
            @ApiParam(value = "Resource json", required = true) @RequestBody ResourceDTO resource,
            HttpServletRequest request, HttpServletResponse response)
            throws NoSuchProviderException, NoSuchResourceException, ResourceProviderException {

        String scopeId = scope.orElse(defaultScope);
        String userId = ControllerUtil.getUserId(request);

        // parse fields from post
        Map<String, Serializable> propertiesMap = Resource.propertiesFromValue(resource.getProperties());
        List<String> tags = new ArrayList<>(Arrays.asList(resource.getTags()));

        resource.id = id;

        _log.debug("update resource " + String.valueOf(id) + " by " + userId + " for scope " + scopeId);

        // call exists to trigger 404, otherwise update() will
        // check permissions *before* checking existence
        resourceService.exists(scopeId, userId, id);

        Resource result = resourceService.update(scopeId, userId, id, propertiesMap, tags);

        // include private fields on update view
        return ResourceDTO.fromResource(result, true);

    }

    @DeleteMapping(value = "/api/c/{scope}/resources/{id}", produces = "application/json")
    @ApiOperation(value = "Delete a specific resource")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @ResponseBody
    public ResourceDTO delete(
            @ApiParam(value = "Scope", defaultValue = "default") @PathVariable("scope") Optional<String> scope,
            @ApiParam(value = "Resource id", required = true) @PathVariable("id") long id,
            HttpServletRequest request, HttpServletResponse response)
            throws NoSuchProviderException, NoSuchResourceException, ResourceProviderException {

        String scopeId = scope.orElse(defaultScope);
        String userId = ControllerUtil.getUserId(request);

        _log.debug("delete resource " + String.valueOf(id) + " by " + userId + " for scope " + scopeId);

        // call exists to trigger 404, otherwise delete() will
        // check permissions *before* checking existence
        resourceService.exists(scopeId, userId, id);

        // fetch resource to provide as result on success
        Resource resource = resourceService.get(scopeId, userId, id);

        resourceService.delete(scopeId, userId, id);

        return ResourceDTO.fromResource(resource, false);

    }

    /*
     * List w/scope
     */

    @GetMapping(value = "/api/c/{scope}/resources", produces = "application/json")
    @ApiOperation(value = "List resources with filters")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @ResponseBody
    public List<ResourceDTO> list(
            @ApiParam(value = "Resource scope", defaultValue = "default") @PathVariable("scope") Optional<String> scope,
            @ApiParam(value = "Resource type") @RequestParam("type") Optional<String> type,
            @ApiParam(value = "Resource provider") @RequestParam("provider") Optional<String> provider,
            @ApiParam(value = "Resource owner") @RequestParam("user") Optional<String> ownerId,
            HttpServletRequest request, HttpServletResponse response,
            Pageable pageable) {

        String scopeId = scope.orElse(defaultScope);
        String userId = ControllerUtil.getUserId(request);

        _log.debug("list resources by " + userId + " for scope " + scopeId);

        long total = 0;
        List<Resource> resources = new ArrayList<>();

        // TODO refactor - ugly
        if (type.isPresent()) {
            total = resourceService.countByType(scopeId, userId, type.get());
            resources = resourceService.listByType(scopeId, userId, type.get());
        } else if (provider.isPresent()) {
            total = resourceService.countByProvider(scopeId, userId, provider.get());
            resources = resourceService.listByProvider(scopeId, userId, provider.get());
        } else if (ownerId.isPresent()) {
            total = resourceService.countByUserId(scopeId, userId, ownerId.get());
            resources = resourceService.listByUserId(scopeId, userId, ownerId.get());
        } else {
            total = resourceService.count(scopeId, userId);
            resources = resourceService.list(scopeId, userId, pageable.getPageNumber(), pageable.getPageSize());
        }
        List<ResourceDTO> results = resources.stream().map(r -> ResourceDTO.fromResource(r))
                .collect(Collectors.toList());
        // add total count as header
        response.setHeader("X-Total-Count", String.valueOf(total));

        return results;
    }

    /*
     * Resource
     */
    @GetMapping(value = "/api/resources/{id}", produces = "application/json")
    @ApiOperation(value = "Fetch a specific resource by id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token"),
            @ApiImplicitParam(name = "X-Scope", value = "Scope", required = false, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "default", defaultValue = "default")
    })
    @ResponseBody
    public ResourceDTO get(
            @ApiParam(value = "Resource id", required = true) @PathVariable("id") long id,
            HttpServletRequest request, HttpServletResponse response)
            throws NoSuchResourceException {

        Optional<String> scopeId = Optional.ofNullable(ControllerUtil.getScopeId(request));
        return get(scopeId, id, request, response);
    }

    @PostMapping(value = "/api/resources", produces = "application/json")
    @ApiOperation(value = "Create a new resource")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token"),
            @ApiImplicitParam(name = "X-Scope", value = "Scope", required = false, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "default", defaultValue = "default")
    })
    @ResponseBody
    public ResourceDTO create(
            @ApiParam(value = "Resource json", required = true) @RequestBody ResourceDTO resource,
            HttpServletRequest request, HttpServletResponse response)
            throws NoSuchProviderException, ResourceProviderException, InvalidNameException, DuplicateNameException {

        Optional<String> scopeId = Optional.ofNullable(ControllerUtil.getScopeId(request));
        return create(scopeId, resource, request, response);
    }

    @PutMapping(value = "/api/resources", produces = "application/json")
    @ApiOperation(value = "Add an existing resource")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token"),
            @ApiImplicitParam(name = "X-Scope", value = "Scope", required = false, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "default", defaultValue = "default")
    })
    @ResponseBody
    public ResourceDTO add(
            @ApiParam(value = "Resource json", required = true) @RequestBody ResourceDTO resource,
            HttpServletRequest request, HttpServletResponse response)
            throws NoSuchProviderException, ResourceProviderException {

        Optional<String> scopeId = Optional.ofNullable(ControllerUtil.getScopeId(request));
        return add(scopeId, resource, request, response);
    }

    @PutMapping(value = "/api/resources/{id}", produces = "application/json")
    @ApiOperation(value = "Update a specific resource")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token"),
            @ApiImplicitParam(name = "X-Scope", value = "Scope", required = false, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "default", defaultValue = "default")
    })
    @ResponseBody
    public ResourceDTO update(
            @ApiParam(value = "Resource id", required = true) @PathVariable("id") long id,
            @ApiParam(value = "Resource json", required = true) @RequestBody ResourceDTO resource,
            HttpServletRequest request, HttpServletResponse response)
            throws NoSuchProviderException, NoSuchResourceException, ResourceProviderException {

        Optional<String> scopeId = Optional.ofNullable(ControllerUtil.getScopeId(request));
        return update(scopeId, id, resource, request, response);
    }

    @DeleteMapping(value = "/api/resources/{id}", produces = "application/json")
    @ApiOperation(value = "Delete a specific resource")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token"),
            @ApiImplicitParam(name = "X-Scope", value = "Scope", required = false, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "default", defaultValue = "default")
    })
    @ResponseBody
    public ResourceDTO delete(
            @ApiParam(value = "Resource id", required = true) @PathVariable("id") long id,
            HttpServletRequest request, HttpServletResponse response)
            throws NoSuchProviderException, NoSuchResourceException, ResourceProviderException {

        Optional<String> scopeId = Optional.ofNullable(ControllerUtil.getScopeId(request));
        return delete(scopeId, id, request, response);
    }

    /*
     * List
     */

    @GetMapping(value = "/api/resources", produces = "application/json")
    @ApiOperation(value = "List resources with filters")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token"),
            @ApiImplicitParam(name = "X-Scope", value = "Scope", required = false, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "default", defaultValue = "default")
    })
    @ResponseBody
    public List<ResourceDTO> list(
            @RequestParam("type") Optional<String> type,
            @RequestParam("provider") Optional<String> provider,
            @RequestParam("user") Optional<String> ownerId,
            HttpServletRequest request, HttpServletResponse response,
            Pageable pageable) {

        Optional<String> scopeId = Optional.ofNullable(ControllerUtil.getScopeId(request));
        return list(scopeId, type, provider, ownerId, request, response, pageable);

    }

    /*
     * Exceptions
     */

    @ExceptionHandler(NoSuchResourceException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String notFound(NoSuchResourceException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(NoSuchProviderException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String notFound(NoSuchProviderException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(ResourceProviderException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String providerError(ResourceProviderException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(InvalidNameException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String nameError(InvalidNameException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(DuplicateNameException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String duplicateError(DuplicateNameException ex) {
        return ex.getMessage();
    }

    /*
     * Helper
     */

//	private String getUserId() {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		if (!(authentication instanceof AnonymousAuthenticationToken)) {
//			String currentUserName = authentication.getName();
//			return currentUserName;
//		}
//	}

}
