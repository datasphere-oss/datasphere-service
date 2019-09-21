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

package com.datasphere.server.common.oauth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by aladin on 2019. 6. 12..
 */
@Component
@ConfigurationProperties(prefix = "datasphere.oauth2")
public class OauthProperties {

  String privateKey;

  String publicKey;

  List<Matcher> permitAll;

  public OauthProperties() {
  }

  public String getPrivateKey() {
    return privateKey;
  }

  public void setPrivateKey(String privateKey) {
    this.privateKey = privateKey;
  }

  public String getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }

  public List<Matcher> getPermitAll() {
    return permitAll;
  }

  public void setPermitAll(List<Matcher> permitAll) {
    this.permitAll = permitAll;
  }

  public static class Matcher {
    HttpMethod method;
    String api;

    public Matcher() {
    }

    public HttpMethod getMethod() {
      return method;
    }

    public void setMethod(HttpMethod method) {
      this.method = method;
    }

    public String getApi() {
      return api;
    }

    public void setApi(String api) {
      this.api = api;
    }
  }
}
