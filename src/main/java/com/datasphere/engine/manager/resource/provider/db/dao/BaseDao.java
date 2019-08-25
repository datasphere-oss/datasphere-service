package com.datasphere.engine.manager.resource.provider.db.dao;

import org.apache.commons.lang3.StringUtils;

import com.datasphere.engine.manager.resource.provider.config.DBConfig;
import com.datasphere.engine.manager.resource.provider.db.model.DBTableField;
import com.datasphere.engine.manager.resource.provider.db.util.DALTypeUtil;

import java.sql.*;
import java.util.*;

public class BaseDao<T extends DBConfig> {
	
	Connection connection;
	
	T config;
	
	static final String CONNECT_FAILED_CODE = "DSDB";
	
	public T getConfig() {
		return config;
	}

	public void setConfig(T config) {
		this.config = config;
	}
	
	public String[] query(String databaseName,String sql,String[] params) throws SQLException{
		Connection conn = getConnection();
		if(!StringUtils.isEmpty(databaseName)){
			conn.setCatalog(databaseName);
		}
		
		try(PreparedStatement pst = conn.prepareStatement(sql)){
			if(params != null){
				for(int i=0;i<params.length;i++){
					pst.setString(i+1, params[i]);
				}
			}
			List<String> dbList = new LinkedList<>();
			ResultSet set = pst.executeQuery();
			while(set.next()) {
				dbList.add(set.getString(1));
			}
			return dbList.toArray(new String[dbList.size()]);
		}
	}
	
	public Map<String,Integer> readTableMetaType(String databaseName,String tableName) throws SQLException{
		String sql = "SELECT * FROM "+tableName + " WHERE 1 = 0";
		Connection conn = getConnection();
		if(!StringUtils.isEmpty(databaseName)){
			conn.setCatalog(databaseName);
		}
		try(PreparedStatement pst = conn.prepareStatement(sql);){
			ResultSet resultSet = pst.executeQuery();
	        ResultSetMetaData metaData = resultSet.getMetaData();
	        int colCount = metaData.getColumnCount();
	        Map<String,Integer> typeMap = new HashMap<>();
	        for(int i=0;i<colCount;i++){
	        	String colName = metaData.getColumnName(i+1);
	        	int colType = metaData.getColumnType(i+1);
	        	typeMap.put(colName, colType);
	        }
	    	return typeMap;
		}

	}
	
	public int readTableRowcount(String databaseName,String tableName) throws SQLException{
		String sql = "SELECT COUNT(1) FROM "+ tableName + "";
		Connection conn = getConnection();
		if(!StringUtils.isEmpty(databaseName)){
			conn.setCatalog(databaseName);
		}
		try(PreparedStatement pst = conn.prepareStatement(sql)){
			ResultSet set = pst.executeQuery();
			set.next();
			return set.getInt(1);
		}
	}
	
	public List<List<DBTableField>> readTable(String databaseName, String tableName) throws SQLException{
		String sql = "SELECT * FROM "+tableName;
		Connection conn = getConnection();
		if(!StringUtils.isEmpty(databaseName)){
			conn.setCatalog(databaseName);
		}
		try(PreparedStatement pst = conn.prepareStatement(sql);){
			List<List<DBTableField>> list = new ArrayList<>();
			ResultSet resultSet = pst.executeQuery();
	        ResultSetMetaData metaData = resultSet.getMetaData();
	        int colCount = metaData.getColumnCount();
	        while (resultSet.next()) {  
	        	List<DBTableField> fieldList = new LinkedList<>();
	            for (int i = 0; i < colCount; i++) {
	            	int type = metaData.getColumnType(i + 1);
	            	if(DALTypeUtil.isSupport(type)){
	            		DBTableField field = new DBTableField();
	            		String colName = metaData.getColumnName(i + 1);
	            		String colValue = DALTypeUtil.getResultSetValue(resultSet, type, i + 1);
	            		int colType = metaData.getColumnType(i + 1);
	            		field.setName(colName);
	            		field.setValue(colValue);
	            		field.setType(colType);
	            		field.setBusinessDataType(DALTypeUtil.convertBusinessType(colType));
	            		fieldList.add(field);
	            	}
	            }
	            list.add(fieldList);
	        }
	    	return list;
		}
		
	}
	
	public Map<String,Integer> readTableColumnByResultSet(ResultSet resultSet,List<String> filter) throws SQLException{
		ResultSetMetaData metaData = resultSet.getMetaData();
		Map<String,Integer> typeMap = new HashMap<>();
		int colCount = metaData.getColumnCount();
		for (int i = 0; i < colCount; i++) {
			String colName = metaData.getColumnName(i + 1);
			if (filter != null && filter.contains(colName)) {
				continue;
			}
			int type = metaData.getColumnType(i + 1);
			typeMap.put(colName, type);
		}
		return typeMap;
	}
	
	public Map<String,String> readTableByResultSet(ResultSet resultSet,List<String> filter) throws SQLException{
		ResultSetMetaData metaData = resultSet.getMetaData();
		Map<String, String> rowMap = new HashMap<>();
		int colCount = metaData.getColumnCount();
		for (int i = 0; i < colCount; i++) {
			String colName = metaData.getColumnName(i + 1);
			if (filter != null && filter.contains(colName)) {
				continue;
			}
			int type = metaData.getColumnType(i + 1);
			String colValue = DALTypeUtil.getResultSetValue(resultSet, type, i + 1);
			rowMap.put(colName, colValue);
		}
		return rowMap;
	}
	
	
	public Connection getConnection() throws SQLException{
		
		if(connection == null)
			throw new SQLException("connect failed!",CONNECT_FAILED_CODE);
		return connection;
	}
	
	public void closeConnection(){
		if(connection != null){
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				connection = null;
			}
		}
	}
	
}
