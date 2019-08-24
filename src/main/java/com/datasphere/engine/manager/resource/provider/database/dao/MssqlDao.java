package com.datasphere.engine.manager.resource.provider.database.dao;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.datasphere.engine.manager.resource.provider.database.config.DBConfig;
import com.datasphere.server.manager.common.utils.O;

public class MssqlDao extends BaseDao<DBConfig> {

	private final static String SQL_DB_LIST = "SELECT name FROM sys.databases WHERE name NOT IN ('master','tempdb','model','msdb')";
	private final static String SQL_TABLE_LIST = "SELECT name FROM sys.tables WHERE  type = 'U' AND SCHEMA_NAME(Schema_id) = ?";
	private final static String SQL_SCHEMA_LIST = "SELECT SCHEMA_NAME(Schema_id) from sys.tables where  type = 'U' group by SCHEMA_NAME(Schema_id)";
	private final static String SQL_TABLE_NAME_EXSIT = "SELECT COUNT(1) FROM sys.tables WHERE type = 'U' AND SCHEMA_NAME(Schema_id) = ? AND name = ?";
	private final static String SQL_TABLE_COL_NUMBER = "SELECT COUNT(1) FROM sys.columns t WHERE t.object_id IN (SELECT object_id  FROM sys.tables WHERE type = 'U' AND SCHEMA_NAME(Schema_id) = ? AND name = ?)";

	/**
	 * 列出所有库的名称
	 * 
	 * @return
	 * @throws SQLException
	 */
	public String[] listDatabase() throws SQLException {
		return query(null, SQL_DB_LIST, null);
	}

	/**
	 * 给定一个数据库名和模式，查询所有表名
	 * 
	 * @return
	 * @throws SQLException
	 */
	public String[] listTable(String databaseName, String schemaName) throws SQLException {
		return query(databaseName, SQL_TABLE_LIST, new String[] { schemaName });
	}

	/**
	 * 给定一个数据库名、模式名和搜索名，根据库名和模式名查询所有表名。
	 * 搜索名则对表名进行模糊查询。
	 * 
	 * @param databaseName
	 * @param schemaName
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public String[] listTable(String databaseName, String schemaName, String name) throws SQLException {
		String sql = SQL_TABLE_LIST + " AND name LIKE '%" + name + "%'";
		O.log(sql);
		return query(databaseName, sql, new String[] { schemaName });
	}

	/**
	 * 给定数据库名称，根据库名查询模式名列表
	 * 
	 * @param databaseName
	 * @return
	 * @throws SQLException
	 */
	public String[] listSchema(String databaseName) throws SQLException {
		return query(databaseName, SQL_SCHEMA_LIST, null);
	}

	/**
	 * 给定数据库名、模式名和表名，查询该库下、该模式下是否存在给定的表
	 * 
	 * @param databaseName
	 * @param schemaName
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public boolean tableExist(String databaseName, String schemaName, String tableName) throws SQLException {
		Connection conn = getConnection();
		conn.setCatalog(databaseName);
		try (PreparedStatement pst = conn.prepareStatement(SQL_TABLE_NAME_EXSIT)) {
			pst.setString(1, schemaName);
			pst.setString(2, tableName);
			ResultSet set = pst.executeQuery();
			set.next();
			int count = set.getInt(1);
			return count == 0 ? false : true;
		}
	}

	/**
	 * 查询一个表的记录总数
	 * 
	 * @param databaseName
	 * @param schemaName
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public int getTableColumnCount(String databaseName, String schemaName, String tableName) throws SQLException {
		Connection conn = getConnection();
		conn.setCatalog(databaseName);
		try (PreparedStatement pst = conn.prepareStatement(SQL_TABLE_COL_NUMBER)) {
			pst.setString(1, schemaName);
			pst.setString(2, tableName);
			ResultSet set = pst.executeQuery();
			set.next();
			return set.getInt(1);
		}
	}

	/**
	 * 分页读取表数据，并过滤不支持的字段类型。
	 * 
	 * @param databaseName
	 * @param schemaName
	 * @param tableName
	 * @param page 读取表的当前页号
	 * @param rows 读取的多少条记录
	 * @param columnTypeMap 存放表的字段信息
	 * key 存放字段名称，value 字段类型是否过滤掉
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, String>> readTableWithColumn(String databaseName, String schemaName, String tableName,
		int page, int rows, Map<String, Integer> columnTypeMap) throws SQLException {

		tableName = schemaName + "." + tableName;
		
		List<Map<String, String>> dataList = new LinkedList<>();
		
		Connection conn = getConnection();
		conn.setCatalog(databaseName);
		try(Statement statement = conn.createStatement()){
			ResultSet rset = statement.executeQuery("SELECT * FROM "+tableName + " WHERE 1 = 0");
			String firstFieldName = rset.getMetaData().getColumnName(1);
			
			int start = (page - 1) * rows;
			int end = rows * page;
			String sql = "SELECT * FROM( SELECT TOP " + end + " ROW_NUMBER() OVER(ORDER BY "+firstFieldName+" ASC) AS ROWID,* FROM "
					+ tableName + ") AS TEMP1 WHERE ROWID > " + start;
			O.log(sql);
			
			List<String> filter = new LinkedList<>();
			filter.add("ROWID");
			try(PreparedStatement pst = conn.prepareStatement(sql)){
				ResultSet resultSet = pst.executeQuery();
				if (columnTypeMap != null) {
					columnTypeMap.putAll(readTableColumnByResultSet(resultSet,filter));
				}
				while (resultSet.next()) {
					dataList.add(readTableByResultSet(resultSet,filter));
				}
				return dataList;
			}
		}

		
	}

	/**
	 * 获取一个JDBC连接
	 * 
	 * @throws SQLException 
	 * jdbc:microsoft:sqlserver://192.168.16.103:1433/DBSyncer
	 * @throws  
	 */
	public Connection getConnection() throws SQLException {
		/*if(connection == null){
			DBConfig config = getConfig();
			StringBuilder sb = new StringBuilder();
			sb.append("jdbc:sqlserver://").append(config.getHost()).append(":").append(config.getPort());
			String connectString = sb.toString();
			
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new JRuntimeException(e.getMessage());
			}
			
			try{
				connection = config.getUser() == null ? 
						DriverManager.getConnection(sb.toString()) : 
						DriverManager.getConnection(connectString,config.getUser(),config.getPassword());
			}catch (Exception e) {
				if(!(e instanceof SQLException)){
					throw new SQLException("connect failed to '"+connectString+"'.",CONNECT_FAILED_CODE);
				}else{
					throw e;
				}
			}
		}*/

		return null;
	}

}
