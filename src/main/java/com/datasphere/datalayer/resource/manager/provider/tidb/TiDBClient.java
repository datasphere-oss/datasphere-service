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

package com.datasphere.datalayer.resource.manager.provider.tidb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * Connect to TiDB via MySQL jdbc 
 */

public class TiDBClient {

    private final String HOST;
    private final int PORT;
    private final String USERNAME;
    private final String PASSWORD;

    public TiDBClient(String host, int port, String username, String password) {
        super();
        HOST = host;
        PORT = port;
        USERNAME = username;
        PASSWORD = password;
    }

    private Connection connect() throws SQLException {
        // no need to connect to a specific db to successfully connect via JDBC
        String url = "jdbc:mysql://" + HOST + ":" + String.valueOf(PORT);
        // append user
        url += "/?user=" + USERNAME + "&password=" + PASSWORD;

        Connection conn = DriverManager.getConnection(url);

        return conn;
    }

    public boolean ping() {
        try {
            Connection conn = connect();
            // 1-second timeout for check
            boolean ret = conn.isValid(1);
            conn.close();
            return ret;
        } catch (Exception ex) {
            // log error?
            return false;
        }
    }

    public boolean hasDatabase(String name) throws SQLException {
        Connection conn = connect();

        // TODO test via cluster deployment if query works
        PreparedStatement stmt = conn
                .prepareStatement("SELECT COUNT(*) AS c FROM information_schema.schemata WHERE schema_name = ?");

        // no validation
        stmt.setObject(1, name);
        ResultSet result = stmt.executeQuery();
        // query always has 1 row for count
        result.next();
        boolean ret = result.getInt("c") > 0;

        result.close();
        stmt.close();
        conn.close();

        return ret;
    }

    public void createDatabase(String name) throws SQLException {
        Connection conn = connect();

        // can not use prepared statements here
        Statement stmt = conn.createStatement();

        // no validation
        stmt.executeUpdate("CREATE DATABASE " + name);

        stmt.close();
        conn.close();
    }

    public void deleteDatabase(String name) throws SQLException {
        Connection conn = connect();

        // can not use prepared statements here
        Statement stmt = conn.createStatement();

        // no validation - wil give error if connection active
        stmt.execute("DROP DATABASE IF EXISTS " + name);

        stmt.close();
        conn.close();
    }

    public void createUser(String database, String username, String password) throws SQLException {
        Connection conn = connect();

        // no validation
        // create user
        // can not use prepared statements here
        Statement create = conn.createStatement();
        create.execute("CREATE USER " + username + " IDENTIFIED BY '" + password + "'");
        create.close();

        // grant privileges
        // can not use prepared statements here
        Statement grant = conn.createStatement();
        grant.execute("GRANT ALL ON " + database + ".* TO " + username);
        grant.close();

        conn.close();
    }

    public void deleteUser(String database, String username) throws SQLException {
        Connection conn = connect();

        // can not use prepared statements here
        Statement drop = conn.createStatement();

        // no validation
        drop.execute("DROP USER IF EXISTS " + username);
        drop.close();

        conn.close();

    }
}
