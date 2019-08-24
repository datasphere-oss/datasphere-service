package com.datasphere.server.connections.dbutils;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionFactory {
	
	public Connection getConnection() throws SQLException ;
	
	public void returnConnection(Connection conn);
	
	public Connection getThreadConnection();
}
