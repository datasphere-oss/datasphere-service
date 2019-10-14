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

import java.nio.charset.Charset;
import java.security.SignatureException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class WebhookClient {
    private final static Logger _log = LoggerFactory.getLogger(WebhookClient.class);

    private String ENDPOINT;

    private String USERNAME;
    private String PASSWORD;
    private String TOKEN;
    private String SECRET;

    // auth modes
    private int authMode = AUTH_NONE;

    // signature modes
    private int signMode = SIGN_NONE;
    private String SIGN_ALG = "HmacSHA1";

    public WebhookClient(String endpoint) {
        super();
        ENDPOINT = endpoint;
        USERNAME = "";
        PASSWORD = "";
        TOKEN = "";
        SECRET = "";
    }
    // 认证Token
    public void setAuthToken(String token) {
        // skip validation
        authMode = AUTH_TOKEN;
        TOKEN = token;
    }
    // 基本认证信息
    public void setAuthBasic(String username, String password) {
        // skip validation
        authMode = AUTH_BASIC;
        USERNAME = username;
        PASSWORD = password;
    }

    public void setSignature(String secret) {
        signMode = SIGN_PAYLOAD;
        SECRET = secret;
    }

    /*
     * Hooks - 调用主机端口数据库
     */
    public void call(
            String event,
            String scopeId, long resourceId,
            String type,
            String host, int port,
            String name,
            String username, String password)
            throws WebhookException {
        try {
            JSONObject json = new JSONObject();
            json.put("event", event);
            json.put("type", type);

            json.put("resourceId", resourceId);
            if (!scopeId.isEmpty()) {
                json.put("scope", scopeId);
            }
            json.put("name", name);
            json.put("host", host);
            json.put("port", String.valueOf(port));
            json.put("username", username);
            json.put("password", password);

            RestTemplate template = new RestTemplate();
            HttpHeaders headers = headers();

            if (signMode == SIGN_PAYLOAD) {
                // hmac json
                String signature = sign(json.toString());
                headers.add("X-Signature", signature);
            }

            HttpEntity<String> entity = new HttpEntity<>(json.toString(), headers);

            _log.debug("call " + event + " on " + ENDPOINT + " for " + scopeId + ":" + String.valueOf(resourceId));
            _log.trace("call headers " + headers.toString());
            _log.trace("call payload " + json.toString());

            // fetch response as String, not used
            ResponseEntity<String> response = template.exchange(ENDPOINT, HttpMethod.POST, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                // ok, nothing to do with response
            } else {
                throw new WebhookException("response error code " + response.getStatusCode());
            }
        } catch (JSONException jex) {
            throw new WebhookException("json error " + jex.getMessage());
        } catch (RestClientException rex) {
            throw new WebhookException("rest error " + rex.getMessage());
        } catch (SignatureException sx) {
            throw new WebhookException("signature error " + sx.getMessage());
        }
    }

    /*
     * Helpers
     */
    private HttpHeaders headers() throws RestClientException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (authMode == AUTH_BASIC) {
            String auth = USERNAME + ":" + PASSWORD;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("UTF-8")));
            String authHeader = "Basic " + new String(encodedAuth);

            headers.set("Authorization", authHeader);

        } else if (authMode == AUTH_TOKEN) {
            // hardcoded "bearer" prefix
            // TODO make it configurable
            headers.set("Authorization", "Bearer " + TOKEN);

        }

        return headers;
    }
    // 签名认证
    private String sign(String content) throws SignatureException {
        try {
            // derive key spec from secret
            SecretKeySpec signingKey = new SecretKeySpec(SECRET.getBytes("UTF-8"), SIGN_ALG);
            // get default MAC for algo
            Mac mac = Mac.getInstance(SIGN_ALG);
            mac.init(signingKey);

            // compute hmac on input, single step
            // TODO iterate over blockSize for large messages
            byte[] hmac = mac.doFinal(content.getBytes("UTF-8"));

            // encode as base64 result
            return Base64.encodeBase64String(hmac);
        } catch (Exception e) {
            throw new SignatureException("HMAC error: " + e.getMessage());
        }
    }

    /*
     * Constants
     */
    public final static int AUTH_NONE = 0;
    public final static int AUTH_BASIC = 1;
    public final static int AUTH_TOKEN = 2;

    public final static int SIGN_NONE = 0;
    public final static int SIGN_PAYLOAD = 1;
//    public final static int SIGN_AWS = 2; //TODO

}
