package com.datasphere.engine.manager.resource.provider.database.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.datasphere.engine.manager.resource.provider.database.dao.OracleDao;
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
public class OracleDatabaseServiceImpl extends AbstractDatabaseService<OracleDao> implements DataSourceDatabaseService {

	/**
	 * 读取表所有数据，该方法用于DataSourceTableMigrationService中使用
	 * 
	 * @param query
	 */
	public List<List<DBTableField>> readTable(DBQuery query) {
		try {
			return baseDao.readTable(null,query.getTableName());
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
				typeMap = baseDao.readTableMetaType(null,tableName);
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
		}
		return columnMap;
		
	}
	
	public List<DBTableInfodmp> listTableInfo(DBQuery query){
		List<DBTableInfodmp> tbinfoList = new LinkedList<>();
		try{
			String[] tableNames = StringUtils.isBlank(query.getSearchName()) ?
					baseDao.listTable() : baseDao.listTable(query.getSearchName());
			
			for(String tableName : tableNames){
				DBTableInfodmp tbinfo = new DBTableInfodmp();
				int tableRows = baseDao.readTableRowcount(null, tableName);
				if(tableRows != 0){
					Map<String,Integer> typeMap = baseDao.readTableMetaType(null, tableName);
					/*			
					int columnSize = 0;
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

	public List<Map<String,String>> readTableWithColumnName(DBQuery query,Map<String,Integer> columnTypeMap) {
		try {
			return baseDao.readTableWithColumn(query.getTableName(), query.getPage(), query.getRows(),columnTypeMap);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JSQLException(e);
		}finally{
			baseDao.closeConnection();
		}
		
	}
	
	public boolean tableExsit(DBQuery query) {
		try {
			return baseDao.tableExist(query.getTableName());
		} catch (SQLException e) {
			throw new JSQLException(e);
		} finally{
			baseDao.closeConnection();
		}
	}

	public String[] listTable(DBQuery query) {
		try {
			return StringUtils.isBlank(query.getSearchName()) ?  
					baseDao.listTable() : 
					baseDao.listTable(query.getSearchName());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JSQLException(e);
		}finally{
			baseDao.closeConnection();
		}
	}

}
