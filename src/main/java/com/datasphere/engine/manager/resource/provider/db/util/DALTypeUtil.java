package com.datasphere.engine.manager.resource.provider.db.util;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

public class DALTypeUtil {
	
	
	public static boolean isSupport(int type){
		return convertBusinessType(type) == null ? false : true;
	}
	
	public static String getResultSetValue(ResultSet set,int type,int index) throws SQLException{
		String result = null;
		switch(type){
			case Types.TINYINT :
			case Types.SMALLINT : 
			case Types.INTEGER : 
			case Types.BIGINT:
				result = set.getString(index);
				break;
			case Types.FLOAT:
			case Types.REAL:
			case Types.DOUBLE:
			case Types.NUMERIC:
			case Types.DECIMAL:
			case 100:
			case 101:
				BigDecimal decimalResult = set.getBigDecimal(index);
				if(decimalResult != null){
					result = decimalResult.toPlainString();
				}
				break;
			case Types.NCHAR:
			case Types.NVARCHAR:
			case Types.LONGNVARCHAR:
			case Types.CHAR:
			case Types.VARCHAR:
			case Types.LONGVARCHAR:
				result = set.getString(index);
				break;
			case Types.DATE:
			case Types.TIME:
			case Types.TIMESTAMP:
			case Types.TIME_WITH_TIMEZONE: 
			case Types.TIMESTAMP_WITH_TIMEZONE:
				Timestamp timestampResult = set.getTimestamp(index); 
				if(timestampResult != null){
					result = timestampResult.toString();
				}
				break;
			case Types.BOOLEAN : 
				Boolean booleanResult = set.getBoolean(index);
				if(booleanResult != null){
					result = Boolean.valueOf(booleanResult).toString();
				}
				break;
		}
		return result;
	}
	
	
	public static String convertBusinessType(int type){
		String result = null;
		switch(type){
			case Types.TINYINT :
			case Types.SMALLINT : 
			case Types.INTEGER : 
			case Types.BIGINT:
//				result = BusinessDataType.BDT_INTEGER;
				break;
				
			case Types.FLOAT : 
			case Types.REAL :
			case Types.DOUBLE :
			case Types.NUMERIC : 
			case Types.DECIMAL : 
			case 100:
			case 101:
//				result = BusinessDataType.BDT_DECIMAL;
				break;
				
			case Types.DATE :
			case Types.TIME :
			case Types.TIMESTAMP :
			case Types.TIME_WITH_TIMEZONE : 
			case Types.TIMESTAMP_WITH_TIMEZONE : 
//				result = BusinessDataType.BDT_DATETIME;
				break;
			case Types.CHAR : 
			case Types.VARCHAR :
			case Types.LONGVARCHAR : 
			case Types.NCHAR:
			case Types.NVARCHAR:
			case Types.LONGNVARCHAR:
//				result = BusinessDataType.BDT_STRING;
				break;
		}
		return result;
	}
}
