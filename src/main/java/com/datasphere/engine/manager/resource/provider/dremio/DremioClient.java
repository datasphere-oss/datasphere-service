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

import java.util.Arrays;

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

public class DremioClient {
    private final static Logger _log = LoggerFactory.getLogger(DremioClient.class);

    private String ENDPOINT;
    private String USERNAME;
    private String PASSWORD;

    private final static String API = "/apiv2/";

    public DremioClient(String endpoint, String username, String password) {
        super();
        ENDPOINT = endpoint;
        USERNAME = username;
        PASSWORD = password;
    }

    public JSONArray listDatasets() throws DremioException {
        try {
            JSONArray datasets = null;

            String token = login();

            RestTemplate template = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "_dremio" + token);
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            HttpEntity<String> entity = new HttpEntity<>(headers);

            // fetch response as String because it may not match the json schema
            ResponseEntity<String> response = template.exchange(ENDPOINT + API + "datasets/search", HttpMethod.GET,
                    entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                // fetch JSON array
                datasets = new JSONArray(response.getBody());
            } else {
                throw new DremioException("response error code " + response.getStatusCode());
            }

            // disconnect to avoid consuming tokens
            logout(token);

            return datasets;
        } catch (RestClientException rex) {
            throw new DremioException("rest error " + rex.getMessage());
        }
    }

    /*
     * Helpers
     */

    private String login() throws DremioException, RestClientException {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        JSONObject json = new JSONObject();
        json.put("userName", USERNAME);
        json.put("password", PASSWORD);

        HttpEntity<String> entity = new HttpEntity<>(json.toString(), headers);

        // fetch response as String because it may not match the json schema
        ResponseEntity<String> response = template.exchange(
                ENDPOINT + API + "/login", HttpMethod.POST, entity,
                String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            // fetch JSON and return token if present
            JSONObject user = new JSONObject(response.getBody());
            return user.optString("token", "");
        } else {
            throw new DremioException("login error code " + response.getStatusCode());
        }
    }

    private void logout(String token) throws DremioException, RestClientException {
        try {
            RestTemplate template = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.set("Authorization", "_dremio" + token);

            String content = "";
            HttpEntity<String> entity = new HttpEntity<>(content, headers);

            // fetch response as String because it may not match the json schema
            ResponseEntity<String> response = template.exchange(
                    ENDPOINT + API + "/login", HttpMethod.DELETE, entity,
                    String.class);

            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                // successfully logged out
            } else {
                // error, log
                _log.error("dremio logout error " + String.valueOf(response.getStatusCodeValue()));
            }
        } catch (Exception ex) {
            // ignore
        }
    }
}
