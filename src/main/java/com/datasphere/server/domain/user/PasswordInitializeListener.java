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

package com.datasphere.server.domain.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Component
public class PasswordInitializeListener implements ApplicationListener<ApplicationReadyEvent>{

  private static final Logger LOGGER = LoggerFactory.getLogger(PasswordInitializeListener.class);

  @Autowired
  UserRepository userRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  private Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    Environment environment = event.getApplicationContext().getEnvironment();
    if(environment.acceptsProfiles("encode-password-initial")){
      LOGGER.debug("Password Initialize profile is active");
      LOGGER.debug("encrypt User Password All..");

      List<User> userList = userRepository.findAll();
      userList.stream().forEach(user -> {
        String userPassword = user.getPassword();
        if (!BCRYPT_PATTERN.matcher(userPassword).matches()) {
          String encodedPassword = passwordEncoder.encode(user.getPassword());
          LOGGER.debug("User {}'s password : {} -> {}",user.getUsername(), user.getPassword(), encodedPassword);
          user.setPassword(encodedPassword);
        } else {
          LOGGER.debug("User {}'s password already encoded : {}",user.getUsername(), user.getPassword());
        }
      });

      userRepository.save(userList);
      userRepository.flush();

    } else {
      LOGGER.debug("Password Initialize profile is not active");
    }
  }
}
