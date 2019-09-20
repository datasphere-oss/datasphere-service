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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.datasphere.engine.common.exception.GlobalErrorCodes;
import com.datasphere.server.common.GlobalObjectMapper;
import com.datasphere.server.common.exception.ErrorResponse;
import com.datasphere.server.util.AuthUtils;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  private static Logger LOGGER = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

  @Override
  public void handle(HttpServletRequest request,
                     HttpServletResponse response,
                     AccessDeniedException accessDeniedException) throws IOException, ServletException {

    String message = String.format("User(%s) attempted to access the protected URI: %s", AuthUtils.getAuthUserName(), request.getRequestURI());

    LOGGER.warn(message);

    if("text/html".equals(request.getContentType())) {
      //response.sendRedirect(request.getContextPath() + "/accessDenied");
      response.getWriter().println("TBD");
    } else {
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      ErrorResponse errorResponse = new ErrorResponse(GlobalErrorCodes.ACCESS_DENIED_CODE,
                                                      "Access denied.",
                                                      message);
      response.getWriter().print(GlobalObjectMapper.writeValueAsString(errorResponse));
    }
  }
}
