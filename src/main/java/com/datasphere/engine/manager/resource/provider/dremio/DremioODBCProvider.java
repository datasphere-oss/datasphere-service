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

package com.datasphere.engine.manager.resource.provider.dremio;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.datasphere.engine.manager.resource.SystemKeys;
import com.datasphere.engine.manager.resource.common.DuplicateNameException;
import com.datasphere.engine.manager.resource.common.InvalidNameException;
import com.datasphere.engine.manager.resource.common.NoSuchProviderException;
import com.datasphere.engine.manager.resource.common.ResourceProviderException;
import com.datasphere.engine.manager.resource.model.Resource;
import com.datasphere.engine.manager.resource.model.ResourceProvider;
import com.datasphere.engine.manager.resource.service.ResourceLocalService;
import com.datasphere.engine.manager.resource.util.OdbcUtil;

@Component
public class DremioODBCProvider extends ResourceProvider {
    private final static Logger _log = LoggerFactory.getLogger(DremioODBCProvider.class);

    public static final String TYPE = SystemKeys.TYPE_ODBC;
    public static final String ID = "dremioodbc";

    private static final String VALID_CHARS = "[a-zA-Z0-9]+";
//    private static final String VIRTUAL_IDENTIFIER = "VIRTUAL_DATASET";

    @Value("${providers.dremio.enable}")
    private boolean ENABLED;

    @Value("${providers.dremio.properties}")
    private List<String> PROPERTIES;

    // dremio connection
    @Value("${providers.dremio.host}")
    private String HOST;

    @Value("${providers.dremio.port}")
    private int PORT;

    @Value("${providers.dremio.ssl}")
    private boolean SSL;

    @Value("${providers.dremio.username}")
    private String USERNAME;

    @Value("${providers.dremio.password}")
    private String PASSWORD;

    @Value("${providers.dremio.odbc.username}")
    private String ODBC_USERNAME;

    @Value("${providers.dremio.odbc.password}")
    private String ODBC_PASSWORD;

    @Value("${providers.dremio.interval}")
    private int INTERVAL;

    @Value("${providers.dremio.sync}")
    private boolean SYNC;

    // all resources will end in default scope
    // could maybe map dremioSpace -> scope?
    @Value("${scopes.default}")
    private String scopeId;

    private DremioClient _client;

    @Autowired
    @Lazy
    private ResourceLocalService resourceLocalService;

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

        if (ENABLED) {
            String endpoint = (SSL ? "https://" : "http://") + HOST + ":" + Integer.toString(PORT);
            _client = new DremioClient(endpoint, USERNAME, PASSWORD);
            _log.info("init, scheduled every " + String.valueOf(INTERVAL) + "ms");
        }

    }

    @Override
    public int getStatus() {
        // NEVER return READY since we can not instantiate resources for users
        if (ENABLED) {
            return SystemKeys.STATUS_ENABLED;
        } else {
            return SystemKeys.STATUS_DISABLED;
        }
    }

    @Override
    public Resource createResource(String scopeId, String userId, String name, Map<String, Serializable> properties)
            throws ResourceProviderException, InvalidNameException, DuplicateNameException {
        throw new ResourceProviderException("not supported");
    }

    @Override
    public void updateResource(Resource resource) throws ResourceProviderException {
    }

    @Override
    public void deleteResource(Resource resource) throws ResourceProviderException {
    }

    @Override
    public void checkResource(Resource resource) throws ResourceProviderException {
    }

    /*
     * Scheduler
     */

    @Scheduled(initialDelay = 10000, fixedRateString = "${providers.dremio.interval}")
    public void reflectDatasets() {
        if (ENABLED) {
            try {
                _log.debug("reflectDatasets execution");

                // fetch resources
                Set<String> keys = new HashSet<>();
                Set<String> virtual = new HashSet<>();
                List<Resource> list = resourceLocalService.listByProvider(ID);
                List<Resource> resources = new ArrayList<>();
                for (Resource r : list) {
                    try {
                        // need to explicitely fetch to decrypt URI
                        Resource res = resourceLocalService.get(r.getId());
                        // fetch KEY
                        keys.add(OdbcUtil.getValues(res.getUri()).get("KEY"));
                        resources.add(res);
                    } catch (Exception e) {
                        // skip
                        _log.error("resource " + String.valueOf(r.getId()) + " error " + e.getMessage());
                    }
                }

                _log.debug("reflectDatasets has found " + String.valueOf(keys.size()) + " resources in DB");
                _log.trace("keys " + keys.toString());

                JSONArray datasets = _client.listDatasets();

                _log.debug("reflectDatasets has found " + String.valueOf(datasets.length()) + " datasets in JSON");

                for (int i = 0; i < datasets.length(); i++) {
                    JSONObject dataset = datasets.getJSONObject(i);

                    try {
                        // read only supported
                        String type = dataset.optString("datasetType", "");
                        if (isTypeSupported(type)) {
                            _log.debug("found dataset type " + type);

                            JSONArray fullPath = dataset.getJSONArray("fullPath");
                            String[] path = new String[fullPath.length()];

                            for (int j = 0; j < path.length; j++) {
                                path[j] = fullPath.getString(j);
                            }

                            // last element is the dataset, store space + folders as schema
//                            String space = path[0];
//                            String table = "[" + space + "].[" + path[1] + "]";
                            String table = path[path.length - 1];
                            StringBuilder sb = new StringBuilder();
                            for (int j = 0; j < path.length - 1; j++) {
                                sb.append(".").append(path[j]);
                            }
                            String schema = sb.toString();
                            if (schema.startsWith(".")) {
                                schema = schema.substring(1);
                            }

                            // use self link as key
                            String key = dataset.getJSONObject("links").optString("self", "");
                            key = URLEncoder.encode(key, "UTF-8");

                            _log.debug("dataset key set as " + key);
                            _log.trace("dataset schema " + schema);
                            _log.trace("dataset table " + table);

                            // save ref
                            virtual.add(key);

                            // check if already exists
                            if (!keys.contains(key)) {
                                // add
                                _log.debug("add virtual dataset " + key);
                                Resource res = addResource(schema, table, key);
                                _log.debug("added resource " + res.getId());

                                keys.add(key);

                            }

                        }
                    } catch (Exception e) {
                        // skip
                        e.printStackTrace();
                        _log.error("error " + e.getMessage());
                    }
                }

                if (SYNC) {
                    _log.debug("clear orphan resources");

                    // remove missing datasets
                    for (Resource res : resources) {
                        String key = OdbcUtil.getValues(res.getUri()).get("KEY");
                        _log.debug("check resource with key " + key);

                        // has to exists in virtual keys
                        if (!virtual.contains(key)) {
                            // remove
                            _log.debug("remove resource with id " + res.getId());
                            resourceLocalService.delete(res.getId());
                        }
                    }

                }

            } catch (Exception ex) {
                ex.printStackTrace();
                _log.error("error " + ex.getMessage());
            }
        }
    }

    /*
     * Helpers
     */

    public Resource addResource(String schema, String table, String datasetId)
            throws NoSuchProviderException, ResourceProviderException, UnsupportedEncodingException {
        // prepare data
        Map<String, Serializable> properties = new HashMap<>();
        List<String> tags = new ArrayList<>();

        // save id as KEY
        String connectionProperties = "KEY=" + datasetId;

        // pack uri
        String uri = OdbcUtil.encodeURI(
                "dremioodbc", "Dremio Connector",
                HOST, 31010,
                ODBC_USERNAME, ODBC_PASSWORD,
                "DREMIO", schema, table,
                connectionProperties);

        // use our id as username
        // TODO replace
        String userId = ID;
        // register an unmanaged resource
        return resourceLocalService.add(scopeId, userId, TYPE, ID, uri, properties, tags);

    }

    private boolean isTypeSupported(String type) {
        boolean is = false;
        switch (type) {
        case "PHYSICAL_DATASET_SOURCE_FILE":
        case "PHYSICAL_DATASET_HOME_FILE":
        case "VIRTUAL_DATASET":
            is = true;
            break;
        }

        return is;
    }

}
