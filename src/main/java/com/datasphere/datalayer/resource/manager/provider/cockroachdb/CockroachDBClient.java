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

package com.datasphere.datalayer.resource.manager.provider.cockroachdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class CockroachDBClient {

    private final String HOST;
    private final int PORT;
    private final String USERNAME;
    private final String PASSWORD;
    private final boolean SSL;

    public CockroachDBClient(String host, int port, boolean ssl, String username, String password) {
        super();
        HOST = host;
        PORT = port;
        USERNAME = username;
        PASSWORD = password;
        SSL = ssl;
    }

    private Connection connect(boolean readonly) throws SQLException {
        String url = "jdbc:postgresql://" + HOST + ":" + String.valueOf(PORT) + "/";
        Properties props = new Properties();
        props.setProperty("user", USERNAME);
        props.setProperty("password", PASSWORD);
        props.setProperty("ssl", String.valueOf(SSL));
        // set read-only if required by operation
        props.setProperty("readOnly", String.valueOf(readonly));

        Connection conn = DriverManager.getConnection(url, props);

        return conn;
    }

    public boolean ping() {
        try {
            Connection conn = connect(true);
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
        boolean ret = false;

        Connection conn = connect(true);

        // can not use prepared statements here
        Statement stmt = conn.createStatement();

        // no validation
        ResultSet result = stmt.executeQuery("SHOW DATABASES");
        while (result.next()) {
            // database names as result rows
            // index of first column is 1
            String db = result.getString(1);
            if (db.equalsIgnoreCase(name)) {
                // match
                ret = true;
                break;
            }
        }

        result.close();
        stmt.close();
        conn.close();

        return ret;
    }

    public void createDatabase(String name) throws SQLException {
        Connection conn = connect(false);

        // can not use prepared statements here
        Statement stmt = conn.createStatement();

        // no validation
        stmt.execute("CREATE DATABASE " + name);

        stmt.close();
        conn.close();
    }

    public void deleteDatabase(String name) throws SQLException {
        Connection conn = connect(false);

        // can not use prepared statements here
        Statement stmt = conn.createStatement();

        // no validation - will give error if connection active
        stmt.execute("DROP DATABASE IF EXISTS " + name);

        stmt.close();
        conn.close();
    }

    public void createUser(String database, String username, String password) throws SQLException {
        Connection conn = connect(false);

        // no validation
        // create user
        // can not use prepared statements here
        Statement create = conn.createStatement();
        create.execute("CREATE USER " + username + " WITH PASSWORD '" + password + "'");
        create.close();

        // grant privileges
        // can not use prepared statements here
        Statement grant = conn.createStatement();
        grant.execute("GRANT ALL ON DATABASE " + database + " TO " + username);
        grant.close();

        conn.close();
    }

    public void deleteUser(String database, String username) throws SQLException {
        Connection conn = connect(false);

        // can not use prepared statements here
        Statement revoke = conn.createStatement();

        // no validation - drop privileges
        revoke.execute("REVOKE ALL ON DATABASE " + database + " FROM " + username);
        revoke.close();

        // can not use prepared statements here
        Statement drop = conn.createStatement();

        // no validation
        drop.execute("DROP USER IF EXISTS " + username);
        drop.close();

        conn.close();

    }

}
