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

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datasphere.datalayer.resource.manager.common.NoSuchProviderException;
import com.datasphere.datalayer.resource.manager.model.ResourceProvider;

@Component
public class ProviderService {
    private final static Logger _log = LoggerFactory.getLogger(ProviderService.class);

    @Autowired
    private ProviderLocalService providerService;

    public Map<String, List<ResourceProvider>> list(String scopeId, String userId) {
        // TODO check auth
        //
        // call local service
        return providerService.listProviders();
    }

    public List<ResourceProvider> list(String scopeId, String userId, String type) {
        // TODO check auth
        //
        // call local service
        return providerService.listProviders(type);
    }

    public List<String> listTypes(String scopeId, String userId) {
        // TODO check auth
        //
        // call local service
        return providerService.listTypes();
    }

    public ResourceProvider get(String scopeId, String userId, String id) throws NoSuchProviderException {
        // TODO check auth
        //
        // call local service
        return providerService.getProvider(id);
    }
}
