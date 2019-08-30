package com.datasphere.engine.manager.resource.provider.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.datasphere.core.common.BaseService;

import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataQueryService extends BaseService {
    private final static Log log = LogFactory.getLog(DataQueryService.class);
    /**
     * 取得数据连接
     * @return
     */
    public Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(this.daasClassName);
            conn = DriverManager.getConnection(this.daasJdbcUrl, this.daasUsername, this.daasPassword);
        } catch (Exception ex) {
            log.error("can not create connection: {}", ex);
            conn = null;
        }
        return conn;
    }

    /**
     * 查询dataset数据
     * @return
     */
    public Map<String, Object> dataQuery(String sql) {
        Map<String, Object> result = new HashMap<String, Object>();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try{
            log.info("查询Dataset数据");
            conn = getConnection();
            List<Object> dataList  = new ArrayList<Object>();
            List<Object> metaList  = new ArrayList<Object>();
            Long rows = 0L;

            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int numColumns = metaData.getColumnCount();
            for (int i = 1; i <= numColumns; i++) {
                Map<String, String> array2 = new HashMap<String, String>();
                array2.put("type", metaData.getColumnTypeName(i));
                array2.put("name",metaData.getColumnLabel(i));
                metaList.add(array2);
            }
            while (rs.next()) {
                HashMap<String, String> array1= new HashMap<String,String>();
                // 遍历每一列
                for (int i = 1; i <= metaList.size(); i++) {
                    array1.put(metaData.getColumnLabel(i), rs.getString(i));
                }
                dataList.add(array1);
                rows ++;
            }
            result.put("schema",metaList);
            result.put("rows",dataList);
            result.put("rowCount",rows);
            result.put("sql",sql);
            return result;
        }catch (Exception e){
            log.error("{}",e);
            result = null;
        }finally {
            try {
                if(rs != null) rs.close();
                if(pst != null) pst.close();
                if(conn != null) conn.close();
            } catch (Exception ex) {
                log.error("{}",ex);
                result = null;
            }
        }
        return result;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {

    }

}
