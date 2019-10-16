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
import com.datasphere.engine.manager.resource.provider.db.model.DBCommonInfo;
import com.datasphere.engine.manager.resource.provider.hbase.model.HBaseConnectionInfo;
import com.datasphere.engine.manager.resource.provider.hive.model.HiveConnectionInfo;
import com.datasphere.engine.manager.resource.provider.model.*;
import com.datasphere.engine.manager.resource.provider.service.UDSMService;
import com.datasphere.engine.shaker.processor.definition.constant.GlobalConstant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.reactivex.Single;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 数据源管理  DB
 */
@Controller
public class DataSphereController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(DataSphereController.class);
	public static final String BASE_PATH = "/datasource1";

	@Autowired
	UDSMService uDSMService;

	/**
	 * 获得全部数据源信息
	 * @param
	 * @return
	 */
	@RequestMapping(value = BASE_PATH+"/listAll", method = RequestMethod.POST) 
	public Single<Map<String,Object>> listAll(@RequestParam Integer pageIndex,@RequestParam Integer pageSize,@RequestParam String name,HttpServletRequest request) {
		return Single.fromCallable(() -> {
			String token = request.getParameters().get("token");
			if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
			return JsonWrapper.successWrapper(uDSMService.listAll(pageIndex,pageSize,name,token));
		});
	}

	/**
	 * 验证数据源名称
	 * @param name
	 * @return
	 */
	@RequestMapping(value = BASE_PATH+"/verifyDatasourceName", method = RequestMethod.POST) 
	public Single<Map<String,Object>> verifyDatasourceName(@RequestParam String name){
		return Single.fromCallable(() -> {
			if(StringUtils.isBlank(name)){
				return JsonWrapper.failureWrapper("名称不能为空!");
			}

			List<String> result = uDSMService.verifyDatasourceName(name);

			if(result == null || result.size() == 0){
				return JsonWrapper.successWrapper("验证成功！");
			}else{
				//返回重复数据
				return JsonWrapper.failureWrapper(result.toString());
			}
		});
	}

	/**
	 * 更新数据源名称及描述
	 * @param dataSource
	 * @return
	 */
	@RequestMapping(value = BASE_PATH+"/update", method = RequestMethod.POST) 
	public Single<Map<String,Object>> update(@RequestBody DataSource dataSource){
		return Single.fromCallable(() -> {
			if (StringUtils.isBlank(dataSource.getId()) && StringUtils.isBlank(dataSource.getName())){
				return JsonWrapper.failureWrapper("id和数据源名称不能为空！");
			}
			//查询数据源信息ById
			DataSource dataSourceinfo = uDSMService.findDataSourceById(dataSource.getId());
			if(dataSourceinfo == null){
				return JsonWrapper.failureWrapper("数据源不存在！");
			}

			if(dataSourceinfo.getName().equals((dataSource.getName()))){
				return JsonWrapper.failureWrapper("数据源名称已经存在！");
			}

			int result = uDSMService.update(dataSource);
			if(result == 1){
				return  JsonWrapper.successWrapper("更新成功");
			}else{
				return  JsonWrapper.successWrapper("更新失败");
			}

		});
	}




	/**
	 * 查询数据库连接中的 DB 数据库和表信息(包含模糊查询功能)
	 * @param info
	 * @return
	 */
	@RequestMapping(value = BASE_PATH+"/listTable", method = RequestMethod.POST) 
	public Single<Map<String,Object>> listTableInfo(@RequestBody DBCommonInfo info){
		return Single.fromCallable(() -> {
			List<DBTableInfodmp> dbTableInfodmps = uDSMService.listTableInfo(info);
			if (dbTableInfodmps == null){
				return  JsonWrapper.failureWrapper("连接失败");
			}
			return JsonWrapper.successWrapper(dbTableInfodmps);
		});
	}

	/**
	 * 创建多个 DB 数据源    create
	 * @param dataSourceInfo
	 * @return
	 */
	@RequestMapping(value = BASE_PATH+"/createDatasource", method = RequestMethod.POST) 
	public Single<Map<String,Object>> create(@RequestBody DBDataSourceInfo dataSourceInfo, HttpServletRequest request){
		return Single.fromCallable(() -> {
			String token = request.getParameters().get("token");
			if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
            int result = uDSMService.create(dataSourceInfo,token);
            if (result == 0){
                return JsonWrapper.failureWrapper("插入失败");
            }
            return JsonWrapper.successWrapper("插入成功");
		});
	}

    /**
     * 测试连接  DB 数据源
     * @param connectionInfo
     * @return
     */
	@RequestMapping(value = BASE_PATH+"/testDatabase", method = RequestMethod.POST) 
    public Single<Map<String,Object>> testDatabase(@RequestBody DBConnectionInfo connectionInfo) {
		return Single.fromCallable(() -> {
            int result = uDSMService.testDatabase(connectionInfo);
            if(result == 0){
                return JsonWrapper.failureWrapper("测试失败");
            }
            return JsonWrapper.successWrapper("测试成功");
		});
    }

	/**
	 * 查询 DB 数据源表中数据  - on-presto
	 * @param connectionInfo
	 * @return
	 */
	@RequestMapping(value = BASE_PATH+"/queryTableData", method = RequestMethod.POST) 
	public Object queryTableData(@RequestBody DBConnectionInfo connectionInfo){
		return Single.fromCallable(() -> {
			return JsonWrapper.successWrapper(uDSMService.queryTableData(connectionInfo));
		});
	}


	/**
	 * 根据id查询 DB and COMPONENT 数据源数据
	 * @param dbQuery
	 * @return
	 */
	@RequestMapping(value = BASE_PATH+"/queryTableDataById", method = RequestMethod.POST) 
	public Single<Map<String,Object>> queryTableDataById(
			@RequestBody DBQuery dbQuery
			){

		return Single.fromCallable(() -> {
			if (StringUtils.isBlank(dbQuery.getId())){
				return JsonWrapper.failureWrapper("id不能为空！");
			}

			//根据不同的组件类型（数据源/组件）查询数据
			if(GlobalConstant.COMPONENT_CLASSIFICATION.MY_DATASOURCE.equals(dbQuery.getClassification())){
				//查询数据源信息ById
				DataSource dataSource = uDSMService.findDataSourceById(dbQuery.getId());
				if(dataSource == null){
					return JsonWrapper.failureWrapper("数据源不存在！");
				}
				String dataConfig = dataSource.getDataConfig();

				//TODO 根据不同的dataSource.getDataDSType()类型做不同的数据查询（mysql、oracle、hive...）

				Type listType = new TypeToken<DBConnectionInfo>(){}.getType();
				DBConnectionInfo dbConnectionInfo= new Gson().fromJson(dataConfig, listType);
				dbConnectionInfo.setTypeName(dataSource.getDataDSType());
				//测试连接信息是否可用
				if(uDSMService.testDatabase(dbConnectionInfo) == 0){
					return JsonWrapper.failureWrapper("数据库连接信息有变动，查询失败请更新！");
				}
				dbConnectionInfo.setQuery(dbQuery.getQuery());
				Map<String, Object> result = uDSMService.queryTableData(dbConnectionInfo);
				if(result != null){
					return JsonWrapper.successWrapper(result);
				}
				return JsonWrapper.failureWrapper("数据库信息查询失败，请检查表是否存在。");
			}else{
				//组件查询
				List<Object> dataList  = new ArrayList<Object>();
				List<Object> metaList  = new ArrayList<Object>();
				int rows=0;
				for (int i = 1; i <= 5; i++) {
					HashMap<String, String> array2= new HashMap<String,String>();
					array2.put("type", "VARCHAR");
					array2.put("Name","Name"+i);
					metaList.add(array2);
				}
				for (int i = 1; i <= 10; i++) {
					HashMap<String, String> array1= new HashMap<String,String>();
					array1.put("Name1","NAME"+i);
					array1.put("Name2","NAME"+i);
					array1.put("Name3","NAME"+i);
					array1.put("Name4","NAME"+i);
					array1.put("Name5","NAME"+i);
					dataList.add(array1);
					rows++;
				}
				HashMap<String, Object> result= new HashMap<String,Object>();
				result.put("tableName","tableName");
				result.put("meta",metaList);
				result.put("data",dataList);
				result.put("rows",rows);
				return JsonWrapper.successWrapper("查询失败");
			}
		});
	}

	/**
	 * 查询单个 DB 数据源连接信息根据id   TODO
	 * @param id
	 * @return
	 */
	@RequestMapping(value = BASE_PATH+"/findConnectionById", method = RequestMethod.POST) 
	public Single<Map<String,Object>> findConnectionById(@RequestParam String id){
		return Single.fromCallable(() -> {
			//查询数据源信息ById
			DataSource dataSource = uDSMService.findDataSourceById(id);
			if(dataSource == null){
				return JsonWrapper.failureWrapper("数据不存在");
			}
			String dataConfig = dataSource.getDataConfig();
			if(StringUtils.isBlank(dataConfig)){
				return JsonWrapper.failureWrapper("数据源连接信息不存在");
			}
			//TODO 根据不同的dataSource.getDataDSType()类型做不同的数据查询（mysql、oracle、hive...）不同的连接信息
			if(dataSource.getDataType() == 0){
				//转换
				Type listType = new TypeToken<DBConnectionInfo>(){}.getType();
				DBConnectionInfo dbConnectionInfo= new Gson().fromJson(dataConfig, listType);
				dbConnectionInfo.setTypeName(dataSource.getDataDSType());
				dbConnectionInfo.setName(dataSource.getName());
				dbConnectionInfo.setBusinessType(dataSource.getBusinessType());
				return JsonWrapper.successWrapper(dbConnectionInfo);
			}else if(dataSource.getDataType() == 1){
				return null;
			}else if(dataSource.getDataType() == 2){
				if("hive".equals(dataSource.getDataDSType())){
					Type listType = new TypeToken<HiveConnectionInfo>(){}.getType();
					HiveConnectionInfo conn = new Gson().fromJson(dataConfig, listType);
					conn.setTypeName(dataSource.getDataDSType());
					conn.setName(dataSource.getName());
					conn.setBusinessType(dataSource.getBusinessType());
					return JsonWrapper.successWrapper(conn);
				}else if("hbase".equals(dataSource.getDataDSType())){
					Type listType = new TypeToken<HBaseConnectionInfo>(){}.getType();
					HBaseConnectionInfo conn = new Gson().fromJson(dataConfig, listType);
					conn.setTypeName(dataSource.getDataDSType());
					conn.setName(dataSource.getName());
					conn.setBusinessType(dataSource.getBusinessType());
					return JsonWrapper.successWrapper(conn);
				}
				return null;
			}else{
				return JsonWrapper.failureWrapper("查询失败");
			}

		});
	}

	/**
	 * 更新单个 DB 数据源
	 * @param dataSourceInfo
	 * @return
	 */
	@RequestMapping(value = BASE_PATH+"/updateDatasource", method = RequestMethod.POST) 
	public Single<Map<String,Object>> updateDatasourceById(@RequestBody DBDataSourceInfo dataSourceInfo, HttpServletRequest request){
		return Single.fromCallable(() -> {
			if(StringUtils.isBlank(dataSourceInfo.getId())){
				return JsonWrapper.failureWrapper("id不能为空！");
			}
			//TODO 根据不同的dataSource.getDataDSType()选择要更新
			//TODO 原有数据库是否被引用 -- 是否 可更新
			//TODO 查询数据库中有无数据源  验证名称是否重复
			String token = request.getParameters().get("token");
			if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
			int rsult = uDSMService.updateDatasourceById(dataSourceInfo,token);
			if(rsult == 0){
				return JsonWrapper.failureWrapper("更新失败！");
			}
			return JsonWrapper.successWrapper();
		});
	}

	/**
	 * 查询单个数据源信息根据id
	 * @param id
	 * @return
	 */
	@RequestMapping(value = BASE_PATH+"/findDatasourceById", method = RequestMethod.POST) 
	public Single<Map<String,Object>> findDatasourceById(@RequestParam String id){
		return Single.fromCallable(() -> {
			//查询数据源信息ById
			DataSource dataSource = uDSMService.findDataSourceById(id);
			dataSource.setDataConfig(null);
			return JsonWrapper.successWrapper(dataSource);
		});
	}



	/**
	 * 批量删除 根据id
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = BASE_PATH+"/delete", method = RequestMethod.POST) 
	public Single<Map<String,Object>> deleteDatasourceById(@RequestParam String ids){
		return Single.fromCallable(() -> {
			//查询数据源信息ById
			int result = uDSMService.deleteDatasourceById(ids);
			if(result == 0){
				return JsonWrapper.failureWrapper("删除失败");
			}
			return JsonWrapper.successWrapper(result);
		});
	}

	/**
	 * 查询已定阅资源
	 * @param requestParams
	 * @return
	 */
	@RequestMapping(value = BASE_PATH+"/subscribeDatasource", method = RequestMethod.POST) 
	public Single<Map<String,Object>> getSubscribeDatasource(@RequestBody RequestParams requestParams){
		return Single.fromCallable(() -> {
			return JsonWrapper.successWrapper(uDSMService.getSubscribeDatasource(requestParams));
		});
	}


	//	type == 'SimpleDataSource' ? url = API.dataSourceDetail : url = API.getInstances;
	/** 
	 * 通过组件实例id，获取数据源详细信息
	 * 触发操作：点击已经拖拽进去的组件
	 * @param id 组件实例id
	 * 	组件代码code			组件分类classification（数据源、预处理、机器学习）
	 * MyDataSource			001
	 * SimpleDataSource		001
	 * DataPreProcess 		002	数据处理
	 * DataFilter  			002	数据过滤
	 * UNION  				002	并表
	 * Split  				002	拆分
	 */
	@RequestMapping(value = BASE_PATH+"/dataSourceDetail", method = RequestMethod.POST) 
	public Object dataSourceDetail(@RequestParam String id,@RequestParam String code,@RequestParam String classification, HttpServletRequest request) {
		return Single.fromCallable(() -> {
			if (!StringUtils.isBlank(id)) {
				if (!StringUtils.isBlank(code) && !StringUtils.isBlank(classification) && classification.startsWith("001") && code.equals("SimpleDataSource")) {
					String token = request.getParameters().get("token");
					if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
					return JsonWrapper.successWrapper(uDSMService.findDataSourceDetail(id,token));//数据源Tree
				} else if (!StringUtils.isBlank(code) && !StringUtils.isBlank(classification) && classification.startsWith("002") && code.equals("DataPreProcess")) { //
					return JsonWrapper.successWrapper(uDSMService.dataPreProcess(id));//数据预处理组件
				} else {
					return JsonWrapper.successWrapper(uDSMService.getInstance(id));//其他，暂时不做
				}
			} else {
				return JsonWrapper.failureWrapper("id参数不能为空");
			}
		});
	}

	/** 
	 * 根据id获得数据源信息
	 * @param id 数据源id
	 * @return
	 */
	@RequestMapping(value = BASE_PATH+"/get", method = RequestMethod.POST) 
	public Object get(@RequestParam String id, HttpServletRequest request) {
		return Single.fromCallable(() -> {
			if(!StringUtils.isBlank(id)) {
				String token = request.getParameters().get("token");
				if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
				DataSourceWithAll dataSource = uDSMService.getWithPanel(id,token);
				if(dataSource!=null){
					return JsonWrapper.successWrapper(dataSource);
				} else {
					return JsonWrapper.failureWrapper("没有该id的数据源");
				}
			} else {
				return JsonWrapper.failureWrapper("id参数不能为空");
			}
		});
	}

}
