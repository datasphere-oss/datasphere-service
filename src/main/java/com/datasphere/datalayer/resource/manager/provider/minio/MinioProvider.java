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

package com.datasphere.datalayer.resource.manager.provider.minio;

import java.io.Serializable;
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

import io.minio.errors.MinioException;

@Component
public class MinioProvider extends ResourceProvider {
    private final static Logger _log = LoggerFactory.getLogger(MinioProvider.class);

    public static final String TYPE = SystemKeys.TYPE_OBJECT;
    public static final String ID = "minio";

    private static final String VALID_CHARS = "[a-zA-Z0-9]+";

    private int STATUS;

    @Value("${providers.minio.enable}")
    private boolean ENABLED;

    @Value("${providers.minio.properties}")
    private List<String> PROPERTIES;

    // minio play connection
    @Value("${providers.minio.host}")
    private String HOST;

    @Value("${providers.minio.port}")
    private int PORT;

    @Value("${providers.minio.ssl}")
    private boolean SSL;

    @Value("${providers.minio.accessKey}")
    private String ACCESS_KEY;

    @Value("${providers.minio.secretKey}")
    private String SECRET_KEY;

//    @Value("${providers.minio.userAccessKey}")
//    private String USER_ACCESS_KEY;
//
//    @Value("${providers.minio.userSecretKey}")
//    private String USER_SECRET_KEY;

    @Value("${providers.minio.clearOnDelete}")
    private boolean CLEAR_ON_DELETE;

    private MinioS3Client _client;

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
            _client = new MinioS3Client(HOST, PORT, SSL, ACCESS_KEY, SECRET_KEY);
            // check minio status
            // TODO implement status check
            // via REST api on client

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
                sb.append(scopeId.replaceAll("[^A-Za-z0-9]", "")).append("-");
                sb.append(userId.replaceAll("[^A-Za-z0-9]", "")).append("-");
                sb.append(name);

                name = sb.toString();

                // check duplicate for scoped name
                if (_client.hasBucket(name)) {
                    throw new DuplicateNameException();
                }
            } else {
                // generate id with limited tries
                name = generateId(scopeId, userId);
                int retry = 0;
                boolean exists = _client.hasBucket(name);
                while (exists && retry < 8) {
                    name = generateId(scopeId, userId);
                    exists = _client.hasBucket(name);
                    retry++;
                }

                if (exists) {
                    throw new ResourceProviderException("error creating bucket");
                }
            }

            _log.info("create bucket " + name + " with scope " + scopeId + " for user " + userId);

            // create bucket
            _client.createBucket(name);

            // create rw policy for bucket
            _client.createPolicy(name, MinioS3Client.POLICY_RW);

            // create user
            // TODO check listUsers to avoid duplicates
            String userAccessKey = RandomStringUtils.randomAlphanumeric(20).toUpperCase();

            String userSecretKey = RandomStringUtils.randomAlphanumeric(20)
                    + RandomStringUtils.randomAlphanumeric(12) //+ "+"
                    + RandomStringUtils.randomAlphanumeric(4);

            _client.createUser(name, userAccessKey, userSecretKey, MinioS3Client.POLICY_RW);

            // generate uri
            String endpoint = HOST + ":" + String.valueOf(PORT);
            String uri = MinioUtils.encodeURI(endpoint, name, userAccessKey, userSecretKey);

            // update res
            res.setName(name);
            res.setUri(uri);

            return res;
        } catch (MinioException mex) {
            _log.error(mex.getMessage());
            throw new ResourceProviderException("minio error");
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
        String bucket = MinioUtils.getBucket(resource.getUri());
        String userAccessKey = MinioUtils.getAccessKey(resource.getUri());
//        String userSecretKey = MinioUtils.getSecretKey(resource.getUri());

        try {

            // delete user
            try {
                _client.removeUser(userAccessKey);
            } catch (MinioException mex) {
                _log.error("remove user " + userAccessKey + " error " + mex.getMessage());
            }
            // drop policies
            try {
                _client.removePolicy(bucket, MinioS3Client.POLICY_RW);
            } catch (MinioException mex) {
                _log.error("remove policy " + MinioS3Client.POLICY_RW + " error " + mex.getMessage());
            }

            try {
                _client.removePolicy(bucket, MinioS3Client.POLICY_RO);
            } catch (MinioException mex) {
                _log.error("remove policy " + MinioS3Client.POLICY_RO + " error " + mex.getMessage());
            }

            // delete bucket with drop all objects?
            _log.info("drop bucket " + bucket + " with clear:" + String.valueOf(CLEAR_ON_DELETE));
            _client.deleteBucket(bucket, CLEAR_ON_DELETE);

        } catch (MinioException mex) {
            _log.error(mex.getMessage());
            throw new ResourceProviderException("minio error");
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
        // use only - as separator to obtain simpler urls
        sb.append(scopeId.replaceAll("[^A-Za-z0-9]", "")).append("-");
        sb.append(userId.replaceAll("[^A-Za-z0-9]", "")).append("-");

        // random suffix length 5
        sb.append(RandomStringUtils.randomAlphanumeric(5));

        // ensure lowercase
        return sb.toString().toLowerCase();
    }

}
