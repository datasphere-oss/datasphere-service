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

package com.datasphere.engine.manager.resource.consumer.sqlpad;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class SqlpadClient {

	private String ENDPOINT;
	private String USERNAME;
	private String PASSWORD;

	private final static String API = "/api/connections";

	public SqlpadClient(String endpoint, String username, String password) {
		super();
		ENDPOINT = endpoint;
		USERNAME = username;
		PASSWORD = password;
	}

	private RestTemplate connect() {
		RestTemplate template = new RestTemplate();
		return template;
	}

	private JSONObject getConnection(String driver, String name) throws SqlpadException {
		try {
			RestTemplate template = connect();

			HttpHeaders headers = createHeaders(USERNAME, PASSWORD);
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

			HttpEntity<String> entity = new HttpEntity<>(headers);

			// fetch response as String because it may not match the json schema
			ResponseEntity<String> response = template.exchange(ENDPOINT + API, HttpMethod.GET, entity, String.class);
			if (response.getStatusCode() == HttpStatus.OK) {
				JSONObject result = null;

				// fetch JSON array inside object
				JSONObject res = new JSONObject(response.getBody());
				JSONArray list = res.getJSONArray("connections");
				for (int i = 0; i < list.length(); i++) {
					JSONObject json = list.getJSONObject(i);
					if (name.equals(json.optString("name", "")) &&
							driver.equals(json.optString("driver", ""))) {
						result = json;
						break;
					}
				}

				return result;
			} else {
				throw new SqlpadException("response error code " + response.getStatusCode());
			}
		} catch (RestClientException rex) {
			throw new SqlpadException("rest error " + rex.getMessage());
		}
	}

	public String hasConnection(String driver, String name) throws SqlpadException {
		// will return id if match found
		String id = "";

		JSONObject json = getConnection(driver, name);
		if (json != null) {
			id = json.getString("_id");
		}

		return id;
	}

	public String addConnection(
			String driver,
			String name,
			String host, int port,
			String database,
			String username, String password)
			throws SqlpadException {
		try {
			JSONObject json = new JSONObject();
			json.put("name", name);
			json.put("driver", driver);
			json.put("host", host);
			json.put("port", String.valueOf(port));
			json.put("database", database);
			json.put("username", username);
			json.put("password", password);

			RestTemplate template = connect();

			HttpHeaders headers = createHeaders(USERNAME, PASSWORD);
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

			HttpEntity<String> entity = new HttpEntity<>(json.toString(), headers);

			// fetch response as String because it may not match the json schema
			ResponseEntity<String> response = template.exchange(ENDPOINT + API, HttpMethod.POST, entity, String.class);
			if (response.getStatusCode() == HttpStatus.OK) {
				// fetch JSON and return id if present
				JSONObject pad = new JSONObject(response.getBody()).getJSONObject("connection");
				return pad.optString("_id", "");
			} else {
				throw new SqlpadException("response error code " + response.getStatusCode());
			}
		} catch (JSONException jex) {
			throw new SqlpadException("json error " + jex.getMessage());
		} catch (RestClientException rex) {
			throw new SqlpadException("rest error " + rex.getMessage());
		}

	}

	public String updateConnection(
			String driver,
			String name,
			String host, int port,
			String database,
			String username, String password)
			throws SqlpadException {
		try {
			JSONObject json = getConnection(driver, name);

			if (json == null) {
				// create if missing
				return addConnection(driver, name, host, port, database, username, password);
			}

			String id = json.getString("_id");
			// update fields
			json.put("host", host);
			json.put("port", String.valueOf(port));
			json.put("database", database);
			json.put("username", username);
			json.put("password", password);

			RestTemplate template = connect();

			HttpHeaders headers = createHeaders(USERNAME, PASSWORD);
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

			HttpEntity<String> entity = new HttpEntity<>(json.toString(), headers);

			// fetch response as String because it may not match the json schema
			ResponseEntity<String> response = template.exchange(
					ENDPOINT + API + "/" + id, HttpMethod.PUT, entity,
					String.class);

			if (response.getStatusCode() == HttpStatus.OK) {
				// fetch JSON and return id if present
				JSONObject pad = new JSONObject(response.getBody()).getJSONObject("connection");
				return pad.optString("_id", "");
			} else {
				throw new SqlpadException("response error code " + response.getStatusCode());
			}
		} catch (RestClientException rex) {
			throw new SqlpadException("rest error " + rex.getMessage());
		}

	}

	public String deleteConnection(String driver, String name) throws SqlpadException {
		try {
			String id = hasConnection(driver, name);

			if (id.isEmpty()) {
				// no match, nothing to do
				return "";
			}

			RestTemplate template = connect();

			HttpHeaders headers = createHeaders(USERNAME, PASSWORD);
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

			HttpEntity<String> entity = new HttpEntity<>(headers);

			// fetch response as String because it may not match the json schema
			ResponseEntity<String> response = template.exchange(
					ENDPOINT + API + "/" + id, HttpMethod.DELETE, entity,
					String.class);

			if (response.getStatusCode() == HttpStatus.OK) {
				return id;
			} else {
				throw new SqlpadException("response error code " + response.getStatusCode());
			}
		} catch (RestClientException rex) {
			throw new SqlpadException("rest error " + rex.getMessage());
		}
	}

	/*
	 * Helpers
	 */
	@SuppressWarnings("serial")
	private HttpHeaders createHeaders(String username, String password) {
		return new HttpHeaders() {
			{
				String auth = username + ":" + password;
				byte[] encodedAuth = Base64.encodeBase64(
						auth.getBytes(Charset.forName("UTF-8")));
				String authHeader = "Basic " + new String(encodedAuth);
				set("Authorization", authHeader);
			}
		};
	}
}
