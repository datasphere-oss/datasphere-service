package com.datasphere.engine.manager.resource.provider.db.dao;

import com.datasphere.core.common.utils.O;
import com.datasphere.engine.common.exception.JRuntimeException;
import com.datasphere.server.connections.constant.ConnectionInfo;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostgreSQLDao {

    public boolean insertDatas(ConnectionInfo ci) throws SQLException {
        Connection con = null;
        PreparedStatement pst = null;
        String datas = ci.getDatas();
//        try {
            con = getConnection(ci);
            if (!datas.equals("") && datas.contains("insert into")) {
                pst = con.prepareStatement(datas);
                pst.execute();// insert remaining records
            }else {
                JsonObject objs = new Gson().fromJson(ci.getDatas(), JsonObject.class);
                JsonArray rows = objs.getAsJsonArray("rows");
                StringBuffer sql = new StringBuffer();
                sql.append("insert into \""+ci.getSchema()+"\".\""+ci.getTableName()+"\" (");

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
                pst = con.prepareStatement(sql.toString(),ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
//            pst = con.prepareStatement(sql.toString());
                final int batchSize = Integer.parseInt(ci.getBatchSize());
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
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }finally {
            try {
                if (con != null) con.close();
                if (pst != null) pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
//        }
        return true;
    }

    public Connection getConnection(ConnectionInfo ci) throws SQLException{
        Connection connection = null;
        if(connection == null){
            StringBuilder sb = new StringBuilder();
            sb.append("jdbc:postgresql://").
                    append(ci.getHostIP()).
                    append(":").
                    append(ci.getHostPort()).
                    append("/").
                    append(ci.getDatabaseName());

            String connectString = sb.toString();
            O.log(connectString);
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                throw new JRuntimeException(e.getMessage());
            }

            try{
                connection = DriverManager.getConnection(connectString,ci.getUserName(),ci.getUserPassword());
            }catch (Exception e) {
                if(!(e instanceof SQLException)){
                    throw new SQLException("connect failed to '"+connectString+"'.");
                }else{
                    throw e;
                }
            }

        }
        return connection;
    }


    public static void main(String[] args) {
        ConnectionInfo ciao = new ConnectionInfo();
        ciao.setBatchSize(50+"");
        ciao.setDatabaseName("test");
        ciao.setSchema("public");
        ciao.setHostIP("127.0.0.1");
        ciao.setHostPort("5432");
        ciao.setUserName("postgres");
        ciao.setUserPassword("admin");

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
//        insertDatas(ciao);
    }

    public String selectFields(ConnectionInfo connectionInfoAndOthers) throws SQLException {
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

    public Map<String, Object> selectDatas(ConnectionInfo connectionInfo) {
        //获取连接
        Connection conn = null;
        PreparedStatement pst = null;
        Statement stat = null;
        ResultSet rs = null;
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            conn = getConnection(connectionInfo);
            conn.setSchema(connectionInfo.getSchema());
            stat = conn.createStatement();
            rs = stat.executeQuery(connectionInfo.getDatas());

            List<Object> dataList = new ArrayList<Object>();
            List<Object> metaList = new ArrayList<Object>();
            int rows = 0;

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            // 遍历ResultSet中的每条数据
            while (rs.next()) {
                HashMap<String, String> array1 = new HashMap<String, String>();
                // 遍历每一列
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnLabel(i);
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
                array2.put("name", metaData.getColumnLabel(i + 1));
                metaList.add(array2);
            }
//            result.put("tableName",connectionInfoAndOthers.getTableName());
            result.put("schema",metaList);
            result.put("rows",dataList);
            result.put("rowCount",rows);
            return result;
        } catch (Exception e) {
            result = null;
        } finally {
            close(conn, pst, rs);
        }
        return result;
    }

    static void close(Connection conn, PreparedStatement pst, ResultSet rs) {
        Map<String, Object> result;
        try {
            if (rs != null) rs.close();
            if(pst != null) pst.close();
            if (conn != null) conn.close();
        } catch (Exception ex) {
            result = null;
        }
    }
}
