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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextImpl;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.datasphere.server.domain.user.User;

public class SessionStatusListener implements HttpSessionListener {

  @Override
  public void sessionCreated(HttpSessionEvent httpSessionEvent) {
  }

  @Override
  public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

    HttpSession session = httpSessionEvent.getSession();
    Object context;
    try {
      context = session.getAttribute("SPRING_SECURITY_CONTEXT");
    } catch (Exception e) {
      // TODO: ExpiringSessionHttpSession My session is again There is no way to check for null. Like a bug. Write with a custom adapter
      return;
    }

    if (context == null) {
      return;
    }

    SecurityContextImpl springSecurityContext = (SecurityContextImpl) context;
    String sessionType = StringUtils.defaultString((String) session.getAttribute("sessionType"));
    if (springSecurityContext != null && !"logout".equals(sessionType)) {
      Authentication authentication = springSecurityContext.getAuthentication();
      User user = (User) authentication.getPrincipal();
      String name = user.getId();
      System.out.println("session expired id=>" + name);
      try {
//        queryEditorService.getUserConnClose(name);
//        queryEditorService.removeUserConnList(name);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
