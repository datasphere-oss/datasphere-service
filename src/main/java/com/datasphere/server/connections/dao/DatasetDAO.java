package com.datasphere.server.connections.dao;

import com.datasphere.common.data.Dataset;
import com.datasphere.resource.manager.module.dal.buscommon.dbutils.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatasetDAO {
	
	public void insert(Dataset dataset) throws SQLException ;
	
	public Boolean exists(String id) throws SQLException ;
	
	public int update(Dataset dataset) throws SQLException ;
	
	public int delete(String id) throws SQLException ;
	
	public Dataset get(String id) throws SQLException ;
//	public Dataset get(String id,Connection conn) throws SQLException ;
	
	public void setConnectionFactory(ConnectionFactory connectionFactory);
}
