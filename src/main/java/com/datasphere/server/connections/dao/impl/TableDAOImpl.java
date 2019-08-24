package com.datasphere.server.connections.dao.impl;

import com.datasphere.resource.manager.module.dal.dao.TableDAO;
import com.datasphere.resource.manager.module.dal.domain.Pager;
import com.datasphere.resource.manager.module.dal.domain.TableMetaData;
import com.datasphere.resource.manager.module.dal.domain.TableQuery;
import com.datasphere.resource.manager.module.dal.service.DataAccessor;
import com.datasphere.common.dmpbase.data.Column;
import com.datasphere.resource.manager.module.dal.buscommon.dbutils.ConnectionFactory;
import com.datasphere.resource.manager.module.dal.buscommon.dbutils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class TableDAOImpl implements TableDAO {
	//	public static final String schemaName = "APP";
	public static final String schemaName = "public";
	public static final Integer VARCHAR_DEFAULT_LENGTH = 32672;
	
	ConnectionFactory connectionFactory;

	@Override
	public void createTable(TableMetaData metadata) throws SQLException {
		Connection conn = connectionFactory.getThreadConnection();
		StringBuffer sb = new StringBuffer();
		sb.append("create table ");
		sb.append(metadata.getTableName());
		sb.append("(id___system  integer  NOT NULL generated always as identity primary key,");//lim:增加主键
		for(Column col: metadata.getColumns()) {
			sb.append(col.getName());
			sb.append(" varchar(" + VARCHAR_DEFAULT_LENGTH + ")");
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(") PERSISTENT ASYNCHRONOUS");
		try(Statement st = conn.createStatement()) {
			st.executeUpdate(sb.toString());
		}
		String[][]data=metadata.getData();
		if(data==null||data.length==0){
			return;//如果数据集为空返回
		}
		sb.delete(0, sb.length());
		sb.append("insert into ");
		sb.append(metadata.getTableName());
		sb.append("(");
		for(int index = 0; index < metadata.getColumns().length; index++) {
			sb.append(metadata.getColumns()[index].getName()+",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");
		
		sb.append(" values(");//lim增加主键
		for(int index = 0; index < metadata.getColumns().length; index++) {
			sb.append("?,");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");
		try(PreparedStatement pst = conn.prepareStatement(sb.toString())) {
			for(String[] row: data) {
				//JDBCUtils.set(pst,1,UUID.randomUUID().toString().replace("-", ""));//lim:pkids
				for(int index = 0; index < row.length; index++) {
					JDBCUtils.set(pst, index + 1, row[index]);//lim:增加主键,原来index+1
				}
				pst.addBatch();
			}
			pst.executeBatch();
		}
	}
	
	@Override
	public void copy(String oldTableName,String newTableName,Column[]columns ) throws SQLException {
		Connection conn = connectionFactory.getThreadConnection();
		conn.setSchema("public");
		StringBuffer sb = new StringBuffer();
		sb.append("create table ");
		sb.append(newTableName);
		sb.append("(id___system  integer  NOT NULL generated always as identity primary key,");//lim:增加主键
		for(Column col: columns) {
			sb.append(col.getName());
			sb.append(" varchar(" + VARCHAR_DEFAULT_LENGTH + ")");
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")  PERSISTENT ASYNCHRONOUS");
		StringBuffer sbinsert = new StringBuffer();
		sbinsert.append("insert into "+ newTableName +"(");
		for(Column col: columns) {
			sbinsert.append(col.getName()+",");
		}
		sbinsert.deleteCharAt(sbinsert.length() - 1);
		sbinsert.append(") select ");
		for(Column col: columns) {
			sbinsert.append(col.getName()+",");
		}
		sbinsert.deleteCharAt(sbinsert.length() - 1);
		sbinsert.append(" from "+oldTableName+" ");
		
		try(Statement st = conn.createStatement()) {
			st.executeUpdate(sb.toString());
			st.executeUpdate(sbinsert.toString());
		}
	}

	@Override
	public void append(TableMetaData metadata) throws SQLException {
		Connection conn = connectionFactory.getThreadConnection();
		conn.setSchema("public");
		StringBuffer sb = new StringBuffer();
		sb.append("insert into ");
		sb.append(metadata.getTableName());
		sb.append("(");
		for(int index = 0; index < metadata.getColumns().length; index++) {
			sb.append(metadata.getColumns()[index].getName()+",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");
		
		sb.append(" values(");
		for(int index = 0; index < metadata.getColumns().length; index++) {
			sb.append("?,");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");
		String[][]data=metadata.getData();
		if(data==null||data.length==0){
			return;//如果数据集为空返回
		}
		try(PreparedStatement pst = conn.prepareStatement(sb.toString())) {
			for(String[] row: data) {
				//JDBCUtils.set(pst,1,UUID.randomUUID().toString().replace("-", ""));//lim:pkids
				for(int index = 0; index < row.length; index++) {
					JDBCUtils.set(pst, index + 1, row[index]);//lim:增加主键,原来index+1
				}
				pst.addBatch();
			}
			pst.executeBatch();
		}
	}



	@Override
	public void deleteTable(String name) throws SQLException {
		Connection conn = connectionFactory.getThreadConnection();
		try(Statement st = conn.createStatement()) {
			st.executeUpdate("drop table " + name);
		}
	}
	
	@Override
	public long getTableVolume(String key) throws SQLException {
		key = key.toUpperCase();
		Connection conn = connectionFactory.getThreadConnection();
		try(Statement st = conn.createStatement()) {
			ResultSet set = st.executeQuery("select TOTAL_SIZE from SYS.MEMORYANALYTICS where TABLE_NAME = '" + schemaName + "." + key + "'");
			if(set.next()) {
				Double totalSize = set.getDouble(1);
				return totalSize.intValue();
			} else {
				//throw new RuntimeException("The dataset of " + key + "does not have table!");
				return 0;
			}
		}
	}

	@Override
	public long count(String tableName) throws SQLException {
		Connection conn = connectionFactory.getThreadConnection();
		try(Statement st = conn.createStatement()) {
			ResultSet set = st.executeQuery("select count(1) from " +  schemaName + "." + tableName);
			set.next();
			return set.getLong(1);
		}
	}

	@Override
	public String[][] getData(TableQuery query) throws SQLException {
		ConnectionFactory connectionFactory = new ConnectionFactoryImpl(DataAccessor.PG_URL,false);
		Connection conn = connectionFactory.getConnection();
		try(Statement st = conn.createStatement()) {
			StringBuffer sb = new StringBuffer();
			sb.append("select ");
			if(query.getColumnNames() == null || query.getColumnNames().length == 0) {		// 未指定列查询全部
				sb.append(" * ");
			} else {							// 查询指定列
				for(String colName: query.getColumnNames()) {	
					sb.append(colName);
					sb.append(",");
				}
				sb.deleteCharAt(sb.length() - 1);
			}
			sb.append(" from public.");
			sb.append(query.getTabelName());
			sb.append(" order by id___system asc ");
			Pager pager = query.getPager();
			if(pager != null) {
				sb.append(" offset ");
				sb.append((pager.getPageNumber() - 1) * pager.getPageSize());
				sb.append(" rows fetch next ");
				sb.append(pager.getPageSize());
				sb.append(" rows only");
			}
			ResultSet set = st.executeQuery(sb.toString());
			return retrieveData(set, query.isRowBase());
		}
	}

	public ConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}
	
	protected String[][] retrieveData(ResultSet set, Boolean rowBase) throws SQLException {
		ResultSetMetaData metaData = set.getMetaData();
		int colCount = metaData.getColumnCount();
		if(rowBase) {
			List<String[]> rows = new LinkedList<String[]>();
			while(set.next()) {
				String[] row = new String[colCount];
				for(int colIndex = 0; colIndex < colCount; colIndex++) {
					row[colIndex] = set.getString(colIndex + 1);
				}
				rows.add(row);
			}
			return rows.toArray(new String[rows.size()][]);
		} else {
			List<List<String>> cols = new LinkedList<List<String>>();
			for(int index = 0; index < colCount; index++) {
				cols.add(new LinkedList<String>());
			}
			while(set.next()) {
				for(int colIndex = 0; colIndex < colCount; colIndex++) {
					cols.get(colIndex).add(set.getString(colIndex + 1));
				}
			}
			String[][] data = new String[colCount][];
			for(int index = 0; index < colCount; index++) {
				int size = cols.get(index).size();
				data[index] = cols.get(index).toArray(new String[size]);
			}
			return data;
		}
	}


	//更新数据集
	@Override
	public void update(TableMetaData metadata) throws SQLException {
		// TODO Auto-generated method stub
		Connection conn = connectionFactory.getThreadConnection();
		StringBuffer sb = new StringBuffer();
		sb.append("update  ");
		sb.append(metadata.getTableName());
		sb.append(" set ");
		for(int index = 0; index < metadata.getColumns().length; index++) {
			Column col=metadata.getColumns()[index];
			if(!col.getName().equalsIgnoreCase("id___system")){
				sb.append(col.getName()+"=?,");
			}
			
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" where id___system=?");
		try(PreparedStatement pst = conn.prepareStatement(sb.toString())) {
			for(String[] row: metadata.getData()) {
				//默认第一列是ID列
				for(int index = 1; index < row.length; index++) {
					JDBCUtils.set(pst, index , row[index]);//lim:增加主键,原来index+1
				}
				JDBCUtils.set(pst,row.length,row[0]);//默认第一列
				pst.executeUpdate();
			}
		}
	}

	@Override
	public void dropColumn(String tableName, String columnName)throws SQLException{
		Connection conn = connectionFactory.getThreadConnection();
		try(Statement st = conn.createStatement()) {
			st.executeUpdate("alter table "+tableName+" drop column "+columnName);
		}
	}
	public String ReplaceNone(String value){
		if(value!=null&&value.equals("None"))return null;
		return value;
	}
}
