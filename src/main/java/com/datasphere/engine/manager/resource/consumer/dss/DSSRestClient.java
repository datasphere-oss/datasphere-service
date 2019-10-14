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

import java.nio.charset.Charset;
import java.util.Arrays;

import javax.servlet.http.HttpUtils;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class DSSRestClient {
    private final static Logger _log = LoggerFactory.getLogger(DSSRestClient.class);

    private String ENDPOINT;
    private String USERNAME;
    private String PASSWORD;
    private String TENANT;

    private final static String API = "/rest/";
    // 设置租户
    public DSSRestClient(String endpoint, String tenant, String username, String password) {
        super();
        ENDPOINT = endpoint;
        TENANT = tenant;
        USERNAME = username;
        PASSWORD = password;

    }

    public JSONArray listDataSources() throws DSSException {
        // DISABLED, dss rest bridge return pagination without any
        // info about total count or pages count
        throw new DSSException("unsupported on DSS rest");
//        try {
//            JSONArray datasets = null;
//
//            RestTemplate template = new RestTemplate();
//            HttpHeaders headers = headers(USERNAME, PASSWORD);
//
//            HttpEntity<String> entity = new HttpEntity<>(headers);
//
//            // fetch response as String because it may not match the json schema
//            ResponseEntity<String> response = template.exchange(ENDPOINT + API + TENANT + "/listDataService",
//                    HttpMethod.GET,
//                    entity, String.class);
//            if (response.getStatusCode() == HttpStatus.OK) {
//                // fetch JSON array
//                datasets = new JSONArray(response.getBody());
//            } else {
//                throw new DSSException("response error code " + response.getStatusCode());
//            }
//
//            return datasets;
//        } catch (RestClientException rex) {
//            throw new DSSException("rest error " + rex.getMessage());
//        }
    }

    public boolean hasSource(String name) throws DSSException {
        boolean exists = false;

        try {

            RestTemplate template = new RestTemplate();
            HttpHeaders headers = headers(USERNAME, PASSWORD);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            // serviceid == name in DSS rest, mappend wrong
            String path = "/getDataService?serviceid=" + name;

            // fetch response as String because it may not match the json schema
            ResponseEntity<String> response = template.exchange(ENDPOINT + API + TENANT + path,
                    HttpMethod.GET,
                    entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                // assume OK means it exists, discard value..
                exists = true;
            } else {
                exists = false;
            }

        } catch (RestClientException rex) {
            // DSS will response with an error instead of 404..
            exists = false;
        }
        return exists;
    }
    // 添加资源
    public String addSource(
            String type,
            String name,
            String host, int port,
            String database,
            String username, String password)
            throws DSSException {
        try {
            JSONObject json = new JSONObject();
            JSONObject data = new JSONObject();

            data.put("name", name);
            data.put("description", type + ": " + name);

            JSONArray configs = new JSONArray();

            JSONObject config = getConfig(type, name, host, port, database, username, password);
            if (config == null) {
                throw new DSSException("error generating data source configuration");
            }

            configs.put(config);
            data.put("configs", configs);
            json.put("data", data);

            RestTemplate template = new RestTemplate();
            HttpHeaders headers = headers(USERNAME, PASSWORD);

            _log.trace("add source json " + json.toString());

            HttpEntity<String> entity = new HttpEntity<>(json.toString(), headers);

            // fetch response as String because it may not match the json schema
            ResponseEntity<String> response = template.exchange(ENDPOINT + API + TENANT + "/saveDataService",
                    HttpMethod.POST,
                    entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return name;
            } else {
                throw new DSSException("response error code " + response.getStatusCode());
            }
        } catch (JSONException jex) {
            throw new DSSException("json error " + jex.getMessage());
        } catch (RestClientException rex) {
            throw new DSSException("rest error " + rex.getMessage());
        }

    }
    // 更新资源
    public String updateSource(
            String type,
            String name,
            String host, int port,
            String database,
            String username, String password)
            throws DSSException {
        // TODO: verify if update is even possible given
        // the lack of access to data sources config in DSS rest
        return name;

    }
    // 删除资源
    public void deleteSource(String name) throws DSSException {
        try {
            RestTemplate template = new RestTemplate();
            HttpHeaders headers = headers(USERNAME, PASSWORD);

            JSONObject json = new JSONObject();
            HttpEntity<String> entity = new HttpEntity<>(json.toString(), headers);

            String path = "/dataService/" + name;

            // fetch response as String because it should be empty
            ResponseEntity<String> response = template.exchange(
                    ENDPOINT + API + TENANT + path, HttpMethod.DELETE, entity,
                    String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                // success
            } else {
                throw new DSSException("response error code " + response.getStatusCode());
            }
        } catch (RestClientException rex) {
            rex.printStackTrace();
            throw new DSSException("rest error " + rex.getMessage());
        }
    }

    /*
     * Helpers - 获取HTTP头信息
     */

    private HttpHeaders headers(String username, String password) throws RestClientException {

        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(Charset.forName("UTF-8")));
        String authHeader = "Basic " + new String(encodedAuth);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        return headers;
    }

    private JSONObject getConfig(
            String type,
            String name,
            String host, int port,
            String database,
            String username, String password) {

        if (type.equals("POSTGRES")) {
            return getPostgresConfiguration(name, host, port, database, username, password);
        }

        if (type.equals("HASHDATA")) {
            return getHashdataConfiguration(name, host, port, database, username, password);
        }
        
        if (type.equals("MYSQL")) {
            return getMySqlConfiguration(name, host, port, database, username, password);
        }

        if (type.equals("MONGODB")) {
            return getMongoConfiguration(name, host, port, database, username, password);
        }
        if (type.equals("DREMIO")) {
            return getDremioConfiguration(name, host, port, database, username, password);
        }

        return null;

    }

    private JSONObject getPostgresConfiguration(
            String name,
            String host, int port,
            String database,
            String username, String password) {

        String connection = "jdbc:postgresql://" + host + ":" + Integer.toString(port) + "/" + database;

        JSONObject json = new JSONObject();

        json.put("id", name);
        json.put("dataSourceType", "RDBMS");
        json.put("exposeAsODataService", false);
        json.put("publicODataService", false);

        JSONArray properties = new JSONArray();

        JSONObject driver = new JSONObject();
        driver.put("name", "driverClassName");
        driver.put("value", "org.postgresql.Driver");
        properties.put(driver);

        JSONObject url = new JSONObject();
        url.put("name", "url");
        url.put("value", connection);
        properties.put(url);

        JSONObject user = new JSONObject();
        user.put("name", "username");
        user.put("value", username);
        properties.put(user);

        JSONObject pass = new JSONObject();
        pass.put("name", "password");
        pass.put("value", password);
        properties.put(pass);

        json.put("properties", properties);

        return json;

    }

    private JSONObject getHashdataConfiguration(
            String name,
            String host, int port,
            String database,
            String username, String password) {

        String connection = "jdbc:postgresql://" + host + ":" + Integer.toString(port) + "/" + database;

        JSONObject json = new JSONObject();

        json.put("id", name);
        json.put("dataSourceType", "RDBMS");
        json.put("exposeAsODataService", false);
        json.put("publicODataService", false);

        JSONArray properties = new JSONArray();

        JSONObject driver = new JSONObject();
        driver.put("name", "driverClassName");
        driver.put("value", "org.postgresql.Driver");
        properties.put(driver);

        JSONObject url = new JSONObject();
        url.put("name", "url");
        url.put("value", connection);
        properties.put(url);

        JSONObject user = new JSONObject();
        user.put("name", "username");
        user.put("value", username);
        properties.put(user);

        JSONObject pass = new JSONObject();
        pass.put("name", "password");
        pass.put("value", password);
        properties.put(pass);

        json.put("properties", properties);

        return json;

    }
    
    private JSONObject getMySqlConfiguration(
            String name,
            String host, int port,
            String database,
            String username, String password) {

        String connection = "jdbc:mysql://" + host + ":" + Integer.toString(port) + "/" + database;

        JSONObject json = new JSONObject();

        json.put("id", name);
        json.put("dataSourceType", "RDBMS");
        json.put("exposeAsODataService", false);
        json.put("publicODataService", false);

        JSONArray properties = new JSONArray();

        JSONObject driver = new JSONObject();
        driver.put("name", "driverClassName");
        driver.put("value", "com.mysql.jdbc.Driver");
        properties.put(driver);

        JSONObject url = new JSONObject();
        url.put("name", "url");
        url.put("value", connection);
        properties.put(url);

        JSONObject user = new JSONObject();
        user.put("name", "username");
        user.put("value", username);
        properties.put(user);

        JSONObject pass = new JSONObject();
        pass.put("name", "password");
        pass.put("value", password);
        properties.put(pass);

        json.put("properties", properties);

        return json;

    }

    private JSONObject getMongoConfiguration(
            String name,
            String host, int port,
            String database,
            String username, String password) {

        String server = host + ":" + Integer.toString(port);

        JSONObject json = new JSONObject();

        json.put("id", name);
        json.put("dataSourceType", "MongoDB");
        json.put("exposeAsODataService", false);
        json.put("publicODataService", false);

        JSONArray properties = new JSONArray();

        JSONObject servers = new JSONObject();
        servers.put("name", "mongoDB_servers");
        servers.put("value", server);
        properties.put(servers);

        JSONObject db = new JSONObject();
        db.put("name", "mongoDB_database");
        db.put("value", database);
        properties.put(db);

        JSONObject user = new JSONObject();
        user.put("name", "username");
        user.put("value", username);
        properties.put(user);

        JSONObject pass = new JSONObject();
        pass.put("name", "password");
        pass.put("value", password);
        properties.put(pass);

        json.put("properties", properties);

        return json;

    }
    // 获得dremio配置
    private JSONObject getDremioConfiguration(
            String name,
            String host, int port,
            String database,
            String username, String password) {

        String schema = database.replaceFirst("DREMIO/", "");
        String connection = "jdbc:dremio:direct=" + host + ":" + Integer.toString(port) + ";schema=" + schema;

        JSONObject json = new JSONObject();

        json.put("id", name);
        json.put("dataSourceType", "RDBMS");
        json.put("exposeAsODataService", false);
        json.put("publicODataService", false);

        JSONArray properties = new JSONArray();

        JSONObject driver = new JSONObject();
        driver.put("name", "driverClassName");
        driver.put("value", "com.dremio.jdbc.Driver");
        properties.put(driver);

        JSONObject url = new JSONObject();
        url.put("name", "url");
        url.put("value", connection);
        properties.put(url);

        JSONObject user = new JSONObject();
        user.put("name", "username");
        user.put("value", username);
        properties.put(user);

        JSONObject pass = new JSONObject();
        pass.put("name", "password");
        pass.put("value", password);
        properties.put(pass);

        json.put("properties", properties);

        return json;

    }

}
