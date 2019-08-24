package com.datasphere.engine.manager.resource.provider.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.datasphere.reource.manager.module.datasource.dao.DataSourceDao;
import com.datasphere.common.corebase.BaseController;
import com.datasphere.common.utils.OkHttpRequest;
import com.datasphere.reource.manager.module.datasource.database.entity.DBCommonInfo;
import com.datasphere.reource.manager.module.datasource.domain.*;
import com.datasphere.reource.manager.module.datasource.domain.catalog.RequestParams;
import com.datasphere.reource.manager.module.datasource.domain.es.QueryDBDataParams;
import com.datasphere.reource.manager.module.datasource.domain.hbase.HbaseConnectionInfo;
import com.datasphere.reource.manager.module.datasource.domain.hive.HiveConnectionInfo;
import com.datasphere.reource.manager.module.datasource.service.DataSourceService;
import com.datasphere.reource.manager.common.constant.GlobalDefine;
import com.datasphere.resource.manager.utils.JsonWrapper;
import com.google.common.base.Splitter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.validation.Validated;
import io.reactivex.Single;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 数据源管理  DB
 */
//@Validated
public class reource.managerDataSourceController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(reource.managerDataSourceController.class);
	public static final String BASE_PATH = "/datasource";

	@Inject
	DataSourceService dataSourceService;

	/**
	 * 获得全部数据源信息 to
	 * @param
	 * @return
	 */
	@Post(BASE_PATH + "/listAll")
	public Single<Map<String,Object>> listAll(@Parameter Integer pageIndex,@Parameter Integer pageSize,@Parameter String name,HttpRequest request) {
		return Single.fromCallable(() -> {
			String token = request.getParameters().get("token");
			if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
			return JsonWrapper.successWrapper(dataSourceService.listAll(pageIndex,pageSize,name,token));
		});
	}

	/**
	 * 验证数据源名称 to
	 * @param name
	 * @return
	 */
	@Post(BASE_PATH + "/verifyDatasourceName")
	public Single<Map<String,Object>> verifyDatasourceName(@Parameter String name){
		return Single.fromCallable(() -> {
			if(StringUtils.isBlank(name)){
				return JsonWrapper.failureWrapper("名称不能为空!");
			}
			List<String> result = dataSourceService.verifyDatasourceName(name);
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
	@Post(BASE_PATH + "/update")
	public Single<Map<String,Object>> update(@Body DataSource dataSource){
		return Single.fromCallable(() -> {
			if (StringUtils.isBlank(dataSource.getId()) && StringUtils.isBlank(dataSource.getName())){
				return JsonWrapper.failureWrapper("id和数据源名称不能为空！");
			}
			//查询数据源信息ById
			DataSource dataSourceinfo = dataSourceService.findDataSourceById(dataSource.getId());
			if(dataSourceinfo == null){
				return JsonWrapper.failureWrapper("数据源不存在！");
			}

			if(dataSourceinfo.getName().equals((dataSource.getName()))){
				return JsonWrapper.failureWrapper("数据源名称已经存在！");
			}

			int result = dataSourceService.update(dataSource);
			if(result == 1){
				return  JsonWrapper.successWrapper("更新成功");
			}else{
				return  JsonWrapper.successWrapper("更新失败");
			}

		});
	}




	/**
	 * 查询数据库连接中的 DB 数据库和表信息(包含模糊查询功能) to
	 * @param info
	 * @return
	 */
	@Post(BASE_PATH + "/listTable")
	public Single<Map<String,Object>> listTableInfo(@Body DBCommonInfo info){
		return Single.fromCallable(() -> {
			List<DBTableInfodmp> dbTableInfodmps = dataSourceService.listTableInfo(info);
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
	@Post(BASE_PATH + "/createDatasource")
	public Single<Map<String,Object>> create(@Body DBDataSourceInfo dataSourceInfo, HttpRequest request){
		return Single.fromCallable(() -> {
			String token = request.getParameters().get("token");
			if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
            int result = dataSourceService.create(dataSourceInfo, token);
            if (result == 0){
                return JsonWrapper.failureWrapper("插入失败");
            }
            return JsonWrapper.successWrapper("插入成功");
		});
	}

    /**
     * 测试连接  DB 数据源  - on-presto   to create
     * @param connectionInfo
     * @return
     */
    @Post(BASE_PATH + "/testDatabase")
    public Single<Map<String,Object>> testDatabase(@Body DBConnectionInfo connectionInfo) {
		return Single.fromCallable(() -> {
            int result = dataSourceService.testDatabase(connectionInfo);
            if(result == 0){
                return JsonWrapper.failureWrapper("测试失败");
            }
            return JsonWrapper.successWrapper("测试成功");
		});
    }

	/**
	 * 查询 DB 数据源表中数据  - on-presto   to
	 * @param connectionInfo
	 * @return
	 */
	@Post(BASE_PATH + "/queryTableData")
	public Object queryTableData(@Body DBConnectionInfo connectionInfo){
		return Single.fromCallable(() -> {
			return JsonWrapper.successWrapper(dataSourceService.queryTableData(connectionInfo));
		});
	}

	/**0
	 * 数据查询：(on-presto + daas)
	 */
	@Post(BASE_PATH + "/queryTableDataById")
	public Single<Map<String,Object>> queryTableDataById(@Body DBQuery dbQuery){
		return Single.fromCallable(() -> {
			if (StringUtils.isBlank(dbQuery.getId())) return JsonWrapper.failureWrapper("id不能为空！");
			return JsonWrapper.successWrapper(dataSourceService.queryTableDataById(dbQuery));
		});
	}

	/**
	 * 查询单个 DB 数据源连接信息根据id   TODO
	 * @param id
	 * @return
	 */
	@Post(BASE_PATH + "/findConnectionById")
	public Single<Map<String,Object>> findConnectionById(@Parameter String id){
		return Single.fromCallable(() -> {
			//查询数据源信息ById
			DataSource dataSource = dataSourceService.findDataSourceById(id);
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
					Type listType = new TypeToken<HbaseConnectionInfo>(){}.getType();
					HbaseConnectionInfo conn = new Gson().fromJson(dataConfig, listType);
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
	 * 更新单个 DB 数据源    to
	 * @param dataSourceInfo
	 * @return
	 */
	@Post(BASE_PATH + "/updateDatasource")
	public Single<Map<String,Object>> updateDatasourceById(@Body DBDataSourceInfo dataSourceInfo, HttpRequest request){
		return Single.fromCallable(() -> {
			if(StringUtils.isBlank(dataSourceInfo.getId())){
				return JsonWrapper.failureWrapper("id不能为空！");
			}
			//TODO 根据不同的dataSource.getDataDSType()选择要更新
			//TODO 原有数据库是否被引用 -- 是否 可更新
			//TODO 查询数据库中有无数据源  验证名称是否重复
			String token = request.getParameters().get("token");
			if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
			int rsult = dataSourceService.updateDatasourceById(dataSourceInfo, token);
			if(rsult == 0){
				return JsonWrapper.failureWrapper("更新失败！");
			}
			return JsonWrapper.successWrapper();
		});
	}

	/**
	 * 查询单个数据源信息根据id     to
	 * @param id
	 * @return
	 */
	@Post(BASE_PATH + "/findDatasourceById")
	public Single<Map<String,Object>> findDatasourceById(@Parameter String id){
		return Single.fromCallable(() -> {
			//查询数据源信息ById
			DataSource dataSource = dataSourceService.findDataSourceById(id);
			dataSource.setDataConfig(null);
			return JsonWrapper.successWrapper(dataSource);
		});
	}



	/**
	 * 批量删除 根据id    to
	 * @param ids
	 * @return
	 */
	@Post(BASE_PATH + "/delete")
	public Single<Map<String,Object>> deleteDatasourceById(@Parameter String ids){
		return Single.fromCallable(() -> {
			//查询数据源信息ById
			int result = dataSourceService.deleteDatasourceById(ids);
			if(result == 0){
				return JsonWrapper.failureWrapper("删除失败");
			}
			return JsonWrapper.successWrapper(result);
		});
	}

	/**
	 * 查询已定阅资源     to
	 * @param requestParams
	 * @return
	 */
	@Post(BASE_PATH + "/subscribeDatasource")
	public Single<Map<String,Object>> getSubscribeDatasource(@Body RequestParams requestParams){
		return Single.fromCallable(() -> {
			return JsonWrapper.successWrapper(dataSourceService.getSubscribeDatasource(requestParams));
		});
	}


	//	type == 'SimpleDataSource' ? url = API.dataSourceDetail : url = API.getInstances;
	/** dataSourceDetail: '/dmp/datasource/dataSourceDetail'
	 * 通过组件实例id，获取数据源详细信息
	 * 触发操作：点击已经拖拽进去的组件 jeq
	 * @param id 组件实例id
	 * com.datalliance.common.dmpbase.constant.GlobalDefine
	 * 	组件代码code			组件分类classification（数据源、预处理、机器学习）
	 * MyDataSource			001
	 * SimpleDataSource		001
	 * DataPreProcess 		002	数据处理
	 * DataFilter  			002	数据过滤
	 * UNION  				002	并表
	 * Split  				002	拆分
	 */
	@Post(BASE_PATH + "/dataSourceDetail")
	public Object dataSourceDetail(@Parameter String id,@Parameter String code,@Parameter String classification, HttpRequest request) {
		return Single.fromCallable(() -> {
			if (!StringUtils.isBlank(id) && !StringUtils.isBlank(code) && !StringUtils.isBlank(classification)) {
				if (classification.startsWith("001") && code.equals("SimpleDataSource")) {
					String token = request.getParameters().get("token");
					if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
					return JsonWrapper.successWrapper(dataSourceService.findDataSourceDetail(id, token));//数据源Tree
				} else if (classification.startsWith("002")) { //
					return JsonWrapper.successWrapper(dataSourceService.dataPreProcess(id));//数据预处理组件
				} else {
					return JsonWrapper.successWrapper(dataSourceService.getInstance(id));//其他，暂时不做
				}
			} else {
				return JsonWrapper.failureWrapper("参数不能为空");
			}
		});
	}

	/** getInstances: '/dmp/dmp-dfc/component/instances/get'
	 * 根据id获得数据源信息
	 * @param id 数据源id
	 * @return
	 */
	@Post(BASE_PATH + "/get")
	public Object get(@Parameter String id, HttpRequest request) {
		return Single.fromCallable(() -> {
			if(!com.datasphere.reource.manager.module.dal.buscommon.utils.StringUtils.isBlank(id)) {
				String token = request.getParameters().get("token");
				if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
				DataSourceWithAll dataSource=dataSourceService.getWithPanel(id, token);
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
