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

package com.datasphere.server.common.saml;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "polaris.saml")
public class SAMLProperties {
  List<SamlMetadata> idp;
  String entityId;
  List<String> idpName;
  String entityBaseUrl;
  boolean requestSigned;
  SamlContext samlContext;
  String userMapperClass;

  public List<SamlMetadata> getIdp() {
    return idp;
  }

  public void setIdp(List<SamlMetadata> idp) {
    this.idp = idp;
  }

  public List<String> getIdpName() {
    return idpName;
  }

  public void setIdpName(List<String> idpName) {
    this.idpName = idpName;
  }

  public String getEntityId() {
    return entityId;
  }

  public void setEntityId(String entityId) {
    this.entityId = entityId;
  }

  public String getEntityBaseUrl() {
    return entityBaseUrl;
  }

  public void setEntityBaseUrl(String entityBaseUrl) {
    this.entityBaseUrl = entityBaseUrl;
  }

  public boolean isRequestSigned() {
    return requestSigned;
  }

  public void setRequestSigned(boolean requestSigned) {
    this.requestSigned = requestSigned;
  }

  public SamlContext getSamlContext() {
    return samlContext;
  }

  public void setSamlContext(SamlContext samlContext) {
    this.samlContext = samlContext;
  }

  public String getUserMapperClass() {
    return userMapperClass;
  }

  public void setUserMapperClass(String userMapperClass) {
    this.userMapperClass = userMapperClass;
  }

  public static class SamlMetadata{
    String name;
    String type;
    String url;
    boolean metadataTrustCheck;
    boolean metadataRequireSignature;

    public SamlMetadata() {
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public String getUrl() {
      return url;
    }

    public boolean isMetadataTrustCheck() {
      return metadataTrustCheck;
    }

    public void setMetadataTrustCheck(boolean metadataTrustCheck) {
      this.metadataTrustCheck = metadataTrustCheck;
    }

    public boolean isMetadataRequireSignature() {
      return metadataRequireSignature;
    }

    public void setMetadataRequireSignature(boolean metadataRequireSignature) {
      this.metadataRequireSignature = metadataRequireSignature;
    }

    public void setUrl(String url) {
      this.url = url;
    }
    
    
  }

  public static class SamlContext{
    //https://docs.spring.io/autorepo/docs/spring-security-saml/1.0.x/reference/html/configuration-advanced.html
    String serverName;
    String scheme;
    int serverPort;
    boolean includeServerPortInRequestURL;
    String contextPath;

    public SamlContext() {
    }

    public String getServerName() {
      return serverName;
    }

    public void setServerName(String serverName) {
      this.serverName = serverName;
    }

    public String getScheme() {
      return scheme;
    }

    public void setScheme(String scheme) {
      this.scheme = scheme;
    }

    public int getServerPort() {
      return serverPort;
    }

    public void setServerPort(int serverPort) {
      this.serverPort = serverPort;
    }

    public boolean isIncludeServerPortInRequestURL() {
      return includeServerPortInRequestURL;
    }

    public void setIncludeServerPortInRequestURL(boolean includeServerPortInRequestURL) {
      this.includeServerPortInRequestURL = includeServerPortInRequestURL;
    }

    public String getContextPath() {
      return contextPath;
    }

    public void setContextPath(String contextPath) {
      this.contextPath = contextPath;
    }
  }
}
