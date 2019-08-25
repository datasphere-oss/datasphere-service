package com.datasphere.engine.manager.resource.provider.db.dao;

import com.datasphere.engine.common.exception.JRuntimeException;
import com.datasphere.engine.manager.resource.provider.config.DBConfig;
import com.datasphere.server.connections.constant.ConnectionInfo;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.*;
import java.util.*;

public class MySQLDao extends BaseDao<DBConfig> {

	private final static String SQL_DB_LIST = "SELECT SCHEMA_NAME FROM information_schema.SCHEMATA";
	private final static String SQL_TABLE_LIST = "SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA = ?";
	private final static String SQL_TABLE_NAME_EXSIT = "SELECT COUNT(1) FROM information_schema.TABLES WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?";
	private final static String SQL_TABLE_COL_NUMBER = "SELECT COUNT(1) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?";

	/**
	 * 查询数据库名列表
	 * 
	 * @return
	 * @throws SQLException
	 */
	public String[] listDatabase() throws SQLException {
		return query(null, SQL_DB_LIST, null);
	}

	/**
	 * 给定一个数据库名，查询该库下所有表
	 * 
	 * @param databaseName
	 * @return
	 * @throws SQLException
	 */
	public String[] listTable(String databaseName) throws SQLException {
		return query(null, SQL_TABLE_LIST, new String[] { databaseName });
	}

	/**
	 * 给定一个数据库名和一个查询名称，模糊查询该库下所有匹配的表
	 * 
	 * @param databaseName
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public String[] listTable(String databaseName, String name) throws SQLException {
		String sql = SQL_TABLE_LIST + " AND TABLE_NAME LIKE ?";
		return query(null, sql, new String[] { databaseName, "%" + name + "%" });
	}

	/**
	 * 给定一个数据库名和一个表名，查询该库下是否存在这个表
	 * 
	 * @param databaseName
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public boolean tableExist(String databaseName, String tableName) throws SQLException {
		Connection conn = getConnection();
		try (PreparedStatement pst = conn.prepareStatement(SQL_TABLE_NAME_EXSIT)) {
			pst.setString(1, databaseName);
			pst.setString(2, tableName);
			ResultSet set = pst.executeQuery();
			set.next();
			int count = set.getInt(1);
			return count == 0 ? false : true;
		}
	}

	/**
	 * 给定一个数据库名和一个表名，查询该表有多少列
	 * 
	 * @param databaseName
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public int getTableColumnCount(String databaseName, String tableName) throws SQLException {
		Connection conn = getConnection(); 
		try (PreparedStatement pst = conn.prepareStatement(SQL_TABLE_COL_NUMBER)) {
			pst.setString(1, databaseName);
			pst.setString(2, tableName);
			ResultSet set = pst.executeQuery();
			set.next();
			return set.getInt(1);
		}
	}

	/**
	 * 分页读取表数据
	 * 
	 * @param databaseName 数据库名
	 * @param tableName 表名
	 * @param page 读取第几页
	 * @param rows 每页读取数量
	 * @param columnMap key 存放字段名称  value 存放该字段类型是否支持
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, String>> readTableWithColumn(String databaseName, String tableName, int page, int rows,
			Map<String, Integer> columnMap) throws SQLException {
		
		int start = (page - 1) * rows;
		int end = rows;
		String sql = "SELECT * FROM " + tableName + " LIMIT " + start + "," + end;
		List<Map<String, String>> dataList = new LinkedList<>();
		Connection conn = getConnection();
		conn.setCatalog(databaseName);
		try(PreparedStatement pst = conn.prepareStatement(sql)){
			ResultSet resultSet = pst.executeQuery();
			if (columnMap != null) {
				columnMap.putAll(readTableColumnByResultSet(resultSet,null));
			}
			while (resultSet.next()) {
				dataList.add(readTableByResultSet(resultSet,null));
			}
			return dataList;
		}

	}

	/**
	 * jdbc:mysql://192.168.15.122:3306/test?user=root&password=123456
	 * @throws SQLException 
	 */
	public Connection getConnection() throws SQLException {
		if(connection == null){
			DBConfig config = getConfig();
			StringBuilder sb = new StringBuilder();
			sb.append("jdbc:mysql://").append(config.getHost()).append(":").append(config.getPort());
			String connectString = sb.toString();

			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
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
		}
		return connection;
	}


	/**
	 * 动态sql插入数据
	 * @param connectionInfo
	 * @return
	 */
	public boolean insertDatas(ConnectionInfo connectionInfo) throws SQLException {
		Connection conn = null;
		PreparedStatement pst = null;
		Gson gson = new Gson();
		String datas = connectionInfo.getDatas();
//		try {
			conn = getConnection(connectionInfo);
			if (!datas.equals("") && datas.contains("insert into")) {
				pst = conn.prepareStatement(datas);
				pst.execute();// insert remaining records
			} else {
				JsonObject objs = gson.fromJson(connectionInfo.getDatas(), JsonObject.class);
				JsonArray rows = objs.getAsJsonArray("rows");
				StringBuffer sql = new StringBuffer();
				sql.append("INSERT INTO "+connectionInfo.getTableName()+ "(");

				JsonArray schema = objs.getAsJsonArray("schema");
				if (schema.size() > 0) {
					for (int j = 0; j < schema.size(); j++) {
						JsonObject field = schema.get(j).getAsJsonObject();
						String middle = field.get("name").getAsString() + (j ==schema.size()-1? ")":",");// 得到 每个对象中的属性值
						sql.append(middle);
					}
				}
				sql.append(" values(");
				for (int k = 0; k < schema.size(); k++) {
					sql.append("?").append(k ==schema.size()-1? ")":",");
				}
				pst = conn.prepareStatement(sql.toString());
				if (rows.size() > 0) {
					final int batchSize = Integer.parseInt(connectionInfo.getBatchSize());
					int count = 0;
					for (int i = 1; i <= rows.size(); i++) {
						// 遍历 jsonarray 数组，把每一个对象转成 json 对象
						JsonObject data = rows.get(i-1).getAsJsonObject();
						if (schema.size() > 0) {
							for (int j = 1; j <= schema.size(); j++) {
								JsonObject field = schema.get(j-1).getAsJsonObject();
								String fieldType = field.get("type").getAsJsonObject().get("name").getAsString();// 得到 每个对象中的属性值
								String fieldName = field.get("name").getAsString();
								if ("INTEGER".equals(fieldType)) {
//									System.out.println(j+" - INTEGER - "+Integer.parseInt(data.get(fieldName).getAsString()));
									pst.setInt(j, Integer.parseInt(data.get(fieldName).getAsString()));
								} else if("VARCHAR".equals(fieldType)) {
//									System.out.println(j+" - VARCHAR - "+data.get(fieldName).getAsString());
									pst.setString(j, data.get(fieldName).getAsString());
								} else if("TIMESTAMP".equals(fieldType)) {
//									System.out.println(j+" - TIMESTAMP - "+Timestamp.valueOf(data.get(fieldName).getAsString()));
									pst.setTimestamp(j, Timestamp.valueOf(data.get(fieldName).getAsString()));
								}
							}
							System.out.println(">>>插入第"+(i)+"条数据");
//							System.out.println("----------------------------------");
						}
						pst.addBatch();
						if(++count % batchSize == 0) {
							pst.executeBatch();
						}
					}
					pst.executeBatch();// insert remaining records
					System.out.println(">>>数据插入成功！");
				}
			}
//		} catch (SQLException e) {
//			throw new RuntimeException(e.getMessage());
//		} finally {
			try {
				if (conn != null) conn.close();
				if (pst != null) pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
//		}
		return true;
	}

	/**
	 * jdbc:mysql://192.168.15.122:3306/test?user=root&password=123456
	 * @throws SQLException
	 */
	public Connection getConnection(ConnectionInfo ci) throws SQLException {
        Connection connection = null;
		if(connection == null){
			StringBuilder sb = new StringBuilder();
			sb.append("jdbc:mysql://").append(ci.getHostIP()).append(":").append(ci.getHostPort())
            .append("/").append(ci.getDatabaseName()).append("?useUnicode=true&characterEncoding=UTF-8");
			String connectString = sb.toString();

			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				throw new JRuntimeException(e.getMessage());
			}
			try{
				connection = DriverManager.getConnection(connectString,ci.getUserName(),ci.getUserPassword());
			} catch (Exception e) {
				if(!(e instanceof SQLException)){
					throw new SQLException("connect failed to '"+connectString+"'.",CONNECT_FAILED_CODE);
				} else {
					throw e;
				}
			}
		}
		return connection;
	}

    public static void main(String[] args) {
        ConnectionInfo ci = new ConnectionInfo();
        ci.setBatchSize(500+"");
        ci.setDatabaseName("catalog_db");
        ci.setHostIP("127.0.0.1");
        ci.setHostPort("3306");
        ci.setUserName("root");
        ci.setUserPassword("gg!007");

        String datas = "{\n" +
                "\t\t\t\"schema\": [\n" +
                "\t\t\t  { \"name\": \"id\",\"type\": { \"name\": \"INTEGER\" } },\n" +
                "\t\t\t  { \"name\": \"name\",\"type\": { \"name\": \"VARCHAR\" } }\n" +
                "\t\t\t],\n" +
                "\t\t\t\"rowCount\": 2,\n" +
                "\t\t\t\"rows\": [\n" ;
//				"\t\t\t\t{ \"name\": \"lisi\", \"id\": 2},\n";
        for(int i=0;i<10000;i++){
            datas += "{ \"name\": \"lisi"+i+"\", \"id\": "+ i +"},\n";
        }



        datas += "\t\t\t\t{ \"name\": \"wangwu\", \"id\": 3 }\n" +
                "\t\t\t]\n" +
                "\t\t}";

        ci.setDatas(datas);
        ci.setTableName("test");
//		insertDatas(ciao);
    }

	public String selectFields(ConnectionInfo connectionInfo) throws SQLException {
		Connection conn = getConnection(connectionInfo);
		String sql = "SELECT * FROM " + connectionInfo.getTableName()+" LIMIT  0";
		conn.setCatalog(connectionInfo.getDatabaseName());
		try(PreparedStatement pst = conn.prepareStatement(sql)){
			ResultSet resultSet = pst.executeQuery();
			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();
			HashMap<String, String> map= new HashMap<String,String>();
				for (int i = 1; i <= columnCount; i++) {
					String columnName =metaData.getColumnLabel(i);
					String columnType = metaData.getColumnTypeName(i);
					map.put(columnName, columnType);
				}
			return map.toString();
		}
	}

    public Map<String, Object> selectDatas(ConnectionInfo connectionInfo) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			conn = getConnection(connectionInfo);
			String sql = connectionInfo.getDatas();//获取转换后的sql
			conn.setCatalog(connectionInfo.getDatabaseName());
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();

			List<Object> dataList  = new ArrayList<Object>();
			List<Object> metaList  = new ArrayList<Object>();
			int rows=0;

			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			// 遍历ResultSet中的每条数据
			while (rs.next()) {
				HashMap<String, String> array1= new HashMap<>();
				// 遍历每一列
				for (int i = 1; i <= columnCount; i++) {
					String columnName =metaData.getColumnLabel(i);
					String value = rs.getString(columnName);
					array1.put(columnName, value);
				}
				dataList.add(array1);
				rows++;
			}

			int numColumns = metaData.getColumnCount();
			for (int i = 1; i <= numColumns; i++) {
				Map<String, String> array2 = new HashMap<String, String>();
				array2.put("type", metaData.getColumnTypeName(i));
				array2.put("name",metaData.getColumnLabel(i));
				metaList.add(array2);
			}
//			result.put("tableName",connectionInfo.getTableName());
			result.put("schema",metaList);
			result.put("rows",dataList);
			result.put("rowCount",rows);
			return  result;
		} catch(Exception e) {
			result = null;
		} finally {
			OracleDao.close(conn, pst, rs);
		}
		return result;
    }
}
