package com.datasphere.server.connections.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.DelegatingConnection;
import org.apache.commons.lang3.StringUtils;

import com.datasphere.server.connections.dbutils.ConnectionFactory;
import com.datasphere.server.connections.utils.Assert;

public class ConnectionFactoryImpl implements ConnectionFactory {
	DataSource dataSource;
	Connection singleton;
	ThreadLocal<Connection> threadConnection = new ThreadLocal<Connection>();
	ThreadLocal<Integer> threadCount = new ThreadLocal<Integer>();
	
	public ConnectionFactoryImpl(String url, Boolean singleton) {
		Assert.isTrue(!StringUtils.isBlank(url) && singleton != null, "Illegal params!");
		if(singleton) {
			try {
				Class.forName("org.postgresql.Driver");
				this.singleton = DriverManager.getConnection(url);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			BasicDataSource dataSource = new BasicDataSource();
			dataSource.setUrl(url);
			dataSource.setDriverClassName("org.postgresql.Driver");
			dataSource.setMaxActive(50);
			dataSource.setUsername("postgres");
			dataSource.setPassword("DataCatalogCenter!123456");
			dataSource.setDefaultTransactionIsolation(2);
			this.dataSource = dataSource;
		}
	}
	
	public ConnectionFactoryImpl(DataSource dataSource) {
		Assert.isTrue(dataSource != null, "DataSource can't be null!");
		this.dataSource = dataSource;
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		Integer count = threadCount.get();
		if(count == null) {	// 第一次借用连接
			if(singleton != null) {
				threadConnection.set(singleton);
			} else if(dataSource != null) {
				threadConnection.set(dataSource.getConnection());
			} else {
				throw new RuntimeException("Unreachable code!");
			}
			threadCount.set(1);
		} else {	// 非第一次借用，计数+1
			threadCount.set(threadCount.get() + 1);
		}
		return threadConnection.get();
	}

	@Override
	public void returnConnection(Connection connection) {
		if(connection != null) {
			Integer count = threadCount.get();
			if(count == null) {
				throw new RuntimeException("Unreachable code!");
			}
			if(--count == 0) {	// 计数为0，归还连接
				threadCount.remove();
				threadConnection.remove();
			}
			if(connection instanceof Connection) {
				// Do none!
			} else if(connection instanceof DelegatingConnection) {
				try {
					connection.close();
				} catch (SQLException e) {
					// 归还连接失败，不作任何记录
				}
			}
		}
	}

	@Override
	public Connection getThreadConnection() {
		Connection conn = threadConnection.get();
		if(conn == null) {
			throw new RuntimeException("Has not allow connection!");
		}
		return conn;
	}
}
