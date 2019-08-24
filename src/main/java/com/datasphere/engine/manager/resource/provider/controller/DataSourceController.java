package com.datasphere.engine.manager.resource.provider.controller;

import com.datasphere.core.common.BaseController;
import com.datasphere.engine.core.utils.JsonWrapper;
import com.datasphere.engine.manager.resource.provider.catalog.model.RequestParams;
import com.datasphere.engine.manager.resource.provider.database.service.DBOperationService;
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
import io.micronaut.context.annotation.Parameter;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.reactivex.Single;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 数据源管理  DB
 */
//@Validated
public class DataSourceController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(DataSourceController.class);
	public static final String BASE_PATH = "/datasource/v1";

	@Inject
	DaasService dataSourceService;
	@Inject
	DBOperationService dbOperationService;


	/**
	 * 获得全部数据源信息
	 * @param
	 * @return
	 */
	@Post(BASE_PATH + "/listAll")
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
	 * 验证数据源名称
	 * @param name
	 * @return
	 */
	@Post(BASE_PATH + "/verifyDatasourceName")
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
	 * 测试数据源  (在daas创建)
	 */
	@Post(BASE_PATH + "/test")
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
	 * 创建数据源时 获取数据库表信息
	 * @param daasName
	 * @return
	 */
	@Post(BASE_PATH + "/listTable")
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
			DataSource dataSourceInfo = dataSourceService.findDataSourceById(dataSource.getId());
			if(dataSourceInfo == null){
				return JsonWrapper.failureWrapper("数据源不存在！");
			}
//			if(dataSourceinfo.getName().equals((dataSource.getName()))){
//				return JsonWrapper.failureWrapper("数据源名称已经存在！");
//			}
			int result = dataSourceService.update(dataSource);
			if(result == 1) return JsonWrapper.successWrapper("更新成功");
			return JsonWrapper.successWrapper("更新失败");
		});
	}

	/**
	 * 创建多个 DB 数据源    create pg数据库
	 * @param dataSourceInfo
	 * @return
	 */
	@Post(BASE_PATH + "/createDataSource")
	public Single<Map<String,Object>> create(@Body DremioDataSourceInfo dataSourceInfo, HttpRequest request){
		return Single.fromCallable(() -> {
			String token = request.getParameters().get("token");
			if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
			//验证名称是否重复
			List<String> names = new ArrayList<>();
			List<Table> tables = dataSourceInfo.getTables();
			for (Table table:tables) {
				names.add(table.getResourceName());
			}
			Map<String, String> verifyResult = dataSourceService.verifyDatasourceName(names);
			if(verifyResult.size() != 0) {
				return JsonWrapper.failureWrapper(verifyResult);//"插入失败!数据源名称重复！"
			}
			//insert
			int result = dataSourceService.create(dataSourceInfo,token);
			if (result == 0) return JsonWrapper.failureWrapper("插入失败");
			return JsonWrapper.successWrapper("插入成功");
		});
	}

	/**
	 * 查询 DB 数据源表中数据  - daas
	 * @param query
	 * @return
	 */
	@Post(BASE_PATH + "/queryTableData")
	public Object queryTableData(@Body QueryDBDataParams query){
		return Single.fromCallable(() -> {
			Map<String, Object> result = dataSourceService.queryTableData(query);
			if(result == null){
				return JsonWrapper.failureWrapper("查询失败，请检查表是否存在!");
			}
			return JsonWrapper.successWrapper(result);
		});
	}


	/**
	 * 根据id查询 DB and COMPONENT 数据源数据 - daas
	 * @param query
	 * @return
	 */
	@Post(BASE_PATH + "/queryTableDataById")
	public Single<Map<String,Object>> queryTableDataById(@Body QueryDBDataParams query){
		return Single.fromCallable(() -> {
			Map<String, Object> result = null;
			if (!StringUtils.isBlank(query.getSql())) {
				//获取sql
				result = dataSourceService.queryTableData(query);
			}else{
				if (StringUtils.isBlank(query.getId())){
					return JsonWrapper.failureWrapper("id不能为空！");
				}
				//根据id查询信息
				DataSource dataSource = dataSourceService.findDataSourceById(query.getId());
				Map<String, Object>  gsonMap = new Gson()
						.fromJson(dataSource.getDataConfig(), new TypeToken<Map<String, Object>>() {
						}.getType());
				query.setDatabaseName("POSTGRES".equals(dataSource.getDataDSType())?
						gsonMap.get("scheme").toString():gsonMap.get("databaseName").toString());
				query.setTableName(gsonMap.get("tableName").toString());
				//查询daasName
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
			return JsonWrapper.failureWrapper("查询失败，请检查表是否存在!");
		});
	}


	/**
	 * 查询单个 DB 数据源连接信息根据id
	 * @param id
	 * @return
	 */
	@Post(BASE_PATH + "/findConnectionById")
	public Single<Map<String,Object>> findConnectionById(@Parameter String id){
		return Single.fromCallable(() -> {
			//查询数据源信息ById
			DremioDataSourceInfo dataSource = dataSourceService.findDataSourceInfo(id);
			if(dataSource == null){
				return JsonWrapper.failureWrapper("数据不存在");
			}
			return JsonWrapper.successWrapper(dataSource);
		});
	}

	/**
	 * 更新单个 DB 数据源
	 * @param source
	 * @return
	 */
	@Post(BASE_PATH + "/updateDatasource")
	public Single<Map<String,Object>> updateDatasourceById(@Body DremioDataSourceInfo source){
		return Single.fromCallable(() -> {
			if(StringUtils.isBlank(source.getId())){
				return JsonWrapper.failureWrapper("id不能为空！");
			}

			int rsult = dataSourceService.updateDatasourceById(source);
			if(rsult == 0){
				return JsonWrapper.failureWrapper("更新失败！");
			}else{
				return JsonWrapper.successWrapper("更新成功");
			}

		});
	}


	/**
	 * 查询单个数据源信息根据id
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
	 * 批量删除 根据id
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
			if(result == 2){
				return JsonWrapper.failureWrapper("数据源被引用,无法删除");
			}
			return JsonWrapper.successWrapper(result);
		});
	}


	/**
	 * 查询已定阅资源（资源目录）
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
	public Object dataSourceDetail(@Parameter String id,@Parameter String code,@Parameter String classification,HttpRequest request) {
		return Single.fromCallable(() -> {
			if (!StringUtils.isBlank(id) && !StringUtils.isBlank(code) && !StringUtils.isBlank(classification)) {
				if ("001".equals(classification) && "SimpleDataSource".equals(code)) {
					String token = request.getParameters().get("token");
					if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
					return JsonWrapper.successWrapper(dataSourceService.findDataSourceDetail(id, token));//数据源Tree
				} else if ("002".equals(classification)) { //
					return JsonWrapper.successWrapper(dataSourceService.dataPreProcess(id));//数据预处理组件
				} else {
					return JsonWrapper.failureWrapper("暂不支持其他类型组件");
//					return JsonWrapper.successWrapper(dataSourceService.getInstance(id));//其他，暂时不做
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
	public Object get(@Parameter String id,HttpRequest request) {
		return Single.fromCallable(() -> {
			if(!com.datalsphere.drmp.module.dal.buscommon.utils.StringUtils.isBlank(id)) {
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


	/**
	 * 预览JSON文件内容
	 * @param file
	 * @return
	 */
	@Post(value = BASE_PATH + "/previewUnsaved", consumes = MediaType.MULTIPART_FORM_DATA)
	public Single<Map<String,Object>> previewUnsaved(CompletedFileUpload file) {
		return Single.fromCallable(() -> {
				return JsonWrapper.successWrapper(dataSourceService.uploadStart(file));
		});
	}

	/**
	 * JSONFinsh方法
	 * @param JSONInfo
	 * @return
	 */
	@Post(BASE_PATH + "/uploadJSON")
	public Single<Map<String,Object>> upload(@Body JSONInfo JSONInfo,HttpRequest request) {
		return Single.fromCallable(() -> {
			String token = request.getParameters().get("token");
			if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
			return JsonWrapper.successWrapper(dataSourceService.uploadFinish(JSONInfo, token));
		});
	}

	/**
	 * 更新JSON
	 * @param JSONInfo
	 * @return
	 */
	@Post(BASE_PATH + "/updateJSON")
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
//	 * 删除JSON
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
	 * 预览CSV文件内容
	 * @param file
	 * @return
	 */
	@Post(value = BASE_PATH + "/previewUnsavedCSV", consumes = MediaType.MULTIPART_FORM_DATA)
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
	 * CSV Finish方法
	 * @param JSONInfo
	 * @return
	 */
	@Post(BASE_PATH + "/uploadCSV")
	public Single<Map<String,Object>> uploadCSV(@Body JSONInfo JSONInfo,HttpRequest request) {
		return Single.fromCallable(() -> {
			String token = request.getParameters().get("token");
			if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
			return JsonWrapper.successWrapper(dataSourceService.uploadFinishCSV(JSONInfo, token));
		});
	}

	/**
	 * 更新CSV
	 * @param JSONInfo
	 * @return
	 */
	@Post(BASE_PATH + "/updateCSV")
	public Single<Map<String,Object>> updateCSV(@Body JSONInfo JSONInfo,HttpRequest request) {
		return Single.fromCallable(() -> {
			String token = request.getParameters().get("token");
			if (token == null) return JsonWrapper.failureWrapper("token不能为空！");
			return JsonWrapper.successWrapper(dataSourceService.updateCSV(JSONInfo, token));
		});
	}

//	@Post(value = "/preview_unsaved", consumes = MediaType.MULTIPART_FORM_DATA)
//	public Single<Map<String,Object>> upload(CompletedFileUpload file,String name) {
//		return Single.fromCallable(() -> {
//			System.out.println(name);
////			if(dataSourceService.test(file)!=1){
//			return JsonWrapper.successWrapper(dataSourceService.uploadStart(file));
////			}
////			return JsonWrapper.failureWrapper("上传失败");
//		});
//	}

	@Post(BASE_PATH + "/listFile")
	public Single<Map<String,Object>> listJSON(@Parameter String id) {
		return Single.fromCallable(() -> {
			return JsonWrapper.successWrapper(dataSourceService.listJSON(id));
		});
	}
}
