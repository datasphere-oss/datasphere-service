/*
 * Copyright 2019, Huahuidata, Inc.
 * DataSphere is licensed under the Mulan PSL v1.
 * You can use this software according to the terms and conditions of the Mulan PSL v1.
 * You may obtain a copy of Mulan PSL v1 at:
 * http://license.coscl.org.cn/MulanPSL
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v1 for more details.
 */

package com.datasphere.engine.manager.resource.provider.db.controller;

import com.datasphere.core.common.BaseController;
import com.datasphere.engine.core.utils.JsonWrapper;
import com.datasphere.engine.manager.resource.provider.catalog.model.RequestParams;
import com.datasphere.engine.manager.resource.provider.db.service.DBOperationService;
import com.datasphere.engine.manager.resource.provider.elastic.model.DremioDataSourceInfo;
import com.datasphere.engine.manager.resource.provider.elastic.model.JSONInfo;
import com.datasphere.engine.manager.resource.provider.elastic.model.QueryDBDataParams;
import com.datasphere.engine.manager.resource.provider.elastic.model.Table;
import com.datasphere.engine.manager.resource.provider.model.*;
import com.datasphere.engine.manager.resource.provider.service.DaasService;
import com.google.common.base.Splitter;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import io.reactivex.Single;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * DataSource Management
 */
@Controller
public class DataSourceController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(DataSourceController.class);
	public static final String BASE_PATH = "/datasource/v1";

	@Autowired
	DaasService dataSourceService;
	@Autowired
	DBOperationService dbOperationService;


	/**
	 * Get all data source information
	 * @param
	 * @return
	 */
	@RequestMapping(value = BASE_PATH + "/listAll", method = RequestMethod.POST) 
	public Single<Map<String,Object>> listAll(
			@Parameter Integer pageIndex,
			@Parameter Integer pageSize,
			@Parameter String name
	) {
		return Single.fromCallable(() -> {
			return JsonWrapper.successWrapper(dataSourceService.listAll(pageIndex,pageSize,name));
		});
	}


	/**
	 * Verify the data source name
	 * @param name
	 * @return
	 */
	@RequestMapping(value = BASE_PATH + "/verifyDatasourceName", method = RequestMethod.POST) 
	public Single<Map<String,Object>> verifyDatasourceName(@Parameter String name){
		return Single.fromCallable(() -> {
			if(StringUtils.isBlank(name)) return JsonWrapper.failureWrapper("数据源名称不能为空!");
			List<String> nameList = Splitter.on("^^").splitToList(name);
			Map<String, String> result = dataSourceService.verifyDatasourceName(nameList);
			if(result.size() == 0) {
				return JsonWrapper.successWrapper("验证成功！");
			} else { //返回重复数据
				return JsonWrapper.failureWrapper(result);
			}
		});
	}

	/**
	 * Test data source
	 */
	@RequestMapping(value = BASE_PATH + "/test", method = RequestMethod.POST) 
	public Single<Map<String,Object>> test(@Body DremioDataSourceInfo es) {
		return Single.fromCallable(() -> {
			String es2 = dataSourceService.createSource(es);
			if (es2.contains("失败")) return JsonWrapper.failureWrapper(es2);
			String id;
			String name = null;
			try {
				JsonParser jsonParser = new JsonParser();
				JsonElement element = jsonParser.parse(es2);
				JsonObject jsonObj = element.getAsJsonObject();
				String propertyValue = jsonObj.get("id").toString();
				id = propertyValue.substring(1,propertyValue.length()-1);
				propertyValue = jsonObj.get("name").toString();
				name = propertyValue.substring(1,propertyValue.length()-1);
			} catch (Exception e) {
				id = null;
			}
			Map<String,String> map = new HashMap<>();
			map.put("daasId",id);
			map.put("name",name);
			if(es2 != null) return JsonWrapper.successWrapper(map);
			return JsonWrapper.failureWrapper("测试失败");
		});
	}


	/**
	 * When creating a data source Get database table information
	 * @param daasName
	 * @return
	 */
	@RequestMapping(value = BASE_PATH + "/listTable", method = RequestMethod.POST) 
	public Single<Map<String,Object>> listTable(@Parameter String daasName) {
		return Single.fromCallable(() -> {
			List<Map<String,String>> es2 = dataSourceService.listTable(daasName);
			if(es2!=null){
				return JsonWrapper.successWrapper(es2);
			}
			return JsonWrapper.failureWrapper("查询失败");
		});
	}


	/**
	 * Update data source name and description
	 * @param dataSource
	 * @return
	 */
	@RequestMapping(value = BASE_PATH + "/update", method = RequestMethod.POST) 
	public Single<Map<String,Object>> update(@Body DataSource dataSource){
		return Single.fromCallable(() -> {
			if (StringUtils.isBlank(dataSource.getId()) && StringUtils.isBlank(dataSource.getName())){
				return JsonWrapper.failureWrapper("The id and data source names cannot be empty!");
			}
			//查询数据源信息ById
			DataSource dataSourceInfo = dataSourceService.findDataSourceById(dataSource.getId());
			if(dataSourceInfo == null){
				return JsonWrapper.failureWrapper("The data source does not exist!");
			}
//			if(dataSourceinfo.getName().equals((dataSource.getName()))){
//				return JsonWrapper.failureWrapper("数据源名称已经存在！");
//			}
			int result = dataSourceService.update(dataSource);
			if(result == 1) return JsonWrapper.successWrapper("update completed");
			return JsonWrapper.successWrapper("Update failed");
		});
	}

	/**
	 * Create multiple DB data sources
	 * @param dataSourceInfo
	 * @return
	 */
	@RequestMapping(value = BASE_PATH + "/createDataSource", method = RequestMethod.POST) 
	public Single<Map<String,Object>> create(@Body DremioDataSourceInfo dataSourceInfo, HttpRequest request){
		return Single.fromCallable(() -> {
			String token = request.getParameters().get("token");
			if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
			// Verify that the name is duplicated
			List<String> names = new ArrayList<>();
			List<Table> tables = dataSourceInfo.getTables();
			for (Table table:tables) {
				names.add(table.getResourceName());
			}
			Map<String, String> verifyResult = dataSourceService.verifyDatasourceName(names);
			if(verifyResult.size() != 0) {
				return JsonWrapper.failureWrapper(verifyResult);// Insert failed! Data source name is duplicated!
			}
			//insert
			int result = dataSourceService.create(dataSourceInfo,token);
			if (result == 0) return JsonWrapper.failureWrapper("Insert failed");
			return JsonWrapper.successWrapper("Insert successfully");
		});
	}

	/**
	 * 查询数据源表中数据 
	 * @param query
	 * @return
	 */
	@RequestMapping(value = BASE_PATH + "/queryTableData", method = RequestMethod.POST) 
	public Object queryTableData(@Body QueryDBDataParams query){
		return Single.fromCallable(() -> {
			Map<String, Object> result = dataSourceService.queryTableData(query);
			if(result == null){
				return JsonWrapper.failureWrapper("The query failed, please check if the table exists!");
			}
			return JsonWrapper.successWrapper(result);
		});
	}


	/**
	 * Query DB and COMPONENT data source data based on id
	 * @param query
	 * @return
	 */
	@RequestMapping(value = BASE_PATH + "/queryTableDataById", method = RequestMethod.POST) 
	public Single<Map<String,Object>> queryTableDataById(@Body QueryDBDataParams query){
		return Single.fromCallable(() -> {
			Map<String, Object> result = null;
			if (!StringUtils.isBlank(query.getSql())) {
				// Get sql
				result = dataSourceService.queryTableData(query);
			}else{
				if (StringUtils.isBlank(query.getId())){
					return JsonWrapper.failureWrapper("id不能为空！");
				}
				// Query information based on id
				DataSource dataSource = dataSourceService.findDataSourceById(query.getId());
				Map<String, Object>  gsonMap = new Gson()
						.fromJson(dataSource.getDataConfig(), new TypeToken<Map<String, Object>>() {
						}.getType());
				query.setDatabaseName("POSTGRES".equals(dataSource.getDataDSType())?
						gsonMap.get("scheme").toString():gsonMap.get("databaseName").toString());
				query.setTableName(gsonMap.get("tableName").toString());
				
				String daasName = dataSourceService.getDaasNameByID(query.getId());
				query.setDaasName(daasName);
				if("CSV".equals(dataSource.getDataDSType()) || "JSON".equals(dataSource.getDataDSType())){
					query.setSql("SELECT * FROM \""+query.getDatabaseName()+"\".\""+query.getTableName()+"\"");
				} else {
					query.setSql("SELECT * FROM \""+daasName+"\".\""+query.getDatabaseName()+"\"."+query.getTableName()+"");
				}
				result = dataSourceService.queryTableData(query);
			}

			if(result != null){
				return JsonWrapper.successWrapper(result);
			}
			return JsonWrapper.failureWrapper("The query failed, please check if the table exists!");
		});
	}


	/**
	 * Query a single DB data source connection information based on id
	 * @param id
	 * @return
	 */
	@RequestMapping(value = BASE_PATH + "/findConnectionById", method = RequestMethod.POST) 
	public Single<Map<String,Object>> findConnectionById(@Parameter String id){
		return Single.fromCallable(() -> {
			DremioDataSourceInfo dataSource = dataSourceService.findDataSourceInfo(id);
			if(dataSource == null){
				return JsonWrapper.failureWrapper("Data does not exist");
			}
			return JsonWrapper.successWrapper(dataSource);
		});
	}

	/**
	 * Update a single DB data source
	 * @param source
	 * @return
	 */
	@RequestMapping(value = BASE_PATH + "/updateDatasource", method = RequestMethod.POST) 
	public Single<Map<String,Object>> updateDatasourceById(@Body DremioDataSourceInfo source){
		return Single.fromCallable(() -> {
			if(StringUtils.isBlank(source.getId())){
				return JsonWrapper.failureWrapper("Id can't be empty!");
			}

			int rsult = dataSourceService.updateDatasourceById(source);
			if(rsult == 0){
				return JsonWrapper.failureWrapper("Update failed!");
			}else{
				return JsonWrapper.successWrapper("update completed");
			}

		});
	}


	/**
	 * Query individual data source information based on id
	 * @param id
	 * @return
	 */
	@RequestMapping(value = BASE_PATH + "/findDatasourceById", method = RequestMethod.POST) 
	public Single<Map<String,Object>> findDatasourceById(@Parameter String id){
		return Single.fromCallable(() -> {
			DataSource dataSource = dataSourceService.findDataSourceById(id);
			dataSource.setDataConfig(null);
			return JsonWrapper.successWrapper(dataSource);
		});
	}

	/**
	 * Batch delete based on id
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = BASE_PATH + "/delete", method = RequestMethod.POST) 
	public Single<Map<String,Object>> deleteDatasourceById(@Parameter String ids){
		return Single.fromCallable(() -> {
			int result = dataSourceService.deleteDatasourceById(ids);
			if(result == 0){
				return JsonWrapper.failureWrapper("failed to delete");
			}
			if(result == 2){
				return JsonWrapper.failureWrapper("Cannot delete a used data source");
			}
			return JsonWrapper.successWrapper(result);
		});
	}


	/**
	 * Query scheduled resources (resource directory)
	 * @param requestParams
	 * @return
	 */
	@RequestMapping(value = BASE_PATH + "/subscribeDatasource", method = RequestMethod.POST) 
	public Single<Map<String,Object>> getSubscribeDatasource(@Body RequestParams requestParams){
		return Single.fromCallable(() -> {
			return JsonWrapper.successWrapper(dataSourceService.getSubscribeDatasource(requestParams));
		});
	}


	//	type == 'SimpleDataSource' ? url = API.dataSourceDetail : url = API.getInstances;
	/** 
	 * Get the data source details through the component instance id.
	 * Action: Click on the component that has been dragged into the panel
	 * @param id Component instance id
	 * Component code       component classification (data source, preprocessing, machine learning)
	 * MyDataSource			001
	 * SimpleDataSource		001
	 * DataPreProcess 		002	data processing
	 * DataFilter  			002	Data filtering
	 * UNION  				002	union table
	 * Split  				002	Split table
	 */
	@RequestMapping(value = BASE_PATH + "/dataSourceDetail", method = RequestMethod.POST) 
	public Object dataSourceDetail(@Parameter String id,@Parameter String code,@Parameter String classification,HttpRequest request) {
		return Single.fromCallable(() -> {
			if (!StringUtils.isBlank(id) && !StringUtils.isBlank(code) && !StringUtils.isBlank(classification)) {
				if ("001".equals(classification) && "SimpleDataSource".equals(code)) {
					String token = request.getParameters().get("token");
					if (token == null) return JsonWrapper.failureWrapper("The token cannot be empty!");
					return JsonWrapper.successWrapper(dataSourceService.findDataSourceDetail(id, token));
				} else if ("002".equals(classification)) { //
					return JsonWrapper.successWrapper(dataSourceService.dataPreProcess(id));
				} else {
					return JsonWrapper.failureWrapper("Other types of components are not supported at this time");
//					return JsonWrapper.successWrapper(dataSourceService.getInstance(id));
				}
			} else {
				return JsonWrapper.failureWrapper("Parameter cannot be empty");
			}
		});
	}

	/** 
	 * Get datasource information based on id
	 * @param id datasource id
	 * @return
	 */
	@RequestMapping(value = BASE_PATH + "/get", method = RequestMethod.POST) 
	public Object get(@Parameter String id,HttpRequest request) {
		return Single.fromCallable(() -> {
			if(!StringUtils.isBlank(id)) {
				String token = request.getParameters().get("token");
				if (token == null) return JsonWrapper.failureWrapper("The token cannot be empty!");
				DataSourceWithAll dataSource=dataSourceService.getWithPanel(id, token);
				if(dataSource!=null){
					return JsonWrapper.successWrapper(dataSource);
				} else {
					return JsonWrapper.failureWrapper("No data source for this id");
				}
			} else {
				return JsonWrapper.failureWrapper("The id parameter cannot be empty");
			}
		});
	}


	/**
	 * Preview JSON file content
	 * @param file
	 * @return
	 */
//	@Post(value = BASE_PATH + "/previewUnsaved", consumes = MediaType.MULTIPART_FORM_DATA)
	@RequestMapping(value = BASE_PATH + "/previewUnsaved", method = RequestMethod.POST) 
	public Single<Map<String,Object>> previewUnsaved(CompletedFileUpload file) {
		return Single.fromCallable(() -> {
				return JsonWrapper.successWrapper(dataSourceService.uploadStart(file));
		});
	}

	/**
	 * JSON Finsh
	 * @param JSONInfo
	 * @return
	 */
	@RequestMapping(value = BASE_PATH + "/uploadJSON", method = RequestMethod.POST) 
	public Single<Map<String,Object>> upload(@Body JSONInfo JSONInfo,HttpRequest request) {
		return Single.fromCallable(() -> {
			String token = request.getParameters().get("token");
			if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
			return JsonWrapper.successWrapper(dataSourceService.uploadFinish(JSONInfo, token));
		});
	}

	/**
	 * update JSON
	 * @param JSONInfo
	 * @return
	 */
	@RequestMapping(value = BASE_PATH + "/updateJSON", method = RequestMethod.POST) 
	public Single<Map<String,Object>> updateJSON(@Body JSONInfo JSONInfo,HttpRequest request) {
		return Single.fromCallable(() -> {
			String token = request.getParameters().get("token");
			if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
			if(dataSourceService.updateJSON(JSONInfo, token) != 0){
				return JsonWrapper.successWrapper("更新成功");
			}
			return JsonWrapper.failureWrapper("更新失败");
		});
	}

//	/**
//	 * delete JSON
//	 * @param JSONInfo
//	 * @return
//	 */
//	@Post("/deleteJSON")
//	public Single<Map<String,Object>> deleteJSON(@Body JSONInfo JSONInfo) {
//		return Single.fromCallable(() -> {
//			return JsonWrapper.successWrapper(dataSourceService.updateJSON(JSONInfo));
//		});
//	}

	/**
	 * Preview CSV file content
	 * @param file
	 * @return
	 */
	@Post(value = BASE_PATH + "/previewUnsavedCSV", consumes = MediaType.MULTIPART_FORM_DATA)
	@RequestMapping(value = BASE_PATH + "/previewUnsavedCSV", method = RequestMethod.POST) 

	public Single<Map<String,Object>> previewUnsavedCSV(CompletedFileUpload file) {
		return Single.fromCallable(() -> {
//			if(dataSourceService.test(file)!=1){
//			System.out.println(JSON.parse(dataSourceService.uploadStart(file)));
			return JsonWrapper.successWrapper(dataSourceService.uploadStartCSV(file));
//			}
//			return JsonWrapper.failureWrapper("上传失败");
		});
	}

	/**
	 * CSV Finish
	 * @param JSONInfo
	 * @return
	 */
	@RequestMapping(value = BASE_PATH + "/uploadCSV", method = RequestMethod.POST) 

	public Single<Map<String,Object>> uploadCSV(@Body JSONInfo JSONInfo,HttpRequest request) {
		return Single.fromCallable(() -> {
			String token = request.getParameters().get("token");
			if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
			return JsonWrapper.successWrapper(dataSourceService.uploadFinishCSV(JSONInfo, token));
		});
	}

	/**
	 * update CSV
	 * @param JSONInfo
	 * @return
	 */
	@RequestMapping(value = BASE_PATH + "/updateCSV", method = RequestMethod.POST) 

	public Single<Map<String,Object>> updateCSV(@Body JSONInfo JSONInfo,HttpRequest request) {
		return Single.fromCallable(() -> {
			String token = request.getParameters().get("token");
			if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
			return JsonWrapper.successWrapper(dataSourceService.updateCSV(JSONInfo, token));
		});
	}


	@RequestMapping(value = BASE_PATH + "/listFile", method = RequestMethod.POST) 
	public Single<Map<String,Object>> listJSON(@Parameter String id) {
		return Single.fromCallable(() -> {
			return JsonWrapper.successWrapper(dataSourceService.listJSON(id));
		});
	}
}
