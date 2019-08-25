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

package com.datasphere.server.connections.jdbc;

import java.util.Map;

/**
 * The interface Jdbc connect information.
 */
public interface JdbcConnectInformation {
  /**
   * Gets authentication type.
   *
   * @return the authentication type
   */
  AuthenticationType getAuthenticationType();

  /**
   * Gets implementor.
   *
   * @return the implementor
   */
  String getImplementor();

  /**
   * Gets url.
   *
   * @return the url
   */
  String getUrl();

  /**
   * Gets options.
   *
   * @return the options
   */
  String getOptions();

  /**
   * Gets hostname.
   *
   * @return the hostname
   */
  String getHostname();

  /**
   * Gets port.
   *
   * @return the port
   */
  Integer getPort();

  /**
   * Gets database.
   *
   * @return the database
   */
  String getDatabase();

  /**
   * Gets sid.
   *
   * @return the sid
   */
  String getSid();

  /**
   * Gets catalog.
   *
   * @return the catalog
   */
  String getCatalog();

  /**
   * Gets properties.
   *
   * @return the properties
   */
  String getProperties();

  /**
   * Gets username.
   *
   * @return the username
   */
  String getUsername();

  /**
   * Gets password.
   *
   * @return the password
   */
  String getPassword();

  /**
   * Gets properties map.
   *
   * @return the properties map
   */
  Map<String, String> getPropertiesMap();

  /**
   * The enum Authentication type.
   */
  enum AuthenticationType {
    /**
     * Manual authentication type.
     */
    MANUAL,
    /**
     * Userinfo authentication type.
     */
    USERINFO,
    /**
     * Dialog authentication type.
     */
    DIALOG
  }
}
