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

package com.datasphere.datalayer.resource.manager.consumer.sqlpad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datasphere.datalayer.resource.manager.SystemKeys;
import com.datasphere.datalayer.resource.manager.common.ConsumerException;
import com.datasphere.datalayer.resource.manager.model.Consumer;
import com.datasphere.datalayer.resource.manager.model.Registration;
import com.datasphere.datalayer.resource.manager.model.Resource;
import com.datasphere.datalayer.resource.manager.provider.cockroachdb.CockroachDBProvider;
import com.datasphere.datalayer.resource.manager.provider.mysql.MySqlProvider;
import com.datasphere.datalayer.resource.manager.provider.postgres.PostgresSqlProvider;
import com.datasphere.datalayer.resource.manager.util.SqlUtil;

public class SqlpadConsumer extends Consumer {

    private final static Logger _log = LoggerFactory.getLogger(SqlpadConsumer.class);

    public static final String TYPE = SystemKeys.TYPE_SQL;
    public static final String ID = "sqlpad";

    // sqlpad connection
    private String endpoint;
    private String username;
    private String password;

    private int STATUS;

    private Registration registration;

    // filters
    private String scopeId;
    private List<String> tags;

    private SqlpadClient _client;

    public SqlpadConsumer() {
        endpoint = "";
        username = "";
        password = "";
        scopeId = "";
        tags = new ArrayList<>();
    }

    public SqlpadConsumer(Map<String, Serializable> properties) {
        this();
        _properties = properties;
    }

    public SqlpadConsumer(Registration reg) {
        this();
        registration = reg;
        _properties = reg.getPropertiesMap();
        scopeId = reg.getScopeId();
        tags = reg.getTags();
    }

    // sqlpad properties
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
        }

        if (!endpoint.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
            _client = new SqlpadClient(endpoint, username, password);
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
                // fetch provider driver from supported
                String driver = getDriver(resource.getProvider());
                if (!driver.isEmpty()) {
                    // supported
                    String uri = resource.getUri();
                    String host = SqlUtil.getHost(uri);
                    int port = SqlUtil.getPort(uri);
                    String uname = SqlUtil.getUsername(uri);
                    String passw = SqlUtil.getPassword(uri);
                    String database = SqlUtil.getDatabase(uri);
                    String name = driver + "_" + database;

                    String padId = _client.addConnection(driver, name, host, port, database, uname, passw);

                    _log.debug("created pad " + padId);
                }
            } catch (SqlpadException e) {
                _log.error("sqlpad error " + e.getMessage());
                throw new ConsumerException(e.getMessage());
            }
        }
    }

    @Override
    public void updateResource(String scopeId, String userId, Resource resource) throws ConsumerException {
        if (checkScope(resource.getScopeId())) {
            _log.debug("update resource " + resource.toString());
            try {
                // fetch provider driver from supported
                String driver = getDriver(resource.getProvider());
                if (!driver.isEmpty()) {
                    // supported, fetch details
                    String uri = resource.getUri();
                    String host = SqlUtil.getHost(uri);
                    int port = SqlUtil.getPort(uri);
                    String uname = SqlUtil.getUsername(uri);
                    String passw = SqlUtil.getPassword(uri);
                    String database = SqlUtil.getDatabase(uri);
                    String name = driver + "_" + database;

                    if (checkTags(resource.getTags())) {
                        // matches, update or create via client
                        // will add as new if not existing
                        String padId = _client.updateConnection(driver, name, host, port, database, uname, passw);
                        _log.debug("updated pad " + padId);
                    } else {
                        // remove previously existing resource
                        // will do nothing if not exists
                        String padId = _client.deleteConnection(driver, name);
                        _log.debug("delete pad " + padId);
                    }
                }
            } catch (SqlpadException e) {
                _log.error("sqlpad error " + e.getMessage());
                throw new ConsumerException(e.getMessage());
            }

        }
    }

    @Override
    public void deleteResource(String scopeId, String userId, Resource resource) throws ConsumerException {
        if (checkScope(resource.getScopeId()) && checkTags(resource.getTags())) {
            _log.debug("delete resource " + resource.toString());
            try {
                // fetch provider driver from supported
                String driver = getDriver(resource.getProvider());
                if (!driver.isEmpty()) {
                    // supported
                    String uri = resource.getUri();
                    String database = SqlUtil.getDatabase(uri);
                    String name = driver + "_" + database;

                    String padId = _client.deleteConnection(driver, name);
                    if (!padId.isEmpty()) {
                        _log.debug("deleted pad " + padId);
                    }
                }
            } catch (SqlpadException e) {
                _log.error("sqlpad error " + e.getMessage());
                throw new ConsumerException(e.getMessage());
            }
        }
    }

    @Override
    public void checkResource(String scopeId, String userId, Resource resource) throws ConsumerException {
        if (checkScope(resource.getScopeId()) && checkTags(resource.getTags())) {
            _log.debug("check resource " + resource.toString());
            try {
                // fetch provider driver from supported
                String driver = getDriver(resource.getProvider());
                if (!driver.isEmpty()) {
                    // supported
                    String uri = resource.getUri();
                    String database = SqlUtil.getDatabase(uri);
                    String name = driver + "_" + database;

                    String padId = _client.hasConnection(driver, name);

                    if (!padId.isEmpty()) {
                        _log.debug("check ok pad " + padId);
                    }
                }
            } catch (SqlpadException e) {
                _log.error("sqlpad error " + e.getMessage());
                throw new ConsumerException(e.getMessage());
            }
        }
    }

    /*
     * Helpers
     */

    public String getDriver(String provider) {
        String driver = "";
        switch (provider) {
        case PostgresSqlProvider.ID:
            driver = "postgres";
            break;
        case CockroachDBProvider.ID:
            // cockroachDB supports postgres line protocol
            driver = "postgres";
            break;
        case MySqlProvider.ID:
            driver = "mysql";
            break;
        }
        return driver;
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
