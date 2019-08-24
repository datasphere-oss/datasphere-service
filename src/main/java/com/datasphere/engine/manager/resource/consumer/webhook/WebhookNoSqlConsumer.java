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

package com.datasphere.engine.manager.resource.consumer.webhook;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.datasphere.engine.manager.resource.SystemKeys;
import com.datasphere.engine.manager.resource.common.ConsumerException;
import com.datasphere.engine.manager.resource.model.Consumer;
import com.datasphere.engine.manager.resource.model.Registration;
import com.datasphere.engine.manager.resource.model.Resource;
import com.datasphere.engine.manager.resource.util.SqlUtil;

public class WebhookNoSqlConsumer extends Consumer {

    private final static Logger _log = LoggerFactory.getLogger(WebhookNoSqlConsumer.class);

    public static final String TYPE = SystemKeys.TYPE_NOSQL;
    public static final String ID = "webhooknosql";

    @Value("${consumers.webhook.credentials}")
    private boolean sendCredentials;

    // endpoint connection
    private String endpoint;

    // basic authentication supported
    private String username;
    private String password;

    // token authentication
    private String token;

    // payload signature
    private String secret;

    private Registration registration;
    private int STATUS;

    // filters
    private String scopeId;
    private List<String> tags;

    private WebhookClient _client;

    public WebhookNoSqlConsumer() {
        endpoint = "";
        username = "";
        password = "";
        token = "";
        secret = "";

        scopeId = "";
        tags = new ArrayList<>();
    }

    public WebhookNoSqlConsumer(Map<String, Serializable> properties) {
        this();
        _properties = properties;
    }

    public WebhookNoSqlConsumer(Registration reg) {
        this();
        registration = reg;
        _properties = reg.getPropertiesMap();
        scopeId = reg.getScopeId();
        tags = reg.getTags();
    }

    // web hook properties
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
        // no access url
        return "";
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
            if (_properties.containsKey("endpoint")) {
                endpoint = _properties.get("endpoint").toString();
            }
            if (_properties.containsKey("username")) {
                username = _properties.get("username").toString();
            }
            if (_properties.containsKey("password")) {
                password = _properties.get("password").toString();
            }
            if (_properties.containsKey("token")) {
                token = _properties.get("token").toString();
            }
            if (_properties.containsKey("secret")) {
                secret = _properties.get("secret").toString();
            }
        }

        if (!endpoint.isEmpty()) {
            _client = new WebhookClient(endpoint);

            // add auth if provided, token > basic
            if (!token.isEmpty()) {
                _client.setAuthToken(token);
            } else if (!username.isEmpty() && !password.isEmpty()) {
                _client.setAuthBasic(username, password);
            }

            // add signature
            if (!secret.isEmpty()) {
                _client.setSignature(secret);
            }

            STATUS = SystemKeys.STATUS_READY;
        } else {
            // no endpoint
            STATUS = SystemKeys.STATUS_ERROR;
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
                // extract properties
                String uri = resource.getUri();
                String host = SqlUtil.getHost(uri);
                int port = SqlUtil.getPort(uri);

                String database = SqlUtil.getDatabase(uri);

                String uname = "";
                String passw = "";

                if (sendCredentials) {
                    uname = SqlUtil.getUsername(uri);
                    passw = SqlUtil.getPassword(uri);
                }

                // call endpoint via client
                _client.call("add", resource.getScopeId(), resource.getId(), TYPE, host, port, database, uname, passw);

                _log.debug("resource added via endpoint " + endpoint);

            } catch (WebhookException e) {
                _log.error("webhook error " + e.getMessage());
                throw new ConsumerException(e.getMessage());
            }
        }

    }

    @Override
    public void updateResource(String scopeId, String userId, Resource resource) throws ConsumerException {
        if (checkScope(resource.getScopeId())) {
            _log.debug("update resource " + resource.toString());
            try {

                // extract properties
                String uri = resource.getUri();
                String host = SqlUtil.getHost(uri);
                int port = SqlUtil.getPort(uri);

                String database = SqlUtil.getDatabase(uri);

                String uname = "";
                String passw = "";

                if (sendCredentials) {
                    uname = SqlUtil.getUsername(uri);
                    passw = SqlUtil.getPassword(uri);
                }

                if (checkTags(resource.getTags())) {
                    // call endpoint via client
                    _client.call("update", resource.getScopeId(), resource.getId(), TYPE, host, port, database, uname,
                            passw);
                    _log.debug("resource updated via endpoint " + endpoint);
                } else {
                    // call endpoint via client
                    _client.call("delete", resource.getScopeId(), resource.getId(), TYPE, host, port, database, uname,
                            passw);
                    _log.debug("resource deleted via endpoint " + endpoint);
                }

            } catch (WebhookException e) {
                _log.error("webhook error " + e.getMessage());
                throw new ConsumerException(e.getMessage());
            }

        }
    }

    @Override
    public void deleteResource(String scopeId, String userId, Resource resource) throws ConsumerException {
        if (checkScope(resource.getScopeId()) && checkTags(resource.getTags())) {
            _log.debug("delete resource " + resource.toString());
            try {
                // extract properties
                String uri = resource.getUri();
                String host = SqlUtil.getHost(uri);
                int port = SqlUtil.getPort(uri);

                String database = SqlUtil.getDatabase(uri);

                String uname = "";
                String passw = "";

                if (sendCredentials) {
                    uname = SqlUtil.getUsername(uri);
                    passw = SqlUtil.getPassword(uri);
                }

                _client.call("delete", resource.getScopeId(), resource.getId(), TYPE, host, port, database, uname,
                        passw);
                _log.debug("resource deleted via endpoint " + endpoint);

            } catch (WebhookException e) {
                _log.error("webhook error " + e.getMessage());
                throw new ConsumerException(e.getMessage());
            }
        }
    }

    @Override
    public void checkResource(String scopeId, String userId, Resource resource) throws ConsumerException {
        // not supported
    }

    /*
     * Helpers
     */

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
