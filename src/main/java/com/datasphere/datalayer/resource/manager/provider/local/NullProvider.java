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

package com.datasphere.datalayer.resource.manager.provider.local;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.datasphere.datalayer.resource.manager.SystemKeys;
import com.datasphere.datalayer.resource.manager.common.ResourceProviderException;
import com.datasphere.datalayer.resource.manager.model.Resource;
import com.datasphere.datalayer.resource.manager.model.ResourceProvider;
import com.datasphere.datalayer.resource.manager.util.SqlUtil;

@Component
public class NullProvider extends ResourceProvider {

    private final static Logger _log = LoggerFactory.getLogger(NullProvider.class);

    public static final String TYPE = SystemKeys.TYPE_SQL;
    public static final String ID = "null";

    private int STATUS;

    @Value("${providers.null.enable}")
    private boolean enabled;

    @Value("${providers.null.properties}")
    private List<String> properties;

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public Set<String> listProperties() {
        return new HashSet<String>(properties);
    }

    /*
     * Init method - POST constructor since spring injects properties *after
     * creation*
     */
    @PostConstruct
    public void init() {
        _log.info("enabled " + String.valueOf(enabled));

        if (enabled) {
            STATUS = SystemKeys.STATUS_READY;
        } else {
            STATUS = SystemKeys.STATUS_DISABLED;
        }

        _log.info("init status " + String.valueOf(STATUS));
    }

    @Override
    public int getStatus() {
        return STATUS;
    }

    @Override
    public Resource createResource(String scopeId, String userId, String name, Map<String, Serializable> properties)
            throws ResourceProviderException {
        Resource res = new Resource();
        res.setType(TYPE);
        res.setProvider(ID);
        res.setPropertiesMap(properties);

        if (name.isEmpty()) {
            name = "nulldb";
        }

        // generate uri
        String uri = SqlUtil.encodeURI("null", "host:981", name, "USER", "PASS");

        // update res
        res.setName(name);
        res.setUri(uri);

        return res;
    }

    @Override
    public void updateResource(Resource resource) throws ResourceProviderException {
        // nothing to do

    }

    @Override
    public void deleteResource(Resource resource) throws ResourceProviderException {
        // nothing to do

    }

    @Override
    public void checkResource(Resource resource) throws ResourceProviderException {
        // nothing to do

    }

}
