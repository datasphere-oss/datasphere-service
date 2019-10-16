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

package com.datasphere.engine.manager.resource.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datasphere.engine.manager.resource.SystemKeys;
import com.datasphere.engine.manager.resource.common.NoSuchProviderException;
import com.datasphere.engine.manager.resource.model.ResourceProvider;

@Component
public class ProviderLocalService {
    private final static Logger _log = LoggerFactory.getLogger(ProviderLocalService.class);

    @Autowired
    private Map<String, ResourceProvider> _providers;

//    public Map<String, ResourceProvider> availableProviders() {
//        // return only active providers
//        return _providers.entrySet().stream()
//                .filter(entry -> (entry.getValue().getStatus() > -1))
//                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
//    }

    public Map<String, List<ResourceProvider>> listProviders() {
        Map<String, List<ResourceProvider>> map = new HashMap<>();

        // static init for all types
        map.put(SystemKeys.TYPE_SQL, new ArrayList<>());
        map.put(SystemKeys.TYPE_NOSQL, new ArrayList<>());
        map.put(SystemKeys.TYPE_FILE, new ArrayList<>());
        map.put(SystemKeys.TYPE_OBJECT, new ArrayList<>());
        map.put(SystemKeys.TYPE_ODBC, new ArrayList<>());

        for (ResourceProvider p : _providers.values()) {
            if (p.getStatus() > -1) {
                map.get(p.getType()).add(p);
            }
        }
        return map;
    }
    // 列出供应商
    public List<ResourceProvider> listProviders(String type) {
        // return only active providers
        return _providers.entrySet().stream()
                .map(entry -> entry.getValue())
                .filter(entry -> (entry.getStatus() == SystemKeys.STATUS_READY && entry.getType().equals(type)))
                .collect(Collectors.toList());
    }
    // 列出类型
    public List<String> listTypes() {
        // return only non empty types
        Set<String> types = new HashSet<>();
        for (ResourceProvider p : _providers.values()) {
            if (p.getStatus() == SystemKeys.STATUS_READY) {
                types.add(p.getType());
            }
        }
        return new ArrayList<>(types);
    }
    // 获得提供商
    public ResourceProvider getProvider(String id) throws NoSuchProviderException {

        // fetch
        ResourceProvider provider = fetchProvider(id);

        // check if ready
        if (provider.getStatus() != SystemKeys.STATUS_READY) {
            _log.error("provider for " + id + " is not available");

            throw new NoSuchProviderException();
        }

        return provider;
    }
    // 查询提供商
    public ResourceProvider fetchProvider(String id) throws NoSuchProviderException {

        // check if id ends with "Provider"
        // spring registers beans with "className" as key
        // code expects provider classes to end with *Provider.java
        if (!id.endsWith("Provider")) {
            id = id.concat("Provider");
        }

//        if (!_providers.containsKey(id)) {
//            _log.error("no provider for " + id);
//
//            throw new NoSuchProviderException();
//        }

        ResourceProvider provider = null;
        if (_providers.containsKey(id)) {
            provider = _providers.get(id);
        } else {
            // iterate to match
            for (String p : _providers.keySet()) {
                if (p.compareToIgnoreCase(id) == 0) {
                    provider = _providers.get(p);
                    break;
                }
            }
        }

        if (provider == null) {
            _log.error("no provider for " + id);
            throw new NoSuchProviderException();
        }

        // check if enabled
        if (provider.getStatus() == SystemKeys.STATUS_DISABLED) {
            _log.error("provider for " + id + " is not available");

            throw new NoSuchProviderException();
        }

        return provider;
    }

}
