package com.datasphere.engine.manager.resource.provider.database.service.impl;

import org.apache.commons.lang3.StringUtils;

import com.datasphere.engine.manager.resource.provider.database.dao.MSSQLDao;
import com.datasphere.engine.manager.resource.provider.database.exception.JSQLException;
import com.datasphere.engine.manager.resource.provider.database.model.DBQuery;
import com.datasphere.engine.manager.resource.provider.database.model.DBTableField;
import com.datasphere.engine.manager.resource.provider.database.service.AbstractDatabaseService;
import com.datasphere.engine.manager.resource.provider.database.service.DataSourceDatabaseService;
import com.datasphere.engine.manager.resource.provider.database.util.DALTypeUtil;
import com.datasphere.engine.manager.resource.provider.model.DBTableInfodmp;

import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Singleton
public class MssqlDatabaseServiceImpl extends AbstractDatabaseService<MSSQLDao> implements DataSourceDatabaseService {

	public List<List<DBTableField>> readTable(DBQuery query) {
		try {
			String tableName = query.getSchemaName() + "." + query.getTableName();
			return baseDao.readTable(query.getDatabaseName(),tableName);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JSQLException(e);
		}finally{
			baseDao.closeConnection();
		}
	}

	public List<DBTableInfodmp> listTableInfo(DBQuery query) {
		String databaseName = query.getDatabaseName();
		String schemaName = query.getSchemaName();
		List<DBTableInfodmp> tbinfoList = new LinkedList<>();
		try{
			String[] tables = StringUtils.isBlank(query.getSearchName()) ?
					baseDao.listTable(databaseName,schemaName) : 
					baseDao.listTable(databaseName,schemaName,query.getSearchName());
			
			for(String table : tables){
				DBTableInfodmp tbinfo = new DBTableInfodmp();
				String tableName = schemaName + "." + table;
				int tableRows = baseDao.readTableRowcount(databaseName,tableName);
				if(tableRows != 0){
					Map<String,Integer> typeMap = baseDao.readTableMetaType(databaseName, tableName);
					/*int columnSize = 0;
					for(Integer type : typeMap.values()){
						if(DALTypeUtil.isSupport(type)){
							columnSize ++;
						}
					}
					if(columnSize > 0){
						tbinfo.setColumns(typeMap.size());
						tbinfo.setRows(tableRows);
						tbinfo.setName(table);
						tbinfoList.add(tbinfo);
					}*/
					tbinfo.setColumns(typeMap.size());
					tbinfo.setRows(tableRows);
					tbinfo.setName(table);
					tbinfoList.add(tbinfo);
				}
			}
		}catch(SQLException e){
			e.printStackTrace();
			throw new JSQLException(e);
		}finally{
			baseDao.closeConnection();
		}
		
		return tbinfoList;
	}

	public List<Map<String, String>>  readTableWithColumnName(DBQuery query,Map<String,Integer> columnTypeMap) {
		try {
			return baseDao.readTableWithColumn(query.getDatabaseName(),query.getSchemaName(),query.getTableName(), query.getPage(), query.getRows(),columnTypeMap);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JSQLException(e);
		}finally{
			baseDao.closeConnection();
		}
	}

	
	public boolean tableExsit(DBQuery query) {
		try {
			return baseDao.tableExist(query.getDatabaseName(),query.getSchemaName(),query.getTableName());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JSQLException(e);
		}finally{
			baseDao.closeConnection();
		}
	}

	public String[] listDatabase() {
		try {
			return baseDao.listDatabase();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JSQLException(e);
		}finally{
			baseDao.closeConnection();
		}
	}

	public Map<String,List<String>> getUnsupportTableColumn(DBQuery query,List<String> tables){
		Map<String,List<String>> columnMap = new HashMap<>();
		try{
			for(String tableName : tables){
				Map<String, Integer> typeMap;
				String schemaTable = query.getSchemaName() + "." + tableName;
				typeMap = baseDao.readTableMetaType(query.getDatabaseName(),schemaTable);
				List<String> unsupportList = new LinkedList<>();
				for(Entry<String, Integer> entry : typeMap.entrySet()){
					if(!DALTypeUtil.isSupport(entry.getValue())){
						unsupportList.add(entry.getKey());
					}
				}
				if(unsupportList.size() > 0){
					columnMap.put(tableName,unsupportList);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JSQLException(e);
		}finally{
			baseDao.closeConnection();
		}
		return columnMap;
		
	}

	@Override
	public String[] listSchema(String databaseName) {
		try {
			return baseDao.listSchema(databaseName);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JSQLException(e);
		}finally{
			baseDao.closeConnection();
		}
	}

	public String[] listTable(DBQuery query) {
		
		try {
			return StringUtils.isBlank(query.getSearchName()) ?  
					baseDao.listTable(query.getDatabaseName(),query.getSchemaName()) : 
					baseDao.listTable(query.getDatabaseName(),query.getSchemaName(),query.getSearchName());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JSQLException(e);
		}finally{
			baseDao.closeConnection();
		}
	}


	
}
