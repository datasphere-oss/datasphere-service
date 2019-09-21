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

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

/**
 * ClientAlreadyExistsException Class action caused by
 *
 * https://github.com/spring-projects/spring-security-oauth/issues/864
 *
 */
public class CustomJdbcClientDetailsServiceBuilder extends ClientDetailsServiceBuilder<CustomJdbcClientDetailsServiceBuilder>  {

  private Set<ClientDetails> clientDetails = new HashSet<ClientDetails>();

  private DataSource dataSource;

  private PasswordEncoder passwordEncoder; // for writing client secrets

  private JdbcClientDetailsService jdbcClientDetailsService;

  public CustomJdbcClientDetailsServiceBuilder dataSource(DataSource dataSource) {
    this.dataSource = dataSource;
    return this;
  }

  public CustomJdbcClientDetailsServiceBuilder passwordEncoder(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
    return this;
  }

  public CustomJdbcClientDetailsServiceBuilder jdbcClientDetailsService(JdbcClientDetailsService jdbcClientDetailsService) {
    this.jdbcClientDetailsService = jdbcClientDetailsService;
    return this;
  }

  @Override
  protected void addClient(String clientId, ClientDetails value) {
    clientDetails.add(value);
  }

  @Override
  protected ClientDetailsService performBuild() {
    //Assert.state(dataSource != null, "You need to provide a DataSource");
    Assert.state(jdbcClientDetailsService != null, "You need to provide a JdbcClientDetailsService");
    if (passwordEncoder != null) {
      // This is used to encode secrets as they are added to the database (if it isn't set then the user has top
      // pass in pre-encoded secrets)
      jdbcClientDetailsService.setPasswordEncoder(passwordEncoder);
    }

    for (ClientDetails client : clientDetails) {
      try {
        jdbcClientDetailsService.updateClientDetails(client);
      } catch (NoSuchClientException e) {
        jdbcClientDetailsService.addClientDetails(client);
      }
    }
    return jdbcClientDetailsService;
  }
}
