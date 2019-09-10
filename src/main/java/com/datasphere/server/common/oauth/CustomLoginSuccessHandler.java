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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by aladin on 2019. 5. 13..
 */
public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomLoginSuccessHandler.class);

  private RequestCache requestCache = new HttpSessionRequestCache();

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request,
                                      HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {

    String accept = request.getHeader("accept");

    SavedRequest savedRequest = requestCache.getRequest(request, response);

    if (savedRequest == null) {
      super.onAuthenticationSuccess(request, response, authentication);

      return;
    }
    String targetUrlParameter = getTargetUrlParameter();
    if (isAlwaysUseDefaultTargetUrl()
            || (targetUrlParameter != null && org.springframework.util.StringUtils.hasText(request
            .getParameter(targetUrlParameter)))) {
      requestCache.removeRequest(request, response);
      super.onAuthenticationSuccess(request, response, authentication);

      return;
    }

    clearAuthenticationAttributes(request);

    // Use the DefaultSavedRequest URL
    String targetUrl = savedRequest.getRedirectUrl();

    // Ajax 타입의 호출일경우 targetUrl 정보만 넘겨줌
    // 현 로그인 페이지와 일치시키기 위함
    if (StringUtils.indexOf(accept, "json") > -1) {
      logger.debug("Send to DefaultSavedRequest Url: " + targetUrl);

      response.setContentType("application/json");
      response.setCharacterEncoding("utf-8");

      String data = "{ \"targetUrl\" : \"" + targetUrl + "\"}";

      PrintWriter out = response.getWriter();
      out.print(data);
      out.flush();
      out.close();

    } else {
      logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
      getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
  }

  @Override
  public void setRequestCache(RequestCache requestCache) {
    this.requestCache = requestCache;
  }

}
