package com.datasphere.engine.manager.resource.provider.database.service;

import org.apache.commons.lang3.StringUtils;

import com.datasphere.engine.manager.resource.provider.database.dao.BaseDao;
import com.datasphere.engine.manager.resource.provider.database.entity.DBQuery;
import com.datasphere.engine.manager.resource.provider.database.exception.JSQLException;

import java.sql.SQLException;
import java.util.Map;


public abstract class AbstractDatabaseService<T extends BaseDao<?>> {
	
	protected T baseDao;

	public T getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(T baseDao) {
		this.baseDao = baseDao;
	}
	
	public String getQueryTable(DBQuery query){
		String schemaName = query.getSchemaName();
		String tableName = query.getTableName();
		if(!StringUtils.isEmpty(schemaName)){
			tableName = schemaName + "." + tableName;
		}
		return tableName;
	}
	
	public Map<String, Integer> getColumnsNameAndJdbcType(DBQuery query){
		try {
			return baseDao.readTableMetaType(query.getDatabaseName(), getQueryTable(query));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JSQLException(e);
		}
	}
	
	public String[] listDatabase() {
		return new String[]{};
	}

	public String[] listSchema(String databaseName) {
		return new String[]{};
	}
	
}
