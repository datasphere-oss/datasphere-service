package com.datasphere.engine.shaker.processor.service;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.datasphere.common.utils.OkHttpRequest;
import com.datasphere.common.utils.RandomUtils;
import com.datasphere.core.common.BaseService;
import com.datasphere.core.common.utils.UUIDUtils;
import com.datasphere.engine.core.utils.JsonWrapper;
import com.datasphere.engine.manager.resource.provider.dao.DataSourceDao;
import com.datasphere.engine.manager.resource.provider.database.service.DBOperationService;
import com.datasphere.engine.manager.resource.provider.model.DataSource;
import com.datasphere.engine.manager.resource.provider.service.DataQueryService;
import com.datasphere.engine.manager.resource.provider.service.ExchangeSSOService;
import com.datasphere.engine.shaker.processor.buscommon.AggregationFunctions;
import com.datasphere.engine.shaker.processor.buscommon.CharReplaceUtils;
import com.datasphere.engine.shaker.processor.buscommon.OperatorUtils;
import com.datasphere.engine.shaker.processor.buscommon.ReturnMessageUtils;
import com.datasphere.engine.shaker.processor.instance.Component;
import com.datasphere.engine.shaker.processor.instance.constant.ComponentInstanceStatus;
import com.datasphere.engine.shaker.processor.instance.dao.ComponentInstanceDao;
import com.datasphere.engine.shaker.processor.instance.dao.ComponentInstanceRelationDao;
import com.datasphere.engine.shaker.processor.instance.model.ComponentInstance;
import com.datasphere.engine.shaker.processor.instance.model.ComponentInstanceRelation;
import com.datasphere.engine.shaker.processor.instance.service.ComponentInstanceRelationService;
import com.datasphere.engine.shaker.processor.instance.service.ComponentInstanceService;
import com.datasphere.engine.shaker.processor.model.BinaryTree;
import com.datasphere.engine.shaker.processor.model.ProcessInstance;
import com.datasphere.engine.shaker.processor.runner.ProcessRunner;
import com.datasphere.engine.shaker.processor.stop.StopSingleInstance;
import com.datasphere.engine.shaker.workflow.panel.constant.PanelState;
import com.datasphere.engine.shaker.workflow.panel.service.PanelServiceImpl;
import com.datasphere.server.connections.dao.DataSetInstanceDao;
import com.datasphere.server.connections.model.DataSetInstance;
import com.datasphere.server.connections.service.DataAccessor;
import com.datasphere.server.sso.service.DSSUserTokenService;
import com.datasphere.server.sso.service.DSSVersionService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;

import org.apache.ibatis.session.SqlSession;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 工作流-服务
 */
@Service
public class ProcessService extends BaseService {
	private final static Logger logger = LoggerFactory.getLogger(ProcessService.class);
	private final ExecutorService executor = Executors.newCachedThreadPool();
	private static Map<Object, String> processAllCiId = new HashMap<>();//正序
	private static LinkedHashMap<Object, String> backtrackingProcessAllCiId = new LinkedHashMap<>();//倒序
    public static String PANEL_ID = "0";
	public static String PROCESS_ID = "0";
	public static String TOKEN;

	@Autowired
	private ProcessDaasService processDaasService;
	@Autowired
	private DSSUserTokenService dssUserTokenService;
	@Autowired
	private DSSVersionService dssVersionService;
	@Autowired
	private DBOperationService dbOperationService;
	@Autowired
	private DataQueryService dataQueryService;
	@Autowired
	private PanelServiceImpl panelService;
	@Autowired
	private ComponentService componentService;
	@Autowired
	private ComponentInstanceService componentInstanceService;
	@Autowired
	private ComponentInstanceRelationService componentInstanceRelationService;
	@Autowired
	private ComponentInstanceSnapshotService componentInstanceSnapshotService;
	@Autowired
	private ProcessInstanceService processInstanceService;
	@Autowired
	private ProcessRecordService processRecordService;
	private DataAccessor dataAccessor;
	@Autowired
	ExchangeSSOService exchangeSSOService;

	public String runProcess(String creator_user_id, String panelId) {
		List<Component> fromComponents = componentService.getBeginComponents(panelId);//获取panel中所有的源组件
		List<Component> toComponents = componentService.getEndComponents(panelId);//获取panel中所有的目标组件
		List<Component> allComponents = componentService.getAllComponentsWithPanel(panelId);//获取panel中所有组件
		try {
			return executeProcess(creator_user_id, panelId, fromComponents, toComponents, allComponents);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	private String executeProcess(String creator_user_id, String panelId, List<Component> fromComponents,
			List<Component> toComponents, List<Component> allComponentsWithProcess) {
			String processId = UUIDUtils.random();
			executor.submit(() -> {
            ProcessInstance processInstance = new ProcessInstance();
            processInstance.setId(processId);
            processInstance.setPanelId(panelId);
            processInstance.setCreateUserId(creator_user_id);
            processInstance.setFromComponentInstanceIds(StringUtils.join(getComponentIds(fromComponents), ","));
            List<String> toComponentIds = getComponentIds(toComponents);
            processInstance.setToComponentInstanceIds(StringUtils.join(toComponentIds, ","));

            ProcessRunner runner = new ProcessRunner(panelId, processInstance.getId(), dataAccessor);
            runner.setFromComponents(fromComponents);
            runner.setAllComponentIdsWithProcess(getComponentIds(allComponentsWithProcess));
            Map<String, Component> allMap = allComponentsWithProcess.stream().collect(Collectors.toMap(Component::getId, (p) -> p));
            runner.setAllComponentsMap(allMap);
            runner.setToComponents(toComponentIds);
            runner.setInstance(processInstance);
            runner.setRecordService(processRecordService);
            runner.setComponentInstanceService(componentInstanceService);
            runner.setUserId(creator_user_id);

            CountDownLatch latch = new CountDownLatch(allComponentsWithProcess.size());
            try {
                componentInstanceSnapshotService.createComponentInstanceSnapshotsByPanelId(panelId, processInstance.getId());//创建 组件实例快照cis
                processInstance.setBeginTime(new Date());
                processInstance.setStatus(PanelState.RUNNING);
                processInstanceService.add(processInstance); //新建 工作流（任务）

                runner.run(latch);
                latch.await();
                processInstance.setEndTime(new Date());
                if (!runner.isError()) {
                    if (processInstance.getStatus().equals(PanelState.STOP)) {
                        processInstance.setStatus(PanelState.STOP);
                        logger.info("The process[" + processInstance.getId() + "] runs stop.");
                    } else {
                        processInstance.setStatus(PanelState.SUCCESS);
                        logger.info("The process[" + processInstance.getId() + "] runs successfully.");
                    }
                } else {
                    processInstance.setStatus(PanelState.FAILURE);
                    logger.info("The process[" + processInstance.getId() + "] runs fail.");
                }
                StopSingleInstance.getInstances().remove(panelId);
            } catch (Exception e) {
                processInstance.setEndTime(new Date());
                processInstance.setStatus(PanelState.FAILURE);
                logger.error("The process[" + processInstance.getId() + "] runs fail.", e);
            } finally {
                processInstanceService.modify(processInstance); //更新 工作流（任务）运行状态
            }
            return null;
        });
		return processId;
	}
	private List<String> getComponentIds(List<Component> components) {
		List<String> list = new ArrayList<String>();
		for (Component c : components) {
			list.add(c.getId());
		}
		return list;
	}
	private List<String> getComponentInstanceIds(List<Component> components) {
		List<String> componentIds = getComponentIds(components);
		List<String> ci_ids = new ArrayList<>();
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceDao dao = sqlSession.getMapper(ComponentInstanceDao.class);
			for (String ci_id : componentIds) {
				ci_ids.add(dao.get(ci_id).getId());
			}
		}
		return ci_ids;
	}
	public String getComponentDefinitionId(String ci_id) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceDao dao = sqlSession.getMapper(ComponentInstanceDao.class);
			ComponentInstance componentInstance = dao.get(ci_id);
			return componentInstance.getComponentDefinitionId();
		}
	}
	private List<String> getComponentDefinitionIds(List<Component> components) {
		List<String> componentIds = getComponentIds(components);
		List<String> cd_ids = new ArrayList<>();
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceDao dao = sqlSession.getMapper(ComponentInstanceDao.class);
			for (String ci_id : componentIds) {
				cd_ids.add(dao.get(ci_id).getComponentDefinitionId());
			}
		}
		return cd_ids;
	}
	public String runProcessFrom(String creator_id, String panelId, String fromComponentId) throws Exception {
		List<Component> toComponents = componentService.getEndComponents(panelId, fromComponentId);
		List<Component> allComponents = componentService.getAllComponentsFromCompIdWithPanel(panelId, fromComponentId);
		List<Component> fromComponents = new ArrayList<>();
		fromComponents.add(componentService.getById(fromComponentId));
		return executeProcess(creator_id, panelId, fromComponents, toComponents, allComponents);
	}
	public String runProcessTo(String creator_id, String panelId, String toComponentId) {
		List<Component> fromComponents = componentService.getBeginComponents(panelId, toComponentId);
		List<Component> allComponents = componentService.getAllComponentsToCompIdWithPanel(panelId, toComponentId);
		List<Component> toComponents = new ArrayList<>();
		toComponents.add(componentService.getById(toComponentId));
		try {
			return executeProcess(creator_id, panelId, fromComponents, toComponents, allComponents);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/** Daas-1
	 * 根据ci_id获取组件实例的sql
	 */
	public Map<String, Object> queryByCIId(String componentInstanceId, String token) {
		TOKEN = token;
		String upper_sql = getSqlByCIId(componentInstanceId).getString("sql");
        if (StringUtils.isBlank(upper_sql)) {
            addCILog(PROCESS_ID, PANEL_ID, componentInstanceId, "没有数据输入!", 3);
            return JsonWrapper.jobFailure(1, "没有数据输入!");
        }
        upper_sql = CharReplaceUtils.replace_N(upper_sql);
        // 由于daas的jdbc驱动[查询条件]不支持utf-8编码，即不支持中文
        /*
		if (IsHaveChinese.isContainChinese(upper_sql)) {//包含中文
			String whereAfter;
			upper_sql = upper_sql.toLowerCase();
			if (upper_sql.contains("where")) {
				whereAfter = upper_sql.split("where")[1];
				if (IsHaveChinese.isContainChinese(whereAfter)) {//where条件中包含中文
					DataSource ds;
					if (componentInstanceService.getComponentInstanceById(componentInstanceId).getComponentClassification().equals("002")) {
						ds = getDataSourceById(getComponentDefinitionId(getSourceCIIdsByDestCIId(componentInstanceId).get(0)));
					} else {
						ds = getDataSourceById(getComponentDefinitionId(componentInstanceId));
					}
					String spaceName = upper_sql.substring(upper_sql.indexOf("\""), upper_sql.lastIndexOf("."));
					ConnectionInfoAndOthers ciao = getConnectionInfoAndOthers(ds, upper_sql);
					upper_sql = upper_sql.replace(spaceName, ciao.getScheme());
					ciao = getConnectionInfoAndOthers(ds, upper_sql);
					return dbOperationService.selectDatas(ciao);
				}
//				else { return dataQueryService.dataQuery(upper_sql); }
			}
//			else { return dataQueryService.dataQuery(upper_sql); }
		}*/
		return dataQueryService.dataQuery(upper_sql);
	}

	/** Daas-2.1
	 * 运行到此
	 */
	public Map<String, Object> runToByCIId(String panelId, String toComponentInstanceId, String token) {
		PANEL_ID = panelId;
		TOKEN = token;
		boolean flag = false;
		String virtualDataset;
		DataSetInstance dataSetInstance;

		List<Component> fromComponents = componentService.getBeginComponents(panelId, toComponentInstanceId);
		List<Component> toComponents = new ArrayList<>();
		toComponents.add(componentService.getById(toComponentInstanceId));

		List<Component> allComponents = componentService.getAllComponentsToCompIdWithPanel(panelId, toComponentInstanceId);//获取panel中所有组件
		if(allComponents.size() == 0) return JsonWrapper.jobFailure(1, ReturnMessageUtils.ComponentIsNull);

		List<String> source_ci_ids = getComponentInstanceIds(fromComponents);// 获取源组件实例的ids
		String first_ci_id = source_ci_ids.get(0);

		// 根据ci_id回溯上游组件实例
		backtrackingProcessAllCiId.put(1, toComponentInstanceId);
		getBacktrackingAllProcessCIId(toComponentInstanceId, 2);// 缓存该工作流的组件实例ids

		int mapSize = backtrackingProcessAllCiId.size();
        ListIterator<Map.Entry<Object,String>> i = new ArrayList<>(backtrackingProcessAllCiId.entrySet()).listIterator(mapSize);

		String current_ci_id;
        while(i.hasPrevious()) {
            Map.Entry<Object, String> ci_id = i.previous();
			componentInstanceService.updateStatus(PANEL_ID, ComponentInstanceStatus.RUNNING);
			current_ci_id = ci_id.getValue();
//			String ciName = componentInstanceService.getComponentInstanceById(current_ci_id).getCiName();
			//根据当前组件实例的ID,判断是数据源组件，还是数据处理组件
			ComponentClassification componentType = booleanComponentType(current_ci_id);
			if ("SimpleDataSource".equals(componentType.name())) {/** 1 数据源组件 */
				if (current_ci_id.equals(first_ci_id)) {//源数据库first_ci_id
					//1.1 获取组件定义的ids，等价于数据源的ids
					List<String> cd_ids = getComponentDefinitionIds(fromComponents);
					String app_ds_id = cd_ids.get(0);
					String daas_ds_id = getDaasCatalogId(app_ds_id);
					//1.2 数据采集：获取到第一个数据源的vds：sql、字段、数据
					virtualDataset = getVDS(daas_ds_id, app_ds_id);
				} else {//目标数据库
					dataSetInstance = getDataSetInstance(current_ci_id);
					virtualDataset = buildVirtualDataset(dataSetInstance, dataSetInstance.getCiSql());
				}
				addCILog(PROCESS_ID,PANEL_ID, current_ci_id,"获取数据集-成功", 1);
            } else {
				virtualDataset = othersProcess(componentType, current_ci_id);
			}
			// 存储与当前组件实例对应的sql语句
			dataSetInstance = saveSql2Ci(virtualDataset, current_ci_id);
			if (dataSetInstance == null) {
				componentInstanceService.updateStatus(PANEL_ID, ComponentInstanceStatus.FAILURE);// 更新组件实例的状态
				addCILog(PROCESS_ID,PANEL_ID, current_ci_id,"数据集缓存-失败", 1);
				flag = false;
				break;
			}
			flag = true;
			componentInstanceService.updateStatus(PANEL_ID, ComponentInstanceStatus.SUCCESS);// 更新组件实例的状态
			addCILog(PROCESS_ID,PANEL_ID, current_ci_id,"数据集缓存-成功", 1);
		}
		backtrackingProcessAllCiId.clear();
		Map<String, Object> datasSave = insertTargetDB(toComponents);//第二步：数据持久化：找到目标源组件
		if (!flag) return JsonWrapper.jobFailure(1, "运行失败");
		return JsonWrapper.jobSuccess(datasSave, "运行成功");
	}

	private String buildVirtualDataset(DataSetInstance dataSetInstance, String sql) {
//		DataSetInstance dataSetInstance = getDataSetInstance(ci_id);
		JSONObject virtualDataset = new JSONObject();
		JSONObject details = new JSONObject();
		details.put("sql", sql);
		JSONObject datasetSummary = new JSONObject();
		datasetSummary.put("fields", dataSetInstance.getColumnsJSON());
		details.put("datasetSummary", datasetSummary);
		virtualDataset.put("details", details);
		return virtualDataset.toString();
	}

	/** Daas-2.2
	 * 从此运行
	 */
	public JSONObject runFromByCIId(String panelId, String fromComponentInstanceId,String token) {
		TOKEN = token;
//		List<Component> toComponents = componentService.getEndComponents(panelId, fromComponentInstanceId);
//		List<Component> allComponents = componentService.getAllComponentsFromCompIdWithPanel(panelId, fromComponentInstanceId);
		List<Component> fromComponents = new ArrayList<>();
		fromComponents.add(componentService.getById(fromComponentInstanceId));
		return new JSONObject();
	}

	/** Daas-3.1 */
	public Map<String, Object> runAllSingleTable(List<Component> fromComponents) {
		String virtualDataset = null;
		boolean flag = false;
		List<String> source_ci_ids = getComponentInstanceIds(fromComponents);// 获取源组件实例的ids
		String first_ci_id = source_ci_ids.get(0);
		processAllCiId.put(1, first_ci_id);
		getAllProcessCIId(source_ci_ids.get(0), 2);// 缓存该工作流的组件实例ids
//        addCILog(PROCESS_ID,PANEL_ID, "","工作流的组件实例id，缓存成功", 1);
		String current_ci_id;
		String current_sql = null;
		for (Map.Entry<Object, String> ci_id:processAllCiId.entrySet()) {
			flag = false;
			current_ci_id = ci_id.getValue();
			JSONObject sqlObj = getSqlByCIId(current_ci_id);
			if (sqlObj.getString("code").equals("0")) {/** SQL存在 */
				current_sql = sqlObj.getString("sql");
			}
			if (current_sql != null) {//判断当前实例SQL是否存在
                String msg = booleanUpdateCISQL(current_ci_id, current_sql);// 判断当前实例SQL是否需要更新
                if (msg.contains("Success")) {// 没有更新
                    flag = true;
                    current_sql = null;
                    continue;
                }
			}
//			String ciName = componentInstanceService.getComponentInstanceById(current_ci_id).getCiName();
			componentInstanceService.updateStatus(PANEL_ID, ComponentInstanceStatus.RUNNING);
			//根据当前组件实例的ID,判断是数据源组件，还是数据处理组件
			ComponentClassification componentType = booleanComponentType(current_ci_id);
			if ("SimpleDataSource".equals(componentType.name())) {/** 1 数据源组件 */
				if (current_ci_id.equals(first_ci_id)) {//源数据库
					//1.1 获取组件定义的ids，等价于数据源的ids
					String app_ds_id = getComponentDefinitionId(current_ci_id);
					String daas_ds_id = getDaasCatalogId(app_ds_id);
					//1.2 数据采集：获取到第一个数据源的vds：sql、字段、数据
					virtualDataset = getVDS(daas_ds_id, app_ds_id);
				} else {//目标数据库
					DataSetInstance dataSetInstance = getDataSetInstance(current_ci_id);
					virtualDataset = buildVirtualDataset(dataSetInstance, dataSetInstance.getCiSql());
				}
				addCILog(PROCESS_ID,PANEL_ID, current_ci_id,"获取数据集-成功", 1);
			} else {
				virtualDataset = othersProcess(componentType, current_ci_id);
			}
			if (virtualDataset.contains("失败")) {
				addCILog(PROCESS_ID,PANEL_ID, current_ci_id,"数据集SQL获取-失败", 1);
				flag = false;
				break;
			}
			DataSetInstance dataSetInstance = saveSql2Ci(virtualDataset, current_ci_id);// 存储与当前组件实例对应的sql语句
			if (dataSetInstance == null) {
				componentInstanceService.updateStatus(PANEL_ID, ComponentInstanceStatus.FAILURE);// 更新组件实例的状态
				addCILog(PROCESS_ID,PANEL_ID, current_ci_id,"数据集缓存-失败", 1);
				flag = false;
				break;
			}
			flag = true;
			componentInstanceService.updateStatus(PANEL_ID, ComponentInstanceStatus.SUCCESS);// 更新组件实例的状态
            addCILog(PROCESS_ID,PANEL_ID, current_ci_id,"数据集缓存-成功", 1);
		}
		processAllCiId.clear();
		if (!flag) {
			if (virtualDataset.contains("失败")) {
				return JsonWrapper.jobFailure(1, virtualDataset);
			}
			return JsonWrapper.jobFailure(1, "数据集缓存-失败");
		}
		return JsonWrapper.jobSuccess("", "Success");
	}

	/**
	 * 根据current_source_ci_id获取组件实例的处理信息ci_params
	 */
	private JSONObject getCiParams(String current_ci_id) {
        ComponentInstance componentInstance = getCIById(current_ci_id);
        Object ci_params = componentInstance.getCiParams();
        Gson gson = new Gson();
        String json = gson.toJson(ci_params);
        return JSON.parseObject(json);
    }

	/**
	 * 数据-单源-处理组件：Filter、TopN、GroupBy、FieldMapper、Tag
	 */
	private String othersProcess(ComponentClassification componentType, String current_ci_id) {
		String outSelectSQL = "";
		DataSetInstance dataSetInstance;
        JSONObject ciParams = getCiParams(current_ci_id);
        if("Filter".equals(componentType.name())){
			outSelectSQL = getSQLFilter(ciParams, current_ci_id);
		} else if("TopN".equals(componentType.name())) {
			outSelectSQL = getSQLTopN(ciParams, current_ci_id);
		} else if("GroupBy".equals(componentType.name())) {
			outSelectSQL = getSQLGroupBy(ciParams, current_ci_id);
        } else if("FieldMapper".equals(componentType.name())) {
			outSelectSQL = getSQLFieldMapper(ciParams, current_ci_id);
		} else if("Tag".equals(componentType.name())) {
			outSelectSQL = getSQLTag(ciParams, current_ci_id);
		}
		if (outSelectSQL == null) {
			String ci_name = componentInstanceService.getComponentInstanceById(current_ci_id).getCiName();
			return "获取处理组件["+ci_name+"]的SQL-失败！";
		}
		addCILog(PROCESS_ID,PANEL_ID, current_ci_id,"-"+componentType.name()+"-处理成功", 1);
		dataSetInstance = getDataSetInstance(getSourceCIIdsByDestCIId(current_ci_id).get(0));
		return buildVirtualDataset(dataSetInstance, outSelectSQL);
	}

    /**
     * Diff - 拼接sql语句 - 差集字段
     */
    private String getSQLDiff(JSONObject ciParams) {
		JSONArray ons = ciParams.getJSONArray("on");
		JSONObject source_ci_ids = ciParams.getJSONObject("sources");
		String left_ci_id = source_ci_ids.getString("left");
		String right_ci_id = source_ci_ids.getString("right");
//        List<String> sourceCIIds = getSourceCIIdsByDestCIId(middle_process_ci_id);

		DataSource source_ds = getDataSourceById(getComponentDefinitionId(left_ci_id));//sourceCIIds.get(0)
		String left_table_name = JSON.parseObject(source_ds.getDataConfig()).getString("tableName");
		DataSource target_ds = getDataSourceById(getComponentDefinitionId(right_ci_id));//sourceCIIds.get(1)
		String right_table_name = JSON.parseObject(target_ds.getDataConfig()).getString("tableName");
		String middle_alias_name;
		String left_alias_name = "l";
		String right_alias_name = "r";

		String left_sql = "";
		String sum_sql = "";

		// 获取目标数据库的 space_name,db_name,table_name
		String daas_ds_id = getDaasCatalogId(getComponentDefinitionId(left_ci_id));
		String space_name = processDaasService.getCatalogEntity(daas_ds_id);
		String db_name;
		if("POSTGRES".equals(source_ds.getDataDSType())) {
			db_name = JSON.parseObject(source_ds.getDataConfig()).getString("scheme");
		} else {
			db_name = JSON.parseObject(source_ds.getDataConfig()).getString("databaseName");
		}
		StringBuilder left_full_name = new StringBuilder();
		left_full_name.append("\"").append(space_name).append("\".").append(db_name).append(".").append(left_table_name);
        String left_full_name2 = left_full_name.toString();
		daas_ds_id = getDaasCatalogId(getComponentDefinitionId(right_ci_id));
		space_name = processDaasService.getCatalogEntity(daas_ds_id);
		if("POSTGRES".equals(source_ds.getDataDSType())) {
			db_name = JSON.parseObject(target_ds.getDataConfig()).getString("scheme");
		} else {
			db_name = JSON.parseObject(target_ds.getDataConfig()).getString("databaseName");
		}
		StringBuilder right_full_name = new StringBuilder();
		right_full_name.append("\"").append(space_name).append("\".").append(db_name).append(".").append(right_table_name);
		String right_full_name2 = right_full_name.toString();
		String middle_full_name;

		String left_columnsJSON = getDataSetInstance(left_ci_id).getColumnsJSON();
		String right_columnsJSON = getDataSetInstance(right_ci_id).getColumnsJSON();
		JSONArray left_columns_arr = JSONArray.parseArray(left_columnsJSON);
		JSONArray right_columns_arr = JSONArray.parseArray(right_columnsJSON);

		StringBuilder stringBuilder = new StringBuilder();
//		[{"name":"id","type":"INTEGER"},{"name":"speed","type":"INTEGER"}]
		for (Object o:left_columns_arr) {
			JSONObject object = (JSONObject)o;
			stringBuilder.append("l.").append(object.getString("name")).append(",");
		}
		String left_fields = stringBuilder.toString();
		left_fields = left_fields.substring(0, left_fields.length()-1);
		stringBuilder = new StringBuilder();
		for (Object o:right_columns_arr) {
			JSONObject object = (JSONObject)o;
			stringBuilder.append("r.").append(object.getString("name")).append(",");
		}
		String right_fields = stringBuilder.toString();
		right_fields = right_fields.substring(0, right_fields.length()-1);
		String all_fields = left_fields+","+right_fields;

		for (int i = 1;i <= 2;i ++) {
			String onStr = "";
			String whereStr = "";
			StringBuilder s = new StringBuilder();
			s.append("select ").append(all_fields).append(" from ");
			s.append(left_full_name2).append(" ").append(left_alias_name).append(" left join ")
					.append(right_full_name2).append(" ").append(right_alias_name).append(" on ");
			StringBuilder s1 = new StringBuilder();
			StringBuilder s2 = new StringBuilder();
			String columnName = "";
			for (Object on : ons) {
				JSONObject parameters = (JSONObject) on;
				String columnNameLeft = parameters.getString("columnNameLeft");
				String columnNameRight =  parameters.getString("columnNameRight");
//			s.append(left_table_name).append(".").append(columnName).append(" ,");
				if (i == 2) {
					columnName = columnNameLeft;
					columnNameLeft = columnNameRight;
					columnNameRight = columnName;
				}
				s1.append(left_alias_name).append(".").append(columnNameLeft).append(" = ")
						.append(right_alias_name).append(".").append(columnNameRight).append(" add ");
				onStr += s1.toString();
				s2.append(right_alias_name).append(".").append(columnNameRight).append(" is null add ");
				whereStr += s2.toString();
			}
			onStr = onStr.substring(0, onStr.length() - 4);
			whereStr = whereStr.substring(0, whereStr.length() - 4);
			left_sql = s.toString() + onStr + " where " + whereStr;

			middle_alias_name = left_alias_name;
			left_alias_name = right_alias_name;
			right_alias_name = middle_alias_name;

            middle_full_name = left_full_name2;
            left_full_name2 = right_full_name2;
            right_full_name2 = middle_full_name;

			sum_sql += " UNION "+left_sql;
		}
		return sum_sql.substring(6, sum_sql.length());
    }

	/**
	 * Filter - 拼接sql语句
	 */
	private String getSQLFilter(JSONObject ciParams, String current_ci_id) {
		JSONArray wheres = ciParams.getJSONArray("where");
		String whereString = "";
		if(wheres == null) return null;
		for (Object where: wheres) {
			JSONObject parameters = JSON.parseObject(where.toString());
			String num = parameters.getString("operator");
			String value = parameters.getString("value");
			String operatorMark = OperatorUtils.getMysqlOperator(num);
			whereString += " and " +parameters.getString("columnName")+ " " +operatorMark+ " " +value;
		}
//		String upper_ci_id = processAllCiId.get(ci_num-1);
		String upper_ci_id = getSourceCIIdsByDestCIId(current_ci_id).get(0);
		String SQL = getDataSetInstance(upper_ci_id).getCiSql();
		return  "select * from ( " +SQL+" ) f where 1=1 "+whereString;
	}

	/**
	 * TopN - 拼接sql语句
	 */
	private String getSQLTopN(JSONObject ciParams, String current_ci_id) {
		JSONArray wheres = ciParams.getJSONArray("where");
		String topNItem = ciParams.getString("topNItem");
		if (StringUtils.isBlank(topNItem)) topNItem = "10";
		String whereStr = " order by ";
		for (Object where: wheres) {
			JSONObject params = JSON.parseObject(where.toString());
			String columnName = params.getString("columnName");
			String sortKey = params.getString("sortKey");
			whereStr += columnName+ " " +sortKey+ ",";
		}
//		String upper_ci_id = processAllCiId.get(ci_num-1);
		String upper_ci_id = getSourceCIIdsByDestCIId(current_ci_id).get(0);
		String SQL = getDataSetInstance(upper_ci_id).getCiSql();
		return "select * from ( " +SQL+" ) t" + whereStr.substring(0,whereStr.length()-1) + " limit " + topNItem;
	}

	/**
	 * Union - 拼接sql语句
	 */
	private String getSQLUnion(JSONObject ciParams) {
//		List<String> source_ci_ids = getSourceCIIdsByDestCIId(current_ci_id);
//		String left_ci_id = source_ci_ids.get(0);
//      String right_ci_id = source_ci_ids.get(1);
        JSONObject source_ci_ids = ciParams.getJSONObject("sources");
        if (source_ci_ids.size() == 0) return null;
        String left_ci_id = source_ci_ids.getString("left");
        String right_ci_id = source_ci_ids.getString("right");
		String left_ci_sql = getSqlByCIId(left_ci_id).getString("sql");
		String right_ci_sql = getSqlByCIId(right_ci_id).getString("sql");

		JSONArray fields = ciParams.getJSONArray("on");
//			SELECT id,name FROM "mysqlDaas牛逼测试2".buffer_test.cc
//			union
//			SELECT id,name FROM "mysqlDaas牛逼测试2".buffer_test.cc
		String fieldStr = "";
//			{ "columnNameLeft": "ID", "columnNameRight": "ID" }
		for (Object field: fields) {
			JSONObject params = JSON.parseObject(field.toString());
			String columnName = params.getString("columnNameLeft");
			fieldStr += columnName+",";
		}
		// 拼接where条件
//			String upper_ci_id = processAllCiId.get(ci_num-1);
		String left_sql = "select " +fieldStr.substring(0,fieldStr.length()-1) + " from ( " +left_ci_sql +")";
		String right_sql = "select " +fieldStr.substring(0,fieldStr.length()-1) + " from ( " +right_ci_sql +")";
		return left_sql + " union " + right_sql;
	}

	/**
	 * FieldMapper - 拼接sql语句
	 */
	private String getSQLFieldMapper(JSONObject ciParams, String current_ci_id) {
		String selectFieldStr ="";
		JSONArray mapperFields = ciParams.getJSONArray("on");
		for (Object obj:mapperFields) {
				JSONObject object = (JSONObject)obj;
				String s = object.getString("source").split("<!>")[0];
				String t = object.getString("target").split("<!>")[0];
			selectFieldStr += " "+s+" AS "+t +",";
		}
		selectFieldStr = selectFieldStr.substring(0, selectFieldStr.length()-1);
		StringBuilder fieldMapperSQL = new StringBuilder();
		fieldMapperSQL.append("select ").append(selectFieldStr);

		DataSource source_ds = getDataSourceById(getComponentDefinitionId(getSourceCIIdsByDestCIId(current_ci_id).get(0)));
		if(source_ds == null){
			fieldMapperSQL.append(" from (").append(getSqlByCIId(getSourceCIIdsByDestCIId(current_ci_id).get(0)).getString("sql")).append(") fm ");
		}else{
			String source_table_name = JSON.parseObject(source_ds.getDataConfig()).getString("tableName");
			String daas_ds_id = getDaasCatalogId(getComponentDefinitionId(getSourceCIIdsByDestCIId(current_ci_id).get(0)));
			String space_name = processDaasService.getCatalogEntity(daas_ds_id);
			String db_name;
			if("POSTGRES".equals(source_ds.getDataDSType())){
				db_name = JSON.parseObject(source_ds.getDataConfig()).getString("scheme");
			}else {
				db_name = JSON.parseObject(source_ds.getDataConfig()).getString("databaseName");
			}
			fieldMapperSQL.append(" from \"").append(space_name).append("\".")
					.append(db_name).append(".").append(source_table_name);
		}
		return fieldMapperSQL.toString();
	}

	/**
	 * Tag - 打标签
	 */
	private String getSQLTag(JSONObject ciParams, String ci_id) {
		JSONArray tags = ciParams.getJSONArray("tags");
		if (tags.size() == 0) return null;
		String caseWhen = "";
		for (Object o:tags) {
			JSONObject object = (JSONObject)o;
			String tag = object.getString("tag");
			JSONArray filterCondition = object.getJSONArray("filterCondition");
			String when = "";
			for (Object ob:filterCondition) {
				JSONObject object2 = (JSONObject)ob;
				String operatorNum = object2.getString("operator");
				String operatorMark = OperatorUtils.getMysqlOperator(operatorNum);
				when += object2.getString("name")+operatorMark+object2.getString("value")+" and ";
			}
			when = when.substring(0, when.length()-4);
			caseWhen += "(case when("+when+") then '"+tag+",'"+" end),";
		}
		caseWhen = caseWhen.substring(0, caseWhen.length()-1);
		String cfId = getComponentDefinitionId(getSourceCIIdsByDestCIId(ci_id).get(0));
		DataSource source_ds = getDataSourceById(cfId);
		StringBuilder fieldMapperSQL = new StringBuilder();
		String alias = "s";//库的别名
		fieldMapperSQL.append("select " + alias + ".*,SUBSTR(CONCAT(")
				.append(caseWhen).append("),0,CHAR_LENGTH(CONCAT(")
				.append(caseWhen).append("))-1) as tag");
		if (source_ds == null) {
			fieldMapperSQL.append(" from (").append(getSqlByCIId(getSourceCIIdsByDestCIId(ci_id).get(0)).getString("sql")).append(") t ").append(alias);
		} else {
			String source_table_name = JSON.parseObject(source_ds.getDataConfig()).getString("tableName");
			String daas_ds_id = getDaasCatalogId(cfId);
			String space_name = processDaasService.getCatalogEntity(daas_ds_id);
			String db_name;
			if ("POSTGRES".equals(source_ds.getDataDSType())) {
				db_name = JSON.parseObject(source_ds.getDataConfig()).getString("scheme");
			} else {
				db_name = JSON.parseObject(source_ds.getDataConfig()).getString("databaseName");
			}
			fieldMapperSQL.append(" from \"").append(space_name).append("\".")
					.append(db_name).append(".").append(source_table_name)
					.append(" ").append(alias);
		}
		return fieldMapperSQL.toString();
	}

	/**
	 * GroupBy - 拼接sql语句
	 */
	private String getSQLGroupBy(JSONObject ciParams, String current_ci_id) {
		JSONArray summerize = ciParams.getJSONArray("where_summerize");
		JSONArray groupby = ciParams.getJSONArray("on_groupby");
		JSONArray having = ciParams.getJSONArray("where_having");
		String summerizeStr;
		String groupbyStr;
		String havingStr;
// class_no,min(age) as age
		StringBuilder s = new StringBuilder();
		for (Object obj : summerize) {
			JSONObject jsonObject = (JSONObject)obj;
			String num = jsonObject.getString("aggregationFunction");
			String aggregationFunction = "";
			String field = jsonObject.getString("name");
			if (num == null || field == null) continue;
			if (!num.equals("0")) {
				aggregationFunction = AggregationFunctions.getAggregationFunction(num);
				s.append(aggregationFunction).append("(").append(field).append(")")
						.append(" ").append("as").append(" ").append(field).append(",");
			} else {
				s.append(field).append(",");
			}
		}
		summerizeStr = s.substring(0, s.length()-1);
// group by class_no,age
		s = new StringBuilder();
		s.append(" group by ");
		for (Object obj : groupby) {
			JSONObject jsonObject = (JSONObject)obj;
			String field = jsonObject.getString("name");
			s.append(field).append(",");
		}
		groupbyStr = s.substring(0, s.length()-1);

// having avg(age) > 18 and class_no < 5;
		s = new StringBuilder();
		s.append(" having ");
		for (Object obj : having) {
			JSONObject jsonObject = (JSONObject)obj;
			String num = jsonObject.getString("aggregationFunction");
			String value = jsonObject.getString("value");
			String field = jsonObject.getString("columnName");
			String operatorNum = jsonObject.getString("operator");
			if (num == null || value == null || field == null || operatorNum == null) continue;
			String aggregationFunction = AggregationFunctions.getAggregationFunction(num);
			String operatorMark = OperatorUtils.getMysqlOperator(operatorNum);
			value = jsonObject.getString("value");
			if (!num.equals("0")) {
				s.append(aggregationFunction).append("(").append(field).append(")").append(" ")
						.append(operatorMark).append(" ").append(value).append(" ").append("and ");
			} else {
				s.append(field).append(" ").append(operatorMark).append(" ").append(value).append(" ").append("and ");
			}
		}
		havingStr = s.substring(0, s.length()-4);
		if (havingStr.equals(" hav")) havingStr = "";

//			select class_no,min(age) as age from (select * from test)
//					group by class_no
//					having avg(age) > 18;
		s = new StringBuilder();
//		String upper_ci_id = processAllCiId.get(ci_num-1);
		String upper_ci_id = getSourceCIIdsByDestCIId(current_ci_id).get(0);
		String SQL = getDataSetInstance(upper_ci_id).getCiSql();
		return s.append("select ").append(summerizeStr).append(" from ")
				.append("(").append(SQL).append(") gb ")
				.append(groupbyStr).append(" ").append(havingStr).toString();
	}

	/**
	 * 获取另一个表的params，作为在Daas上执行的入参
	 */
	public JSONObject getAnyOneTableParams(String right_source_ci_id) {
		String next_process_ci_id = getDestCIIdBySourceCIId(right_source_ci_id);//获取下一个组件实例ID
		ComponentInstance componentInstance = getCIById(next_process_ci_id);
		Object ci_params = componentInstance.getCiParams();
		Gson gson = new Gson();
		String json = gson.toJson(ci_params);
		JSONObject ciParams = JSON.parseObject(json);

		String joinType = ciParams.getString("code");//LeftJoin
		JSONArray onArrs = ciParams.getJSONArray("on");//join条件

		/** ------start------ */
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("type","join");
		if ("LeftJoin".equals(joinType)) {// 横向并表
			jsonParam.put("joinType","LeftOuter");// left join
		} else if ("Join".equals(joinType)) {// 交集字段
			jsonParam.put("joinType","Inner");// inner join
		} else if ("RightJoin".equals(joinType)) {
			jsonParam.put("joinType","RightOuter");// right join
		} else if ("Union".equals(joinType)) {// 纵向并表
			jsonParam.put("joinType","Union");//full join
            return jsonParam;
		} else if("Diff".equals(joinType)) { // 差集字段
            jsonParam.put("joinType","Diff");//full join
            return jsonParam;
		}

		String right_cd_id = getComponentDefinitionId(right_source_ci_id);// 根据右表组件实例ID获取到组件定义ID，即数据源ID
		String daas_ds_id = getDaasCatalogId(right_cd_id);// 根据右表组件定义ID获取到dass_ds_id
		// 获取(right-源数据库)的space_name,db_name,table_name
		String space_name;
		DataSource dataSource;
		String db_name;
		String table_name;

//		if (StringUtils.isBlank(daas_ds_id)) return jsonParam;//处理方案1
		if (StringUtils.isBlank(daas_ds_id)) {//处理方案2
			String upper_ci_id = getSourceCIIdsByDestCIId(right_source_ci_id).get(0);
			right_cd_id = getComponentDefinitionId(upper_ci_id);// 根据组件实例ID获取到组件定义ID，即数据源ID
			daas_ds_id = getDaasCatalogId(right_cd_id);
		}
		space_name = processDaasService.getCatalogEntity(daas_ds_id);
		if (space_name == null) return null;
		dataSource = getDataSourceById(right_cd_id);
		if("POSTGRES".equals(dataSource.getDataDSType())) {
			db_name = JSON.parseObject(dataSource.getDataConfig()).getString("scheme");
		} else {
			db_name = JSON.parseObject(dataSource.getDataConfig()).getString("databaseName");
		}
		table_name = JSON.parseObject(dataSource.getDataConfig()).getString("tableName");
		List<String> rightTableFullPathList = new ArrayList<>();
		rightTableFullPathList.add(space_name);
		rightTableFullPathList.add(db_name);
		rightTableFullPathList.add(table_name);

		jsonParam.put("rightTableFullPathList", rightTableFullPathList);
		jsonParam.put("joinConditionsList", onArrs);
		/** -------end------ */
//		addCILog(PROCESS_ID,PANEL_ID,right_source_ci_id,"获取右表的"+jsonParam.getString("joinType")+"参数信息",1);
		return jsonParam;
	}

	/** Daas-3.2 */
	public String runAllJoinTable(List<Component> fromComponents) {
		// 以一个表为基础sql：select * from  "mysqlDaas牛逼测试2".test.demo
		List<String> source_ci_ids = getComponentInstanceIds(fromComponents);// 获取源组件实例的ids
		String left_next_ci_id = getDestCIIdBySourceCIId(source_ci_ids.get(0));
		String right_next_ci_id = getDestCIIdBySourceCIId(source_ci_ids.get(1));
		if(StringUtils.isBlank(left_next_ci_id)) {
			addCILog(PROCESS_ID, PANEL_ID, left_next_ci_id, "无输出端！", 3);
			return "无输出端！";
		}
		if(StringUtils.isBlank(right_next_ci_id)) {
			addCILog(PROCESS_ID, PANEL_ID, right_next_ci_id, "无输出端！", 3);
			return "无输出端！";
		}
		if (!left_next_ci_id.equals(right_next_ci_id)) {
//			String middle_process_ci_id = booleanJoin(source_ci_ids.get(0), source_ci_ids.get(1));
//			if (middle_process_ci_id == null) return false;
			Map<String, Object> rs = runAllMoreTableMix(fromComponents);
			if (Integer.parseInt(rs.get("code")+"") == 1) {
				return "工作流运行失败，"+rs.get("message");
			}
			return "Success";
		}
		return run_mix_process_unit(left_next_ci_id);
	}

	private String booleanJoin(String left_ci_id, String right_ci_id) {
		String left_next_ci_id = getDestCIIdBySourceCIId(left_ci_id);
		String right_next_ci_id = getDestCIIdBySourceCIId(right_ci_id);
		if(StringUtils.isBlank(left_next_ci_id)) {
			addCILog(PROCESS_ID, PANEL_ID, left_next_ci_id, "无输出端！", 3);
			return null;
		}
		if(StringUtils.isBlank(right_next_ci_id)) {
			addCILog(PROCESS_ID, PANEL_ID, right_next_ci_id, "无输出端！", 3);
			return null;
		}
		if (!left_next_ci_id.equals(right_next_ci_id)) {
			String ci_id = booleanJoin(left_next_ci_id, right_next_ci_id);
			if(ci_id == null) return null;
			return ci_id;
		}
		return left_next_ci_id;
	}

	private Map<String, String> getLeftOrRightTableCIId(String middle_ci_id) {
	    Map<String, String> map = new HashMap<>();
        List<ComponentInstanceRelation> cirList = componentInstanceRelationService.getComponentInstanceRelationsByDestinationId(middle_ci_id);
        if (cirList != null && cirList.size() != 0) {
            for (ComponentInstanceRelation cir:cirList) {
                String ci_id = cir.getDestComponentInstanceId();
                if (cir.getDestInputName().equals("IN001")) map.put("left", ci_id);
                if (cir.getDestInputName().equals("IN002")) map.put("right", ci_id);
            }
        }
        return map;
    }

    public String run_mix_process_unit(String middle_process_ci_id) {// 处理组件实例的id
		List<String> source_ids = getSourceCIIdsByDestCIId(middle_process_ci_id);
        Map<String, String> map = getLeftOrRightTableCIId(middle_process_ci_id);

		String left_source_ci_id = map.get("left");//source_ids.get(0);// 获取左源的id
        String right_source_ci_id = map.get("right");//"";// 获取右源的id
//        for (String source_id : source_ids) {
//            if (!source_id.equals(left_source_ci_id)) {
//                right_source_ci_id = source_id;
//                break;
//            }
//        }
		String virtualDataset;
		if (!right_source_ci_id.equals("")) {
			JSONObject jsonParam = getAnyOneTableParams(right_source_ci_id);
			if (jsonParam.getString("joinType").equals("Union")) {
                JSONObject ci_params = getCiParams(middle_process_ci_id);
                String union_sql = getSQLUnion(ci_params);
				virtualDataset = processDaasService.runSqlOnDaas(union_sql);
            } else if (jsonParam.getString("joinType").equals("Diff")) {
                JSONObject ci_params = getCiParams(middle_process_ci_id);
                String diff_sql = getSQLDiff(ci_params);
				virtualDataset = buildVirtualDataset(getDataSetInstance(left_source_ci_id), diff_sql);
            } else {
				virtualDataset = twoTableJoin(left_source_ci_id, jsonParam);
            }
            if (virtualDataset.equals("daas_ds_id==null")) return "daas_ds_id不能为空！";
			saveSql2Ci(virtualDataset, middle_process_ci_id); // 存储与当前组件实例对应的sql语句
			addCILog(PROCESS_ID,PANEL_ID,middle_process_ci_id,"-"+jsonParam.getString("joinType")+"-结果集，存储成功",1);
		}
		if (source_ids.size() >= 2) {// 判断本次是否为两表关联处理
			if (getDataSetInstance(middle_process_ci_id) != null) {
				middle_process_ci_id = getDestCIIdBySourceCIId(middle_process_ci_id);
				if (middle_process_ci_id != null) {
					run_mix_process_unit(middle_process_ci_id);
				}
			}
		}
		return "Success";
    }

	public String twoTableJoin(String right_source_ci_id, JSONObject jsonParam) {
		String virtualDataset = "";
		String second_path_prefix = "/dataset/tmp.UNTITLED/version/";
		String joinNewVersion = "000" + RandomUtils.getNumStr_13();
		String second_path_next = "/transformAndPreview?newVersion=" +joinNewVersion+ "&limit=150";

		String right_cd_id = getComponentDefinitionId(right_source_ci_id);// 根据右表组件实例ID获取到组件定义ID，即数据源ID
		String daas_ds_id = getDaasCatalogId(right_cd_id);// 根据右表组件定义ID获取到dass_ds_id
		if(daas_ds_id == null) return "daas_ds_id==null";
		String secondPath = second_path_prefix + getNewVersion2(daas_ds_id,right_cd_id) + second_path_next;// 两表join
		String urlPath = this.daasServerAPIV2RootUrl + secondPath;
		try {
			virtualDataset = OkHttpRequest.okHttpClientPost(urlPath, jsonParam.toJSONString(), dssUserTokenService.getCurrentToken());
			addCILog(PROCESS_ID,PANEL_ID,right_source_ci_id,"两表"+jsonParam.getString("joinType")+"成功",1);
		} catch (Exception e) {
			addCILog(PROCESS_ID,PANEL_ID,right_source_ci_id,"两表"+jsonParam.getString("joinType")+"异常:"+e.getMessage(),2);
			logger.error("ProcessService.twoTableJoin(panel_id):请求DAAS异常");
		}
		return virtualDataset;
	}

	private String getNewVersion2(String daas_ds_id, String right_cd_id) {
		// 获取(right-源数据库)的space_name,db_name,table_name
		String space_name = processDaasService.getCatalogEntity(daas_ds_id);
		DataSource dataSource = getDataSourceById(right_cd_id);
		JSONObject dataConfig = JSON.parseObject(dataSource.getDataConfig());
		String db_name;
		if("POSTGRES".equals(dataSource.getDataDSType())) {
			db_name = dataConfig.getString("scheme");
		} else {
			db_name = dataConfig.getString("databaseName");
		}
		String table_name = dataConfig.getString("tableName");
		return getNewVersion(space_name, db_name, table_name);
	}

	public String twoTableJoin2(String new_version, JSONObject jsonParam) {
		String virtualDataset = "";
		String second_path_prefix = "/dataset/tmp.UNTITLED/version/";
		String joinNewVersion = "000"+RandomUtils.getNumStr_13();
		String second_path_next = "/transformAndPreview?newVersion=" +joinNewVersion+ "&limit=150";
		String secondPath = second_path_prefix + new_version + second_path_next;// 两表join
		String urlPath = this.daasServerAPIV2RootUrl + secondPath;
		try {
			virtualDataset = OkHttpRequest.okHttpClientPost(urlPath, jsonParam.toJSONString(), dSSUserTokenService.getCurrentToken());
			addCILog(PROCESS_ID,PANEL_ID,"","两表"+jsonParam.getString("joinType")+"成功",1);
		} catch (Exception e) {
			addCILog(PROCESS_ID,PANEL_ID,"","两表"+jsonParam.getString("joinType")+"异常:"+e.getMessage(),2);
			logger.error("ProcessService.twoTableJoin(panel_id):请求DAAS异常");
		}
		return virtualDataset;
	}

	public String getVDS(String dass_ds_id, String app_ds_id) {
		// 获取源数据库的 space_name,db_name,table_name
		String space_name = processDaasService.getCatalogEntity(dass_ds_id);
		DataSource dataSource = getDataSourceById(app_ds_id);
		String db_name;
		if("POSTGRES".equals(dataSource.getDataDSType())) {
			db_name = JSON.parseObject(dataSource.getDataConfig()).getString("scheme");
		} else {
			db_name = JSON.parseObject(dataSource.getDataConfig()).getString("databaseName");
		}
		String table_name = JSON.parseObject(dataSource.getDataConfig()).getString("tableName");
		String new_version = getNewVersion(space_name, db_name, table_name);
		String second_path_prefix = "/datasets/new_untitled?parentDataset=%22";
		String secondPath = second_path_prefix + space_name + "%22." + db_name + "." + table_name + "&newVersion=" + new_version + "&limit=150";

		JSONObject jsonParam = new JSONObject();
		jsonParam.put("parentDataset", space_name +"."+ db_name +"."+ table_name);
		jsonParam.put("newVersion", new_version);
		jsonParam.put("limit", 150);
		try {
			return OkHttpRequest.okHttpClientPost(this.daasServerAPIV2RootUrl + secondPath, jsonParam.toString(), dSSUserTokenService.getCurrentToken());
		} catch (Exception e) {
			logger.error("ProcessService.getVDS(panel_id):请求DAAS异常");
		}
		return "";
	}

	/** Daas-3.3 */
	public Map<String, Object> runAllMoreTableMix(List<Component> fromComponents) {//, List<Component> toComponents
		int maxLength = 0;
		int current_length;
		String ci_id = "";
		String ci_name = "";
		/** 选出一条最长路径，并开始从第一个数据源开始遍历执行 */
		for(Component component:fromComponents) {
			List<String> nodePath = new ArrayList<>();
			ComponentClassification cc = component.getComponentType();
			if (!"SimpleDataSource".equals(cc.name())) {
				ci_id = null;
				break;
			}
		    ci_id = component.getId();
			nodePath.add(ci_id);
			current_length = getNodePathByCiId(nodePath,ci_id).size();
			if (current_length > maxLength) {
				maxLength = current_length;
			}
		}
		if ("".equals(ci_name)) ci_name = componentInstanceService.getComponentInstanceById(ci_id).getCiName();
		if (null == ci_id) return JsonWrapper.jobFailure(1, "处理组件["+ci_name+"]没有数据来源：1010！");
        logger.info("该工作流遍历的起始的组件实例ID是："+ci_id);
		String max_path_ci_id = ci_id;//5
		String msg = iteratorCITree(max_path_ci_id);
		if(!"Success".equals(msg)) {
			return JsonWrapper.jobFailure(1, "处理组件["+ci_name+"]遍历失败：984！");
		}
	    return JsonWrapper.jobSuccess("", "Success");
	}

	/**
	 * 以最长路径中最开始的一个实例为基准，开始向下遍历整个Tree
	 */
	public String iteratorCITree(String source_ci_id) {
		String target_ci_id = getDestCIIdBySourceCIId(source_ci_id);
		if (!StringUtils.isBlank(target_ci_id)) {
			String msg = saveNextCISql(target_ci_id);
			if(!"Success".equals(msg)) return msg;// 每一步都存sql语句
			String msg0 = iteratorCITree(target_ci_id);
			if (!"Success".equals(msg0)) return msg0;
		}
		return "Success";
	}

	/**
	 * 向上迭代遍历Tree，直到遇到有sql的组件实例
	 */
	public String upperIteratorCITree(String target_ci_id) {//入参是join的左上节点组件实例
		String source_ci_id = getSourceCIIdsByDestCIId(target_ci_id).get(0);
		/** 是否还有上级节点 */
		if (!StringUtils.isBlank(source_ci_id)) {//有
			String sql = getSqlByCIId(source_ci_id).getString("sql");
			/** 上级节点是否有对应sql语句 */
			if (!StringUtils.isBlank(sql)) {//有
				return sql;
			} else {//没有
				return upperIteratorCITree(source_ci_id);
			}
		} else {//没有，则当前实例就是数据源，即含sql，再以此为基准向下执行，直到遇到join处理
			String ci_id = "";
			while (!ci_id.equals(target_ci_id)) {
				ci_id = downIteratorCITree(target_ci_id);
			}
			return getSqlByCIId(ci_id).getString("sql");
		}
	}
	public String downIteratorCITree(String ci_id) {
		String target_ci_id = getDestCIIdBySourceCIId(ci_id);
		if (!StringUtils.isBlank(target_ci_id)) {
			saveNextCISql(target_ci_id);// 存sql语句
			downIteratorCITree(target_ci_id);
		}
		return target_ci_id;
	}

	/**
	 * 存储下一个组件实例的SQL语句
	 */
	public String saveNextCISql(String second_ci_id) {//从第二个组件实例开始的
		String virtualDatasetStr;
		DataSetInstance dataSetInstance;
		String current_sql;
		JSONObject sqlObj = getSqlByCIId(second_ci_id);
		if (sqlObj.getString("code").equals("0")) {/** 组件实例Dataset存在 */
			current_sql = sqlObj.getString("sql");
			if ("".equals(current_sql) || null == current_sql) {// SQL不存在
				String ci_name = componentInstanceService.getComponentInstanceById(second_ci_id).getCiName();
				if (!delSqlByCIId(second_ci_id)) return "删除处理组件:"+ci_name+"的数据集-失败！";
				return "处理组件:"+ci_name+"，获取SQL失败！1077";
			} else {// SQL存在
                String msg = booleanUpdateCISQL(second_ci_id, current_sql);// 判断当前实例SQL是否需要更新
                if (msg.contains("Success")) return "Success";// 没有更新
            }
			dataSetInstance = getDataSetInstance(getSourceCIIdsByDestCIId(second_ci_id).get(0));
			virtualDatasetStr = buildVirtualDataset(dataSetInstance, current_sql);
		} else {/** SQL不存在 */ // 除了数据源实例的sql直接查，其他都是根据上游的sql拼接
			ComponentClassification componentType = booleanComponentType(second_ci_id);
//			JSONObject ciParams = getCiParams(ci_id);
			// 处理组件：表关联Join、LeftJoin
			if ("Join".equals(componentType.name()) || "LeftJoin".equals(componentType.name())) {//5、6
//				List<String> source_ci_ids = getSourceCIIdsByDestCIId(second_ci_id);
//				String left_source_ci_id = source_ci_ids.get(0);
//				String right_source_ci_id = "";
//				for (String ciId:source_ci_ids) {
//					if (!left_source_ci_id.equals(ciId)) {
//						right_source_ci_id = ciId;
//						break;
//					}
//				}
                Map<String, String> map = getLeftOrRightTableCIId(second_ci_id);
                String left_source_ci_id = map.get("left");
                String right_source_ci_id = map.get("right");
				sqlObj = getSqlByCIId(left_source_ci_id);
				current_sql = sqlObj.getString("sql");
				JSONObject jsonParam_left = getAnyOneTableParams(left_source_ci_id);
				if (jsonParam_left == null) return "未成功获取table的params作为在Daas上执行的入参！1091";
				JSONObject jsonParam_right = getAnyOneTableParams(right_source_ci_id);
				if (jsonParam_right == null) return "未成功获取table的params作为在Daas上执行的入参！1093";
				if (sqlObj.getString("code").equals("1")) {//没有sql
					// 往上回溯，直到遇到源表，再开始向下执行有sql的组件实例
					current_sql = upperIteratorCITree(left_source_ci_id);
				}
				List<String> bs = (ArrayList)jsonParam_left.get("rightTableFullPathList");
				virtualDatasetStr = twoTableJoin2(getNewVersion(bs.get(0), bs.get(1) , bs.get(2)), jsonParam_right);
				if (current_sql == null || "".equals(current_sql)) return "实例的SQL获取失败！1100";
				if (virtualDatasetStr.contains("\"errorMessage\"")) return "两表在DAAS上执行失败！1101："+virtualDatasetStr;
				virtualDatasetStr = CharReplaceUtils.replaceSql(virtualDatasetStr, current_sql);
				addCILog(PROCESS_ID,PANEL_ID, second_ci_id,"两表"+jsonParam_right.getString("joinType")+"结果集，处理成功",1);
			} else if("Union".equals(componentType.name())) {//处理组件：Union
				JSONObject ci_params = getCiParams(second_ci_id);
				String union_sql = getSQLUnion(ci_params);
				if (union_sql == null) return "处理组件:"+componentType.name()+"，获取SQL失败！1106";
				virtualDatasetStr = processDaasService.runSqlOnDaas(union_sql);
				addCILog(PROCESS_ID,PANEL_ID,second_ci_id,"并表结果集，存储成功",1);
			} else {
				if("Diff".equals(componentType.name())) {//处理组件：Diff
					JSONObject ci_params = getCiParams(second_ci_id);
					current_sql = getSQLDiff(ci_params);
					dataSetInstance = getDataSetInstance(second_ci_id);
					addCILog(PROCESS_ID,PANEL_ID,second_ci_id,"差集结果集，存储成功",1);
				} else {//单源-处理组件：Filter、TopN、GroupBy、FieldMapper、Tag
					current_sql = othersProcess(componentType, second_ci_id);
					dataSetInstance = getDataSetInstance(getSourceCIIdsByDestCIId(second_ci_id).get(0));
				}
				if (current_sql == null) return "处理组件:"+componentType.name()+"，获取SQL失败！1118";
				virtualDatasetStr = buildVirtualDataset(dataSetInstance, current_sql);
			}
		}
		DataSetInstance dsi = saveSql2Ci(virtualDatasetStr, second_ci_id);//存储与当前组件实例对应的sql语句
		if (dsi == null) return "存储与当前组件实例对应的SQL语句失败！1123";
		return "Success";
	}

	/**
	 * 获取当前源实例的路径
	 */
	private List<String> getNodePathByCiId(List<String> nodePath,String source_ci_id) {
		String target_ci_id = getDestCIIdBySourceCIId(source_ci_id);
		if (!StringUtils.isBlank(target_ci_id)) {
			nodePath.add(target_ci_id);
			getNodePathByCiId(nodePath,target_ci_id);
		}
		return nodePath;
	}

	/** Daas-3 */
	public Map<String, Object> runAll(String panelId,String token) {
		TOKEN = token;
		PROCESS_ID = UUIDUtils.random();
	    PANEL_ID = panelId;
		// 第一步：校验项目面板是否为空
//		List<Component> allComponents = componentService.getAllComponentsWithPanel(panelId);//获取panel中所有组件
//		if(allComponents.size() == 0) return JsonWrapper.jobFailure(1,  ReturnMessageUtils.ComponentIsNull);
		// 第二步：数据预备：根据panelId，获取组件实例
		List<Component> fromComponents = componentService.getBeginComponents(panelId);//获取panel中所有的源组件
        // 第三步：数据处理
		if (fromComponents.size() == 1) { //单个数据源，进行：数据过滤Filter、分组统计GroupBy、TopN、字段映射FieldMapper
			Map<String, Object> rs = runAllSingleTable(fromComponents);
			if (Integer.parseInt(rs.get("code").toString()) == 1) {
			    componentInstanceService.updateStatus(panelId,ComponentInstanceStatus.FAILURE);
				addCILog(PROCESS_ID,PANEL_ID, "","单表处理-工作流运行失败"+rs.get("message"), 2);
                return JsonWrapper.jobFailure(1, "1#工作流运行失败："+rs.get("message"));
            }
		} else if(fromComponents.size() == 2) { //多个数据源，进行：横向并表
			String str = runAllJoinTable(fromComponents);
			if (!"Success".equals(str)) {
                componentInstanceService.updateStatus(panelId,ComponentInstanceStatus.FAILURE);
				addCILog(PROCESS_ID,PANEL_ID, "","多表处理-工作流运行失败："+str, 2);
                return JsonWrapper.jobFailure(1, "2#工作流运行失败："+str);
            }
		} else { //多个数据源，进行多表join
            Map<String, Object> rs = runAllMoreTableMix(fromComponents);//, toComponents
            if (Integer.parseInt(rs.get("code")+"") == 1) {
                componentInstanceService.updateStatus(panelId,ComponentInstanceStatus.FAILURE);
				addCILog(PROCESS_ID,PANEL_ID, "","多表混合处理-工作流运行失败，"+rs.get("message"), 2);
                return JsonWrapper.jobFailure(1, "3#工作流运行失败："+rs.get("message"));
            }
        }
		// 第四步：数据持久化：save目标源组件
		List<Component> toComponents = componentService.getEndComponents(panelId);//获取panel中所有的目标组件
		Map<String, Object> results = insertTargetDB(toComponents);
		if (results.get("code").equals(102)) {//入库失败
			componentInstanceService.updateStatus(panelId,ComponentInstanceStatus.FAILURE);
		} else {// 未接目标库 | 入库成功
			componentInstanceService.updateStatus(panelId,ComponentInstanceStatus.SUCCESS);
		}
		return results;
	}

	/**
	 * 表dataset_instance：根据组件实例id获取sql
	 */
	public JSONObject getSqlByCIId(String ci_id) {
		DataSetInstance dataSetInstance = getDataSetInstance(ci_id);
		JSONObject jsonObject = new JSONObject();
		if (dataSetInstance == null) {
			jsonObject.put("code", "1");//不存在
			jsonObject.put("jobId", ReturnMessageUtils.IsNull);
			jsonObject.put("sql", ReturnMessageUtils.IsNull);
			jsonObject.put("message", ReturnMessageUtils.ComponentInstanceResultsIsNull);
		} else {
			jsonObject.put("code", "0");//存在
			jsonObject.put("jobId", "-");
			jsonObject.put("sql", dataSetInstance.getCiSql());
			jsonObject.put("message", ReturnMessageUtils.ComponentInstanceResultsIsNotNull);
		}
		return jsonObject;
	}
	public boolean delSqlByCIId(String ci_id) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			DataSetInstanceDao dao = sqlSession.getMapper(DataSetInstanceDao.class);
			boolean flag = dao.delete(ci_id);
			sqlSession.commit();
			if (flag) return true;
			return false;
		}
	}

	public ComponentInstance getCIById(String current_ci_id) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceDao dao = sqlSession.getMapper(ComponentInstanceDao.class);
			return dao.get(current_ci_id);
		}
	}

	public ComponentClassification booleanComponentType(String ci_id) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceDao dao = sqlSession.getMapper(ComponentInstanceDao.class);
			ComponentInstance componentInstance = dao.get(ci_id);
			return componentInstance.getComponentType();
		}
	}

	public void getAllProcessCIId(String first_ci_id, int index) {
		List<String> dest_ci_ids;
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceRelationDao dao = sqlSession.getMapper(ComponentInstanceRelationDao.class);
			dest_ci_ids = dao.getDestIdBySourceId(first_ci_id);
			sqlSession.close();
		}
        if (dest_ci_ids.size() > 0) {
			processAllCiId.put(index++, dest_ci_ids.get(0));
            getAllProcessCIId(dest_ci_ids.get(0), index);
        }
	}

	public void getBacktrackingAllProcessCIId(String dest_ci_id, int index) {
		String source_ci_id = "";
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceRelationDao dao = sqlSession.getMapper(ComponentInstanceRelationDao.class);
			List<String> source_ci_ids = dao.getSouceCIIdsByDestCIIds(dest_ci_id);
			if (source_ci_ids.size() > 0) {
				source_ci_id = source_ci_ids.get(0);
			}
			sqlSession.close();
		}
		if (!source_ci_id.equals("")) {
			backtrackingProcessAllCiId.put(index++, source_ci_id);
			getBacktrackingAllProcessCIId(source_ci_id, index);
		}
	}

	public String getDaasCatalogId(String app_ds_id) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			DataSourceDao dao = sqlSession.getMapper(DataSourceDao.class);
			return dao.getDaasV3Catalog(app_ds_id);
		}
	}
	public DataSource getDataSourceById(String app_ds_id) {
		DataSource dataSource;
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			DataSourceDao dao = sqlSession.getMapper(DataSourceDao.class);
			dataSource = dao.findDataSourceById(app_ds_id);
			sqlSession.close();
		}
		return dataSource;
	}
	public String getDestCIIdBySourceCIId(String source_ci_id) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()){
			ComponentInstanceRelationDao dao = sqlSession.getMapper(ComponentInstanceRelationDao.class);
			List<String> dest_ci_ids = dao.getDestIdBySourceId(source_ci_id);
			if (dest_ci_ids.size() == 0) {
				return null;
			}
			return dest_ci_ids.get(0);
		}
	}
	public List<String> getSourceCIIdsByDestCIId(String target_ci_id) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()){
			ComponentInstanceRelationDao dao = sqlSession.getMapper(ComponentInstanceRelationDao.class);
			return dao.getSouceCIIdsByDestCIIds(target_ci_id);
		}
	}
	public List<String> getSourceCIIdsByDestCIIds(List<String> target_ci_ids) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()){
			ComponentInstanceRelationDao dao = sqlSession.getMapper(ComponentInstanceRelationDao.class);
			return dao.getSouceCIIdsByDestCIIds(target_ci_ids.get(0));
		}
	}

	public DataSetInstance saveSql2Ci(String virtualDataset, String current_ci_id) { //, String sqlBackup
		Gson gson = new Gson();
		DataSetInstance dataSetInstance = new DataSetInstance();
		dataSetInstance.setId(current_ci_id);
		if(virtualDataset == null) return null;
		JSONObject vds = JSON.parseObject(virtualDataset);
		JSONObject dataset = vds.getJSONObject("dataset");
		String sql;
		if (dataset == null || dataset.equals("")) {
			JSONObject details = vds.getJSONObject("details");
			if (details == null || details.equals("")) {
				sql = vds.getString("sql");
				dataSetInstance.setColumnsJSON(vds.getJSONArray("schema").toJSONString());
			} else {
				sql = details.getString("sql");
				JSONObject datasetSummary;
				if (sql.contains("\"details\"")) {
					vds = JSON.parseObject(sql);
					details = vds.getJSONObject("details");
					sql = details.getString("sql");
					datasetSummary = details.getJSONObject("datasetSummary");
				} else {
					datasetSummary = details.getJSONObject("datasetSummary");
				}
				dataSetInstance.setColumnsJSON(gson.toJson(datasetSummary.getJSONArray("fields")));
			}
		} else {
			sql = dataset.getString("sql");
			JSONObject data = dataset.getJSONObject("data");
			if(data == null) {
				JSONObject data1 = JSON.parseObject(virtualDataset).getJSONObject("data");
				dataSetInstance.setColumnsJSON(gson.toJson(data1.getJSONArray("columns")));
			} else {
				dataSetInstance.setColumnsJSON(gson.toJson(data.getJSONArray("columns")));
			}
		}
		sql = CharReplaceUtils.replace_N(sql);
		dataSetInstance.setCiSql(sql);
		String panel_name = panelService.getPanelById(PANEL_ID).getPanelName();
		String ci_name = componentInstanceService.getComponentInstanceById(current_ci_id).getCiName();
		dataSetInstance.setDescription("项目面板:["+panel_name+"],组件实例:["+ci_name+"]");

		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			DataSetInstanceDao dao = sqlSession.getMapper(DataSetInstanceDao.class);
            String msg = booleanUpdateCISQL(current_ci_id, sql);
			if (msg.contains("insert")) {
				dao.insert(dataSetInstance);
			} else if (msg.contains("update")) {
				dao.update(dataSetInstance);
			} else {
			    return dataSetInstance;
            }
			sqlSession.commit();
		} catch (Exception e) {
			componentInstanceService.updateStatus(PANEL_ID,ComponentInstanceStatus.FAILURE);
			return null;
		}
		return dataSetInstance;
	}

	private String booleanUpdateCISQL(String current_ci_id, String sql) {
        DataSetInstance dsi = getDataSetInstance(current_ci_id);
        if (dsi == null) {
            return "组件实例SQL-insert";
        }
        if (!dsi.getCiSql().equals(sql)) {
            return "组件实例SQL-update";
        }
        return "Success";
    }

	/**
	 * 将 主题数据 插入目标库
	 */
    public Map<String, Object> insertTargetDB(List<Component> toComponents) {
        List<String> target_ci_ids = getComponentInstanceIds(toComponents);// 获取目标组件实例的ids
		if (target_ci_ids.size() > 1) return JsonWrapper.jobFailure(100, "暂不支持"+target_ci_ids.size()+"个目标库的工作流模型！");
		// 校验数据源是否为目标端
		if (!verifyDataSourceIsTarget(target_ci_ids)) {
			return JsonWrapper.jobFailure(100, "任务执行完毕，工作流未处理任何数据！");
		}
        ComponentClassification componentType = booleanComponentType(target_ci_ids.get(0));
        if ("Filter".equals(componentType.name()) || "Join".equals(componentType.name()) ||
				"Diff".equals(componentType.name()) || "Union".equals(componentType.name()) ||
				"GroupBy".equals(componentType.name()) || "TopN".equals(componentType.name()) ||
				"LeftJoin".equals(componentType.name()) || "FieldMapper".equals(componentType.name()) ||
				"Tag".equals(componentType.name())) {
            addCILog(PROCESS_ID,PANEL_ID, target_ci_ids.get(0),"任务执行完毕，工作流尚未连接目标库", 1);
            return JsonWrapper.jobFailure(101, "任务执行完毕，工作流尚未连接目标库！");
        } else {
            addCILog(PROCESS_ID,PANEL_ID, target_ci_ids.get(0),"结果集开始入库", 1);
            List<String> upper_ci_ids = getSourceCIIdsByDestCIIds(target_ci_ids);
			String upper_sql = getSqlByCIId(upper_ci_ids.get(0)).getString("sql");
            String result = processDaasService.runSqlOnDaas(upper_sql);
            return insertToTargetDB(toComponents, result);
        }
    }
	
	private boolean verifyDataSourceIsTarget(List<String> target_ci_ids) {
		boolean flag = true;
		for (String target_ci_id:target_ci_ids) {
			List<Object> inputNode = (List<Object>)getCIById(target_ci_id).getInputName();
			if (inputNode.size() == 0) {
				addCILog(PROCESS_ID,PANEL_ID, target_ci_id,"任务执行完毕，工作流未处理任何数据！", 3);
				flag = false;
			}
		}
		return flag;
	}

	/**
	 * 即将修改，还未修改
	 */
	public Map<String, Object> insertToTargetDB(List<Component> toComponents, String result) {
		String target_cd_id = getComponentDefinitionId(toComponents.get(0).getId());
		DataSource target_ds = getDataSourceById(target_cd_id);
		ConnectionInfoAndOthers ciao = getConnectionInfoAndOthers(target_ds, result);
        addCILog(PROCESS_ID,PANEL_ID, toComponents.get(0).getId(),"工作流运行结束", 1);
        try {
            dbOperationService.insertDatas(ciao); //mysql,oracle+pg+es+Mem
            componentInstanceService.updateStatus(PANEL_ID,ComponentInstanceStatus.SUCCESS);
            addCILog(PROCESS_ID,PANEL_ID, toComponents.get(0).getId(),"结果集入库-成功", 1);
            return JsonWrapper.jobSuccess(result, "结果集入库-成功");
        } catch (Exception e) {
            addCILog(PROCESS_ID,PANEL_ID, toComponents.get(0).getId(),"结果集入库-失败:"+e.getMessage(),2);
            return JsonWrapper.jobFailure(102, "结果集入库-失败");
        }
	}

	/**
	 * 组织目标库的连接信息 和 数据(sql)
	 * 批量入库：默认500条
	 */
	private ConnectionInfoAndOthers getConnectionInfoAndOthers(DataSource target_ds, String resultOrSql) {
		String db_type = target_ds.getDataDSType();
		JSONObject dataConfig = JSON.parseObject(target_ds.getDataConfig());
		String host_ip = dataConfig.getString("hostname");
		String host_port = dataConfig.getString("port");
		if("ELASTIC".equals(db_type)){
			List<Map<String, String>>  hostlist = new Gson()
					.fromJson(dataConfig.getString("hostList"), new TypeToken<List<Map<String, String>>>() {
					}.getType());
			host_ip = hostlist.get(0).get("hostname");
			host_port = hostlist.get(0).get("port");
		}
		String user_name = dataConfig.getString("username");
		String user_password = dataConfig.getString("password");
		String scheme = dataConfig.getString("scheme") == null ? "":dataConfig.getString("scheme");
		String db_name;
		if("ORACLE".equals(db_type)) {
			db_name = dataConfig.getString("instance");
		} else {
			db_name = dataConfig.getString("databaseName");
		}
		String table_name = dataConfig.getString("tableName");

		ConnectionInfoAndOthers connectionInfo = new ConnectionInfoAndOthers();
		connectionInfo.setTypeName(db_type);
		connectionInfo.setHostIP(host_ip);
		connectionInfo.setHostPort(host_port);
		connectionInfo.setUserName(user_name);
		connectionInfo.setUserPassword(user_password);
		connectionInfo.setDatabaseName(db_name);
		connectionInfo.setScheme(scheme);
		connectionInfo.setTableName(table_name);
		connectionInfo.setDatas(resultOrSql);
		connectionInfo.setBatchSize("500");
		return connectionInfo;
	}

	public String getNewVersion(String space_name, String db_name, String table_name) {
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("spaceName", space_name);
		jsonParam.put("dbName", db_name);
		jsonParam.put("tableName", table_name);
		return dssVersionService.getCurrentVersion(jsonParam);
	}

	public DataSetInstance getDataSetInstance(String upper_ci_id) {
		DataSetInstance dataSetInstance = null;
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			DataSetInstanceDao dao = sqlSession.getMapper(DataSetInstanceDao.class);
			if (upper_ci_id != null) dataSetInstance = dao.get(upper_ci_id);
		}
		return dataSetInstance;
	}

	/**
	 * 记录运行日志
	 * @param logsType 1-info，2-error，3-warn
	 */
	public void addCILog(String processId, String panelId, String componentInstanceId, String actionMsg, int logsType) {
		String user_id = exchangeSSOService.getAccount(TOKEN);
		JSONObject msgObj = new JSONObject();
		String msg;
		if (StringUtils.isBlank(componentInstanceId)) {
			msg = "["+new DateTime().toString("YYYY-MM-dd HH:mm:ss")+"] 面板["+panelService.getPanelById(panelId).getPanelName()+"] "+actionMsg;
		} else {
			msg = "["+new DateTime().toString("YYYY-MM-dd HH:mm:ss")+"] 组件["+componentInstanceService.getComponentInstanceById(componentInstanceId).getCiName()+"] "+actionMsg;
		}
		msgObj.put("id", UUIDUtils.random());
		msgObj.put("recordId", "-");
		msgObj.put("createTime", new DateTime().toString("YYYY-MM-dd HH:mm:ss"));
		msgObj.put("logsType", logsType);
		msgObj.put("message", msg);
		processRecordService.create(processId, panelId, componentInstanceId, user_id, msgObj);
	}

    /**
     * 暂停任务=取消job
     */
    public String cancelJobById(String jobId, String token) {
    	TOKEN = token;
        String urlPath = this.daasServerAPIV2RootUrl + "/job/" + jobId + "/cancel";
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("type", "OK");
        jsonParam.put("message", "Job cancellation requested");
		String result = null;
		try {
			result = OkHttpRequest.okHttpClientPost(urlPath, jsonParam.toString(), dSSUserTokenService.getCurrentToken());
        } catch (Exception e) {
            logger.error("ProcessService.cancelJobById(jobId):请求DAAS异常");
			addCILog(PROCESS_ID,PANEL_ID, "","暂停任务-失败："+e.getMessage(), 1);
        }
		addCILog(PROCESS_ID,PANEL_ID, "","暂停任务-成功", 1);
        return result;
    }

	public Map<String,Object> checkIsOrLegal(String panelId) {
		Map<String,Object> resultMap = new HashMap<>();
		List<Component> toComponents = componentService.getEndComponents(panelId);//获取panel中所有的目标组件
		if (toComponents.size() == 0 || toComponents.size() != 1) {
			resultMap.put("code", "1");
			resultMap.put("message", "暂不支持"+toComponents.size()+"个并行工作流模型！");
		} else {
//			Component target = toComponents.get(0);
			/*
			List<Component> allComponents = componentService.getAllComponentsWithPanel(panelId);//获取panel中所有组件
			int[] array = new int[allComponents.size()];
			for(int i = 0;i < allComponents.size();i ++) {
				array[i] = Integer.parseInt(allComponents.get(i).getId().toString());
			}
//			int[] array = {12,76,35,22,16,48,90,46,9,40};
//			BinaryTree root = new BinaryTree(array[0]); //创建二叉树
			BinaryTree root = new BinaryTree(Integer.parseInt(target.getId()));
			for(int i = 1;i < array.length;i ++) {
				root.insert(root, array[i]); //向二叉树中插入数据
			}
			System.out.println("先根遍历：");
			preOrder(root);
			*/
			/*
			tree = new ArrayList<>();
			treeList = new ArrayList<>();
			String ciId = target.getId();
			upperIteratorCTree(ciId);
			if (treeList.size() > 1) {
				resultMap.put("message", "暂不支持"+treeList.size()+"个并行工作流模型！");
			}
			System.err.println(treeList);
			*/
			resultMap.put("code", "0");
		}
		return resultMap;
	}

	/**
	 * 勿删：向上迭代遍历Tree，直到没有组件实例
	 */
//	private static List<String> tree = null;
//	private static List<List<String>> treeList = null;
//	public void upperIteratorCTree(String target_ci_id) {//入参是最后一个组件实例
//		tree.add(target_ci_id);
//		List<String> source_ci_ids = getSourceCIIdsByDestCIId(target_ci_id);
//		if (source_ci_ids.size() == 0) {// 顶端：源
//			treeList.add(tree);
//			tree = new ArrayList<>();
//		} else if (source_ci_ids.size() == 1) {// 一个数据来源
//			String sourceCiId = source_ci_ids.get(0);
//			upperIteratorCTree(sourceCiId);
//		} else {// 两个数据来源
//			String sourceCiId1 = source_ci_ids.get(0);
//			String sourceCiId2 = source_ci_ids.get(1);
//			upperIteratorCTree(sourceCiId1);
//			upperIteratorCTree(sourceCiId2);
//		}
//	}
//    public static void preOrder(BinaryTree root) {//先根遍历
//        if(root != null) {
//            System.out.print(root.data+"-");
//            preOrder(root.left);
//            preOrder(root.right);
//        }
//    }
}
