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

package com.datasphere.datalayer.resource.manager.provider.hashdata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class PostgresSqlClient {

	private final String HOST;
	private final int PORT;
	private final String USERNAME;
	private final String PASSWORD;
	private final boolean SSL;

	// postgres system schema
	// required for JDBC connection
	private final static String DB = "postgres";

	public PostgresSqlClient(String host, int port, boolean ssl, String username, String password) {
		super();
		HOST = host;
		PORT = port;
		USERNAME = username;
		PASSWORD = password;
		SSL = ssl;
	}

	private Connection connect(String database, boolean readonly) throws SQLException {
		// required DB NAME to successfully connect via JDBC
		// use internal "postgres" if needed
		String url = "jdbc:postgresql://" + HOST + ":" + String.valueOf(PORT) + "/" + database;
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
			// use internal "postgres" db
			Connection conn = connect(DB, true);
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
		// use internal "postgres" db
		Connection conn = connect(DB, true);

		PreparedStatement stmt = conn.prepareStatement("SELECT 1 AS EXISTS from pg_database WHERE datname = ?");
		// no validation
		stmt.setObject(1, name);
		ResultSet result = stmt.executeQuery();
		// if query has one row, db exists
		boolean ret = result.next();

		result.close();
		stmt.close();
		conn.close();

		return ret;
	}

	public void createDatabase(String name) throws SQLException {
		// use internal "postgres" db
		Connection conn = connect(DB, false);

		// can not use prepared statements here
		Statement stmt = conn.createStatement();

		// no validation
		stmt.execute("CREATE DATABASE " + name);

		stmt.close();
		conn.close();
	}

	public void deleteDatabase(String name) throws SQLException {
		// use internal "postgres" db
		Connection conn = connect(DB, false);

		// can not use prepared statements here
		Statement stmt = conn.createStatement();

		// no validation - wil give error if connection active
		stmt.execute("DROP DATABASE IF EXISTS " + name);

		stmt.close();
		conn.close();
	}

	public void createUser(String database, String username, String password) throws SQLException {
		// use provided db - must exist
		Connection conn = connect(database, false);

		// no validation
		// create user (role)
		// can not use prepared statements here
		Statement create = conn.createStatement();
		create.execute("CREATE ROLE " + username + " WITH LOGIN");
		create.close();

		// set password
		// can not use prepared statements here
		Statement alter = conn.createStatement();
		alter.execute("ALTER ROLE " + username + " WITH PASSWORD '" + password + "'");
		alter.close();

		// grant privileges
		// can not use prepared statements here
		Statement grant = conn.createStatement();
		grant.execute("GRANT ALL ON DATABASE " + database + " TO " + username);
		grant.close();

		conn.close();
	}

	public void deleteUser(String database, String username) throws SQLException {
		// use provided db - must exist
		Connection conn = connect(database, false);

		// can not use prepared statements here
		Statement owned = conn.createStatement();

		// no validation - drop ALL objects with cascade
		owned.execute("DROP OWNED BY " + username + " CASCADE");
		owned.close();

		// can not use prepared statements here
		Statement drop = conn.createStatement();

		// no validation
		drop.execute("DROP USER IF EXISTS " + username);
		drop.close();

		conn.close();

	}

}
