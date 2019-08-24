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

package com.datasphere.engine.manager.resource.consumer.metabase;

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

public class MetabaseConsumer extends Consumer {

    private final static Logger _log = LoggerFactory.getLogger(MetabaseConsumer.class);

    public static final String TYPE = SystemKeys.TYPE_SQL;
    public static final String ID = "metabase";

    // metabase connection
    private String endpoint;
    private String username;
    private String password;

    private int STATUS;

    private Registration registration;

    // filters
    private String scopeId;
    private List<String> tags;

    private MetabaseClient _client;

    public MetabaseConsumer() {
        endpoint = "";
        username = "";
        password = "";
        scopeId = "";
        tags = new ArrayList<>();
    }

    public MetabaseConsumer(Map<String, Serializable> properties) {
        this();
        _properties = properties;
    }

    public MetabaseConsumer(Registration reg) {
        this();
        registration = reg;
        _properties = reg.getPropertiesMap();
        scopeId = reg.getScopeId();
        tags = reg.getTags();
    }

    // metabase properties
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
            _client = new MetabaseClient(endpoint, username, password);
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
                // fetch provider engine from supported
                String engine = getEngine(resource.getProvider());
                if (!engine.isEmpty()) {
                    // supported
                    String uri = resource.getUri();
                    String host = SqlUtil.getHost(uri);
                    int port = SqlUtil.getPort(uri);
                    // TODO: ssl support
                    boolean ssl = false;

                    String uname = SqlUtil.getUsername(uri);
                    String passw = SqlUtil.getPassword(uri);
                    String database = SqlUtil.getDatabase(uri);
                    String name = engine + "_" + database;

                    long id = _client.addDatabase(engine, name, host, port, ssl, database, uname, passw);

                    _log.debug("created database " + String.valueOf(id));
                }
            } catch (MetabaseException e) {
                _log.error("metabase error " + e.getMessage());
                throw new ConsumerException(e.getMessage());
            }
        }
    }

    @Override
    public void updateResource(String scopeId, String userId, Resource resource) throws ConsumerException {
        if (checkScope(resource.getScopeId())) {
            _log.debug("update resource " + resource.toString());
            try {
                // fetch provider engine from supported
                String engine = getEngine(resource.getProvider());
                if (!engine.isEmpty()) {
                    // supported, fetch details
                    String uri = resource.getUri();
                    String host = SqlUtil.getHost(uri);
                    int port = SqlUtil.getPort(uri);
                    // TODO: ssl support
                    boolean ssl = false;

                    String uname = SqlUtil.getUsername(uri);
                    String passw = SqlUtil.getPassword(uri);
                    String database = SqlUtil.getDatabase(uri);
                    String name = engine + "_" + database;

                    if (checkTags(resource.getTags())) {
                        // matches, update or create via client
                        // will add as new if not existing
                        long id = _client.updateDatabase(engine, name, host, port, ssl, database, uname, passw);
                        _log.debug("updated database " + String.valueOf(id));
                    } else {
                        // remove previously existing resource
                        // will do nothing if not exists
                        _client.deleteDatabase(engine, name);
                        _log.debug("deleted");
                    }
                }
            } catch (MetabaseException e) {
                _log.error("metabase error " + e.getMessage());
                throw new ConsumerException(e.getMessage());
            }

        }
    }

    @Override
    public void deleteResource(String scopeId, String userId, Resource resource) throws ConsumerException {
        if (checkScope(resource.getScopeId()) && checkTags(resource.getTags())) {
            _log.debug("delete resource " + resource.toString());
            try {
                // fetch provider engine from supported
                String engine = getEngine(resource.getProvider());
                if (!engine.isEmpty()) {
                    // supported
                    String uri = resource.getUri();
                    String database = SqlUtil.getDatabase(uri);
                    String name = engine + "_" + database;

                    _client.deleteDatabase(engine, name);
                    _log.debug("deleted");

                }
            } catch (MetabaseException e) {
                _log.error("metabase error " + e.getMessage());
                throw new ConsumerException(e.getMessage());
            }
        }
    }

    @Override
    public void checkResource(String scopeId, String userId, Resource resource) throws ConsumerException {
        if (checkScope(resource.getScopeId()) && checkTags(resource.getTags())) {
            _log.debug("check resource " + resource.toString());
            try {
                // fetch provider engine from supported
                String engine = getEngine(resource.getProvider());
                if (!engine.isEmpty()) {
                    // supported
                    String uri = resource.getUri();
                    String database = SqlUtil.getDatabase(uri);
                    String name = engine + "_" + database;

                    boolean exists = _client.hasDatabase(engine, name);

                    if (exists) {
                        _log.debug("check ok");
                    }
                }
            } catch (MetabaseException e) {
                _log.error("metabase error " + e.getMessage());
                throw new ConsumerException(e.getMessage());
            }
        }
    }

    /*
     * Helpers
     */

    public String getEngine(String provider) {
        String driver = "";
        switch (provider) {
        case PostgresSqlProvider.ID:
            driver = "postgres";
            break;
        // TODO test, also check "insecure" without password
//        case CockroachDBProvider.ID:
//            // cockroachDB supports postgres line protocol
//            driver = "postgres";
//            break;
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
