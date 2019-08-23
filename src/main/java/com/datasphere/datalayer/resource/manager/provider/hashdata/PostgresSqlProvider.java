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

package com.datasphere.datalayer.resource.manager.provider.hashdata;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.datasphere.datalayer.resource.manager.SystemKeys;
import com.datasphere.datalayer.resource.manager.common.DuplicateNameException;
import com.datasphere.datalayer.resource.manager.common.InvalidNameException;
import com.datasphere.datalayer.resource.manager.common.ResourceProviderException;
import com.datasphere.datalayer.resource.manager.model.Resource;
import com.datasphere.datalayer.resource.manager.model.ResourceProvider;
import com.datasphere.datalayer.resource.manager.util.SqlUtil;
import com.datasphere.datalayer.resource.manager.util.StringUtils;

@Component
public class PostgresSqlProvider extends ResourceProvider {
    private final static Logger _log = LoggerFactory.getLogger(PostgresSqlProvider.class);

    public static final String TYPE = SystemKeys.TYPE_SQL;
    public static final String ID = "postgresSql";

    private static final String VALID_CHARS = "[a-zA-Z0-9]+";

    private int STATUS;

    @Value("${providers.postgressql.enable}")
    private boolean ENABLED;

    @Value("${providers.postgressql.properties}")
    private List<String> PROPERTIES;

    // postgres connection
    @Value("${providers.postgressql.host}")
    private String HOST;

    @Value("${providers.postgressql.port}")
    private int PORT;

    @Value("${providers.postgressql.ssl}")
    private boolean SSL;

    @Value("${providers.postgressql.username}")
    private String USERNAME;

    @Value("${providers.postgressql.password}")
    private String PASSWORD;

    private PostgresSqlClient _client;

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
        return new HashSet<String>(PROPERTIES);
    }

    /*
     * Init method - POST constructor since spring injects properties *after
     * creation*
     */
    @PostConstruct
    public void init() {
        _log.info("enabled " + String.valueOf(ENABLED));
        STATUS = SystemKeys.STATUS_DISABLED;

        if (ENABLED) {
            _client = new PostgresSqlClient(HOST, PORT, SSL, USERNAME, PASSWORD);
            // check postgres availability

            if (_client.ping()) {
                STATUS = SystemKeys.STATUS_READY;
            } else {
                STATUS = SystemKeys.STATUS_ERROR;
            }

        }

        _log.info("init status " + String.valueOf(STATUS));
    }

    @Override
    public int getStatus() {
        return STATUS;
    }

    @Override
    public Resource createResource(String scopeId, String userId, String name, Map<String, Serializable> properties)
            throws ResourceProviderException, InvalidNameException, DuplicateNameException {

        Resource res = new Resource();
        res.setType(TYPE);
        res.setProvider(ID);
        res.setPropertiesMap(properties);

        try {
            if (!name.isEmpty()) {
                // validate name
                if (!name.matches(VALID_CHARS)) {
                    throw new InvalidNameException();
                }

                // build scoped name
                StringBuilder sb = new StringBuilder();
                sb.append(scopeId.replaceAll("[^A-Za-z0-9]", "")).append("_");
                sb.append(userId.replaceAll("[^A-Za-z0-9]", "")).append("_");
                sb.append(name);

                name = sb.toString();

                // check duplicate for scoped name
                if (_client.hasDatabase(name)) {
                    throw new DuplicateNameException();
                }
            } else {
                // generate id with limited tries
                name = generateId(scopeId, userId);
                int retry = 0;
                boolean exists = _client.hasDatabase(name);
                while (exists && retry < 8) {
                    name = generateId(scopeId, userId);
                    exists = _client.hasDatabase(name);
                    retry++;
                }

                if (exists) {
                    throw new ResourceProviderException("error creating database");
                }
            }

            _log.info("create database " + name + " with scope " + scopeId + " for user " + userId);

            // create database
            _client.createDatabase(name);

            // create username = dbname
            String username = name;
            String password = RandomStringUtils.randomAlphanumeric(10);

            _log.info("create user " + username + " for database " + name);

            _client.createUser(name, username, password);

            // generate uri
            String endpoint = HOST + ":" + String.valueOf(PORT);
            String uri = SqlUtil.encodeURI("postgres", endpoint, name, username, password);

            // update res
            res.setName(name);
            res.setUri(uri);

            return res;
        } catch (SQLException sex) {
            _log.error(sex.getMessage());
            throw new ResourceProviderException("sql error");
        }
    }

    @Override
    public void updateResource(Resource resource) throws ResourceProviderException {
        // TODO

    }

    @Override
    public void deleteResource(Resource resource) throws ResourceProviderException {

        _log.info("delete resource " + String.valueOf(resource.getId())
                + " with scope " + resource.getScopeId()
                + " for user " + resource.getUserId());

        // extract info from resource
        String database = SqlUtil.getDatabase(resource.getUri());
        String username = SqlUtil.getUsername(resource.getUri());

        try {
            if (!USERNAME.equals(username) && !username.isEmpty()) {
                // delete user first
                _log.info("drop user " + username + " for database " + database);
                _client.deleteUser(database, username);
            }

            if (!database.isEmpty()) {
                // delete database
                _log.info("drop database " + database);
                _client.deleteDatabase(database);
            }
        } catch (SQLException sex) {
            _log.error(sex.getMessage());
            throw new ResourceProviderException("sql error");
        }
    }

    @Override
    public void checkResource(Resource resource) throws ResourceProviderException {
        // TODO
    }

    /*
     * Helpers
     */
    private String generateId(String scopeId, String userId) {
        // build id from context plus random string
        StringBuilder sb = new StringBuilder();
        // cleanup scope and userId to alphanum - will strip non ascii
        // use only _ as separator otherwise postgres will complain
        sb.append(StringUtils.shorten(scopeId.replaceAll("[^A-Za-z0-9]", ""), 10).toLowerCase()).append("_");
        sb.append(StringUtils.shorten(userId.replaceAll("[^A-Za-z0-9]", ""), 12).toLowerCase()).append("_");
        
        // random suffix length 6
        sb.append(RandomStringUtils.randomAlphanumeric(6));

        // ensure lowercase
        return sb.toString().toLowerCase();
    }
}
