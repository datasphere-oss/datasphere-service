package com.datasphere.engine.manager.resource.provider.database.service;

import java.util.Map;

import com.datasphere.engine.manager.resource.provider.database.config.DBConfig;
import com.datasphere.engine.manager.resource.provider.database.config.OracleDBConfig;
import com.datasphere.engine.manager.resource.provider.database.dao.MssqlDao;
import com.datasphere.engine.manager.resource.provider.database.dao.MysqlDao;
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
				MysqlDao mysqlDao = new MysqlDao();
				DBConfig dbconf = BeanToMapUtil.convertMap(DBConfig.class, config);
				mysqlDao.setConfig(dbconf);
				mysqlService.setBaseDao(mysqlDao);
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
				MssqlDao mssqlDao = new MssqlDao();
				DBConfig msconf = BeanToMapUtil.convertMap(DBConfig.class, config);
				mssqlDao.setConfig(msconf);
				mssqlService.setBaseDao(mssqlDao);
				return mssqlService;
			}	
		}
		
		return null;
	}

}
