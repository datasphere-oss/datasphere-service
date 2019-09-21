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

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import java.util.Arrays;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomSecurityContextLogoutHandler extends SecurityContextLogoutHandler {

  private boolean clearCookie = true;

  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    super.logout(request, response, authentication);

    // 在注销时删除cookie
    if(clearCookie){
    		// cookie包含登录口令, 登录口令类型, 刷新登录口令类型,登录用户ID,权限
      String[] cookies = new String[]{"LOGIN_TOKEN", "LOGIN_TOKEN_TYPE", "REFRESH_LOGIN_TOKEN", "LOGIN_USER_ID", "PERMISSION"};
      // 将cookie信息添加到响应中
      Arrays.stream(cookies).forEach(cookieName -> {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
      });
    }
  }

  public void setClearCookie(boolean clearCookie) {
    this.clearCookie = clearCookie;
  }
}
