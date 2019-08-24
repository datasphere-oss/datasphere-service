package com.datasphere.server.connections.dao;


import com.datasphere.resource.manager.module.dal.domain.TableMetaData;
import com.datasphere.resource.manager.module.dal.domain.TableQuery;
import com.datasphere.common.dmpbase.data.Column;
import com.datasphere.resource.manager.module.dal.buscommon.dbutils.ConnectionFactory;

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
