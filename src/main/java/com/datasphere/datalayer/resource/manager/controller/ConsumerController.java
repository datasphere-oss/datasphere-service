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

import com.datasphere.datalayer.resource.manager.common.ConsumerException;
import com.datasphere.datalayer.resource.manager.common.NoSuchConsumerException;
import com.datasphere.datalayer.resource.manager.dto.ConsumerDTO;
import com.datasphere.datalayer.resource.manager.model.Consumer;
import com.datasphere.datalayer.resource.manager.model.Registration;
import com.datasphere.datalayer.resource.manager.model.Resource;
import com.datasphere.datalayer.resource.manager.service.ConsumerService;
import com.datasphere.datalayer.resource.manager.util.ControllerUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(value = "/consumers")
public class ConsumerController {

    private final static Logger _log = LoggerFactory.getLogger(ConsumerController.class);

    @Value("${scopes.default}")
    private String defaultScope;

    @Autowired
    private ConsumerService consumerService;

    /*
     * Consumer registration w/scope
     */
    @GetMapping(value = "/api/c/{scope}/consumers/{id}", produces = "application/json")
    @ApiOperation(value = "Fetch a specific consumer by id")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @ResponseBody
    public ConsumerDTO get(
            @ApiParam(value = "Scope", defaultValue = "default") @PathVariable("scope") Optional<String> scope,
            @ApiParam(value = "Consumer id", required = true) @PathVariable("id") long id,
            HttpServletRequest request, HttpServletResponse response)
            throws NoSuchConsumerException {

        String scopeId = scope.orElse(defaultScope);
        String userId = ControllerUtil.getUserId(request);

        _log.debug("get consumer " + String.valueOf(id) + " by " + userId + " for scope " + scopeId);

        // call exists to trigger 404, otherwise get() will
        // check permissions *before* checking existence
        consumerService.exists(scopeId, userId, id);

        Consumer consumer = consumerService.lookup(scopeId, userId, id);

        // include private fields on detail view
        return ConsumerDTO.fromConsumer(consumer, true);
    }

    @PostMapping(value = "/api/c/{scope}/consumers", produces = "application/json")
    @ApiOperation(value = "Add a new consumer")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @ResponseBody
    public ConsumerDTO add(
            @ApiParam(value = "Scope", defaultValue = "default") @PathVariable("scope") Optional<String> scope,
            @ApiParam(value = "Consumer json", required = true) @RequestBody ConsumerDTO res,
            HttpServletRequest request, HttpServletResponse response)
            throws NoSuchConsumerException, ConsumerException {

        String scopeId = scope.orElse(defaultScope);
        String userId = ControllerUtil.getUserId(request);

        _log.debug("add consumer by " + userId + " for scope " + scopeId);

        // parse fields from post
        Map<String, Serializable> propertiesMap = Resource.propertiesFromValue(res.getProperties());
        List<String> tags = new ArrayList<>(Arrays.asList(res.getTags()));

        Registration reg = consumerService.add(scopeId, userId, res.getType(), res.getConsumer(), propertiesMap, tags);

        // include private fields on create view
        return ConsumerDTO.fromRegistration(reg, true);

    }

    @PutMapping(value = "/api/c/{scope}/consumers/{id}", produces = "application/json")
    @ApiOperation(value = "Update a consumer")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @ResponseBody
    public ConsumerDTO update(
            @ApiParam(value = "Scope", defaultValue = "default") @PathVariable("scope") Optional<String> scope,
            @ApiParam(value = "Consumer id", required = true) @PathVariable("id") long id,
            @ApiParam(value = "Consumer json", required = true) @RequestBody ConsumerDTO res,
            HttpServletRequest request, HttpServletResponse response)
            throws NoSuchConsumerException, ConsumerException {

        String scopeId = scope.orElse(defaultScope);
        String userId = ControllerUtil.getUserId(request);

        _log.debug("update consumer by " + userId + " for scope " + scopeId);

        // parse fields from post
        Map<String, Serializable> propertiesMap = Resource.propertiesFromValue(res.getProperties());
        List<String> tags = new ArrayList<>(Arrays.asList(res.getTags()));

        res.id = id;

        // call exists to trigger 404, otherwise update() will
        // check permissions *before* checking existence
        consumerService.exists(scopeId, userId, id);

        Registration reg = consumerService.update(scopeId, userId, id, propertiesMap, tags);

        // include private fields on create view
        return ConsumerDTO.fromRegistration(reg, true);

    }

    @DeleteMapping(value = "/api/c/{scope}/consumers/{id}", produces = "application/json")
    @ApiOperation(value = "Delete a specific consumer by id")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @ResponseBody
    public ConsumerDTO delete(
            @ApiParam(value = "Scope", defaultValue = "default") @PathVariable("scope") Optional<String> scope,
            @ApiParam(value = "Consumer id", required = true) @PathVariable("id") long id,
            HttpServletRequest request, HttpServletResponse response)
            throws NoSuchConsumerException {

        String scopeId = scope.orElse(defaultScope);
        String userId = ControllerUtil.getUserId(request);

        _log.debug("delete consumer " + String.valueOf(id) + " by " + userId + " for scope " + scopeId);

        // call exists to trigger 404, otherwise delete() will
        // check permissions *before* checking existence
        consumerService.exists(scopeId, userId, id);

        // fetch resource to provide as result on success
        Consumer consumer = consumerService.lookup(scopeId, userId, id);

        consumerService.delete(scopeId, userId, id);

        return ConsumerDTO.fromConsumer(consumer, false);
    }

    /*
     * List w/scope
     */

    @GetMapping(value = "/api/c/{scope}/consumers", produces = "application/json")
    @ApiOperation(value = "List consumers with filters")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @ResponseBody
    public List<ConsumerDTO> list(
            @ApiParam(value = "Scope", defaultValue = "default") @PathVariable("scope") Optional<String> scope,
            @ApiParam(value = "Consumer type") @RequestParam("type") Optional<String> type,
            @ApiParam(value = "Consumer id") @RequestParam("consumer") Optional<String> consumer,
            @ApiParam(value = "Consumer owner") @RequestParam("user") Optional<String> ownerId,
            HttpServletRequest request, HttpServletResponse response,
            Pageable pageable) {

        String scopeId = scope.orElse(defaultScope);
        String userId = ControllerUtil.getUserId(request);

        _log.debug("list consumers by " + userId + " for scope " + scopeId);

        long total = 0;
        List<Registration> registrations = new ArrayList<>();

        // TODO refactor - ugly
        if (type.isPresent()) {
            total = consumerService.countByType(scopeId, userId, type.get());
            registrations = consumerService.listByType(scopeId, userId, type.get());
        } else if (consumer.isPresent()) {
            total = consumerService.countByConsumer(scopeId, userId, consumer.get());
            registrations = consumerService.listByConsumer(scopeId, userId, consumer.get());
        } else if (ownerId.isPresent()) {
            total = consumerService.countByUserId(scopeId, userId, ownerId.get());
            registrations = consumerService.listByUserId(scopeId, userId, ownerId.get());
        } else {
            total = consumerService.count(scopeId, userId);
            registrations = consumerService.list(scopeId, userId, pageable.getPageNumber(), pageable.getPageSize());
        }

        List<ConsumerDTO> results = registrations.stream().map(r -> ConsumerDTO.fromRegistration(r))
                .collect(Collectors.toList());
        // add total count as header
        response.setHeader("X-Total-Count", String.valueOf(total));

        return results;
    }

    /*
     * Resource
     */
    @GetMapping(value = "/api/consumers/{id}", produces = "application/json")
    @ApiOperation(value = "Fetch a specific consumer by id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token"),
            @ApiImplicitParam(name = "X-Scope", value = "Scope", required = false, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "default", defaultValue = "default")
    })
    @ResponseBody
    public ConsumerDTO get(
            @ApiParam(value = "Consumer id", required = true) @PathVariable("id") long id,
            HttpServletRequest request, HttpServletResponse response)
            throws NoSuchConsumerException {

        Optional<String> scopeId = Optional.ofNullable(ControllerUtil.getScopeId(request));
        return get(scopeId, id, request, response);
    }

    @PostMapping(value = "/api/consumers", produces = "application/json")
    @ApiOperation(value = "Add a new consumer")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token"),
            @ApiImplicitParam(name = "X-Scope", value = "Scope", required = false, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "default", defaultValue = "default")
    })
    @ResponseBody
    public ConsumerDTO add(
            @ApiParam(value = "Consumer json", required = true) @RequestBody ConsumerDTO consumer,
            HttpServletRequest request, HttpServletResponse response)
            throws NoSuchConsumerException, ConsumerException {
        Optional<String> scopeId = Optional.ofNullable(ControllerUtil.getScopeId(request));
        return add(scopeId, consumer, request, response);

    }

    @PutMapping(value = "/api/consumers/{id}", produces = "application/json")
    @ApiOperation(value = "Update a consumer")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token"),
            @ApiImplicitParam(name = "X-Scope", value = "Scope", required = false, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "default", defaultValue = "default")
    })
    @ResponseBody
    public ConsumerDTO update(
            @ApiParam(value = "Consumer id", required = true) @PathVariable("id") long id,
            @ApiParam(value = "Consumer json", required = true) @RequestBody ConsumerDTO consumer,
            HttpServletRequest request, HttpServletResponse response)
            throws NoSuchConsumerException, ConsumerException {
        Optional<String> scopeId = Optional.ofNullable(ControllerUtil.getScopeId(request));
        return update(scopeId, id, consumer, request, response);

    }

    @DeleteMapping(value = "/api/consumers/{id}", produces = "application/json")
    @ApiOperation(value = "Delete a specific consumer by id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token"),
            @ApiImplicitParam(name = "X-Scope", value = "Scope", required = false, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "default", defaultValue = "default")
    })
    @ResponseBody
    public ConsumerDTO delete(
            @ApiParam(value = "Consumer id", required = true) @PathVariable("id") long id,
            HttpServletRequest request, HttpServletResponse response)
            throws NoSuchConsumerException {

        Optional<String> scopeId = Optional.ofNullable(ControllerUtil.getScopeId(request));
        return delete(scopeId, id, request, response);

    }

    /*
     * List
     */

    @GetMapping(value = "/api/consumers", produces = "application/json")
    @ApiOperation(value = "List consumers with filters")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token"),
            @ApiImplicitParam(name = "X-Scope", value = "Scope", required = false, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "default", defaultValue = "default")
    })
    @ResponseBody
    public List<ConsumerDTO> list(
            @ApiParam(value = "Consumer type") @RequestParam("type") Optional<String> type,
            @ApiParam(value = "Consumer id") @RequestParam("consumer") Optional<String> consumer,
            @ApiParam(value = "Consumer owner") @RequestParam("user") Optional<String> ownerId,
            HttpServletRequest request, HttpServletResponse response,
            Pageable pageable) {

        Optional<String> scopeId = Optional.ofNullable(ControllerUtil.getScopeId(request));
        return list(scopeId, type, consumer, ownerId, request, response, pageable);
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

    @ExceptionHandler(ConsumerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String consumerError(ConsumerException ex) {
        return ex.getMessage();
    }

    /*
     * Helper
     */

}
