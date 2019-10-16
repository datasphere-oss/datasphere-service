package com.datasphere.engine.manager.resource.provider.db.service;

import java.util.Map;

import com.datasphere.engine.manager.resource.provider.config.DBConfig;
import com.datasphere.engine.manager.resource.provider.config.OracleDBConfig;
import com.datasphere.engine.manager.resource.provider.db.dao.MSSQLDao;
import com.datasphere.engine.manager.resource.provider.db.dao.MySQLDao;
import com.datasphere.engine.manager.resource.provider.db.dao.OracleDao;
import com.datasphere.engine.manager.resource.provider.db.service.impl.MSSQLDatabaseServiceImpl;
import com.datasphere.engine.manager.resource.provider.db.service.impl.MySQLDatabaseServiceImpl;
import com.datasphere.engine.manager.resource.provider.db.service.impl.OracleDatabaseServiceImpl;
import com.datasphere.engine.manager.resource.provider.db.util.BeanToMapUtil;


public class DataSourceDatabaseFactory {

	public static DataSourceDatabaseService create(Map<String,Object> config) {
		if(config != null && config.get("databaseType") != null){
			switch (config.get("databaseType").toString().toUpperCase()) {
			case "MYSQL":
				MySQLDatabaseServiceImpl mysqlService = new MySQLDatabaseServiceImpl();
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
				MSSQLDatabaseServiceImpl mssqlService = new MSSQLDatabaseServiceImpl();
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
