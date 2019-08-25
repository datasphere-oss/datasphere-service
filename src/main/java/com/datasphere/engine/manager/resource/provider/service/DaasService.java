package com.datasphere.engine.manager.resource.provider.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.datasphere.common.utils.OkHttpRequest;
import com.datasphere.common.utils.PageUtil;
import com.datasphere.common.utils.RandomUtils;
import com.datasphere.core.common.BaseService;
import com.datasphere.engine.common.exception.JIllegalOperationException;
import com.datasphere.engine.common.exception.http.HttpClientException;
import com.datasphere.engine.core.utils.JAssert;
import com.datasphere.engine.manager.resource.provider.catalog.model.RequestParams;
import com.datasphere.engine.manager.resource.provider.dao.DataSourceDao;
import com.datasphere.engine.manager.resource.provider.elastic.model.CSVInfo;
import com.datasphere.engine.manager.resource.provider.elastic.model.DremioDataSourceInfo;
import com.datasphere.engine.manager.resource.provider.elastic.model.JSONInfo;
import com.datasphere.engine.manager.resource.provider.elastic.model.QueryDBDataParams;
import com.datasphere.engine.manager.resource.provider.model.*;
import com.datasphere.engine.shaker.processor.common.constant.ComponentClassification;
import com.datasphere.engine.shaker.processor.definition.ComponentDefinition;
import com.datasphere.engine.shaker.processor.definition.dao.ComponentDefinitionDao;
import com.datasphere.engine.shaker.processor.instance.dao.ComponentInstanceDao;
import com.datasphere.engine.shaker.processor.instance.model.ComponentInstance;
import com.datasphere.engine.shaker.processor.instance.model.ComponentInstanceRelation;
import com.datasphere.engine.shaker.processor.instance.service.ComponentInstanceRelationService;
import com.datasphere.engine.shaker.processor.instance.service.ComponentInstanceService;
import com.datasphere.server.connections.dao.DataSetInstanceDao;
import com.datasphere.server.connections.model.DataSetInstance;
import com.datasphere.server.connections.utils.StringUtils;
import com.datasphere.server.sso.service.DSSUserTokenService;
import com.google.common.base.Splitter;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import org.apache.ibatis.session.SqlSession;
import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;
import org.mozilla.intl.chardet.nsPSMDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * 数据源管理
 */
@Service
public class DaasService extends BaseService {
	private static final Logger log = LoggerFactory.getLogger(DaasService.class);
	private static final OkHttpClient httpClient = new OkHttpClient.Builder()
			.connectTimeout(10, TimeUnit.SECONDS)
			.readTimeout(10, TimeUnit.SECONDS)
			.writeTimeout(10, TimeUnit.SECONDS).build();
	private static final MediaType ESJSON = MediaType.parse("application/json; charset=utf-8");
	private static final String AUTO = "auto_";
	@Autowired
	ExchangeSSOService exchangeSSOService;

	@Autowired
	ComponentInstanceService ciService;
	@Autowired
	ComponentInstanceRelationService cirService;
	@Autowired
	DataQueryService dataQueryService;
	@Autowired
	DSSUserTokenService dSSUserTokenService;

	/**
	 * 获得全部数据源信息
	 *
	 * @return
	 */
	public Map<String, Object> listAll(Integer pageIndex, Integer pageSize, String name
	) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			DataSourceDao dataSourceDao = sqlSession.getMapper(DataSourceDao.class);
			ComponentInstanceDao componentInstanceDao = sqlSession.getMapper(ComponentInstanceDao.class);
			Map<String, Object> map = new HashMap<>();
			map.put("name", name);
			map.put("pageIndex", PageUtil.getPageStart(pageIndex, pageSize));
			map.put("pageSize", pageSize);
			map.put("dataFrom", "DataWave");
			Integer count = dataSourceDao.count(map);
			Map<String, Object> map1 = new HashMap<>();
			List<DataSource> sourceList = dataSourceDao.listAll(map);
			for (int i = 0; i <sourceList.size() ; i++) {
				DataSource dataSource = sourceList.get(i);
				dataSource.setDataQuote(componentInstanceDao.getCountByCFId(sourceList.get(i).getId()));
			}
			map1.put("list", dataSourceDao.listAll(map));
			map1.put("totalRecords", count);
			return map1;
		}
	}


	/**
	 * 创建多个数据源
	 *
	 * @param daas
	 * @return
	 */
	public int create(DremioDataSourceInfo daas, String token) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			//数据源
			DataSourceDao dataSourceDao = sqlSession.getMapper(DataSourceDao.class);
			//组建定义
			ComponentDefinitionDao dictionaryDao = sqlSession.getMapper(ComponentDefinitionDao.class);
			int result = 0;
			if (daas.getTables() != null && daas.getTables().size() > 0) {
				for (int i = 0; i < daas.getTables().size(); i++) {
					DataSource dataSource = new DataSource();
					String id = UUID.randomUUID().toString();
					dataSourceDao.insertDS(daas.getDaasId(), id, daas.getName());
					dataSource.setId(id);
					dataSource.setName(daas.getTables().get(i).getResourceName());
					dataSource.setDataDesc(daas.getTables().get(i).getResourceDesc());
					dataSource.setBusinessType(daas.getBusinessType());
					if ("MEMSQL".equals(daas.getSpareType())) {
						daas.setType("MEMSQL");
					}
					dataSource.setDataDSType(daas.getType());
					dataSource.setDataType(0);
					dataSource.setDataFrom("DataWave");
					dataSource.setClassification("001");
					dataSource.setCode("SimpleDataSource");
					dataSource.setCreateTime(new Date());
					dataSource.setCreator(exchangeSSOService.getAccount(token));

					//数据连接信息
					Map<String, Object> gsonMap = new Gson()
														  .fromJson(new Gson().toJson(daas.getConfig()), new TypeToken<Map<String, Object>>() {
														  }.getType());

					if("POSTGRES".equals(daas.getType())){
						gsonMap.put("scheme", daas.getTables().get(i).getDatabaseName());
					}else{
						gsonMap.put("databaseName", daas.getTables().get(i).getDatabaseName());
					}
					gsonMap.put("tableName", daas.getTables().get(i).getTableName());
					dataSource.setDataConfig(new Gson().toJson(gsonMap));

					//新增组件定义
					ComponentDefinition cd = new ComponentDefinition();
					cd.setId(id);
					cd.setCode(ComponentClassification.SimpleDataSource.name());
					cd.setName(daas.getTables().get(i).getResourceName());
					cd.setClassification("001");
					cd.setCreator(exchangeSSOService.getAccount(token));
					cd.setParams("");
					dictionaryDao.insert(cd);
					//新增数据源
					dataSourceDao.insert(dataSource);
					result++;
				}
				sqlSession.commit();
			}
			return result;
		}
	}

	/*public synchronized void createDaas(DaasDataSourceInfo daas) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			DataSourceDao dataSourceDao = sqlSession.getMapper(DataSourceDao.class);
			if (daas.getDaasId() != null && daas.getName() != null) {
				dataSourceDao.insertDS(daas.getDaasId(), process_cd_id, daas.getName());


				sqlSession.commit();
			}
		}
	}*/


	/**
	 * 查询数据集--daas
	 *
	 * @param query
	 * @return
	 */
	public Map<String, Object> queryTableData(QueryDBDataParams query) {
		try {
			if (StringUtils.isBlank(query.getSql())) {
				//获取sql
				query.setSql(oneTableQuery(query));
			}
			if (StringUtils.isBlank(query.getSql())) {
				return null;
			}

			//jdbc查询方式
			 return dataQueryService.dataQuery(query.getSql());
			} catch (Exception e) {
				log.error("{}", e);
			}
		return null;
	}

	//获取sql
	public String oneTableQuery(QueryDBDataParams query) {
		//获取sql
		String vds = "";
		try {
			String version = "000"+RandomUtils.getNumStr_13();
			String secondPath = "/datasets/new_untitled?parentDataset=%22" +
										query.getDaasName() + "%22." +
										query.getDatabaseName() + "." +
										query.getTableName() +
										"&newVersion=" + version +
										"&limit=150";// 单表
			String urlPath = this.daasServerAPIV2RootUrl + secondPath;
			String jsonStr = "{\"parentDataset\":\"'" + query.getDaasName() +
									 "'." + query.getDatabaseName() + ".'" +
									 query.getTableName()
									 + "'\",\"newVersion\":\"" +
									 version +
									 "\",\"limit\":\"150\"}";
			try {
				vds = OkHttpRequest.okHttpClientPost(urlPath, jsonStr, dSSUserTokenService.getCurrentToken());
			} catch (Exception e) {
				log.error("ProcessService.oneTableQuery(panel_id):请求DAAS异常");
			}
			JSONObject second_vds = null;
			if (vds.contains("details")) {
				second_vds = JSON.parseObject(vds).getJSONObject("details");
			} else {
				second_vds = JSON.parseObject(vds).getJSONObject("dataset");
			}
			return second_vds.getString("sql");
		} catch (Exception ex) {
			log.error(ex.toString());
		}
		return null;
	}

	//获取jobId
	public String getJobId(String sql) {
		String job_id = "";
		String urlPath = this.daasServerAPIV3RootUrl + "/sql";
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("sql", sql);
		try {
			job_id = OkHttpRequest.okHttpClientPost(urlPath, jsonParam.toString(), dSSUserTokenService.getCurrentToken());
		} catch (Exception e) {
			log.error("ProcessService.getJobId(sql):请求DAAS异常");
		}
		return JSON.parseObject(job_id).getString("id");
	}

	//获取job状态
	public String getJobStatus(String job_id) {
		String result = "";
		String urlPath = this.daasServerAPIV3RootUrl + "/job/" + job_id;
		try {
			result = OkHttpRequest.okHttpClientGet(urlPath, dSSUserTokenService.getCurrentToken());
		} catch (Exception e) {
			log.error("ProcessService.getJobStatus(job_id):请求DAAS异常");
		}
		return result;
	}

	//获取结果集
	public String getJobResults(String job_id) {
		String results = "";
		String urlPath = this.daasServerAPIV3RootUrl + "/job/" + job_id + "/results?offset=0&limit=100";
		System.err.println(urlPath);
		try {
			results = OkHttpRequest.okHttpClientGet(urlPath, dSSUserTokenService.getCurrentToken());
		} catch (Exception e) {
			log.error("ProcessService.getJobResults(job_id):请求DAAS异常");
		}
		return results;
	}

	/**
	 * 根据DaasID查询name   -- daas
	 *
	 * @param daasId
	 * @return
	 */
	public String findDaasNameByDaasId(String daasId) {
		DremioDataSourceInfo dataSourceInfo = null;
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			DataSourceDao dataSourceDao = sqlSession.getMapper(DataSourceDao.class);
			//查询daas数据源信息

			try {
				String url = this.daasServerAPIV3RootUrl+"/source/" + daasId;
				Request request = new Request.Builder()
										  .addHeader("Authorization", dSSUserTokenService.getCurrentToken())
										  .url(url)
										  .build();
				Response response = httpClient.newCall(request).execute();
				log.info(response.toString());
				String responseBody = response.body().string();
				dataSourceInfo = new Gson()
										 .fromJson(responseBody, DremioDataSourceInfo.class);
				//response.close();
				return dataSourceInfo.getName();
			} catch (Exception ex) {
				log.error("ex: {}", ex);
			}
		}
		return null;
	}

	/**
	 * 更新数据元名称及描述
	 *
	 * @param dataSource
	 * @return
	 */
	public int update(DataSource dataSource) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			DataSourceDao dataSourceDao = sqlSession.getMapper(DataSourceDao.class);
			ComponentDefinitionDao dictionaryDao = sqlSession.getMapper(ComponentDefinitionDao.class);
			int result = dataSourceDao.update(dataSource);
			ComponentDefinition cd = new ComponentDefinition();
			cd.setId(dataSource.getId());
			cd.setName(dataSource.getName());
			dictionaryDao.update(cd);//更新数据源
			sqlSession.commit();
			return result;
		}
	}

	/**
	 * 根据ID查询数据源信息   -- daas
	 *
	 * @param id
	 * @return
	 */
	public DremioDataSourceInfo findDataSourceInfo(String id) {
		DremioDataSourceInfo dataSourceInfo = null;
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			DataSourceDao dataSourceDao = sqlSession.getMapper(DataSourceDao.class);
			DataSource dataSource = dataSourceDao.findDataSourceById(id);
			//查询daas数据源信息
			String daasId = dataSourceDao.getDaasV3Catalog(id);

			try {
				String url = this.daasServerAPIV3RootUrl+"/source/" + daasId;
				String token = "_dremiobmulbkj5m9cqh7nksk6cr2nfvt";
				Request request = new Request.Builder()
						.addHeader("Authorization", dSSUserTokenService.getCurrentToken())
						.url(url)
						.build();
				Response response = httpClient.newCall(request).execute();
				log.info(response.toString());
				String responseBody = response.body().string();
				dataSourceInfo = new Gson()
						.fromJson(responseBody, DremioDataSourceInfo.class);
				dataSourceInfo.setId(id);
				dataSourceInfo.setDaasId(daasId);
				if ("MEMSQL".equals(dataSource.getDataDSType())) {
					dataSourceInfo.setType("MEMSQL");
				}
				dataSourceInfo.setDescription(dataSource.getDataDesc());
				dataSourceInfo.setBusinessType(dataSource.getBusinessType());
				dataSourceInfo.setConfig(new Gson()
						.fromJson(dataSource.getDataConfig(), new TypeToken<Map<String, Object>>() {
						}.getType()));
				//response.close();
				return dataSourceInfo;
			} catch (Exception ex) {
				log.error("ex: {}", ex);
			}
		}
		return dataSourceInfo;
	}

	/**
	 * 根据ID查询数据源信息(名称 描述)
	 *
	 * @param id
	 * @return
	 */
	public DataSource findDataSourceById(String id) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			DataSourceDao dataSourceDao = sqlSession.getMapper(DataSourceDao.class);
			DataSource dataSource = dataSourceDao.findDataSourceById(id);
			return dataSource;
		}
	}


	/**
	 * 删除数据源
	 *
	 * @param ids
	 * @return
	 */
	public int deleteDatasourceById(String ids) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			List<String> idlst = Splitter.on("^^").splitToList(ids == null ? "" : ids);
			DataSourceDao dataSourceDao = sqlSession.getMapper(DataSourceDao.class);
			ComponentInstanceDao componentInstanceDao = sqlSession.getMapper(ComponentInstanceDao.class);
			ComponentDefinitionDao dictionaryDao = sqlSession.getMapper(ComponentDefinitionDao.class);
			int num = 0;
			for (int i = 0; i < idlst.size(); i++) {
				//查询被应用的次数  为0 可以删除
				if(componentInstanceDao.getCountByCFId(idlst.get(i))>0){
					return 2;
				}
				String daasId = dataSourceDao.getDaasId(idlst.get(i));
				int daasIdSum = dataSourceDao.getDaasIdSum(daasId);
				if (daasIdSum == 1) {
					String daasName = dataSourceDao.getDaasName(daasId);
					deleteFile(daasName);
					deleteDaasSource(daasName); //删除daas
				}
				dataSourceDao.deleteDSByAppId(idlst.get(i)); //删除关联表
				dictionaryDao.delete(idlst.get(i));//删除组件定义
				num += dataSourceDao.deleteDatasourceById(idlst.get(i)); //删除数据资源表
			}
			sqlSession.commit();
			return num;
		}
	}




	/**
	 * 更新数据源
	 *
	 * @param source
	 * @return
	 */
	public int updateDatasourceById(DremioDataSourceInfo source) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			//数据源
			DataSourceDao dataSourceDao = sqlSession.getMapper(DataSourceDao.class);
			//组建定义
			ComponentDefinitionDao dictionaryDao = sqlSession.getMapper(ComponentDefinitionDao.class);
			int result = 0;
			if (source.getTables() != null && source.getTables().size() > 0) {
				for (int i = 0; i < source.getTables().size(); i++) {
					DataSource dataSource = new DataSource();
					dataSource.setId(source.getId());
					dataSource.setName(source.getTables().get(i).getResourceName());
					dataSource.setDataDesc(source.getTables().get(i).getResourceDesc());
					dataSource.setBusinessType(source.getBusinessType());
					dataSource.setDataDSType(source.getType());
					dataSource.setDataType(0);
					dataSource.setDataFrom("DataWave");
					dataSource.setClassification("001");
					dataSource.setCode("SimpleDataSource");
					Map<String, Object> gsonMap = new Gson()
														  .fromJson(new Gson().toJson(source.getConfig()), new TypeToken<Map<String, Object>>() {
														  }.getType());
					gsonMap.put("databaseName", source.getTables().get(i).getDatabaseName());
					gsonMap.put("tableName", source.getTables().get(i).getTableName());
					dataSource.setDataConfig(new Gson().toJson(gsonMap));

					//新增组件定义
					ComponentDefinition cd = new ComponentDefinition();
					cd.setId(dataSource.getId());
					cd.setName(dataSource.getName());
					cd.setLastModified(new Date());
					dictionaryDao.update(cd);//更新数据源
					//新增数据源
					result = dataSourceDao.updateDatasourceById(dataSource);
					result++;
				}
				sqlSession.commit();
			}
			return result;
		}
	}

	/**
	 * 校验name
	 */
	public Map<String, String> verifyDatasourceName(List<String> nameList) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
//			DataSourceDao dataSourceDao = sqlSession.getMapper(DataSourceDao.class);
			ComponentDefinitionDao dictionaryDao = sqlSession.getMapper(ComponentDefinitionDao.class);
			Set<String> set = new HashSet<>(nameList);
			if(nameList.size() != set.size()) {
				return getTheSameName(nameList);
			}
			Map<String, String> returnMap = new HashMap<>();
			for (int i = 0; i < nameList.size(); i++) {
				//TODO 根据名称和用户查询是否有数据
				//组件定义名称是否重复
				int count = dictionaryDao.getCountByName(nameList.get(i));
//				int count = dataSourceDao.findDatasourceByName(namelst.get(i));
				if (count > 0) returnMap.put(nameList.get(i), "出现" +count+ "次");
			}
			return returnMap;
		}
	}

	private Map<String, String> getTheSameName(List<String> list) {
		String str = list.toString();
		str = str.substring(1, str.length() - 1);
		String[] stringArray = str.split(",");
		String word;
		Map<String, Integer> wordMap = new HashMap<>();
		for (String e : stringArray) {
			word = e.trim();
			if (word.equals("")) continue;
			if (wordMap.containsKey(word)) {
				wordMap.put(word, wordMap.get(word) + 1);
			} else {
				wordMap.put(word, 1);
			}
		}
		Map<String, String> returnMap = new HashMap<>();
		for (String name : wordMap.keySet()) {
			int num = Integer.parseInt(wordMap.get(name).toString());
			if (num != 1) {
				System.out.printf("The String contains word %s %s times.\n", name, num);
				returnMap.put(name, "出现" + num + "次");
			}
		}
		return returnMap;
	}

	/**
	 * 查询已定阅资源   -- OPEN-API
	 *
	 * @return
	 */
	public Map<String, Object> getSubscribeDatasource(RequestParams requestParams) {
		List<DataSource> dataSourceList = new ArrayList<>();
		Map<String, Object> map1 = new HashMap<>();
		try (HttpClient httpClient = HttpClient.create(new URL(this.OpenAPIServerRootUrl));
			 SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()
		) {
			DataSourceDao baseDao = sqlSession.getMapper(DataSourceDao.class);
//            if (requestParams.getStatus() == 0){
			String result = httpClient
									.toBlocking()
									.retrieve(
											POST(this.OpenAPIServerRootUrl + "/datasource/getSubscribeDatasource",
													requestParams));
			Map<String, List<Map<String, String>>> gsonMap = new Gson()
																	 .fromJson(result, new TypeToken<Map<String, List<Map<String, String>>>>() {
																	 }.getType());
			List<Map<String, String>> data = gsonMap.get("data");
			for (int i = 0; i < data.size(); i++) {
				DataSource dataSource = new DataSource();
				dataSource.setId(data.get(i).get("id"));
				dataSource.setName(data.get(i).get("name"));
				dataSource.setDataConfig(data.get(i).get("dataConfig"));
				if ("relation_database".equals(data.get(i).get("dataType"))) {
					dataSource.setDataType(0);
				} else if ("file_storage".equals(data.get(i).get("dataType"))) {
					dataSource.setDataType(1);
				} else if ("big_data".equals(data.get(i).get("dataType"))) {
					dataSource.setDataType(2);
				} else if ("analysis_calculation_engine".equals(data.get(i).get("dataType"))) {
					dataSource.setDataType(3);
				} else if ("Interface_catalog".equals(data.get(i).get("dataType"))) {
					dataSource.setDataType(4);
				}
				dataSource.setDataDSType(data.get(i).get("dataDSType"));
				dataSource.setDataDesc(data.get(i).get("dataDesc"));
				dataSource.setBusinessType(data.get(i).get("businessType"));
				SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				dataSource.setCreateTime(date.parse(data.get(i).get("subscribeTime")));
				dataSource.setDataSize(0L);
				dataSource.setCreator(data.get(i).get("creator"));
				dataSource.setDataFrom("已定阅");
				dataSourceList.add(dataSource);

				DataSource dataSourceById = baseDao.findDataSourceById(dataSource.getId());
				if (dataSourceById == null) {
					baseDao.insert(dataSource);
				} else {
					baseDao.updateDatasourceById(dataSource);
				}
			}
			sqlSession.commit();

//            }
			Map<String, Object> map = new HashMap<>();
			map.put("name", requestParams.getName());
			map.put("pageIndex", PageUtil.getPageStart(
					requestParams.getPageIndex(), requestParams.getPageSize()));
			map.put("pageSize", requestParams.getPageSize());
			map.put("dataFrom", "已定阅");
			List<DataSource> dataSourceList1 = baseDao.listAll(map);
			map1.put("list", dataSourceList1);
			map1.put("totalRecords", baseDao.count(map));

//            requestParams.setStatus(1);
			return map1;
		} catch (Exception ex) {
			log.error("{}", ex.toString());
		}
		return map1;
	}


//*****************************************组件***********************************************

	/**
	 * 通过组件实例id获取数据源详细信息和列表
	 *
	 * @param id
	 * @return
	 */
	public DataSource findDataSourceDetail(String id, String token) {
		ComponentInstance cinstances = getInstance(id);//根据id获取 组件实例
		String creator = exchangeSSOService.getAccount(token);
		JAssert.isTrue(cinstances != null, "组件实例不存在：" + id);
		DataSource dataSource = null;
		DataSourceDao baseDao = null;
		DataSetInstanceDao dataSetInstanceDao = null;
		if (cinstances != null) {
//			String dsId = instances.get("componentDefinitionId").toString();
			String cdId = cinstances.getComponentDefinitionId();//从 组件实例 中获取 组件定义ID
			try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
				baseDao = sqlSession.getMapper(DataSourceDao.class);
				dataSource = baseDao.findDataSourceById(cdId);//根据ID查询datasource
			}
			if (dataSource != null && GlobalDefine.COMPONENT_CLASSIFICATION.MY_DATASOURCE.equals(dataSource.getClassification())) {
				if (!creator.equals(dataSource.getCreator())) dataSource = null;// 我的数据源，较验创建者
			}
			JAssert.isTrue(dataSource != null, "数据源不存在：id:" + cdId + ",用户:" + creator);
			if (dataSource != null) {
				try {
					List<Map<String, String>> columns = new ArrayList<Map<String, String>>();
//                    //查询数据
//                    Map<String, Object>  gsonMap = new Gson()
//                        .fromJson(dataSource.getDataConfig(), new TypeToken<Map<String, Object>>() {
//                        }.getType());
//                    QueryDBDataParams query = new QueryDBDataParams();
//                    String daasName = baseDao.getDaasNameByID(cdId);
//                    query.setDaasName(daasName);
//                    query.setDatabaseName(gsonMap.get("dataBaseName").toString());
//                    query.setTableName(gsonMap.get("tableName").toString());
//                    Map<String,Object> result = ciService.queryTableData(query);
//
//                    for (int i = 0; i < result.size(); i++) {
//                        Map<String, String> colMap = new LinkedHashMap<>();
//                        colMap.put(GlobalDefine.DATASOURCE_COLUMNNAME, result.get("name").toString());
//                        colMap.put(GlobalDefine.DATASOURCE_COLUMNTYPE, result.get("type").toString());
//                        columns.add(colMap);
//                    }

//                    List<ComponentInstanceRelation> cirList= cirService.getComponentInstanceRelationsBySourceId(cdId);
//                    if(cirList == null && cirList.size() == 0){
//                        return null;
//                    }
					DataSetInstance dataSetInstance = null;
					try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
						dataSetInstanceDao = sqlSession.getMapper(DataSetInstanceDao.class);
						dataSetInstance = dataSetInstanceDao.get(id);
					}

					List<Map<String, String>> columnsMap = new Gson()
							   .fromJson(dataSetInstance.getColumnsJSON(), new TypeToken<List<Map<String, String>>>() {
							   }.getType());
					for (int i = 0; i < columnsMap.size(); i++) {
						Map<String, String> colMap = new LinkedHashMap<>();
						colMap.put(GlobalDefine.DATASOURCE_COLUMNNAME, columnsMap.get(i).get("name"));
						colMap.put(GlobalDefine.DATASOURCE_COLUMNTYPE, columnsMap.get(i).get("type"));
						columns.add(colMap);
					}


					dataSource.setDataColumnsType(columns);
					dataSource.setDataPerform(JSON.parseObject(dataSource.getDataConfig().toString()).getString("rows"));
					dataSource.setDataColumns(String.valueOf(columns.size()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return dataSource;
	}

	/**
	 * 根据组件实例id，获取组件实例
	 *
	 * @param id
	 * @return
	 */
	public ComponentInstance getInstance(String id) {
		return ciService.get(id);
	}


	/**
	 * 通过组件实例id获取组件实例预处理信息
	 *
	 * @param processId
	 * @return
	 */
	public Map<String, Object> dataPreProcess(String processId) {
		Map<String, Object> preProcess = new HashMap<>();
		// TODO 组建处理相关
//		PreProcess= (Map)WebsiteTerminalUtils.sendRequest(preprocessTerminal,HttpMethod.GET, "/program/getDefault?processId="+id,"");
//        ProgramOutputData data = programService.getDefaultProgram(processId);
		ComponentInstance data = ciService.get(processId);

		//查询getoutput（上一级组件字段信息）
		List<Object> getOutPut = new ArrayList<>();
		ComponentInstance componentInstance = null;
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceDao componentInstanceDao = sqlSession.getMapper(ComponentInstanceDao.class);
			componentInstance = componentInstanceDao.get(processId);

		}
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			DataSetInstanceDao dataSetInstanceDao = sqlSession.getMapper(DataSetInstanceDao.class);
			List<ComponentInstanceRelation> cirList;
			List<ComponentInstanceRelation> cirList2;
			if (ComponentClassification.from("FieldMapper").equals(componentInstance.getComponentType())) {
				cirList2 = cirService.getComponentInstanceRelationsBySourceId(processId);//根据 源CIID 查询
				if (cirList2 != null && cirList2.size() != 0) {
					for (ComponentInstanceRelation cir : cirList2) {
						DataSetInstance dataSetInstance = dataSetInstanceDao.get(cir.getDestComponentInstanceId());
						if (cir.getDestInputName().equals("IN001")) dataSetInstance.setDescription("LeftTable");
						if (cir.getDestInputName().equals("IN002")) dataSetInstance.setDescription("RightTable");
						getOutPut.add(dataSetInstance);
					}
				}
			}
			cirList = cirService.getComponentInstanceRelationsByDestinationId(processId);//根据 目标CIID 查询
			if (cirList != null && cirList.size() != 0) {
				for (ComponentInstanceRelation cir : cirList) {
					DataSetInstance dataSetInstance = dataSetInstanceDao.get(cir.getSourceComponentInstanceId());
					if (cir.getDestInputName().equals("IN001")) dataSetInstance.setDescription("LeftTable");
					if (cir.getDestInputName().equals("IN002")) dataSetInstance.setDescription("RightTable");
					getOutPut.add(dataSetInstance);
				}
			}
		}

		preProcess.put("DataPreProcess", data);
		preProcess.put("getOutPut", getOutPut);
		return preProcess;
	}


	/**
	 * 获取单个的数据源
	 *
	 * @param id
	 * @return
	 */
	public DataSourceWithAll getWithPanel(String id, String token) {
		List<DataSourceWithAll> all = null;
		String userId = exchangeSSOService.getAccount(token);
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			DataSourceDao baseDao = sqlSession.getMapper(DataSourceDao.class);
			all = baseDao.getWithAll(id, userId);
		}
		if (all != null && all.size() > 0) {
			DataSourceWithAll dataSourceWithAll = all.get(0);
			List panels = getCitedByPanel(dataSourceWithAll.getId(), userId);
			O.json(panels);
			if (panels != null && panels.size() > 0) {
				for (Object panel : panels) {
					Map<String, String> panelMap = (Map<String, String>) panel;
					Map<String, String> componentDefinitionPanel = new HashMap<>();
					componentDefinitionPanel.put("panelId", panelMap.get("id"));
					componentDefinitionPanel.put("panelName", panelMap.get("panelName"));
					componentDefinitionPanel.put("projectId", panelMap.get("projectId"));
					componentDefinitionPanel.put("componentDefinitionId", dataSourceWithAll.getId());
					//jeq
//					JAssert.isTrue(projectService.get(panelMap.get("projectId")) != null, "该项目不存在：" + panelMap.get("projectId"));
//					componentDefinitionPanel.put("projectName", projectService.get(panelMap.get("projectId")).getProjectName());
					dataSourceWithAll.addPanels(componentDefinitionPanel);
				}
				dataSourceWithAll.setPanelCount((long) panels.size());
			}
			return dataSourceWithAll;
		} else {
			return null;
		}
	}

	/**
	 * 获取单个数据源被引用面板详细信息
	 *
	 * @param id
	 * @param userId
	 * @return
	 */
	public List getCitedByPanel(String id, String userId) {
		List panels = null;
		try {
			String params = "id=" + id + "&creator=" + userId;
			// TODO  面板相关
//			panels= (List)WebsiteTerminalUtils.sendRequest(terminal,HttpMethod.POST, "/panel/sourceTrace", params);
			O.json(panels);
		} catch (HttpClientException e) {
			e.printStackTrace();
			throw new JIllegalOperationException(e.getErrorCode(), e.getMessage());
		}
		return panels;
	}
	//***********************************************************************daas**********************************
	/**
	 * 测试连接时在daas的创建数据源  -- daas
	 */
	public String createSource(DremioDataSourceInfo es) {
		String json;
		String name = AUTO + UUID.randomUUID().toString();
		es.setName(name);
		es.setDescription(AUTO + es.getType());
		Gson gson = new Gson();
		json = gson.toJson(es);
		RequestBody body = RequestBody.create(ESJSON, json);
		{
			{
				log.info("create dataSource");
				try {
					String url = this.daasServerAPIV3RootUrl+"/source";
					String token = dSSUserTokenService.getCurrentToken();
//                        httpClient.setConnectTimeout(10, TimeUnit.SECONDS);
//                        httpClient.setWriteTimeout(10, TimeUnit.SECONDS);
//                        httpClient.setReadTimeout(30, TimeUnit.SECONDS);
					Request request = new Request.Builder()
											  .addHeader("Authorization", token)
											  .url(url)
											  .post(body)
											  .build();
					Response response = httpClient.newCall(request).execute();
					log.info(response.toString());
					if (response.code() == 400 || response.code() == 500) {
						url = this.daasServerAPIV2RootUrl+"/source/"+name+"?nocache=1545733655186";
						request = new Request.Builder()
								.addHeader("Authorization", token)
								.url(url).post(body).build();
						response = httpClient.newCall(request).execute();
						log.info(response.toString());
					}
					String responseBody = response.body().string();
					if (response.code() != 200) return "创建失败";
					log.info("{}", responseBody);
//					System.err.println(response.code());
					//response.close();
					return responseBody;
				} catch (Exception ex) {
					log.error("ex: {}", ex);
					return "数据源连接失败，请检查连接信息！";
				}
			}
		}
	}

	/**
	 * 页面获取数据列表    --daas
	 *
	 * @param daasName
	 * @return
	 */
	public List<Map<String, String>> listTable(String daasName) {
		log.info("list Table");
		String responseBody = listDBName(daasName);
		log.info("{}", responseBody);
		List<Map<String, String>> propertyValue = new ArrayList<>();
		JsonParser jsonParser = new JsonParser();
		JsonElement element = jsonParser.parse(responseBody);
		JsonObject jsonObj = element.getAsJsonObject();
		JsonObject contents = jsonObj.getAsJsonObject("contents");
		JsonArray DBS = contents.getAsJsonArray("folders");
		for (JsonElement jsonElement : DBS) {
			String DBName = jsonElement.getAsJsonObject().get("name").toString();
			String DBName2 = DBName.substring(1, DBName.length() - 1);
			List<String> tableNames = listDaasTables(daasName, DBName2);
			for (String table : tableNames) {
				Map<String, String> map = new HashMap<>();
				map.put("databaseName", DBName2);
				map.put("tableName", table);
				propertyValue.add(map);
			}
		}
		return propertyValue;
	}


	/**
	 * 根据daas名字从daas获取相关数据库名称
	 *
	 * @param daasName
	 * @return
	 */
	public String listDBName(String daasName) {
		try {
			String url = this.daasServerAPIV2RootUrl+"/source/" + daasName;
			Request request = new Request.Builder()
									  .addHeader("Authorization", dSSUserTokenService.getCurrentToken())
									  .url(url)
									  .build();
			Response response = httpClient.newCall(request).execute();
			log.info(response.toString());
			String responseBody = response.body().string();
			//response.close();
			return responseBody;
		} catch (Exception ex) {
			log.error("ex: {}", ex);
		}
		return null;
	}


	/**
	 * 根据数据库名和daasName获取表名   --daas
	 *
	 * @param daasName
	 * @param DBName
	 * @return
	 */
	public List<String> listDaasTables(String daasName, String DBName) {
		try {
			String urlString = URLEncoder.encode(daasName, "utf-8");
			String url = this.daasServerAPIV2RootUrl+"/source/" + urlString + "/folder/" + DBName;
			Request request = new Request.Builder()
									  .addHeader("Authorization", dSSUserTokenService.getCurrentToken())
									  .url(url)
									  .build();
			Response response = httpClient.newCall(request).execute();
			log.info(response.toString());
			String responseBody = response.body().string();
			List<String> tables = new ArrayList<>();
			JsonParser jsonParser = new JsonParser();
			JsonElement element = jsonParser.parse(responseBody);
			JsonObject jsonObj = element.getAsJsonObject();
			JsonObject contents = jsonObj.getAsJsonObject("contents");
			JsonArray DBS = contents.getAsJsonArray("physicalDatasets");
			for (JsonElement jsonElement : DBS) {
				String datasetName = jsonElement.getAsJsonObject().get("datasetName").toString();
				String datasetName2 = datasetName.substring(1, datasetName.length() - 1);
//				System.out.println(datasetName2);
				tables.add(datasetName2);
			}
			//response.close();
			return tables;
		} catch (Exception ex) {
			log.error("ex: {}", ex);
		}
		return null;
	}

	/**
	 * 删除功能
	 *
	 * @param daasName
	 * @return
	 */
	public boolean deleteDaasSource(String daasName) {
		try {
			String urlString = URLEncoder.encode(daasName, "utf-8");
			String url = this.daasServerAPIV2RootUrl+"/source/" + urlString + "?version=0";
			Request request = new Request.Builder()
									  .addHeader("Authorization", dSSUserTokenService.getCurrentToken())
									  .url(url)
									  .delete()
									  .build();
			Response response = httpClient.newCall(request).execute();
			log.info(response.toString());
			String responseBody = response.body().string();
			log.info(responseBody);
			//response.close();
			return true;
		} catch (Exception ex) {
			log.error("ex: {}", ex);
		}
		return false;
	}


	/**
	 * JSON上传开始
	 * @param file1
	 * @return
	 * @throws IOException
	 */
	public JSONInfo uploadStart(CompletedFileUpload file1) throws IOException {
		File tempFile = new File("temp" + "//" + UUID.randomUUID().toString()+file1.getFilename());
//		tempFile = new File("/home/yun-lian-data/app/java/daas_reource.manager/temp"+UUID.randomUUID().toString()+file1.getFilename());
		Path path = Paths.get(tempFile.getAbsolutePath());
//		System.out.println(path);
//		String name = path.toString().substring(path.toString().lastIndexOf("\\") + 1);
		String fileName = tempFile.getName();

		log.info("file-name: {}", fileName);
//		System.out.println(fileName);
		Files.write(path, file1.getBytes());
		MediaType type = MediaType.parse("application/octet-stream");
		File file = new File(tempFile.getPath());
		RequestBody fileBody = RequestBody.create(type, file);
		RequestBody multipartBody = new MultipartBody.Builder()
											.setType(MultipartBody.FORM)
											.addFormDataPart("file", fileName, fileBody)//这里选择本地缓存的文件名
											.addFormDataPart("fileName", fileName)//这里可以自定义上传到daas的名字
											.build();
		try {
//            String urlString = URLEncoder.encode(daasName, "utf-8");
			String url = this.daasServerAPIV2RootUrl +"/home/%40daas/upload_start/";
			Request request = new Request.Builder()
									  .addHeader("Authorization", dSSUserTokenService.getCurrentToken())
									  .header("Content-Type", "application/json")
									  .url(url)
									  .post(multipartBody)
									  .build();
			Response response = httpClient.newCall(request).execute();
			log.info(response.toString());
			JsonParser jsonParser = new JsonParser();
			String responseBody = response.body().string();
			if (file.exists() && file.isFile()) {//删除本地文件
				file.delete();
			}
			if (response.code() != 200) {
				return null;
			}
			JsonElement element = jsonParser.parse(responseBody);
			JsonObject jsonObj = element.getAsJsonObject();
			String location = jsonObj.getAsJsonObject("fileFormat").getAsJsonObject("fileFormat").get("location").toString();
			String location2 = location.substring(1, location.length() - 1);
//			System.out.println(location2);
			String preview = preview(location2, fileName);
			JSONInfo JSONInfo = new JSONInfo();
			JSONInfo.setFile(JSON.parseObject(preview));
			JSONInfo.setType("JSON");
			JSONInfo.setName(fileName);
			JSONInfo.setLocation(location2);
			//response.close();
			return JSONInfo;
		} catch (Exception ex) {
			log.error("ex: {}", ex);
			return null;
		}
	}


	/**
	 * JSON预览文件
	 * @param location
	 * @param name
	 * @return
	 */
	public String preview(String location, String name) {
		String json = null;
		JSONInfo JSONInfo = new JSONInfo();
		JSONInfo.setLocation(location);
		JSONInfo.setType("JSON");
		Gson gson = new Gson();
		json = gson.toJson(JSONInfo);
		RequestBody body = RequestBody.create(ESJSON, json);
		{
			{
				try {
					String url = this.daasServerAPIV2RootUrl +"/home/%40daas/file_preview_unsaved/" + name;
					Request request = new Request.Builder()
											  .addHeader("Authorization", dSSUserTokenService.getCurrentToken())
											  .header("Content-Type", "application/json")
											  .url(url)
											  .post(body)
											  .build();
					Response response = httpClient.newCall(request).execute();

					String responseBody = response.body().string();
//					System.out.println("回显**********" + responseBody);
					if (response.code() != 200) {
						return "预览失败";
					}
//					System.out.println(response.code());
					//response.close();
					return responseBody;
				} catch (Exception ex) {
					log.error("ex: {}", ex);
				}
			}
		}
		return null;
	}


	/**
	 * JSON上传finish
	 * @param JSONInfo2
	 * @return
	 */
	public boolean uploadFinish(JSONInfo JSONInfo2, String token) {
		String json = null;
		JSONInfo JSONInfo = new JSONInfo();
		JSONInfo.setLocation(JSONInfo2.getLocation());
		JSONInfo.setType("JSON");
		Gson gson = new Gson();
		json = gson.toJson(JSONInfo);
		RequestBody body = RequestBody.create(ESJSON, json);
		{
			{
				try {
					String url = this.daasServerAPIV2RootUrl+"/home/%40daas/upload_finish/" + JSONInfo2.getName();
					Request request = new Request.Builder()
											  .addHeader("Authorization", dSSUserTokenService.getCurrentToken())
											  .header("Content-Type", "application/json")
											  .url(url)
											  .post(body)
											  .build();
					Response response = httpClient.newCall(request).execute();
					log.info(response.toString());
					String responseBody = response.body().string();
					if (response.code() != 200) return false;
					log.info("{}", responseBody);
//					System.out.println(response.code());
					//response.close();
				} catch (Exception ex) {
					log.error("ex: {}", ex);
				}
				createJSON(JSONInfo2, token);
			}
		}
		return true;
	}

	/**
	 * 根据ID查询daasName
	 * @param id
	 * @return
	 */
	public String getDaasNameByID(String id) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			DataSourceDao dataSourceDao = sqlSession.getMapper(DataSourceDao.class);
			String daasName = dataSourceDao.getDaasNameByID(id);
			return daasName;
		}
	}


	/**
	 * json文件信息传入数据库
	 * @param daas
	 * @return
	 */
	public int createJSON(JSONInfo daas, String token) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			String id=null;
			//数据源
			DataSourceDao dataSourceDao = sqlSession.getMapper(DataSourceDao.class);
			//组建定义
			ComponentDefinitionDao dictionaryDao = sqlSession.getMapper(ComponentDefinitionDao.class);
			int result = 0;
			if (daas != null) {
				DataSource dataSource = new DataSource();
				if(daas.getResourceId()==null){
					id = UUID.randomUUID().toString();
				}else{
					id=daas.getResourceId();
				}
				dataSource.setId(id);
				dataSource.setName(daas.getResourceName());
				dataSource.setDataDesc(daas.getResourceDesc());
				dataSource.setBusinessType(daas.getBusinessType());
				dataSource.setDataDSType(daas.getType());
				if("TEXT".equals(daas.getType())){
					dataSource.setDataDSType("CSV");
				}
				dataSource.setDataType(0);
				dataSource.setDataFrom("DataWave");
				dataSource.setClassification("001");
				dataSource.setCode("SimpleDataSource");
				dataSource.setCreateTime(new Date());
				dataSource.setCreator(exchangeSSOService.getAccount(token));
				JsonObject jsonObject=new JsonObject();
				jsonObject.addProperty("tableName",daas.getName());
				jsonObject.addProperty("databaseName","@daas"); //TODO 暂时写死
				//数据连接信息
				dataSource.setDataConfig(jsonObject.toString());
				//新增组件定义
				ComponentDefinition cd = new ComponentDefinition();
				cd.setId(id);
				cd.setCode(ComponentClassification.SimpleDataSource.name());
				cd.setName(daas.getResourceName());
				cd.setClassification("001");
				cd.setCreator(exchangeSSOService.getAccount(token));
				cd.setParams("");
				//新增数据源
				if(daas.getResourceId()==null){
					dataSourceDao.insertDS(null, id, daas.getName());
					dictionaryDao.insert(cd);
					dataSourceDao.insert(dataSource);
				}
				result++;
				sqlSession.commit();
			}
			return result;
		}
	}


	/**
	 * 更新JSON
	 * @param daas
	 * @return
	 */
	public int updateJSON(JSONInfo daas, String token) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			//数据源
			DataSourceDao dataSourceDao = sqlSession.getMapper(DataSourceDao.class);
			//组建定义
			ComponentDefinitionDao dictionaryDao = sqlSession.getMapper(ComponentDefinitionDao.class);
			uploadFinish(daas, token);
			String daasName = getDaasNameByID(daas.getResourceId());
			deleteFile(daasName);
			int result = 0;
			if (daas != null) {
				DataSource dataSource = new DataSource();
				String id = daas.getResourceId();
				dataSourceDao.updateDSByDataSourceID(null, id, daas.getName());
				dataSource.setId(id);
				dataSource.setName(daas.getResourceName());
				dataSource.setDataDesc(daas.getResourceDesc());
				dataSource.setBusinessType(daas.getBusinessType());
				dataSource.setDataDSType(daas.getType());
				dataSource.setDataType(0);
				dataSource.setDataFrom("DataWave");
				dataSource.setClassification("001");
				dataSource.setCode("SimpleDataSource");
				dataSource.setCreateTime(new Date());
				dataSource.setCreator(exchangeSSOService.getAccount(token));
				//数据连接信息
				dataSource.setDataConfig(daas.getType());
				//更新组件定义
				ComponentDefinition cd = new ComponentDefinition();
				cd.setId(id);
				cd.setCode(ComponentClassification.SimpleDataSource.name());
				cd.setName(daas.getResourceName());
				cd.setClassification("001");
				cd.setCreator(exchangeSSOService.getAccount(token));
				cd.setParams("");
				dictionaryDao.update(cd);
				//更新数据源
				dataSourceDao.updateDatasourceById(dataSource);
				sqlSession.commit();
				result ++;
			}
			return result;
		}
	}


	/**
	 * 查询
	 * @param id
	 * @return
	 */
	public Object listJSON(String id)  {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			DataSourceDao dataSourceDao = sqlSession.getMapper(DataSourceDao.class);
			String daasName = dataSourceDao.getDaasNameByID(id);
			String daasName2 = URLEncoder.encode(daasName, "utf-8");
			RequestBody body = new MultipartBody.Builder()
												.setType(MultipartBody.FORM)
												.addFormDataPart("id",id)
												.build();
			String url = this.daasServerAPIV2RootUrl+"/datasets/new_untitled?parentDataset=%22%40daas%22.%22"+daasName2+"%22&newVersion="+"000" + RandomUtils.getNumStr_13()+"&limit=150";
			Request request = new Request.Builder()
									  .addHeader("Authorization", dSSUserTokenService.getCurrentToken())
									  .header("Content-Type", "application/json")
									  .url(url)
									  .post(body)
									  .build();
			Response response = httpClient.newCall(request).execute();
			log.info(response.toString());
			String responseBody = response.body().string();
			//response.close();
			return JSON.parseObject(responseBody);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "查询失败";
	}


	/**
	 * 上传结束CSV
	 * @param JSONInfo2
	 * @return
	 */
	public boolean uploadFinishCSV(JSONInfo JSONInfo2, String token) {
		String json = null;
		CSVInfo csvInfo = new CSVInfo();
		csvInfo.setLocation(JSONInfo2.getLocation());
		csvInfo.setType("Text");
		csvInfo.setComment("#");
		csvInfo.setEscape("\"");
		csvInfo.setExtractHeader(false);
		csvInfo.setFieldDelimiter(",");
		csvInfo.setLineDelimiter("↵");
		csvInfo.setQuote("\"");
		csvInfo.setSkipFirstLine(false);
		csvInfo.setTrimHeader(true);
		Gson gson = new Gson();
		json = gson.toJson(csvInfo);
		RequestBody body = RequestBody.create(ESJSON, json);
		{
			{
				try {
					String url = this.daasServerAPIV2RootUrl+"/home/%40daas/upload_finish/" + JSONInfo2.getName();
					Request request = new Request.Builder()
											  .addHeader("Authorization", dSSUserTokenService.getCurrentToken())
											  .header("Content-Type", "application/json")
											  .url(url)
											  .post(body)
											  .build();
					Response response = httpClient.newCall(request).execute();
					log.info(response.toString());
					String responseBody = response.body().string();
					if (response.code() != 200) {
						return false;
					}
					log.info("{}", responseBody);
					//response.close();
				} catch (Exception ex) {
					log.error("ex: {}", ex);
				}
				createJSON(JSONInfo2, token);
			}
		}
		return true;
	}


	/**
	 * 上传CSV开始
	 *
	 */
	public JSONInfo uploadStartCSV(CompletedFileUpload file1) throws IOException {
		File tempFile = new File("temp" + "//" + UUID.randomUUID().toString()+file1.getFilename());
//		tempFile = new File("temp"+UUID.randomUUID().toString()+file1.getFilename());
//		System.out.println("*************************"+tempFile.getPath());
		Path path = Paths.get(tempFile.getAbsolutePath());
		String fileName = tempFile.getName();

		log.info("file-name: {}", fileName);
//		System.out.println(fileName);
		Files.write(path, file1.getBytes());
		MediaType type = MediaType.parse("application/octet-stream");
		File file = new File(tempFile.getPath());
		URL url1 = file.toURI().toURL();
        String code= getCharset(url1.openStream());
        if("UTF-8".equals(code)){
        }else{
            convert(file);//本地gbk转换utf-8
        }
		RequestBody fileBody = RequestBody.create(type, file);
		RequestBody multipartBody = new MultipartBody.Builder()
											.setType(MultipartBody.FORM)
											.addFormDataPart("file", fileName, fileBody)//这里选择本地缓存的文件名
											.addFormDataPart("fileName", fileName)//这里可以自定义上传到daas的名字
											.build();
		try {
//            String urlString = URLEncoder.encode(daasName, "utf-8");
			String url = this.daasServerAPIV2RootUrl+"/home/%40daas/upload_start/";
			Request request = new Request.Builder()
									  .addHeader("Authorization", dSSUserTokenService.getCurrentToken())
									  .header("Content-Type", "application/json")
									  .url(url)
									  .post(multipartBody)
									  .build();
			Response response = httpClient.newCall(request).execute();
			log.info(response.toString());
			JsonParser jsonParser = new JsonParser();
			String responseBody = response.body().string();
			if (file.exists() && file.isFile()) {//删除本地文件
				file.delete();
			}
			if (response.code() != 200) {
				return null;
			}
			JsonElement element = jsonParser.parse(responseBody);
			JsonObject jsonObj = element.getAsJsonObject();
			String location = jsonObj.getAsJsonObject("fileFormat").getAsJsonObject("fileFormat").get("location").toString();
			String location2 = location.substring(1, location.length() - 1);
//			System.out.println(location2);
//            if(uploadFinish(location2,name)){
//                return 0;
//            }
			String preview = previewCSV(location2, fileName);
			JSONInfo JSONInfo = new JSONInfo();
			JSONInfo.setFile(JSON.parseObject(preview));
			JSONInfo.setType("Text");
			JSONInfo.setName(fileName);
			JSONInfo.setLocation(location2);
			//response.close();
			return JSONInfo;
		} catch (Exception ex) {
			log.error("ex: {}", ex);
		}
//		System.out.println(path);
		return null;
	}

	/**
	 * 预览CSV文件
	 * @param location
	 * @param name
	 * @return
	 */
	public String previewCSV(String location, String name) {
		String json = null;
		JSONInfo JSONInfo = new JSONInfo();
		JSONInfo.setLocation(location);
		JSONInfo.setType("Text");
		Gson gson = new Gson();
		json = gson.toJson(JSONInfo);
		RequestBody body = RequestBody.create(ESJSON, json);
		{
			{
				try {
					String url = this.daasServerAPIV2RootUrl+"/home/%40daas/file_preview_unsaved/" + name;
					Request request = new Request.Builder()
											  .addHeader("Authorization", dSSUserTokenService.getCurrentToken())
											  .header("Content-Type", "application/json")
											  .url(url)
											  .post(body)
											  .build();
					Response response = httpClient.newCall(request).execute();
					String responseBody = response.body().string();
//					System.out.println("回显**********" + responseBody);
					if (response.code() != 200) return "预览失败";
//					System.out.println(response.code());
					//response.close();
					return responseBody;
				} catch (Exception ex) {
					log.error("ex: {}", ex);
				}
			}
		}
		return null;
	}


	/**
	 * 更新CSV文件
	 * @param daas
	 * @return
	 */
	public int updateCSV(JSONInfo daas, String token) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			//数据源
			DataSourceDao dataSourceDao = sqlSession.getMapper(DataSourceDao.class);
			//组建定义
			ComponentDefinitionDao dictionaryDao = sqlSession.getMapper(ComponentDefinitionDao.class);
			uploadFinishCSV(daas, token);
			int result = 0;
			if (daas != null) {
				DataSource dataSource = new DataSource();
				String id = daas.getResourceId();
				dataSourceDao.updateDSByDataSourceID(null, id, daas.getName());
				dataSource.setId(id);
				dataSource.setName(daas.getResourceName());
				dataSource.setDataDesc(daas.getResourceDesc());
				dataSource.setBusinessType(daas.getBusinessType());
				dataSource.setDataDSType(daas.getType());
				dataSource.setDataType(0);
				dataSource.setDataFrom("DataWave");
				dataSource.setClassification("001");
				dataSource.setCode("SimpleDataSource");
				dataSource.setCreateTime(new Date());
				dataSource.setCreator(exchangeSSOService.getAccount(token));
				JsonObject jsonObject=new JsonObject();
				jsonObject.addProperty("tableName",daas.getName());
				jsonObject.addProperty("databaseName","@daas"); //TODO 暂时写死
				//数据连接信息
				dataSource.setDataConfig(jsonObject.toString());
				//更新组件定义
				ComponentDefinition cd = new ComponentDefinition();
				cd.setId(id);
				cd.setCode(ComponentClassification.SimpleDataSource.name());
				cd.setName(daas.getResourceName());
				cd.setClassification("001");
				cd.setCreator(exchangeSSOService.getAccount(token));
				cd.setParams("");
				dictionaryDao.update(cd);
				//更新数据源
				dataSourceDao.updateDatasourceById(dataSource);
				sqlSession.commit();
			}
			return result;
		}
	}

	/**
	 * 删除JSON CSV
	 * @param name
	 * @return
	 */
	public String deleteFile(String name) {
		try {
			String url = this.daasServerAPIV2RootUrl+"/home/%40daas/file/" + name + "?version=0";
			Request request = new Request.Builder()
									  .addHeader("Authorization", dSSUserTokenService.getCurrentToken())
									  .header("Content-Type", "application/json")
									  .url(url).delete()
									  .build();
			Response response = httpClient.newCall(request).execute();

			String responseBody = response.body().string();
			if (response.code() > 200) {
				return "删除失败";
			}
			//response.close();
			return responseBody;
		} catch (Exception ex) {
			log.error("ex: {}", ex);

		}
		return "删除成功";
	}

	/**
	 * file gbk转换utf-8
	 * @param file
	 * @throws IOException
	 */
	public void convert(File file) throws IOException {
		// 如果是文件则进行编码转换，写入覆盖原文件
		if (file.isFile()) {
			// 只处理.java结尾的代码文件
			if (file.getPath().indexOf(".csv") == -1) {
				return;
			}
			InputStreamReader isr = new InputStreamReader(new FileInputStream(
					file), "gbk");
			BufferedReader br = new BufferedReader(isr);
			StringBuffer sb = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null) {
				// 注意写入换行符
				sb.append(line + "\n");
			}
			br.close();
			isr.close();

			File targetFile = new File(file.getPath()); //+ "." + "utf8");
			OutputStreamWriter osw = new OutputStreamWriter(
					new FileOutputStream(targetFile), "utf8");
			BufferedWriter bw = new BufferedWriter(osw);
			// 以字符串的形式一次性写入
			bw.write(sb.toString());
			bw.close();
			osw.close();

//			System.out.println("Deal:" + file.getPath());
//			fileCount++;
		} else {
			for (File subFile : file.listFiles()) {
				convert(subFile);
			}
		}
	}

	/**
	 * 文件编码判断
	 */
	private static boolean found = false;
	private static String charSet = "UTF-8";
	public static String getCharset(InputStream in) {

		charSet = "UTF-8";
		try {
			BufferedInputStream imp = new BufferedInputStream(in);
			// int lang = (argv.length == 2)? Integer.parseInt(argv[1])
			// : nsPSMDetector.ALL ;
			int lang = nsPSMDetector.CHINESE;
			nsDetector det = new nsDetector(lang);

			// Set an observer...
			// The Notify() will be called when a matching charset is found.

			det.Init(new nsICharsetDetectionObserver() {
				public void Notify(String charset) {
					found = true;
					charSet = charset;
				}
			});

			byte[] buf = new byte[1024];
			int len;
			boolean done = false;
			boolean isAscii = true;

			while ((len = imp.read(buf, 0, buf.length)) != -1) {
				// Check if the stream is only ascii.
				if (isAscii)
					isAscii = det.isAscii(buf, len);

				// DoIt if non-ascii and not done yet.
				if (!isAscii && !done)
					done = det.DoIt(buf, len, false);
			}
			det.DataEnd();

			if (isAscii) {
				System.out.println("CHARSET = ASCII");
				found = true;
				return "ASCII";
			} else if (!found) {
				String prob[] = det.getProbableCharsets();
				return prob[0];
				// for (int i = 0; i < prob.length; i++) {
				// System.out.println("Probable Charset = " + prob[i]);
				// }
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return charSet;
	}

}
