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

package com.datasphere.datalayer.resource.manager.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import com.datasphere.datalayer.resource.manager.SystemKeys;
import com.datasphere.datalayer.resource.manager.common.DuplicateNameException;
import com.datasphere.datalayer.resource.manager.common.InvalidNameException;
import com.datasphere.datalayer.resource.manager.common.NoSuchProviderException;
import com.datasphere.datalayer.resource.manager.common.NoSuchResourceException;
import com.datasphere.datalayer.resource.manager.common.ResourceProviderException;
import com.datasphere.datalayer.resource.manager.model.Resource;

@Component
public class ResourceService {

    private final static Logger _log = LoggerFactory.getLogger(ResourceService.class);

    @Autowired
    private ResourceLocalService resourceLocalService;

    /*
     * Data
     */
    @PreAuthorize("hasPermission(#scopeId, '" + SystemKeys.SCOPE + "', '"
            + SystemKeys.PERMISSION_RESOURCE_CREATE + "')")
    public Resource create(String scopeId, String userId,
            String type, String providerId, String name,
            Map<String, Serializable> properties, List<String> tags)
            throws NoSuchProviderException, ResourceProviderException, InvalidNameException, DuplicateNameException {
        _log.info("create resource with " + String.valueOf(providerId) + " by user " + userId);

        // call local service
        return resourceLocalService.create(scopeId, userId, type, providerId, name, properties, tags);

    }

    @PreAuthorize("hasPermission(#scopeId, '" + SystemKeys.SCOPE + "', '"
            + SystemKeys.PERMISSION_RESOURCE_CREATE + "')")
    public Resource add(String scopeId, String userId, String type, String providerId,
            String uri,
            Map<String, Serializable> properties, List<String> tags)
            throws NoSuchProviderException, ResourceProviderException {
        _log.info("add resource with " + String.valueOf(providerId) + " by user " + userId);

        // call local service
        return resourceLocalService.add(scopeId, userId, type, providerId, uri, properties, tags);

    }

    @PreAuthorize("hasPermission(#id, '" + SystemKeys.ENTITY_RESOURCE +
            "', '" + SystemKeys.PERMISSION_RESOURCE_UPDATE + "')")
    public Resource update(String scopeId, String userId, long id,
            Map<String, Serializable> properties, List<String> tags)
            throws NoSuchResourceException, NoSuchProviderException, ResourceProviderException {
        _log.info("update resource " + String.valueOf(id) + " by user " + userId);

        // call local service
        return resourceLocalService.update(id, properties, tags);

    }

    @PreAuthorize("hasPermission(#id, '" + SystemKeys.ENTITY_RESOURCE +
            "', '" + SystemKeys.PERMISSION_RESOURCE_DELETE + "')")
    public void delete(String scopeId, String userId, long id)
            throws NoSuchResourceException, NoSuchProviderException, ResourceProviderException {
        _log.info("delete resource " + String.valueOf(id) + " by user " + userId);

        // call local service
        resourceLocalService.delete(id);
    }

    @PreAuthorize("hasPermission(#id, '" + SystemKeys.ENTITY_RESOURCE +
            "', '" + SystemKeys.PERMISSION_RESOURCE_VIEW + "')")
    public Resource get(String scopeId, String userId, long id) throws NoSuchResourceException {
        _log.info("get resource " + String.valueOf(id) + " by user " + userId);

        // call local service
        return resourceLocalService.get(id);
    }

    @PreAuthorize("hasPermission(#scopeId, '" + SystemKeys.SCOPE +
            "', '" + SystemKeys.PERMISSION_RESOURCE_VIEW + "')")
    public boolean exists(String scopeId, String userId, long id) throws NoSuchResourceException {
        _log.info("exists resource " + String.valueOf(id) + " by user " + userId);

        // call local service
        boolean ret = resourceLocalService.exists(id);
        if (!ret) {
            throw new NoSuchResourceException();
        }

        return ret;
    }
    /*
     * Count
     */

    @PreAuthorize("hasPermission(#scopeId, '" + SystemKeys.SCOPE +
            "', '" + SystemKeys.PERMISSION_RESOURCE_VIEW + "')")
    public long count(String scopeId, String userId) {

        // call local service with scope
        return resourceLocalService.countByScopeId(scopeId);
    }

    @PreAuthorize("hasPermission(#scopeId, '" + SystemKeys.SCOPE
            + "', '" + SystemKeys.PERMISSION_RESOURCE_VIEW + "')")
    public long countByType(String scopeId, String userId, String type) {

        // call local service
        return resourceLocalService.countByTypeAndScopeId(type, scopeId);
    }

    @PreAuthorize("hasPermission(#scopeId, '" + SystemKeys.SCOPE
            + "', '" + SystemKeys.PERMISSION_RESOURCE_VIEW + "')")
    public long countByProvider(String scopeId, String userId, String provider) {

        // call local service
        return resourceLocalService.countByProviderAndScopeId(provider, scopeId);
    }

    @PreAuthorize("hasPermission(#scopeId, '"
            + SystemKeys.SCOPE + "', '" + SystemKeys.PERMISSION_RESOURCE_VIEW + "')")
    public long countByUserId(String scopeId, String userId, String ownerId) {

        // call local service
        return resourceLocalService.countByUserIdAndScopeId(scopeId, ownerId);
    }

    /*
     * List
     */

    @PreAuthorize("hasPermission(#scopeId, '" + SystemKeys.SCOPE + "', '" + SystemKeys.PERMISSION_RESOURCE_VIEW + "')")
    @PostFilter("hasPermission(filterObject, '" + SystemKeys.PERMISSION_RESOURCE_VIEW + "')")
    public List<Resource> list(String scopeId, String userId) {

        // call local service with scope
        // need to create new MUTABLE list for postFilter usage of collection.clear()
        // see DefaultMethodSecurityExpressionHandler.java
        return new ArrayList<>(resourceLocalService.listByScopeId(scopeId));
    }

    public List<Resource> list(String scopeId, String userId, int page, int pageSize) {

        // call local service
        return list(scopeId, userId, page, pageSize, "id", SystemKeys.ORDER_ASC);
    }

    @PreAuthorize("hasPermission(#scopeId, '" + SystemKeys.SCOPE + "', '" + SystemKeys.PERMISSION_RESOURCE_VIEW + "')")
    @PostFilter("hasPermission(filterObject, '" + SystemKeys.PERMISSION_RESOURCE_VIEW + "')")
    public List<Resource> list(String scopeId, String userId, int page, int pageSize, String orderBy, String order) {

        Sort sort = (order.equals(SystemKeys.ORDER_ASC) ? Sort.by(orderBy).ascending() : Sort.by(orderBy).descending());
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        // call local service
        // need to create new MUTABLE list for postFilter usage of collection.clear()
        // see DefaultMethodSecurityExpressionHandler.java
        return new ArrayList<>(resourceLocalService.listByScopeId(scopeId, pageable));
    }

    @PreAuthorize("hasPermission(#scopeId, '" + SystemKeys.SCOPE + "', '" + SystemKeys.PERMISSION_RESOURCE_VIEW + "')")
    @PostFilter("hasPermission(filterObject, '" + SystemKeys.PERMISSION_RESOURCE_VIEW + "')")
    public List<Resource> listByType(String scopeId, String userId, String type) {

        // call local service
        // need to create new MUTABLE list for postFilter usage of collection.clear()
        // see DefaultMethodSecurityExpressionHandler.java
        return new ArrayList<>(resourceLocalService.listByTypeAndScopeId(type, scopeId));
    }

    @PreAuthorize("hasPermission(#scopeId, '" + SystemKeys.SCOPE + "', '" + SystemKeys.PERMISSION_RESOURCE_VIEW + "')")
    @PostFilter("hasPermission(filterObject, '" + SystemKeys.PERMISSION_RESOURCE_VIEW + "')")
    public List<Resource> listByProvider(String scopeId, String userId, String provider) {

        // call local service
        // need to create new MUTABLE list for postFilter usage of collection.clear()
        // see DefaultMethodSecurityExpressionHandler.java
        return new ArrayList<>(resourceLocalService.listByProviderAndScopeId(provider, scopeId));
    }

    @PreAuthorize("hasPermission(#scopeId, '" + SystemKeys.SCOPE + "', '" + SystemKeys.PERMISSION_RESOURCE_VIEW + "')")
    @PostFilter("hasPermission(filterObject, '" + SystemKeys.PERMISSION_RESOURCE_VIEW + "')")
    public List<Resource> listByUserId(String scopeId, String userId, String ownerId) {

        // call local service
        // need to create new MUTABLE list for postFilter usage of collection.clear()
        // see DefaultMethodSecurityExpressionHandler.java
        return new ArrayList<>(resourceLocalService.listByUserIdAndScopeId(ownerId, scopeId));
    }

    /*
     * Check
     */
    @PreAuthorize("hasPermission(#scopeId, '" + SystemKeys.SCOPE + "', '" + SystemKeys.PERMISSION_RESOURCE_VIEW + "')")
    public void check(String scopeId, String userId, long id)
            throws NoSuchResourceException, NoSuchProviderException, ResourceProviderException {
        _log.info("check resource " + String.valueOf(id) + " by user " + userId);

        // call local service
        resourceLocalService.check(id);

    }

}
