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

package com.datasphere.engine.shaker.processor.instance.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.datasphere.common.data.Dataset;
import com.datasphere.common.utils.OkHttpServletRequest;
import com.datasphere.common.utils.RandomUtils;
import com.datasphere.core.common.BaseService;
import com.datasphere.core.common.utils.UUIDUtils;
import com.datasphere.engine.common.exception.JIllegalOperationException;
import com.datasphere.engine.common.exception.JRuntimeException;
import com.datasphere.engine.core.utils.ExceptionConst;
import com.datasphere.engine.core.utils.JAssert;
import com.datasphere.engine.core.utils.ObjectMapperUtils;
import com.datasphere.engine.manager.resource.provider.db.dao.DataSourceDao;
import com.datasphere.engine.manager.resource.provider.elastic.model.QueryDBDataParams;
import com.datasphere.engine.manager.resource.provider.model.DataSource;
import com.datasphere.engine.manager.resource.provider.service.DataQueryService;
import com.datasphere.engine.shaker.processor.common.constant.ComponentClassification;
import com.datasphere.engine.shaker.processor.definition.ComponentDefinition;
import com.datasphere.engine.shaker.processor.definition.constant.GlobalDefine;
import com.datasphere.engine.shaker.processor.definition.dao.ComponentDefinitionDao;
import com.datasphere.engine.shaker.processor.instance.analysis.WarpInputAndOutput;
import com.datasphere.engine.shaker.processor.instance.constant.ComponentInstanceStatus;
import com.datasphere.engine.shaker.processor.instance.constant.DataKeyPrefix;
import com.datasphere.engine.shaker.processor.instance.dao.ComponentInstanceDao;
import com.datasphere.engine.shaker.processor.instance.dao.ComponentInstanceRelationDao;
import com.datasphere.engine.shaker.processor.instance.model.ComponentInstance;
import com.datasphere.engine.shaker.processor.instance.model.ComponentInstanceRelation;
import com.datasphere.engine.shaker.processor.instance.model.DeleteComponentInstanceResult;
import com.datasphere.engine.shaker.processor.instance.model.GraphNode;
import com.datasphere.engine.shaker.processor.instance.model.UpdatePositionEntity;
import com.datasphere.engine.shaker.workflow.panel.dao.PanelDao;
import com.datasphere.server.connections.dao.DataSetInstanceDao;
import com.datasphere.server.connections.model.DataSetInstance;
import com.datasphere.server.connections.service.DataAccessor;
import com.datasphere.server.sso.service.DSSUserTokenService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;


/**
 * 组件实例服务接口实现类
 */
@Service
public class ComponentInstanceService extends BaseService {
	private final static Log logger = LogFactory.getLog(ComponentInstanceService.class);
	private final String OUTNAME = "OUT001";
	@Autowired
	DSSUserTokenService dSSUserTokenService;
	@Autowired
	DataQueryService dataQueryService;
//	@Autowired
//	ExchangeSSOService exchangeSSOService;

	public ComponentInstance get(String id) {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceDao dao = sqlSession.getMapper(ComponentInstanceDao.class);
			ComponentInstance instance = dao.get(id);
			if (instance == null) {
				throw new JIllegalOperationException("组件不存在！ID＝" + id);
			}
			return instance;
		}
	}

	/**
	 * 创建组件实例：组件拖拽进panel
	 */
	public int insert(ComponentInstance componentInstance, String token) throws SQLException{
		ComponentDefinition componentDefinition = null;
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentDefinitionDao componentDefinitionDao = sqlSession.getMapper(ComponentDefinitionDao.class);
			// 判断组件是否存在
			componentDefinition = componentDefinitionDao.get(componentInstance.getComponentDefinitionId());
			if (componentDefinition == null) {
				throw new JIllegalOperationException("组件不存在！ID＝" + componentInstance.getComponentDefinitionId());
			}
		}
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			PanelDao panelDao = sqlSession.getMapper(PanelDao.class);
			// 判断面板是否存在
			JAssert.isTrue(panelDao.exists(componentInstance.getPanelId()), "面板不存在！ID＝" + componentInstance.getPanelId());
			if (null != componentDefinition.getParams())
				componentInstance.setCiParams(ObjectMapperUtils.writeValue(componentDefinition.getParams()));
			// 冗余保存组件英文名称
			try {
				componentInstance.setComponentType(Enum.valueOf(ComponentClassification.class, componentDefinition.getCode()));
//				 判断是否为子流程
			} catch (RuntimeException e) {
				throw new JRuntimeException(GlobalDefine.ERROR_CODE.COMPONENT_NAME_CONSISTENCY,
						"组件名称与应用内部定义不一致！NAME＝" + componentDefinition.getCode());
			}
			componentInstance.setComponentClassification(componentDefinition.getClassification());// 冗余保存组件分类
			componentInstance.setInputName(ObjectMapperUtils.writeValue(componentDefinition.getOut_point()));// input output
			componentInstance.setOutputName(ObjectMapperUtils.writeValue(componentDefinition.getIn_point()));
		}
		componentInstance = setComponentInstance(componentDefinition.getName(),componentInstance,componentInstance.getCiFrontPage(),token);
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceDao componentInstanceDao = sqlSession.getMapper(ComponentInstanceDao.class);
			if(componentInstance.getComponentDefinitionId() != null){
				try{
					//insert dataset_instance
					DataSetInstanceDao dataSetInstanceDao = sqlSession.getMapper(DataSetInstanceDao.class);
					QueryDBDataParams query = findDataSourceById(componentInstance.getComponentDefinitionId());
					if(query != null) {
						Map<String,Object> result = queryTableData(query);
						DataSetInstance dataSetInstance = new DataSetInstance();
						dataSetInstance.setId(componentInstance.getId());
						dataSetInstance.setColumnsJSON(new Gson().toJson(result.get("schema")));
						dataSetInstance.setCiSql(result.get("sql").toString());
						dataSetInstance.setDescription("源数据实例查询");
						dataSetInstanceDao.insert(dataSetInstance);//insert
					}
				}catch (Exception e){
					e.printStackTrace();
					return 0;
				}

			}

			int flag = componentInstanceDao.insert(componentInstance);
			sqlSession.commit();
			return flag;
		}
	}

	public ComponentInstance setComponentInstance(String name, ComponentInstance componentInstance, String ciFrontPage, String token) {
		String componentNameValue = "";
		String paramsValue = "";
		String inputNameValue = "";
		String outputNameValue = "";
		try {
			JsonParser jsonParser = new JsonParser();
			JsonElement element = jsonParser.parse(ciFrontPage);
			JsonObject jsonObj = element.getAsJsonObject();
			componentNameValue = jsonObj.get("componentName").toString().replace("\"","").replace("\"","");
			paramsValue = jsonObj.get("params").toString();
			inputNameValue = jsonObj.get("targetPort").toString();
			outputNameValue = jsonObj.get("sourcePort").toString();
		} catch (Exception e) {
			throw new JIllegalOperationException("ciFrontPage入参缺少！");
		}
		componentInstance.setCiName(componentNameValue);
		// 请求带名称，则判断名称是否重复。请求不带名称，则自动生成名称
		if (StringUtils.isEmpty(componentInstance.getCiName())) {
			componentInstance.setCiName(grtName(componentInstance.getPanelId(), name));
		} else {
			if (exists(componentInstance.getPanelId(), componentInstance.getCiName())) {
//				throw new JIllegalOperationException(ExceptionConst.NAME_REPEAT, "组件实例名称[" + componentInstance.getCiName() + "]已存在！");
				componentInstance.setCiName(grtName(componentInstance.getPanelId(), name));
			}
		}
		componentInstance.setStatus(ComponentInstanceStatus.PREPARED);// 设置状态为预处理状态：3
		componentInstance.setId(UUIDUtils.random());//jeq
//		componentInstance.setCreator(exchangeSSOService.getAccount(token));
		componentInstance.setCiParams(paramsValue);
		componentInstance.setInputName(inputNameValue);
		componentInstance.setOutputName(outputNameValue);
		componentInstance.setCiDescription("The component instance name is："+componentNameValue);
		return componentInstance;
	}

	public int update(ComponentInstance componentInstance) {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceDao dao = sqlSession.getMapper(ComponentInstanceDao.class);
			JAssert.isTrue(dao.belong(componentInstance.getId(), componentInstance.getCreator()), "不能操作他人的组件实例！");
		}
		ComponentInstance ci = null;
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceDao dao = sqlSession.getMapper(ComponentInstanceDao.class);
			// 查询当前面板所有组件实例名称
			ci = dao.get(componentInstance.getId());
			JAssert.isTrue(ci != null, "待修改的组件实例不存在！ID=" + componentInstance.getId());
			if (ci.getComponentType() == ComponentClassification.DataPreProcess) {

			} else {
				Object paramsStr = ci.getCiParams();
				if (null != paramsStr && componentInstance.getCiParams() != null) {
					HashMap<String, Object> reqParams = (HashMap<String, Object>) paramsStr;
					Gson gson=new Gson();
					HashMap<String, Object> p = gson.fromJson(gson.toJson(componentInstance.getCiParams()), HashMap.class);
					for (Entry<String, Object> key : p.entrySet()) {
						reqParams.put(key.getKey(), key.getValue());
					}
					componentInstance.setCiParams(ObjectMapperUtils.writeValue(reqParams));
				}
			}
		}
		// 名称没变，设置为空表示不修改。名称改变，查询新名称是否唯一。
		if (ci.getCiName().equals(componentInstance.getCiName())) {
			componentInstance.setCiName(null);
		} else {
			if (!StringUtils.isEmpty(componentInstance.getCiName())) {
				if (exists(ci.getPanelId(), componentInstance.getCiName())) {
					throw new JIllegalOperationException(ExceptionConst.NAME_REPEAT, "组件实例名称[" + componentInstance.getCiName() + "]已存在！");
				}
			}
		}
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceDao dao = sqlSession.getMapper(ComponentInstanceDao.class);
			int flag = dao.update(componentInstance); // 根据实体ID更新实体属性。为空的属性不予更新
			sqlSession.commit();
			return flag;
		}
	}

	public void updatePosition(UpdatePositionEntity entity) {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceDao dao = sqlSession.getMapper(ComponentInstanceDao.class);
			// 更新组件实例位置。如果组件实例不存在，则报错
			JAssert.isTrue(dao.update(entity) == 1, "组件实例不存在！ID=" + entity.getId());
			sqlSession.commit();
		}
		// 更新组件实例相关的关联线位置
		String relations = entity.getRelations();
		if (!StringUtils.isEmpty(relations)) {
			List<LinkedHashMap<String, Object>> cirs = ObjectMapperUtils.readValue(relations, List.class);
			for (Map<String, Object> cir : cirs) {
				String id = (String) cir.get("id");
				Object ciFrontPage = cir.get("ciFrontPage");
				String ciFrontPage2 = "";
				// 如果关联线的前端参数是对象，则转换成串
				if (ciFrontPage != null && !(ciFrontPage instanceof String)) {
					ciFrontPage2 = ObjectMapperUtils.writeValue(ciFrontPage);
				}
				if (!StringUtils.isEmpty(id) && !StringUtils.isEmpty(ciFrontPage2)) {
					try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
						ComponentInstanceRelationDao componentInstanceRelationDao = sqlSession.getMapper(ComponentInstanceRelationDao.class);
						componentInstanceRelationDao.update(new ComponentInstanceRelation(id, ciFrontPage2.toString()));
						sqlSession.commit();
					}
				}
			}
		}
	}

	public DeleteComponentInstanceResult deleteComponentInstance(String id, String token) {
		// TODO 注释
		// deleteDataSet(id); //jeq

		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceDao dao = sqlSession.getMapper(ComponentInstanceDao.class);
			// 判断当前组件实例的创建者是否是当前用户
//			JAssert.isTrue(dao.belong(id, exchangeSSOService.getAccount(token)), "组件实例不存在或者删除了他人的组件实例！");
		}

		int flag = 0;
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceDao dao = sqlSession.getMapper(ComponentInstanceDao.class);
			DataSetInstanceDao dataSetInstanceDao = sqlSession.getMapper(DataSetInstanceDao.class);
			dataSetInstanceDao.delete(id);
			flag = dao.delete(id);
			sqlSession.commit();
		}


		if (flag == 1) {
			DeleteComponentInstanceResult result = new DeleteComponentInstanceResult();
			result.setComponentInstanceId(id);
			try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
				ComponentInstanceRelationDao componentInstanceRelationDao = sqlSession.getMapper(ComponentInstanceRelationDao.class);
				result.setComponentInstanceRelationIds(componentInstanceRelationDao.listBySourceIdOrDestId(id));
				componentInstanceRelationDao.deleteBySourceIdOrDestId(id);
				sqlSession.commit();
				return result;
			}
		}
		return null;
	}

	public String matchDataKey(String outName, String id) {
		return (DataKeyPrefix.PREFIX_WORKFLOW + outName + "_" + id).toLowerCase();
	}

	public void deleteDataSet(String id) {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			DataAccessor dataAccessor = sqlSession.getMapper(DataAccessor.class);
			ComponentInstance instance = this.get(id);
			if (ComponentClassification.SimpleDataSource != instance.getComponentType()) {
				Gson gson=new Gson();
				String outputNameArray = gson.toJson(instance.getOutputName());
				List<WarpInputAndOutput> output =gson.fromJson(outputNameArray, new TypeToken<List<WarpInputAndOutput>>(){}.getType());
				if (null != output) {
					for (WarpInputAndOutput warp : output) {
						String dataKey = matchDataKey(warp.getCode(), id);
						try {
							logger.info("删除dateset,dataKey=" + dataKey);
							dataAccessor.removeDataset(dataKey);
						} catch (SQLException e) {
							logger.error(e.getMessage());
						}
					}
				}
			}
		}
	}

//	public ComponentInstance copy(ComponentInstance instance) {

	/**
	 * 组件实例复制功能
	 * @param creator
	 * @param componentInstanceId
	 * @return
	 */
	public ComponentInstance copy(String creator, String componentInstanceId) {
		ComponentInstance componentInstance;
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceDao dao = sqlSession.getMapper(ComponentInstanceDao.class);
			// 判断当前组件实例的创建者是否是当前用户
			 JAssert.isTrue(dao.belong(componentInstanceId, creator), "组件实例不存在或者删除了他人的组件实例！");
			String id = componentInstanceId;
			componentInstance = dao.get(id);
			componentInstance.setId(UUIDUtils.random());
			if (componentInstance.getComponentType() == ComponentClassification.DataPreProcess) {
				boolean b = copyPreDataProcessComponent(id, componentInstance.getId(), creator);
				JAssert.isTrue(b, ExceptionConst.Failed, "预处理复制失败");
			}
		}
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceDao dao = sqlSession.getMapper(ComponentInstanceDao.class);
			componentInstance.setCiName(grtName(componentInstance.getPanelId(), componentInstance.getCiName()));
			String ci_front_page = componentInstance.getCiFrontPage();
			JSONObject jsonObject = JSON.parseObject(ci_front_page);
			int x = Integer.parseInt(jsonObject.getString("x"));
			jsonObject.put("x", x+=235);
			componentInstance.setCiFrontPage(jsonObject.toJSONString());
			componentInstance.setCiParams(ObjectMapperUtils.writeValue(componentInstance.getCiParams()));
			componentInstance.setInputName(ObjectMapperUtils.writeValue(componentInstance.getInputName()));
			componentInstance.setOutputName(ObjectMapperUtils.writeValue(componentInstance.getOutputName()));
			componentInstance.setStatus(ComponentInstanceStatus.PREPARED);
			dao.insert(componentInstance);
			sqlSession.commit();
			sqlSession.close();
			return componentInstance;
		}
	}

	// tested
	public ComponentInstance getComponentInstanceById(String id) {
        return get(id);
	}

	/**
	 * 根据ID查询所有的源CI
	 * @param panelId
	 * @return
	 */
	public List<ComponentInstance> getBeginComponentInstances(String panelId) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceDao dao = sqlSession.getMapper(ComponentInstanceDao.class);
			return dao.listBeginComponentInstancesByPanelId(panelId);
		}
	}

	/**
	 * 根据ID查询所有的CI
	 * @param panelId
	 * @return
	 */
	public List<ComponentInstance> getAllComponentInstancesWithPanel(String panelId) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceDao dao = sqlSession.getMapper(ComponentInstanceDao.class);
			return dao.listBy(new ComponentInstance(panelId));
		}
	}

	// tested
	public List<ComponentInstance> getAllComponentInstancesFromCompInsIdWithPanel(String panelId,
			String fromComponentInstanceId) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceRelationDao componentInstanceRelationDao = sqlSession.getMapper(ComponentInstanceRelationDao.class);
			List<ComponentInstance> cis = getAllComponentInstancesWithPanel(panelId);
			if (cis.size() > 0) {
				// 转化成图结点
				List<GraphNode> nodes = wrapper(cis);
				// 建立索引
				Map<String, GraphNode> index = buildIndex(nodes);
				// 查询出面板全部关联线
				List<ComponentInstanceRelation> cirs = componentInstanceRelationDao
						.listBy(new ComponentInstanceRelation(panelId));
				// 给图结点建立关联
				buildRelation(index, cirs);
				// 查询出起始结点
				GraphNode startNode = index.get(fromComponentInstanceId);
				// 遍历查询出从起始点开始经过的全部结点
				Set<GraphNode> set = new HashSet<GraphNode>();
				set.add(startNode);
				for (GraphNode.NodePoint np : startNode.nexts()) {
					getNextComponentInstances(np.getNode(), set);
				}
				return unWrapper(set);
			}
			return cis;
		}
	}

	// tested
	public List<ComponentInstance> getAllComponentInstancesToCompInsIdWithPanel(String panelId,
			String toComponentInstanceId) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceRelationDao componentInstanceRelationDao = sqlSession.getMapper(ComponentInstanceRelationDao.class);
			List<ComponentInstance> cis = getAllComponentInstancesWithPanel(panelId);
			if (cis.size() > 0) {
				// 转化成图结点
				List<GraphNode> nodes = wrapper(cis);
				// 建立索引
				Map<String, GraphNode> index = buildIndex(nodes);
				// 查询出面板全部关联线
				List<ComponentInstanceRelation> cirs = componentInstanceRelationDao
						.listBy(new ComponentInstanceRelation(panelId));
				// 给图结点建立关联
				buildRelation(index, cirs);
				// 查询出终止结点
				GraphNode stopNode = index.get(toComponentInstanceId);
				// 遍历查询出从起始点到终结点经过的全部结点
				Set<GraphNode> set = new HashSet<GraphNode>();
				set.add(stopNode);
				for (GraphNode.NodePoint np : stopNode.prevs()) {
					getPrevComponentInstances(np.getNode(), set);
				}
				return unWrapper(set);
			}
			return cis;
		}
	}

	// tested
	public List<ComponentInstance> getBeginComponentInstances(String panelId, String toComponentInstanceId) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			List<ComponentInstance> start = new LinkedList<ComponentInstance>();
			List<ComponentInstance> list = getAllComponentInstancesToCompInsIdWithPanel(panelId, toComponentInstanceId);
			for (ComponentInstance ci : list) {// 根据组件分类判断是否起始点
				if (ci.getComponentClassification().startsWith(GlobalDefine.COMPONENT_CLASSIFICATION.MY_DATASOURCE)) {
					start.add(ci);
				}
			}
			return start;
		}
	}

	public List<ComponentInstance> getEndComponentInstances(String panelId) {
		List<ComponentInstance> cis = getAllComponentInstancesWithPanel(panelId);//根据panel_id查询所有的CI
		if (cis.size() > 0) {
			List<GraphNode> nodes = wrapper(cis);// 转化成图结点
			Map<String, GraphNode> index = buildIndex(nodes);// 建立索引
			try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
				ComponentInstanceRelationDao componentInstanceRelationDao = sqlSession.getMapper(ComponentInstanceRelationDao.class);
				// 查询出面板全部关联线
				List<ComponentInstanceRelation> cirs = componentInstanceRelationDao.listBy(new ComponentInstanceRelation(panelId));
				buildRelation(index, cirs);// 给图结点建立关联
				// 遍历查询出没有输出点的组件实例
				Set<GraphNode> set = new HashSet<GraphNode>();
				for (GraphNode np : nodes) {
					if (np.nexts().size() == 0) set.add(np);
				}
				return unWrapper(set);
			}
		}
		return cis;
	}

	public List<ComponentInstance> getEndComponentInstances(String panelId, String fromComponentInstanceId) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceRelationDao componentInstanceRelationDao = sqlSession.getMapper(ComponentInstanceRelationDao.class);
			List<ComponentInstance> cis = getAllComponentInstancesWithPanel(panelId);
			if (cis.size() > 0) {
				// 转化成图结点
				List<GraphNode> nodes = wrapper(cis);
				// 建立索引
				Map<String, GraphNode> index = buildIndex(nodes);
				// 查询出面板全部关联线
				List<ComponentInstanceRelation> cirs = componentInstanceRelationDao
						.listBy(new ComponentInstanceRelation(panelId));
				// 给图结点建立关联
				buildRelation(index, cirs);
				// 查询出起始结点
				GraphNode startNode = index.get(fromComponentInstanceId);
				// 遍历查询出从起始点开始经过的全部结点
				Set<GraphNode> set = new HashSet<GraphNode>();
				set.add(startNode);
				for (GraphNode.NodePoint np : startNode.nexts()) {
					getJoinComponentInstances(np.getNode(), set);
				}
				// 遍历子图查询全部结束点
				Set<GraphNode> endSet = new HashSet<GraphNode>();
				for (GraphNode node : set) {
					if (node.nexts().size() == 0) {
						endSet.add(node);
					}
				}
				return unWrapper(endSet);
			}
			return cis;
		}
	}

	public Boolean existsBy(String componentDefinitionId) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceDao baseDao = sqlSession.getMapper(ComponentInstanceDao.class);
			return baseDao.existsBy(componentDefinitionId);
		}
	}

	public int updateStatus(String panelId, String status) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceDao baseDao = sqlSession.getMapper(ComponentInstanceDao.class);
			int rs = baseDao.updateStatusByPanelId(panelId, status);
			sqlSession.commit();
			return rs;
		}
	}

	public int batchUpdateStatus(List<String> idList) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceDao baseDao = sqlSession.getMapper(ComponentInstanceDao.class);
			return baseDao.batchUpdateStatus(idList);
		}
	}

	public List<GraphNode> wrapper(List<ComponentInstance> list) {
		List<GraphNode> nodes = new LinkedList<GraphNode>();
		for (ComponentInstance ci : list) {
			nodes.add(new GraphNode(ci));
		}
		return nodes;
	}

	public List<ComponentInstance> unWrapper(Collection<GraphNode> list) {
		List<ComponentInstance> nodes = new LinkedList<ComponentInstance>();
		for (GraphNode node : list) {
			nodes.add(node.getPayload());
		}
		return nodes;
	}

	public Map<String, GraphNode> buildIndex(List<GraphNode> nodes) {
		Map<String, GraphNode> index = new HashMap<String, GraphNode>();
		for (GraphNode node : nodes) {
			index.put(node.getPayload().getId(), node);
		}
		return index;
	}

	public void buildRelation(Map<String, GraphNode> index, List<ComponentInstanceRelation> cirs) {
		for (ComponentInstanceRelation cir : cirs) {
			GraphNode pNode = index.get(cir.getSourceComponentInstanceId());
			GraphNode nNode = index.get(cir.getDestComponentInstanceId());
			pNode.setNext(cir.getSourceOutputName(), nNode);
			nNode.setPrev(cir.getDestInputName(), pNode);
		}
	}

	public Set<GraphNode> getJoinComponentInstances(GraphNode node, Set<GraphNode> set) {
		boolean exists = set.add(node);
		if (!exists)
			return set;
		for (GraphNode.NodePoint np : node.prevs()) {
			getJoinComponentInstances(np.getNode(), set);
		}
		for (GraphNode.NodePoint np : node.nexts()) {
			getJoinComponentInstances(np.getNode(), set);
		}
		return set;
	}

	public Set<GraphNode> getPrevComponentInstances(GraphNode node, Set<GraphNode> set) {
		boolean exists = set.add(node);
		if (!exists)
			return set;
		for (GraphNode.NodePoint np : node.prevs()) {
			getPrevComponentInstances(np.getNode(), set);
		}
		return set;
	}

	public Set<GraphNode> getNextComponentInstances(GraphNode node, Set<GraphNode> set) {
		boolean exists = set.add(node);
		if (!exists)
			return set;
		for (GraphNode.NodePoint np : node.nexts()) {
			getNextComponentInstances(np.getNode(), set);
		}
		return set;
	}

	public String grtName(String panelId, String prefix) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceDao dao = sqlSession.getMapper(ComponentInstanceDao.class);
			// 获取此面板下所有的组件实例名称
			List<String> names = dao.listNamesByPanelId(panelId);
			int suffix = 1;
			String ciName = prefix;
			while (names.contains(ciName)) {
				ciName = prefix + "-" + suffix++;
			}
			return ciName;
		}
	}

	public Boolean exists(String panelId, String name) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceDao baseDao = sqlSession.getMapper(ComponentInstanceDao.class);
			// 获取此面板下所有的组件实例名称
			List<String> names = baseDao.listNamesByPanelId(panelId);
			return names.contains(name.trim());
		}
	}

	public List<String> listIdByPanelIdList(List<String> panelIdList) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceDao baseDao = sqlSession.getMapper(ComponentInstanceDao.class);
			return baseDao.listIdByPanelIdList(panelIdList);
		}
	}

	public int deleteByPanelIdList(List<String> panelIdList) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceDao baseDao = sqlSession.getMapper(ComponentInstanceDao.class);
			// Set<String> set = new HashSet<>(panelIdList);
			// for (String panelId : panelIdList) {
			// set.addAll(this.listSubIdByPanelId(panelId));
			// }
			for (int i = 0; i < panelIdList.size(); i++) {
				List<ComponentInstance> instances = this.getAllComponentInstancesWithPanel(panelIdList.get(i));
				for (ComponentInstance instance : instances) {
					//TODO 注释
//					deleteDataSet(instance.getId());
				}
			}
			return baseDao.deleteByPanelIdList(panelIdList);
		}
	}

	/**
	 * 项目面板的另存为：整个工作流的复制 = 项目面板的复制
	 * @param oldPanelId
	 * @param newPanelId
	 * @param creator
	 * @return
	 */
	public Map<String, String> copy(String oldPanelId, String newPanelId, String creator) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceDao baseDao = sqlSession.getMapper(ComponentInstanceDao.class);
			Map<String, String> map = new HashMap<String, String>();
			List<ComponentInstance> cis = baseDao.listBy(new ComponentInstance(oldPanelId));
			for (ComponentInstance ci : cis) {
				String oldCiid = ci.getId();
				ci.setId(UUIDUtils.random());
				if (ci.getComponentType() == ComponentClassification.DataPreProcess) {
					boolean b = copyPreDataProcessComponent(oldCiid, ci.getId(), creator);
					JAssert.isTrue(b, ExceptionConst.Failed, "预处理复制失败");
				}
				ci.setCreator(creator);
				ci.setPanelId(newPanelId);
				ci.setStatus(ComponentInstanceStatus.PREPARED);
				ci.setCiParams(ObjectMapperUtils.writeValue(ci.getCiParams()));
				ci.setInputName(ObjectMapperUtils.writeValue(ci.getInputName()));
				ci.setOutputName(ObjectMapperUtils.writeValue(ci.getOutputName()));

				baseDao.insert(ci);
				sqlSession.commit();
				map.put(oldCiid, ci.getId());
			}
			return map;
		}
	}

	public boolean copyPreDataProcessComponent(String oldProcessId, String newProcessId, String creator) {

		try {
			return false;
			// TODO 注释
//			return preDataComponentService.copy(oldProcessId, newProcessId, creator);
		} catch (Exception e) {
			return false;
		}
	}

	public ComponentInstance getInstanceBySubPanelId(ComponentInstance instance) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceDao baseDao = sqlSession.getMapper(ComponentInstanceDao.class);
			// TODO Auto-generated method stub
			return baseDao.getInstanceBySubPanelId(instance);
		}
	}

	public List<Dataset> getDatasetByComponentId(String componentId, String outputName) throws Exception {
		DataAccessor dataAccessor = new DataAccessor();
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			List<Dataset> datasets = new ArrayList<>();
			Map<String, String> map = this.getComponentOutDataSetKey(componentId);
			if (StringUtils.isEmpty(outputName)) {
				for (Entry<String, String> key : map.entrySet()) {
					Dataset dataset = dataAccessor.getDataset(key.getValue(), 1, 100);
					if (null != dataset)
						datasets.add(dataset);
				}
			} else {
				for (Entry<String, String> key : map.entrySet()) {
					if (outputName.equals(key.getKey())) {
						Dataset dataset = dataAccessor.getDataset(key.getValue(), 1, 100);
						if (null != dataset)
							datasets.add(dataset);
					}
				}
			}
			return datasets;
		}
	}

	public Map<String, String> getComponentOutDataSetKey(String componentId) {
		Map<String, String> map = new HashMap<>();
		ComponentInstance instance = this.get(componentId);
		JAssert.isTrue(instance != null, "组件实例不存在！componentId=" + componentId);
		if (ComponentClassification.SimpleDataSource == instance.getComponentType()) {
			map.put(OUTNAME, instance.getComponentDefinitionId());
		} else if (ComponentClassification.DataPreProcess == instance.getComponentType()) {
			HashMap<String, Object> reqParams = (HashMap<String, Object>) instance.getCiParams();
			String dataKey = String.valueOf(reqParams.get("dataKey"));
			map.put(OUTNAME, dataKey);
		} else {
			Gson gson = new Gson();
			String outputNameArray = gson.toJson(instance.getOutputName());
			List<WarpInputAndOutput> output = gson.fromJson(outputNameArray, new TypeToken<List<WarpInputAndOutput>>(){}.getType());
			if (null != output) {
				for (WarpInputAndOutput warp : output) {
					String dataKey = matchDataKey(warp.getCode(), componentId);
					map.put(warp.getCode(), dataKey);
				}
			}
		}
		return map;
	}

	/**
	 * 根据ID查询源查询条件
	 * @param id
	 * @return
	 */
	public QueryDBDataParams findDataSourceById(String id) {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			DataSourceDao dataSourceDao = sqlSession.getMapper(DataSourceDao.class);
//            String dassId = dataSourceDao.getDaasV3Catalog(id);
			//获取daasName 获取databaseName and tableName
			DataSource dataSource = dataSourceDao.findDataSourceById(id);
			if(dataSource == null){
				return null;
			}
			Map<String, Object>  gsonMap = new Gson()
					.fromJson(dataSource.getDataConfig(), new TypeToken<Map<String, Object>>() {
					}.getType());
			QueryDBDataParams query = new QueryDBDataParams();
			query.setDaasName(dataSourceDao.getDaasNameByID(id));
			if("POSTGRES".equals(dataSource.getDataDSType())){
				query.setDatabaseName(gsonMap.get("scheme").toString());
			}else{
				query.setDatabaseName(gsonMap.get("databaseName").toString());
			}
			query.setTableName(gsonMap.get("tableName").toString());
			if("CSV".equals(dataSource.getDataDSType()) || "JSON".equals(dataSource.getDataDSType())){
				query.setSql("SELECT * FROM \""+query.getDatabaseName()+"\".\""+query.getTableName()+"\" limit 1000");
			}else{
				query.setSql("SELECT * FROM \""+query.getDaasName()+"\"."+query.getDatabaseName()+"."+query.getTableName()+" limit 1000");
			}
			return query;
		}
	}


	/**
	 * 查询数据集
	 *
	 * @param query
	 * @return
	 */
	public Map<String, Object> queryTableData(QueryDBDataParams query) {
		try {
			if (StringUtils.isEmpty(query.getSql())) {
				query.setSql(oneTableQuery(query));
			}
			if (StringUtils.isEmpty(query.getSql())) {
				return null;
			}

			return dataQueryService.dataQuery(query.getSql());
		} catch (Exception e) {
			logger.error("{}", e);
		}
		return null;
	}

	public String oneTableQuery(QueryDBDataParams query){
		String vds = "";
		try {
			String secondPath = "/datasets/new_untitled?parentDataset=%22" +
					query.getDaasName() + "%22." +
					query.getDatabaseName() + "." +
					query.getTableName() +
					"&newVersion=" + "000" + RandomUtils.getNumStr_13() +
					"&limit=150";// 单表
			String urlPath = this.daasServerAPIV2RootUrl + secondPath;
			String jsonStr = "{\"parentDataset\":\"'" + query.getDaasName() +
					"'." + query.getDatabaseName() + ".'" +
					query.getTableName()
					+ "'\",\"newVersion\":\"" +
					"000" + RandomUtils.getNumStr_13() +
					"\",\"limit\":\"150\"}";
			try {
				vds = OkHttpServletRequest.okHttpClientPost(urlPath, jsonStr, dSSUserTokenService.getCurrentToken());
			} catch (Exception e) {
				logger.error("ProcessService.oneTableQuery(panel_id):请求DAAS异常");
			}

			JSONObject second_vds = null;
			if (vds.contains("details")) {
				second_vds = JSON.parseObject(vds).getJSONObject("details");
			} else {
				second_vds = JSON.parseObject(vds).getJSONObject("dataset");
			}
			return second_vds.getString("sql");
		} catch(Exception ex) {
			logger.error(ex.toString());
		}
		return null;
	}

//	//获取jobId
//	public String getJobId(String sql) {
//		String job_id = "";
//		String urlPath = this.daasServerAPIV3RootUrl + "/sql";
//		JSONObject jsonParam = new JSONObject();
//		jsonParam.put("sql", sql);
//		try {
//			job_id = OkHttpServletRequest.okHttpClientPost(urlPath, jsonParam.toString(), daasUserTokenService.getCurrentToken());
//		} catch (Exception e) {
//			logger.error("ProcessService.getJobId(sql):请求DAAS异常");
//		}
//		return JSON.parseObject(job_id).getString("id");
//	}
//
//	//获取job状态
//	public String getJobStatus(String job_id) {
//		String result = "";
//		String urlPath = this.daasServerAPIV3RootUrl + "/job/" + job_id;
//		try {
//			result = OkHttpServletRequest.okHttpClientGet(urlPath, daasUserTokenService.getCurrentToken());
//		} catch (Exception e) {
//			logger.error("ProcessService.getJobStatus(job_id):请求DAAS异常");
//		}
//		return result;
//	}
//
//	//获取结果集
//	public String getJobResults(String job_id) {
//		String results = "";
//		String urlPath = this.daasServerAPIV3RootUrl + "/job/" +job_id+ "/results?offset=0&limit=100";
//		System.out.println(urlPath);
//		try {
//			results = OkHttpServletRequest.okHttpClientGet(urlPath, daasUserTokenService.getCurrentToken());
//		} catch (Exception e) {
//			logger.error("ProcessService.getJobResults(job_id):请求DAAS异常");
//		}
//		return results;
//	}
//
	/**
	 * 查询dataset数据
	 * @param id
	 * @return
	 */
	public DataSetInstance getDataSetInfo(String id) {
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			DataSetInstanceDao dataSetInstanceDao = sqlSession.getMapper(DataSetInstanceDao.class);
			DataSetInstance dataSetInstance = dataSetInstanceDao.get(id);
			return dataSetInstance;
		}
	}


}
