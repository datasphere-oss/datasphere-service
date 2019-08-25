package com.datasphere.engine.manager.resource.provider.db.util;

import java.util.HashMap;
import java.util.Map;

import com.datasphere.engine.manager.resource.provider.exception.JSQLException;

/**
 * DB.MYSQL.08S01=数据库访问失败，可能是主机名或端口号错误 
DB.MYSQL.28000=数据库访问失败，可能是用户名或密码错误
DB.ORACLE.61000=数据库访问失败，可能是主机名或端口号错误 
DB.ORACLE.72000=数据库访问失败，可能是用户名或密码错误
DB.ORACLE.66000=数据库访问失败，可能是SID或服务名称不存在
DB.MSSQL.08S01=数据库访问失败，可能是主机名或端口号错误
DB.MSSQL.S0001=数据库访问失败，可能是用户名或密码错误
 * @author admin
 *
 */
public class DBErrorUtil {
	private final static Map<String,Integer> sqlState;
	private final static String[] messages = new String[]{
			"数据库访问失败，可能是主机名或端口号错误",
			"数据库访问失败，可能是用户名或密码错误",
			"数据库访问失败，可能是SID或服务名称不存在",
	};
	static{
		sqlState = new HashMap<String,Integer>();
		sqlState.put("08S01", 0);
		sqlState.put("28000", 1);
		sqlState.put("61000", 0);
		sqlState.put("72000", 1);
		sqlState.put("66000", 2);
		sqlState.put("S0001", 1);
		sqlState.put("DSDB", 0);
	}
	
	public static String getMessage(JSQLException e){
		if(sqlState.get(e.getSQLState()) != null){
			return messages[sqlState.get(e.getSQLState())];
		}else{
			return "数据库访问失败,错误代码["+e.getSQLState()+"]["+e.getVendorCode()+"]";
		}
	}
}
