package com.datasphere.engine.manager.resource.provider.database.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.datasphere.engine.manager.resource.provider.database.dao.MySQLDao;
import com.datasphere.engine.manager.resource.provider.database.exception.JSQLException;
import com.datasphere.engine.manager.resource.provider.database.model.DBQuery;
import com.datasphere.engine.manager.resource.provider.database.model.DBTableField;
import com.datasphere.engine.manager.resource.provider.database.service.AbstractDatabaseService;
import com.datasphere.engine.manager.resource.provider.database.service.DataSourceDatabaseService;
import com.datasphere.engine.manager.resource.provider.database.util.DALTypeUtil;
import com.datasphere.engine.manager.resource.provider.model.DBTableInfodmp;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Service
public class MySQLDatabaseServiceImpl extends AbstractDatabaseService<MySQLDao> implements DataSourceDatabaseService {

	public boolean tableExsit(DBQuery query) {
		try {
			return baseDao.tableExist(query.getDatabaseName(),query.getTableName());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JSQLException(e);
		}finally{
			baseDao.closeConnection();
		}
	}

	public List<DBTableInfodmp> listTableInfo(DBQuery query) {
		List<DBTableInfodmp> tbinfoList = new LinkedList<>();
		String databaseName = query.getDatabaseName();
		try{
			String[] tableNames = StringUtils.isBlank(query.getSearchName()) ?
					baseDao.listTable(databaseName) : baseDao.listTable(databaseName,query.getSearchName());
			
			for(String tableName : tableNames){
				DBTableInfodmp tbinfo = new DBTableInfodmp();
				int tableRows = baseDao.readTableRowcount(databaseName,tableName);
				if(tableRows != 0){
					Map<String,Integer> typeMap = baseDao.readTableMetaType(databaseName, tableName);
	/*				int columnSize = 0;
					for(Integer type : typeMap.values()){
						if(DALTypeUtil.isSupport(type)){
							columnSize ++;
						}
					}
					if(columnSize > 0){
						tbinfo.setColumns(typeMap.size());
						tbinfo.setRows(tableRows);
						tbinfo.setName(tableName);
						tbinfoList.add(tbinfo);
					}*/
					tbinfo.setColumns(typeMap.size());
					tbinfo.setRows(tableRows);
					tbinfo.setName(tableName);
					tbinfoList.add(tbinfo);
				}else{
					Map<String,Integer> typeMap = baseDao.readTableMetaType(databaseName, tableName);
					/*				int columnSize = 0;
									for(Integer type : typeMap.values()){
										if(DALTypeUtil.isSupport(type)){
											columnSize ++;
										}
									}
									if(columnSize > 0){
										tbinfo.setColumns(typeMap.size());
										tbinfo.setRows(tableRows);
										tbinfo.setName(tableName);
										tbinfoList.add(tbinfo);
									}*/
									tbinfo.setColumns(typeMap.size());
									tbinfo.setRows(0);
									tbinfo.setName(tableName);
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

	@Override
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
	
	public String[] listTables(String databaseName) {
		try {
			return baseDao.listTable(databaseName);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JSQLException(e);
		}finally{
			baseDao.closeConnection();
		}
	}

	public List<Map<String, String>> readTableWithColumnName(DBQuery query,Map<String,Integer> columnTypeMap) {
		try {
			return baseDao.readTableWithColumn(query.getDatabaseName(),query.getTableName(), query.getPage(), query.getRows(),columnTypeMap);
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
				Map<String, Integer> typeMap = baseDao.readTableMetaType(query.getDatabaseName(),tableName);
				List<String> unsupportList = new LinkedList<>();
				typeMap.entrySet();
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
	public List<List<DBTableField>> readTable(DBQuery query) {
		try {
			return baseDao.readTable(query.getDatabaseName(),query.getTableName());
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
					baseDao.listTable(query.getDatabaseName()) : 
					baseDao.listTable(query.getDatabaseName(),query.getSearchName());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JSQLException(e);
		}finally{
			baseDao.closeConnection();
		}
	}
}
