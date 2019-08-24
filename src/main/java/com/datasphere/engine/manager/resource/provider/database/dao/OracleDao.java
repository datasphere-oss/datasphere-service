package com.datasphere.engine.manager.resource.provider.database.dao;

import com.datasphere.engine.common.exception.JRuntimeException;
import com.datasphere.engine.manager.resource.provider.database.config.OracleDBConfig;
import com.datasphere.server.manager.common.constant.ConnectionInfoAndOthers;
import com.datasphere.server.manager.common.utils.O;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.*;
import java.util.*;


public class OracleDao extends BaseDao<OracleDBConfig> {
	
	private final static String SQL_TABLE_NAME_EXSIT = "SELECT count(1) FROM user_tables WHERE TABLE_NAME = ?";
	private final static String SQL_TABLE_NAME_LIST = "SELECT TABLE_NAME FROM user_tables";
	private final static String SQL_TABLE_USE_SIZE = "SELECT a.TABLE_NAME,b.BYTES FROM user_tables a LEFT JOIN dba_segments b ON a.TABLE_NAME = b.SEGMENT_NAME";
	private final static String SQL_TABLE_DESC = "SELECT COLUMN_NAME, DATA_TYPE FROM DBA_TAB_COLUMNS WHERE  OWNER = ? AND  TABLE_NAME = ? ORDER BY COLUMN_ID";
	private final static String SQL_TABLE_COL_NUMBER = "SELECT count(1) FROM DBA_TAB_COLUMNS WHERE  OWNER = ? AND TABLE_NAME = ?";
	
	/**
	 * 预留功能
	 * 根据SQL,读取表暂用空间大小
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	protected Map<String,Long> readTableUseSize(String sql) throws SQLException{
		Connection conn = getConnection();
		try(PreparedStatement pst = conn.prepareStatement(sql)) {
			Map<String,Long> resultMap = new HashMap<>();
			ResultSet set = pst.executeQuery();
			while(set.next()) {
				resultMap.put(set.getString(1), set.getLong(2));
			}
			return resultMap;
		}
	}
	
	/**
	 * 预留功能
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Map<String,Long> getTableUseSize() throws SQLException{
		return readTableUseSize(SQL_TABLE_USE_SIZE);
	}
	
	/**
	 * 预留功能
	 * 由于ORACLE只有一个数据库，所以只按表名搜索
	 * 
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public Map<String,Long> searchTableUseSizeByName(String name) throws SQLException{
		String sql = SQL_TABLE_USE_SIZE + " WHERE a.TABLE_NAME LIKE '%"+name.toUpperCase()+"%'";
		return readTableUseSize(sql);
	}
	
	/**
	 * 列出库中所有用户表名
	 * 
	 * @return
	 * @throws SQLException
	 */
	public String[] listTable() throws SQLException{
		return query(null,SQL_TABLE_NAME_LIST,null);
	}
	
	/**
	 * 根据名称模糊查找，列出匹配的所有表名
	 * 
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public String[] listTable(String name) throws SQLException{
		String sql = SQL_TABLE_NAME_LIST + " WHERE TABLE_NAME LIKE '%"+name.toUpperCase()+"%'";
		return query(null,sql,null);
	}
	
	/**
	 * 根据表名判断该表名是否存在
	 * 
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public boolean tableExist(String tableName) throws SQLException{
		Connection conn = getConnection();
		try(PreparedStatement pst = conn.prepareStatement(SQL_TABLE_NAME_EXSIT)) {
			pst.setString(1, tableName);
			ResultSet set = pst.executeQuery();
			set.next();
			int count = set.getInt(1);
			return count == 0 ? false : true;
		}
	}
	
	/**
	 * 获得一个表有多少列
	 * 
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public int getTableColumnCount(String tableName) throws SQLException{
		Connection conn = getConnection();
		try(PreparedStatement pst = conn.prepareStatement(SQL_TABLE_COL_NUMBER)) {
			pst.setString(1, getConfig().getUser().toUpperCase());
			pst.setString(2, tableName);
			ResultSet set = pst.executeQuery();
			set.next();
			return set.getInt(1);
		}
	}
	
	/**
	 * 获得表中的字段名称和字段类型，key为名称 value为类型
	 * 
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public Map<String,String> getColumnsNameWithType(String tableName) throws SQLException{
		Connection conn = getConnection();
		try(PreparedStatement pst = conn.prepareStatement(SQL_TABLE_DESC)) {
			Map<String,String> rmap = new HashMap<>();
			pst.setString(1, getConfig().getUser().toUpperCase());
			pst.setString(2, tableName);
			ResultSet set = pst.executeQuery();
			while(set.next()) {
				rmap.put(set.getString(1), set.getString(2));
			}
			return rmap;
		}
	}
	
	/**
	 * 分页读取表数据
	 * 
	 * @param tableName 表名
	 * @param page 读取第几页
	 * @param rows 每页读取数量
	 * @param columnMap key 存放字段名称 value 存放该字段类型是否支持
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String,String>> readTableWithColumn(String tableName,int page,int rows,
			Map<String,Integer> columnMap) throws SQLException{
		int start = (page-1)*rows+1;
		int end = page*rows+1;
		String sql = "SELECT * FROM ( SELECT A.*, ROWNUM RN FROM (SELECT * FROM "+tableName+") A WHERE ROWNUM < "+end+")  WHERE RN >= " +start;
		O.log(sql);
		List<Map<String,String>> dataList = new LinkedList<>();
		try(Connection conn = getConnection();PreparedStatement pst = conn.prepareStatement(sql)) {
			ResultSet resultSet = pst.executeQuery();
			List<String> filter = new LinkedList<String>();
			filter.add("RN");
			if (columnMap != null) {
				columnMap.putAll(readTableColumnByResultSet(resultSet,filter));
			}
			while (resultSet.next()) {
				dataList.add(readTableByResultSet(resultSet,filter));
			}
	        return dataList;
		}
	}
	
	/**
	 * jdbc:oracle:thin:oracle/123456@192.168.15.122:1521:ora11g
	 * jdbc:oracle:thin:@//<host>:<port>/ServiceName
	 * jdbc:oracle:thin:@<host>:<port>:<SID>
	 * @throws SQLException 
	 */
	public Connection getConnection() throws SQLException{
		//Connection connection = null;
		if(connection == null){
			OracleDBConfig config = getConfig();
			StringBuilder sb = new StringBuilder();
		
			if("1".equals(config.getServiceType())){
				sb.append("jdbc:oracle:thin:").
				append("@").
				append(config.getHost()).
				append(":").
				append(config.getPort()).
				append(":").
				append(config.getServiceName());
			}else{
				sb.append("jdbc:oracle:thin:@//").
				append(config.getHost()).
				append(":").
				append(config.getPort()).
				append("/").
				append(config.getServiceName());
			}
			String connectString = sb.toString();
			
			O.log(connectString);
			
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
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


	public boolean insertDatas(ConnectionInfoAndOthers ciao) throws SQLException {
		Connection con = null;
		PreparedStatement pst = null;
		String datas = ciao.getDatas();
//		try {
			con = getConnection(ciao);
			if (!datas.equals("") && datas.contains("insert into")) {
				pst = con.prepareStatement(datas);
				pst.execute();// insert remaining records
			}else {
				JsonObject objs = new Gson().fromJson(ciao.getDatas(), JsonObject.class);
				JsonArray rows = objs.getAsJsonArray("rows");
				StringBuffer sql = new StringBuffer();
				sql.append("insert into \""+ciao.getTableName()+"\" NOLOGGING (");

				JsonArray schema = objs.getAsJsonArray("schema");
				if (schema.size() > 0) {
					for (int j = 0; j < schema.size(); j++) {
						JsonObject field = schema.get(j).getAsJsonObject();
						String middle = "\""+field.get("name").getAsString() +"\""+ (j ==schema.size()-1? "":",");// 得到 每个对象中的属性值
						sql.append(middle) ;
					}
					sql.append(")");
					sql.append(" values(");
					for (int k = 0; k < schema.size(); k++) {
						sql.append("?").append((k ==schema.size()-1? "":","));
					}
					sql .append(")");
					System.out.println(sql);
				}
				// 关闭事务自动提交
				con.setAutoCommit(false);

				Long startTime = System.currentTimeMillis();
				pst = con.prepareStatement(sql.toString());
				final int batchSize = Integer.parseInt(ciao.getBatchSize());
				int count = 0;
				for (int i = 0; i < rows.size(); i++) {
					JsonObject data = rows.get(i).getAsJsonObject();

					for (int k = 0; k < schema.size(); k++){
						JsonObject field = schema.get(k).getAsJsonObject();

						String fieldType = field.get("type").getAsJsonObject().get("name").getAsString();// 得到 每个对象中的属性值
						String fieldName = field.get("name").getAsString();
						if ("INTEGER".equals(fieldType)) {
							System.out.println(k+" - INTEGER - "+Integer.parseInt(data.get(fieldName).getAsString()));
							pst.setInt(k+1, Integer.parseInt(data.get(fieldName).getAsString()));
						} else if("VARCHAR".equals(fieldType)) {
							System.out.println(k+" - VARCHAR - "+data.get(fieldName).getAsString());
							pst.setString(k+1, data.get(fieldName).getAsString());
						}
					}
					System.out.println(">>>插入第"+(i+1)+"条数据");
					System.out.println("------------------------");
					pst.addBatch();
					if(++count % batchSize == 0) {
						pst.executeBatch();
					}
				}
				// 执行批量更新
				pst.executeBatch();
				// 语句执行完毕，提交本事务
				con.commit();
				Long endTime = System.currentTimeMillis();
				System.out.println("用时：" + (endTime - startTime));
			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}finally {
			try {
				if (con != null) con.close();
				if (pst != null) pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
//		}
		return true;
	}

	public Connection getConnection(ConnectionInfoAndOthers ciao) throws SQLException{
		Connection connection = null;
		if(connection == null){
			StringBuilder sb = new StringBuilder();

			if("1".equals("1")){
				sb.append("jdbc:oracle:thin:").
						append("@").
						append(ciao.getHostIP()).
						append(":").
						append(ciao.getHostPort()).
						append(":").
						append(ciao.getDatabaseName());
			}else{
				sb.append("jdbc:oracle:thin:@//").
						append(ciao.getHostIP()).
						append(":").
						append(ciao.getHostPort()).
						append("/").
						append(ciao.getDatabaseName());
			}
			String connectString = sb.toString();

			O.log(connectString);

			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
			} catch (ClassNotFoundException e) {
				throw new JRuntimeException(e.getMessage());
			}

			try{
				connection =
//						config.getUser() == null ?
//						DriverManager.getConnection(sb.toString()) :
						DriverManager.getConnection(connectString,ciao.getUserName(),ciao.getUserPassword());
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


	public static void main(String[] args) {
		ConnectionInfoAndOthers ciao = new ConnectionInfoAndOthers();
		ciao.setBatchSize(50+"");
		ciao.setDatabaseName("helowin");
		ciao.setHostIP("117.107.241.79");
		ciao.setHostPort("1528");
		ciao.setUserName("datalliance");
		ciao.setUserPassword("datalliance");

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

		ciao.setDatas(datas);
		ciao.setTableName("test");
//		insertDatas(ciao);
	}

	public String selectFields(ConnectionInfoAndOthers connectionInfoAndOthers) throws SQLException {
		Connection conn = getConnection(connectionInfoAndOthers);
		String sql = "SELECT * FROM " + connectionInfoAndOthers.getTableName()+" LIMIT  0";
		conn.setCatalog(connectionInfoAndOthers.getDatabaseName());
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

	public Map<String,Object> selectDatas(ConnectionInfoAndOthers connectionInfoAndOthers) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		Map<String, Object> result = new HashMap<String, Object>();
		try{
			conn = getConnection(connectionInfoAndOthers);
			String sql = connectionInfoAndOthers.getDatas();
			sql = sql.replaceAll("\\n", "");
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();

			List<Object> dataList  = new ArrayList<Object>();
			List<Object> metaList  = new ArrayList<Object>();
			int rows=0;

			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			// 遍历ResultSet中的每条数据
			while (rs.next()) {
				HashMap<String, String> array1= new HashMap<String,String>();
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
			for (int i = 0; i < numColumns; i++) {
				Map<String, String> array2 = new HashMap<String, String>();
				array2.put("type", metaData.getColumnTypeName(i + 1));
				array2.put("name",metaData.getColumnLabel(i + 1));
				metaList.add(array2);
			}
//			result.put("tableName",connectionInfoAndOthers.getTableName());
			result.put("schema",metaList);
			result.put("rows",dataList);
			result.put("rowCount",rows);
			return result;
		} catch(Exception e) {
			result = null;
		} finally {
			close(conn, pst, rs);
		}
		return result;
	}

	static void close(Connection conn, PreparedStatement pst, ResultSet rs) {
		Map<String, Object> result;
		PostgresDao.close(conn, pst, rs);
	}
}
