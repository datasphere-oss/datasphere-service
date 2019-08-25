package com.datasphere.engine.manager.resource.provider.database.service;

import java.util.Map;

import com.datasphere.engine.manager.resource.provider.database.config.DBConfig;
import com.datasphere.engine.manager.resource.provider.database.config.OracleDBConfig;
import com.datasphere.engine.manager.resource.provider.database.dao.MSSQLDao;
import com.datasphere.engine.manager.resource.provider.database.dao.MySQLDao;
import com.datasphere.engine.manager.resource.provider.database.dao.OracleDao;
import com.datasphere.engine.manager.resource.provider.database.service.impl.MssqlDatabaseServiceImpl;
import com.datasphere.engine.manager.resource.provider.database.service.impl.MysqlDatabaseServiceImpl;
import com.datasphere.engine.manager.resource.provider.database.service.impl.OracleDatabaseServiceImpl;
import com.datasphere.engine.manager.resource.provider.database.util.BeanToMapUtil;


public class DataSourceDatabaseFactory {

	public static DataSourceDatabaseService create(Map<String,Object> config) {
		if(config != null && config.get("databaseType") != null){
			switch (config.get("databaseType").toString().toUpperCase()) {
			case "MYSQL":
				MysqlDatabaseServiceImpl mysqlService = new MysqlDatabaseServiceImpl();
				MySQLDao mySQLDao = new MySQLDao();
				DBConfig dbconf = BeanToMapUtil.convertMap(DBConfig.class, config);
				mySQLDao.setConfig(dbconf);
				mysqlService.setBaseDao(mySQLDao);
				return mysqlService;
			case "ORACLE":
				OracleDatabaseServiceImpl oracleService = new OracleDatabaseServiceImpl();
				OracleDao oracleDao = new OracleDao();
				OracleDBConfig oraconf = BeanToMapUtil.convertMap(OracleDBConfig.class, config);
				oracleDao.setConfig(oraconf);
				oracleService.setBaseDao(oracleDao);
				return oracleService;
			case "MSSQL":
				MssqlDatabaseServiceImpl mssqlService = new MssqlDatabaseServiceImpl();
				MSSQLDao mSSQLDao = new MSSQLDao();
				DBConfig msconf = BeanToMapUtil.convertMap(DBConfig.class, config);
				mSSQLDao.setConfig(msconf);
				mssqlService.setBaseDao(mSSQLDao);
				return mssqlService;
			}	
		}
		
		return null;
	}

}
