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

import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class MetabaseClient {
    private String ENDPOINT;
    private String USERNAME;
    private String PASSWORD;

    // api prefix, to be updated on metabase releases
    private final static String API = "/api/";

    public MetabaseClient(String endpoint, String username, String password) {
        super();
        ENDPOINT = endpoint;
        USERNAME = username;
        PASSWORD = password;
    }

    public boolean ping() throws MetabaseException {

        String token = "";
        boolean ret = false;

        try {
            token = login();
            // call an unauthenticated endpoint to check connectivity
            RestTemplate template = new RestTemplate();
            HttpHeaders headers = headers(token);

            JSONObject json = new JSONObject();
            HttpEntity<String> entity = new HttpEntity<>(json.toString(), headers);

            // fetch response as String because it may not match the json schema
            ResponseEntity<String> response = template.exchange(
                    ENDPOINT + API + "util/random_token", HttpMethod.GET, entity,
                    String.class);

            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                ret = true;
            } else {
                ret = false;
            }

            // logout since sessions are limited
            token = logout(token);

            return ret;
        } catch (MetabaseException e) {
            throw e;
        } finally {
            // clear session if needed
            if (!token.isEmpty()) {
                try {
                    logout(token);
                } catch (Exception ex) {
                    // ignore
                }
            }
        }

    }

    public JSONArray listDatabases() throws MetabaseException {
        String token = "";
        try {
            token = login();
            RestTemplate template = new RestTemplate();
            HttpHeaders headers = headers(token);

            JSONObject json = new JSONObject();
            HttpEntity<String> entity = new HttpEntity<>(json.toString(), headers);

            // fetch response as String because it may not match the json schema
            ResponseEntity<String> response = template.exchange(
                    ENDPOINT + API + "database", HttpMethod.GET, entity,
                    String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // fetch JSON array
                JSONArray list = new JSONArray(response.getBody());

                // logout since sessions are limited
                token = logout(token);

                return list;
            } else {
                throw new MetabaseException("response error code " + response.getStatusCode());
            }

        } catch (RestClientException e) {
            throw new MetabaseException(e.getMessage());
        } catch (MetabaseException e) {
            throw e;
        } finally {
            // clear session if needed
            if (!token.isEmpty()) {
                try {
                    logout(token);
                } catch (Exception ex) {
                    // ignore
                }
            }
        }
    }

    public JSONObject getDatabase(String engine, String name) throws MetabaseException {
        JSONObject db = null;

        // leverage list and filter
        JSONArray list = listDatabases();
        for (int i = 0; i < list.length(); i++) {
            JSONObject d = list.getJSONObject(i);
            if (d.optString("engine", "").equals(engine) && d.optString("name", "").equals(name)) {
                db = d;
                break;
            }
        }

        return db;
    }

    public boolean hasDatabase(String engine, String name) throws MetabaseException {
        // lookup if exists via call to list => get
        JSONObject db = getDatabase(engine, name);
        return (db != null);
    }

    public long addDatabase(
            String engine,
            String name,
            String host, int port, boolean ssl,
            String database,
            String username, String password) throws MetabaseException {
        String token = "";
        try {
            token = login();
            RestTemplate template = new RestTemplate();
            HttpHeaders headers = headers(token);

            JSONObject json = new JSONObject();
            json.put("engine", engine);
            json.put("name", name);
            json.put("is_full_sync", true);

            // connection details
            JSONObject details = new JSONObject();
            details.put("host", host);
            details.put("port", port);
            details.put("dbname", database);
            details.put("user", username);
            details.put("password", password);
            details.put("ssl", ssl);

            json.put("details", details);

            // default schedule : full scan daily + metadata hourly
            JSONObject schedules = new JSONObject();
            JSONObject scheduleCache = new JSONObject();
            scheduleCache.put("schedule_type", "daily");

            JSONObject scheduleMeta = new JSONObject();
            scheduleMeta.put("schedule_type", "hourly");

            schedules.put("cache_field_values", scheduleCache);
            schedules.put("metadata_sync", scheduleMeta);

            json.put("schedules", schedules);

            HttpEntity<String> entity = new HttpEntity<>(json.toString(), headers);

            // fetch response as String because it may not match the json schema
            ResponseEntity<String> response = template.exchange(
                    ENDPOINT + API + "database", HttpMethod.POST, entity,
                    String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // fetch JSON object
                JSONObject db = new JSONObject(response.getBody());

                // logout since sessions are limited
                token = logout(token);

                return db.getLong("id");
            } else {
                throw new MetabaseException("response error code " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            throw new MetabaseException(e.getMessage());
        } catch (MetabaseException e) {
            throw e;
        } finally {
            // clear session if needed
            if (!token.isEmpty()) {
                try {
                    logout(token);
                } catch (Exception ex) {
                    // ignore
                }
            }
        }
    }

    public long updateDatabase(
            String engine,
            String name,
            String host, int port, boolean ssl,
            String database,
            String username, String password) throws MetabaseException {
        String token = "";
        try {
            long id = 0;

            JSONObject db = getDatabase(engine, name);

            if (db == null) {
                return addDatabase(engine, name, host, port, ssl, database, username, password);
            }

            id = db.getLong("id");

            token = login();
            RestTemplate template = new RestTemplate();
            HttpHeaders headers = headers(token);

            JSONObject json = new JSONObject();
            json.put("engine", engine);
            json.put("name", name);
            json.put("is_full_sync", true);

            // connection details
            JSONObject details = new JSONObject();
            details.put("host", host);
            details.put("port", port);
            details.put("dbname", database);
            details.put("user", username);
            details.put("password", password);
            details.put("ssl", ssl);

            json.put("details", details);

            HttpEntity<String> entity = new HttpEntity<>(json.toString(), headers);

            // fetch response as String because it may not match the json schema
            ResponseEntity<String> response = template.exchange(
                    ENDPOINT + API + "database/" + Long.toString(id), HttpMethod.PUT, entity,
                    String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // fetch JSON object
                db = new JSONObject(response.getBody());

                // logout since sessions are limited
                token = logout(token);

                return db.getLong("id");
            } else {
                throw new MetabaseException("response error code " + response.getStatusCode());
            }

        } catch (RestClientException e) {
            throw new MetabaseException(e.getMessage());
        } catch (MetabaseException e) {
            throw e;
        } finally {
            // clear session if needed
            if (!token.isEmpty()) {
                try {
                    logout(token);
                } catch (Exception ex) {
                    // ignore
                }
            }
        }
    }

    public void deleteDatabase(String engine, String name) throws MetabaseException {
        String token = "";
        try {
            long id = 0;

            JSONObject db = getDatabase(engine, name);

            if (db == null) {
                return;
            }

            id = db.getLong("id");

            token = login();
            RestTemplate template = new RestTemplate();
            HttpHeaders headers = headers(token);

            JSONObject json = new JSONObject();
            HttpEntity<String> entity = new HttpEntity<>(json.toString(), headers);

            // fetch response as String because it may not match the json schema
            ResponseEntity<String> response = template.exchange(
                    ENDPOINT + API + "database/" + Long.toString(id), HttpMethod.DELETE, entity,
                    String.class);

            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                // logout since sessions are limited
                token = logout(token);

                return;
            } else {
                throw new MetabaseException("response error code " + response.getStatusCode());
            }

        } catch (RestClientException e) {
            throw new MetabaseException(e.getMessage());
        } catch (MetabaseException e) {
            throw e;
        } finally {
            // clear session if needed
            if (!token.isEmpty()) {
                try {
                    logout(token);
                } catch (Exception ex) {
                    // ignore
                }
            }
        }
    }

    public void syncDatabase(long id) throws MetabaseException {
        String token = "";
        try {

            token = login();
            RestTemplate template = new RestTemplate();
            HttpHeaders headers = headers(token);

            JSONObject json = new JSONObject();
            HttpEntity<String> entity = new HttpEntity<>(json.toString(), headers);

            // fetch response as String because it may not match the json schema
            ResponseEntity<String> response = template.exchange(
                    ENDPOINT + API + "database/" + Long.toString(id) + "/sync_schema", HttpMethod.POST, entity,
                    String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // logout since sessions are limited
                token = logout(token);

                return;
            } else {
                throw new MetabaseException("response error code " + response.getStatusCode());
            }

        } catch (RestClientException e) {
            throw new MetabaseException(e.getMessage());
        } catch (MetabaseException e) {
            throw e;
        } finally {
            // clear session if needed
            if (!token.isEmpty()) {
                try {
                    logout(token);
                } catch (Exception ex) {
                    // ignore
                }
            }
        }
    }

    /*
     * Session
     */

    private HttpHeaders headers(String token) throws MetabaseException, RestClientException {

        if (token.isEmpty()) {
            throw new MetabaseException("session invalid");
        }

        HttpHeaders headers = new HttpHeaders();
        // token goes into custom header
        headers.set("X-Metabase-Session", token);
        // REST api supports only json
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        return headers;
    }

    private String login() throws MetabaseException, RestClientException {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        JSONObject json = new JSONObject();
        json.put("username", USERNAME);
        json.put("password", PASSWORD);

        HttpEntity<String> entity = new HttpEntity<>(json.toString(), headers);

        // fetch response as String because it may not match the json schema
        ResponseEntity<String> response = template.exchange(
                ENDPOINT + API + "session", HttpMethod.POST, entity,
                String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            // fetch JSON and return token if present
            JSONObject session = new JSONObject(response.getBody());
            return session.optString("id", "");
        } else {
            throw new MetabaseException("login error code " + response.getStatusCode());
        }
    }

    private String logout(String token) throws MetabaseException, RestClientException {

        if (token.isEmpty()) {
            return "";
        }

        RestTemplate template = new RestTemplate();
        HttpHeaders headers = headers(token);
        JSONObject json = new JSONObject();
        json.put("metabase-session-id", token);

        HttpEntity<String> entity = new HttpEntity<>(json.toString(), headers);

        // fetch response as String because it may not match the json schema
        ResponseEntity<String> response = template.exchange(
                ENDPOINT + API + "session", HttpMethod.DELETE, entity,
                String.class);

        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            return "";
        } else {
            // ignore logout error
//            throw new MetabaseException("logout error code " + response.getStatusCode());
            return token;
        }
    }

}
