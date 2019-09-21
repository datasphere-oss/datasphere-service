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

import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.providers.ExpiringUsernameAuthenticationToken;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.datasphere.server.util.AuthUtils;

public class SAMLAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(SAMLAuthenticationSuccessHandler.class);
  
  @Autowired
  JwtAccessTokenConverter jwtAccessTokenConverter;

  @Autowired
  DefaultTokenServices defaultTokenServices;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
    LOGGER.debug("authentication = {}", authentication);

    String userName = null;
    try{
      SAMLAuthenticationInfo samlAuthenticationInfo = new SAMLAuthenticationInfo(authentication);
      LOGGER.debug(samlAuthenticationInfo.toString());
      userName = samlAuthenticationInfo.getGeneral().getName();
    }catch (MessageEncodingException e){

    }

    //1. UsernamePasswordAuthenticationToken -> OAuth2Authentication
    ExpiringUsernameAuthenticationToken token = (ExpiringUsernameAuthenticationToken) authentication;

    HashMap<String, String> authorizationParameters = new HashMap<String, String>();
    authorizationParameters.put("scope", "read");
    authorizationParameters.put("username", "user");
    authorizationParameters.put("client_id", "client_id");
    authorizationParameters.put("grant", "password");

    Set<String> responseType = new HashSet<String>();
    responseType.add("password");

    Set<String> scopes = new HashSet<String>();
    scopes.add("read");
    scopes.add("write");

    OAuth2Request authorizationRequest = new OAuth2Request(
            authorizationParameters, "datasphere_client",
            token.getAuthorities(), true, scopes, null, "",
            responseType, null);

    OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(authorizationRequest, token);
    oAuth2Authentication.setAuthenticated(true);

    //2. Create OAuth2 Token (converted JWT Token via JWTTokenEnhancer)
    OAuth2AccessToken oAuth2AccessToken = defaultTokenServices.createAccessToken(oAuth2Authentication);
    LOGGER.debug("oAuth2AccessToken = " + oAuth2AccessToken);
    // Generate Security Context to get auth information
    SecurityContext context = SecurityContextHolder.getContext();
    Authentication auth = context.getAuthentication();
    List<String> permissions = AuthUtils.getPermissions();
    LOGGER.debug("permissions = " + permissions);

    //3. Write Cookie
    getResponse(oAuth2AccessToken, response, userName, permissions);

    super.onAuthenticationSuccess(request, response, authentication);
  }

  private void getResponse(OAuth2AccessToken accessToken, HttpServletResponse response, String userName, List<String> permissions) {
    // 添加 "LOGIN_TOKEN" cookie
	Cookie cookie = new Cookie("LOGIN_TOKEN", accessToken.getValue());
    cookie.setPath("/");
    cookie.setMaxAge(60*60*24) ;
    response.addCookie(cookie);
    // 添加 "LOGIN_TOKEN_TYPE" cookie
    cookie = new Cookie("LOGIN_TOKEN_TYPE", accessToken.getTokenType());
    cookie.setPath("/");
    cookie.setMaxAge(60*60*24) ;
    response.addCookie(cookie);
    // 添加 "REFRESH_LOGIN_TOKEN" cookie
    cookie = new Cookie("REFRESH_LOGIN_TOKEN", accessToken.getRefreshToken().getValue());
    cookie.setPath("/");
    cookie.setMaxAge(60*60*24);
    response.addCookie(cookie);
    // 添加 "LOGIN_USER_ID" cookie
    cookie = new Cookie("LOGIN_USER_ID", userName);
    cookie.setPath("/");
    cookie.setMaxAge(60*60*24) ;
    response.addCookie(cookie);
    // 添加 "PERMISSION" cookie
    cookie = new Cookie( "PERMISSION", String.join( "==", permissions ) );
    cookie.setPath("/");
    cookie.setMaxAge(60*60*24) ;
    response.addCookie(cookie);

    response.setHeader("P3P","CP=\"IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT\"");


  }
}
