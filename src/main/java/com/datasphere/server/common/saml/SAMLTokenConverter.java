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

import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.saml.SAMLCredential;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SAMLTokenConverter extends JwtAccessTokenConverter {
  private static final Logger LOGGER = LoggerFactory.getLogger(SAMLTokenConverter.class);

  @Override
  public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

    // 将SAMLCredential信息添加到令牌
    if(accessToken instanceof DefaultOAuth2AccessToken){
      LOGGER.debug("accessToken : {}", accessToken);
      Authentication userAuthentication = authentication.getUserAuthentication();
      if(userAuthentication != null){
        if(userAuthentication.getCredentials() != null && userAuthentication.getCredentials() instanceof SAMLCredential){
          try{

            SAMLAuthenticationInfo samlAuthenticationInfo = new SAMLAuthenticationInfo(userAuthentication);
            SAMLCredential samlCredential = (SAMLCredential) userAuthentication.getCredentials();

            Map<String, Object> samlAdditionalMap = new HashMap<>();

            Map<String, Object> attributeMap = new HashMap<>();
            for(Map attrMap : samlAuthenticationInfo.getAttributes()){
              attributeMap.put((String) attrMap.get("name"), attrMap.get("value"));
            }
            samlAdditionalMap.put("attribute", attributeMap);

            Map<String, Object> credentialMap = new HashMap<>();
            credentialMap.put("remoteEntityID", samlCredential.getRemoteEntityID());
            credentialMap.put("localEntityID", samlCredential.getLocalEntityID());

            List<String> sessionIndexs = new ArrayList<>();
            for (AuthnStatement statement : samlCredential.getAuthenticationAssertion().getAuthnStatements()) {
              sessionIndexs.add(statement.getSessionIndex());
            }
            credentialMap.put("sessionIndexs", sessionIndexs);

            HashMap<String, String> nameIDMap = new HashMap<>();
            nameIDMap.put("Format", samlCredential.getNameID().getFormat());
            nameIDMap.put("NameQualifier", samlCredential.getNameID().getNameQualifier());
            nameIDMap.put("SPNameQualifier", samlCredential.getNameID().getSPNameQualifier());
            nameIDMap.put("SPProvidedID", samlCredential.getNameID().getSPProvidedID());
            nameIDMap.put("Value", samlCredential.getNameID().getValue());
            credentialMap.put("nameID", nameIDMap);

//            samlAdditionalMap.put("credential", credentialMap);

            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(samlAdditionalMap);
          } catch (MessageEncodingException e){
            e.printStackTrace();
          }
        }
      }
    }

    return super.enhance(accessToken, authentication);
  }
}
