package com.datasphere.engine.manager.resource.provider.db.service.impl;

import com.datasphere.common.data.Column;
import com.datasphere.common.data.Dataset;
import com.datasphere.engine.manager.resource.provider.db.model.DBTableField;
import com.datasphere.server.connections.service.DataAccessor;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DataSourcePlatformServiceImpl{

	@Autowired
	DataAccessor dataAccessor;
	
	public void write(String key,String tableName,List<List<DBTableField>> dataList) throws Exception {
		if(dataList == null || dataList.size() == 0){
			return ;
		}
		
		List<Column> colList = new LinkedList<>();
		for(DBTableField field : dataList.get(0)){
			Column column = new Column();
			column.setSourceType(field.getType());
			column.setName(field.getName());
			column.setType(field.getBusinessDataType());
			colList.add(column);
		}
		
		Dataset dataSet = new Dataset();
		dataSet.setColumnsMeta(colList.toArray(new Column[colList.size()]));
		dataSet.setCreateTime(new Date());
		dataSet.setData(new String[][]{});
		dataSet.setMessage(tableName);
		dataSet.setDataKey(key);
		dataAccessor.setDebugMode(true);
		dataAccessor.setDataset(dataSet);
	}
	
	public void add(String key,List<List<DBTableField>> dataList) throws Exception{
		if(dataList == null || dataList.size() == 0){
			return ;
		}
		
		String[][] data = new String[dataList.size()][dataList.get(0).size()];
		int i= 0 ;
		for(List<DBTableField> fieldList : dataList){
			int j = 0;
			for(DBTableField field : fieldList){
				data[i][j] = field.getValue();
				j++;
			}
			i++;
		}
		
		dataAccessor.append(key, data);
	}

	public Dataset read(String key, Integer pageNumber, Integer pageSize) throws Exception {
		return dataAccessor.getDataset(key, pageNumber,  pageSize);
	}
	
	public boolean remove(String key) throws Exception{
		return dataAccessor.removeDataset(key);
	}
	
	public long readSize(String key) throws Exception {
		//dal返回的数据大小单位为KB
		long dataSize = dataAccessor.getDatasetVolume(key);
		//此处返回数据大小为Byte
		return dataSize * 1024;
	}

	public Map<String, Integer> check(String[] words) {
		return dataAccessor.check(words);
	}

}
