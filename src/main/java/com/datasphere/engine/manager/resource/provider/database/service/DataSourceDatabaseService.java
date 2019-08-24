package com.datasphere.engine.manager.resource.provider.database.service;

import java.util.List;
import java.util.Map;

import com.datasphere.engine.manager.resource.provider.database.entity.DBQuery;
import com.datasphere.engine.manager.resource.provider.database.entity.DBTableField;
import com.datasphere.engine.manager.resource.provider.model.DBTableInfodmp;


public interface DataSourceDatabaseService {
	
	public List<List<DBTableField>> readTable(DBQuery query);
	
	public List<DBTableInfodmp> listTableInfo(DBQuery query);
	
	public List<Map<String, String>> readTableWithColumnName(DBQuery query, Map<String, Integer> typeMap);
	
	public boolean tableExsit(DBQuery query);
	
	public String[] listDatabase();
	
	public String[] listTable(DBQuery query);
	
	public String[] listSchema(String databaseName);
	
	public Map<String,List<String>> getUnsupportTableColumn(DBQuery query, List<String> tables);

	public Map<String, Integer> getColumnsNameAndJdbcType(DBQuery query);
}
