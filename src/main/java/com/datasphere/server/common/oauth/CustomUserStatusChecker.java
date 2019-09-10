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

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

import com.datasphere.server.domain.user.User;

/**
 * Created by aladin on 2019. 2. 19..
 */
public class CustomUserStatusChecker implements UserDetailsChecker {

  private MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

  @Override
  public void check(UserDetails toCheck) {

    User user = (User) toCheck;

    if (!user.isAccountNonLocked()) {
      throw new LockedException(messages.getMessage(
          "AccountStatusUserDetailsChecker.locked", "User account is locked"));
    }

    if (!user.isAccountNonExpired()) {
      throw new AccountExpiredException(
          messages.getMessage("AccountStatusUserDetailsChecker.expired",
              "User account has expired"));
    }

    if (!user.isCredentialsNonExpired()) {
      throw new CredentialsExpiredException(messages.getMessage(
          "AccountStatusUserDetailsChecker.credentialsExpired",
          "User credentials have expired"));
    }

    // Check Custom Status
    User.Status status = user.getStatus();

    switch (status) {
      case REQUESTED:
        throw new RequestStatusException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.userRequested",
            "Your username is being approved by system administrator. Pleases wait a minute"));
      case EXPIRED:
      case LOCKED:
      case REJECTED:
      case DELETED:
        throw new InactivatedStatusException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.userInactivated",
            "Username is not available. Please contact your system administrator separately."));
    }
  }

  public void setMessageSource(MessageSource messageSource) {
    this.messages = new MessageSourceAccessor(messageSource);
  }
}
