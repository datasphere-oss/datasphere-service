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

package com.datasphere.engine.manager.resource.consumer.dss;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datasphere.engine.manager.resource.SystemKeys;
import com.datasphere.engine.manager.resource.common.ConsumerException;
import com.datasphere.engine.manager.resource.model.Consumer;
import com.datasphere.engine.manager.resource.model.Registration;
import com.datasphere.engine.manager.resource.model.Resource;
import com.datasphere.engine.manager.resource.provider.cockroachdb.CockroachDBProvider;
import com.datasphere.engine.manager.resource.provider.mysql.MySqlProvider;
import com.datasphere.engine.manager.resource.provider.postgres.PostgresSqlProvider;
import com.datasphere.engine.manager.resource.util.SqlUtil;

public class DSSSqlConsumer extends Consumer {

    private final static Logger _log = LoggerFactory.getLogger(DSSSqlConsumer.class);

    public static final String TYPE = SystemKeys.TYPE_SQL;
    public static final String ID = "dsssql";

    // DSS connection
    private String endpoint;
    private String username;
    private String password;
    private String tenant;

    private int STATUS;

    private Registration registration;

    // filters
    private String scopeId;
    private List<String> tags;

    private DSSRestClient _client;

    public DSSSqlConsumer() {
        endpoint = "";
        username = "";
        password = "";
        tenant = "carbon.super"; // default tenant
        scopeId = "";
        tags = new ArrayList<>();
    }

    public DSSSqlConsumer(Map<String, Serializable> properties) {
        this();
        _properties = properties;
    }

    public DSSSqlConsumer(Registration reg) {
        this();
        registration = reg;
        _properties = reg.getPropertiesMap();
        scopeId = reg.getScopeId();
        tags = reg.getTags();
    }

    // DSS additional properties
    private Map<String, Serializable> _properties;

    public Map<String, Serializable> getProperties() {
        return _properties;
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getUrl() {
        // build access url from endpoint
        return endpoint;
    }

    @Override
    public Registration getRegistration() {
        return registration;
    }

    /*
     * Init method - POST constructor since spring injects properties *after
     * creation*
     */
    @PostConstruct
    public void init() {
        _log.debug("init called");

        STATUS = SystemKeys.STATUS_UNKNOWN;

        if (_properties != null) {
            if (_properties.containsKey("endpoint")
                    && _properties.containsKey("username")
                    && _properties.containsKey("password")) {

                endpoint = _properties.get("endpoint").toString();
                username = _properties.get("username").toString();
                password = _properties.get("password").toString();

            }

            if (_properties.containsKey("tenant")) {
                tenant = _properties.get("tenant").toString();
            }
        }

        if (!endpoint.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
            _client = new DSSRestClient(endpoint, tenant, username, password);

            STATUS = SystemKeys.STATUS_READY;
        }
        _log.debug("init status is " + String.valueOf(STATUS));

    }

    @Override
    public int getStatus() {
        return STATUS;
    }

    @Override
    public void addResource(String scopeId, String userId, Resource resource) throws ConsumerException {
        if (checkScope(resource.getScopeId()) && checkTags(resource.getTags())) {
            _log.debug("add resource " + resource.toString());
            try {
                // fetch type from supported
                String type = getType(resource.getProvider());
                if (!type.isEmpty()) {
                    // supported
                    String uri = resource.getUri();
                    String host = SqlUtil.getHost(uri);
                    int port = SqlUtil.getPort(uri);
                    String uname = SqlUtil.getUsername(uri);
                    String passw = SqlUtil.getPassword(uri);
                    String database = SqlUtil.getDatabase(uri);
                    String name = type.toLowerCase() + "_" + database;

                    name = _client.addSource(type, name, host, port, database, uname, passw);
                    _log.debug("created source " + name);
                }
            } catch (DSSException e) {
                _log.error("dss error " + e.getMessage());
                throw new ConsumerException(e.getMessage());
            }
        }
    }

    @Override
    public void updateResource(String scopeId, String userId, Resource resource) throws ConsumerException {
        if (checkScope(resource.getScopeId())) {
            _log.debug("update resource " + resource.toString());
            try {
                // fetch type from supported
                String type = getType(resource.getProvider());
                if (!type.isEmpty()) {
                    // supported
                    String uri = resource.getUri();
                    String host = SqlUtil.getHost(uri);
                    int port = SqlUtil.getPort(uri);
                    String uname = SqlUtil.getUsername(uri);
                    String passw = SqlUtil.getPassword(uri);
                    String database = SqlUtil.getDatabase(uri);
                    String name = type.toLowerCase() + "_" + database;

                    if (checkTags(resource.getTags())) {
                        // matches, update or create via client
                        if (_client.hasSource(name)) {
                            // exists, update
                            name = _client.updateSource(type, name, host, port, database, uname, passw);
                            _log.debug("updated source " + name);
                        } else {
                            // create as new
                            name = _client.addSource(type, name, host, port, database, uname, passw);
                            _log.debug("created source " + name);
                        }
                    } else {
                        if (_client.hasSource(name)) {
                            // remove previously existing resource
                            _client.deleteSource(name);
                            _log.debug("deleted source " + name);
                        }
                    }
                }
            } catch (DSSException e) {
                _log.error("dss error " + e.getMessage());
                throw new ConsumerException(e.getMessage());
            }
        }
    }

    @Override
    public void deleteResource(String scopeId, String userId, Resource resource) throws ConsumerException {
        if (checkScope(resource.getScopeId()) && checkTags(resource.getTags())) {
            _log.debug("delete resource " + resource.toString());
            try {
                // fetch type from supported
                String type = getType(resource.getProvider());
                if (!type.isEmpty()) {
                    // supported
                    String uri = resource.getUri();
                    String database = SqlUtil.getDatabase(uri);
                    String name = type.toLowerCase() + "_" + database;

                    _client.deleteSource(name);
                    _log.debug("deleted if existing source " + name);

                }
            } catch (DSSException e) {
                _log.error("dss error " + e.getMessage());
                throw new ConsumerException(e.getMessage());
            }
        }
    }

    @Override
    public void checkResource(String scopeId, String userId, Resource resource) throws ConsumerException {
        if (checkScope(resource.getScopeId()) && checkTags(resource.getTags())) {
            _log.debug("check resource " + resource.toString());
            try {
                // fetch type from supported
                String type = getType(resource.getProvider());
                if (!type.isEmpty()) {
                    // supported
                    String uri = resource.getUri();
                    String database = SqlUtil.getDatabase(uri);
                    String name = type.toLowerCase() + "_" + database;

                    boolean exists = _client.hasSource(name);

                    if (exists) {
                        _log.debug("check ok source " + name);
                    }
                }
            } catch (DSSException e) {
                _log.error("dss error " + e.getMessage());
                throw new ConsumerException(e.getMessage());
            }
        }
    }

    /*
     * Helpers
     */
    public String getType(String provider) {
        String type = "";
        switch (provider) {
        case PostgresSqlProvider.ID:
            type = "POSTGRES";
            break;
        case CockroachDBProvider.ID:
            // cockroachDB supports postgres line protocol
            type = "POSTGRES";
            break;
        case MySqlProvider.ID:
            type = "MYSQL";
            break;
        }
        return type;
    }

    public boolean checkTags(List<String> tags) {
        boolean ret = true;
        if (!this.tags.isEmpty() || !tags.isEmpty()) {
            ret = false;
            // look for at least one match
            for (String t : tags) {
                if (this.tags.contains(t)) {
                    ret = true;
                    break;
                }
            }
        }

        return ret;
    }

    public boolean checkScope(String scope) {
        if (!this.scopeId.isEmpty()) {
            return scopeId.equals(scope);
        } else {
            // if global scope
            return true;
        }
    }

}
