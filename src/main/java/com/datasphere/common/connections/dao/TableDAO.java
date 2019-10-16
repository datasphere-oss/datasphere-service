package com.datasphere.common.connections.dao;


import com.datasphere.common.data.Column;
import com.datasphere.engine.datasource.connections.dbutils.ConnectionFactory;
import com.datasphere.engine.datasource.connections.model.TableMetaData;
import com.datasphere.engine.datasource.connections.model.TableQuery;

import java.sql.SQLException;

public interface TableDAO {

	public void createTable(TableMetaData metadata) throws SQLException;
	
	public void append(TableMetaData metadata) throws SQLException;
	
	public void deleteTable(String name) throws SQLException;
	
	public long getTableVolume(String tableName) throws SQLException;
	
	public long count(String tableName) throws SQLException;
	
	public String[][] getData(TableQuery query) throws SQLException;
	
	public void setConnectionFactory(ConnectionFactory connectionFactory);
	
	public void update(TableMetaData metadata) throws SQLException;
	
	public void copy(String oldTableName, String newTableName, Column[] columns)throws SQLException;
	
	public void dropColumn(String tableName, String ColumnName)throws SQLException;
}
