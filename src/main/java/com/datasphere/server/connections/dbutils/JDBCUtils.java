package com.datasphere.server.connections.dbutils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class JDBCUtils {
	
	public static void set(PreparedStatement pst, int index, String value) throws SQLException {
		if(value == null) {
			pst.setNull(index, Types.NULL);
		}else if(value.trim().equals("")){
			pst.setNull(index, Types.NULL);
		} 
		else if(value.equals("None")){
			pst.setNull(index, Types.NULL);
		}else{
			pst.setString(index, value);
		}
	}
	
	public static String getString(ResultSet set, int index) throws SQLException {
/*		Object obj = set.getObject(index);
		if(obj == null) {
			return null;
		}*/
		return set.getString(index);
	}
}
