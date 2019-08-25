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

package com.datasphere.server.connections.jdbc.connector;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.datasphere.server.connections.jdbc.JdbcConnectInformation;
import com.datasphere.server.connections.jdbc.dialect.JdbcDialect;


/**
 * The interface Jdbc connector.
 */
public interface JdbcConnector {

  /**
   * Gets driver.
   *
   * @param connectionUrl   the connection url
   * @param driverClassName the driver class name
   * @return the driver
   * @throws SQLException the sql exception
   */
  Driver getDriver(String connectionUrl, String driverClassName) throws SQLException;

  /**
   * Gets connection.
   *
   * @param connection      the connection
   * @param jdbcDialect     the jdbc dialect
   * @param database        the database
   * @param includeDatabase the include database
   * @return the connection
   */
  Connection getConnection(JdbcConnectInformation connection, JdbcDialect jdbcDialect, String database, boolean includeDatabase);

  /**
   * Gets connection.
   *
   * @param connection      the connection
   * @param jdbcDialect     the jdbc dialect
   * @param database        the database
   * @param includeDatabase the include database
   * @param username        the username
   * @param password        the password
   * @return the connection
   */
  Connection getConnection(JdbcConnectInformation connection, JdbcDialect jdbcDialect, String database, boolean includeDatabase, String username, String password);

  /**
   * Gets connection.
   *
   * @param connectionUrl   the connection url
   * @param properties      the properties
   * @param driverClassName the driver class name
   * @return the connection
   * @throws SQLException the sql exception
   */
  Connection getConnection(String connectionUrl, Properties properties, String driverClassName) throws SQLException;

  /**
   * Close connection.
   *
   * @param connection the connection
   * @param stmt       the stmt
   * @param rs         the rs
   */
  void closeConnection(Connection connection, Statement stmt, ResultSet rs);
}
