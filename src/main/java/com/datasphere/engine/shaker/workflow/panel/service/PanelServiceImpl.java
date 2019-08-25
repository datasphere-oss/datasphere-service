package com.datasphere.engine.shaker.workflow.panel.service;

import java.util.*;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.datasphere.core.common.BaseService;
import com.datasphere.engine.common.exception.JIllegalOperationException;
import com.datasphere.engine.common.named.NameGenerator;
import com.datasphere.engine.core.utils.ExceptionConst;
import com.datasphere.engine.core.utils.JAssert;
import com.datasphere.engine.manager.resource.provider.mybatis.page.Pager;
import com.datasphere.engine.manager.resource.provider.service.ExchangeSSOService;
import com.datasphere.engine.shaker.processor.instance.constant.ComponentInstanceModified;
import com.datasphere.engine.shaker.processor.instance.constant.ComponentInstanceStatus;
import com.datasphere.engine.shaker.processor.instance.model.ComponentInstance;
import com.datasphere.engine.shaker.processor.instance.model.ComponentInstanceRelation;
import com.datasphere.engine.shaker.processor.instance.service.ComponentInstanceRelationService;
import com.datasphere.engine.shaker.processor.instance.service.ComponentInstanceService;
import com.datasphere.engine.shaker.processor.model.ProcessInstance;
import com.datasphere.engine.shaker.processor.model.ProcessRecord;
import com.datasphere.engine.shaker.workflow.panel.constant.PanelState;
import com.datasphere.engine.shaker.workflow.panel.dao.PanelDao;
import com.datasphere.engine.shaker.workflow.panel.model.Panel;
import com.datasphere.engine.shaker.workflow.panel.model.PanelWithAll;
import com.datasphere.server.manager.common.utils.UUIDUtils;
import com.datasphere.server.manager.module.login.redis.RedisServiceImpl;

import io.micronaut.core.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;

@Singleton
public class PanelServiceImpl extends BaseService {
	public static final String CustomPanelName = "默认项目";// 自定义面板默认名称

//	@Inject
//    ProjectServiceImpl projectService;
	@Inject
    ComponentInstanceService componentInstanceService;
    @Inject
    ComponentInstanceRelationService componentInstanceRelationService;
	@Inject
	NameGenerator nameGenerator;
//	@Inject
//	ProcessRecordService processRecordServiceImpl;
	@Inject
	ExchangeSSOService exchangeSSOService;
	@Inject
	RedisServiceImpl redisService;

	/**
	 * 创建面板（包含"自动保存"的自定义面板创建，命名为draft1,draft2,....）
	 */
	public Panel create(Panel panel, String token) {
		panel.setId(UUIDUtils.random());
		if (StringUtils.isBlank(panel.getPanelName())) {
			List<String> panelNames;
			try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
				PanelDao dao = sqlSession.getMapper(PanelDao.class);
				panelNames = dao.listNameByPanelId(panel.getId());
			}
			panel.setPanelName(nameGenerator.generate(CustomPanelName, panelNames));
//			panel.setPanelName("默认项目");
		}
//        String userId;
//		List<String> departmentIds;
//        userId = exchangeSSOService.getUserId(token); //根据token获取用户信息（userId）
//        if(userId == null) return "获取用户信息失败！";
//        //2.获取用户所在部门id接口（get请求）
//        departmentIds = exchangeSSOService.getCurDepAndSubDepIds(userId,token);
//        if(departmentIds == null) return "获取用户所在部门失败！";
//        String departmentId = null;
//        for (String dId:departmentIds) {
//        	dId = dId+",";
//			departmentId+=dId;
//		}
//		departmentId = departmentId.substring(0, departmentId.length()-1);
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			PanelDao dao = sqlSession.getMapper(PanelDao.class);
			panel.setCreator(exchangeSSOService.getAccount(token));
			panel.setCreateTime(new Date(System.currentTimeMillis()));
//            panel.setUserId(userId);
//            panel.setDepartmentId(departmentId);
//            panel.setDepartmentName(exchangeSSOService.getCurDepNameIdByUserId(userId,token));
			dao.insert(panel);
			sqlSession.commit();
			return panel;
//			return "Success";
		}
	}

	/**
	 * 根据id获得面板信息(加载过project) select * from app_panel where id = ?
	 * @param id
	 * @return
	 */
	public Panel getPanelById(String id) {
		Panel panel;
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			PanelDao dao = sqlSession.getMapper(PanelDao.class);
			panel = dao.getPanelById(id);
		}
		List<ComponentInstance> cis = componentInstanceService.getAllComponentInstancesWithPanel(id);
		List<ComponentInstanceRelation> cirs = componentInstanceRelationService.getComponentInstanceRelationsByPanelId(id);
		panel.setComponentInstanceCount(cis.size());
		panel.setComponentInstanceRelationCount(cirs.size());
		return panel;
	}

	public int update(Panel newPanel) {
		Panel panel;
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			PanelDao dao = sqlSession.getMapper(PanelDao.class);
			panel = dao.get(newPanel.getId());
			sqlSession.close();
		}
		// 新旧名称不同时，判断项目下是否已经有些名称的面板。
		if (panel.getPanelName().equals(newPanel.getPanelName())) {
			JAssert.isTrue(!verifyName(newPanel.getPanelName(), null, newPanel.getCreator()), ExceptionConst.NAME_REPEAT, "面板名称已存在！");
		}
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			PanelDao dao = sqlSession.getMapper(PanelDao.class);
			int flag = dao.update(newPanel);
			sqlSession.commit();
			return flag;
		}
	}

	public List<String>	getInstanceId(String panelId){
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			PanelDao dao = sqlSession.getMapper(PanelDao.class);
			List<String>  getInstanceId = dao.getInstanceId(panelId);
			return getInstanceId;
		}
	}

	public int deleteInstance(List<String> instanceIds){
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			PanelDao dao = sqlSession.getMapper(PanelDao.class);
			if(instanceIds.size()>0){
				int flag = 0;
				for(String instanceId : instanceIds){
					dao.deleteDataInstance(instanceId);
					sqlSession.commit();
					flag++;
				}
				return flag;
			}else{
				return 0;
			}
		}
	}

	public int deleteRelation(String panelId){
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			PanelDao dao = sqlSession.getMapper(PanelDao.class);
			int flag=dao.deleteRelation(panelId);
			sqlSession.commit();
			return flag;
		}
	}

//	public List<String> getDefinitionIds(String panelId){
//		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
//			PanelDao dao = sqlSession.getMapper(PanelDao.class);
//			List<String>  definitionIds = dao.getDefinitionIds(panelId);
//			return definitionIds;
//		}
//	}

	public int delete(String id) {
		PanelWithAll all = this.panelRunStatus(id);
		if (PanelState.RUNNING.equals(all.getStatus())) {
			return ExceptionConst.DELETE_PANEL_EXISTS_RUNNING_EXCEPTION;
		}
		List<String> instanceIds = getInstanceId(id);
		deleteInstance(instanceIds);
		deleteRelation(id);
//		List<String> panelIdList = new ArrayList<>();
//		panelIdList.add(id);
//		deleteByPanelIdList(panelIdList);
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			PanelDao dao = sqlSession.getMapper(PanelDao.class);
			int flag = dao.delete(id);
			sqlSession.commit();
			return flag;
		}
	}

	/**
	 * 检验名称是否已经存在
	 * @return
	 */
	public boolean verifyName(String panelName, String projectId, String userId) {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			PanelDao dao = sqlSession.getMapper(PanelDao.class);
			return dao.verifyName(panelName, projectId, userId);
		}
	}

	/**
	 * 查询最后一词访问的面板
	 * @param creator
	 * @return
	 */
	public Panel getLast(String creator) {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			PanelDao dao = sqlSession.getMapper(PanelDao.class);
			String id = dao.getLastAccessPanelId(creator);
			if (!StringUtils.isBlank(id)) return dao.get(id);
			return null;
		}
	}

	/**
	 * 模糊查询
	 * @param t
	 * @return
	 */
	public List<Panel> listBy(Panel t) {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			PanelDao dao = sqlSession.getMapper(PanelDao.class);
			return dao.listBy(t);
		}
	}

	/**
	 * 根据id查询project, 关联将panel查询出来，帶排序，加分页
	 * @param panel
	 * @return
	 */
	public List<Panel> getPanelsByProjectIdOrdeyPager(Panel panel) {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			PanelDao dao = sqlSession.getMapper(PanelDao.class);
			return dao.getPanelsByProjectIdOrdeyPager(panel.getProjectId(), panel.getCreator(), panel.getSortField(),
					panel.getOrderBy(), panel.getPager());
		}
	}

	/**
	 * 根据多个项目id查找每个项目下的面板列表，分页，排序功能需要实现。返回包含所以项目（每个项目id + 对应的面板信息）
	 * @param panel
	 * @return
	 */
	public Map<String, Pager<Panel>> getByProjectIdList(Panel panel) {
		Pager<Panel> page = panel.getPager();
		List<String> projectIds = Arrays.asList(panel.getProjectId().split(","));
		Map<String, Pager<Panel>> map = new HashMap<>();
		for (String id : projectIds) {
			List<Panel> list = new ArrayList<>();
			if (page == null) {
				Pager<Panel> pager = new Pager<>();
				try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
					PanelDao dao = sqlSession.getMapper(PanelDao.class);
					list = dao.getPanelsByProjectIdOrdey(id, panel.getCreator(), panel.getSortField(),
							panel.getOrderBy());
					pager.setList(list);
					pager.setTotalRecords(list.size());
					map.put(id, pager);
				}
			} else {
				Pager<Panel> pager = new Pager<>();
				pager.setPageSize(page.getPageSize());
				pager.setPageNumber(page.getPageNumber());
				try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
					PanelDao dao = sqlSession.getMapper(PanelDao.class);
					list = dao.getPanelsByProjectIdOrdeyPager(id, panel.getCreator(), panel.getSortField(),
							panel.getOrderBy(), pager);
					pager.setList(list);
					map.put(id, pager);
				}
			}

		}
		return map;
	}

	/**
	 * dmp：获取面板详细信息。情况一：有项目和面板，正常处理；情况二：有项目没有面板，返回null，提示没有面板；情况三：没有项目没有面板，创建默认项目、
	 * reource.manager：情况一：有项目面板，返回null，提示没有面板；情况二：没有项目面板，创建默认项目面板，返回空白面板信息
	 * @param projectId
	 * @param panelId
	 * @return
	 */
	public PanelWithAll panelDetail(String projectId, String panelId, String token) {
		JAssert.isTrue(panelId != null, "项目面板ID不能为空!");
//		id = getPanelById(panelId).getId();
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			PanelDao dao = sqlSession.getMapper(PanelDao.class);
			String username = exchangeSSOService.getAccount(token);
			// 如果是超级管理员，则可以操作
			boolean root = false;
			try {
				Object obj = redisService.get(token);
				if (obj != null) {
					String allInfo = obj.toString();
					JSONObject allInfoJson = JSON.parseObject(allInfo);
					JSONArray roles = allInfoJson.getJSONArray("roles");
// {"roles":[{"level":"ROOT","name":"系统超级管理员","remark":"系统默认角色,拥有系统访问的最大权限","id":"superAdmin","type":"DEFAULT","roleGroupId":"defaultRoleGroup"}]
					for(Object role:roles) {
						JSONObject role2 = JSON.parseObject(role.toString());
						if("superAdmin".equals(role2.getString("id"))) {
							root = true;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (!root) JAssert.isTrue(dao.existsByCreator(panelId, username), "面板不存在！ID="+panelId+"，用户："+username);
		}
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			PanelDao dao = sqlSession.getMapper(PanelDao.class);
			dao.updateLastAccess(panelId);
		}
		PanelWithAll panelWithAll = this.panelRunStatus(panelId);
		panelWithAll = this.getComponentInstanceAndWrap(panelWithAll);
		return panelWithAll;
	}


	/** new0
	 * 获取面板详细信息
	 * @return
	 */
	public Map<String, Object> panelPageDetail(String projectId, String panelId, String token) {
		String userId = exchangeSSOService.getAccount(token);
		Map<String, Object> panelList = null;
		//项目不存在，面板不存在
//		if(StringUtils.isBlank(projectId) && StringUtils.isBlank(panelId)) {
		if(StringUtils.isBlank(panelId)) {
			//取该用户上一次访问面板详细信息
			Map<String, String> lastPanel = getLastAccessPanel(userId);
			if(lastPanel == null) {	// 如果还没创建面板，在“默认项目”下创建一个面板
				//该用户上一次访问面板详细信息不存在，则创建默认面板
//				panelId = getDefaultPanel().getId();
				Panel panel = new Panel();
				panel = create(panel, token);
				JAssert.isTrue(panel != null,"创建默认面板-面板不能为空！");
				panelId = panel.getId();
			} else {
				//该用户上一次访问面板详细信息存在，则获取上次的面板流程详细信息
				panelId = lastPanel.get("id");
//				projectId = lastPanel.get("projectId");
			}
		}
//		else if(!StringUtils.isBlank(projectId) && StringUtils.isBlank(panelId)){
//			//该项目下没有面板时，提示没有面板
//			return null;
//		}

		//获取面板及流程详细信息，并将该用户下的全部项目信息放入结果中。
		getPanelWithAll(projectId, panelId, userId, token);

		return panelList;
	}

	//默认项目面板
	public Panel getDefaultPanel(String token) {
		Panel panel = new Panel();
		panel.setCreator(exchangeSSOService.getAccount(token));
		panel.setProjectName("默认项目");
		List<Panel> panelList = null;
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			PanelDao dao = sqlSession.getMapper(PanelDao.class);
			panelList = dao.listBy(panel);
		}
		if (panelList.size() > 0) {
			return panelList.get(0);
		} else {
			panel.setProjectDesc("该项目存储自定义面板!");
			try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
				PanelDao dao = sqlSession.getMapper(PanelDao.class);
				dao.insert(panel);
				sqlSession.commit();
			}
			return panel;
		}
	}

	/**
	 * 另存为
	 * @return
	 */
	public Map<String, String> saveAs(String id, String projectId, String panelName, String panelDesc, String token) {
//		String creator = exchangeSSOService.getAccount(token);//UserContextHolder.getUserContext().getOmSysUser().getUserId();
		Map<String, String> map = new HashMap<>();
		Panel panel = new Panel();
		panel.setId(id);
		panel.setProjectId(projectId);
		panel.setPanelName(panelName);
		panel.setCreator(exchangeSSOService.getAccount(token));
		if(!StringUtils.isBlank(panelDesc)) {
			panel.setProjectDesc(panelDesc);
		}
//			List panelList = (List)WebsiteTerminalUtils.sendRequest(terminal,HttpMethod.POST, "/panel/saveAsCyl", params);
		if (!StringUtils.isBlank(panel.getPanelName())) {
			if (!verifyName(panel.getPanelName(), panel.getProjectId(), panel.getCreator())) {
				String id2 = saveAs(panel).getId();
				Panel panel2 = getPanelById(id2);
				List<Panel> list = new ArrayList<>();
				list.add(panel2);
//					return JsonWrapper.successWrapper(list);
				return (Map<String, String>) list.get(0);
			} else {
				map.put("state", ExceptionConst.NAME_REPEAT.toString());
				map.put("data", ExceptionConst.get(ExceptionConst.NAME_REPEAT).toString());
//					return JsonWrapper.failureWrapper(ExceptionConst.NAME_REPEAT,ExceptionConst.get(ExceptionConst.NAME_REPEAT));
				return map;
			}
		} else {
			map.put("state", ExceptionConst.NAMEORID_IS_NULL.toString());
			map.put("data", ExceptionConst.get(ExceptionConst.NAMEORID_IS_NULL).toString());
//				return JsonWrapper.failureWrapper(ExceptionConst.NAMEORID_IS_NULL, ExceptionConst.get(ExceptionConst.NAMEORID_IS_NULL));
			return map;
		}
	}

	/**
	 * 另存为
	 * @param newPanel
	 * @return
	 */
	public Panel saveAs(Panel newPanel) {
		String oldPanelId = newPanel.getId();
		Panel oldPanel = null;
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			PanelDao dao = sqlSession.getMapper(PanelDao.class);
			oldPanel = dao.get(oldPanelId);
		}
		JAssert.isTrue(oldPanel != null, "面板不存在！ID=" + oldPanelId);
//		List<String> panelNames = null;
		if (StringUtils.isBlank(newPanel.getPanelName())) {
//			panelNames = dao.listNamesByProjectId(newPanel.getProjectId());
			newPanel.setPanelName(newPanel.getPanelName());
		} else {
			if (!StringUtils.isBlank(newPanel.getPanelName()) && verifyName(newPanel.getPanelName(), newPanel.getProjectId(), newPanel.getCreator())) {
				throw new JIllegalOperationException("面板重命名！");
			}
		}
		if (StringUtils.isBlank(newPanel.getPanelDesc())) {
			newPanel.setPanelDesc(oldPanel.getPanelDesc());
		}
		newPanel.setId(UUIDUtils.random());
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			PanelDao dao = sqlSession.getMapper(PanelDao.class);
			dao.insert(newPanel);
			sqlSession.commit();
		}
		String newPanelId = newPanel.getId();

		Map<String, String> cisTocis = componentInstanceService.copy(oldPanelId, newPanelId, newPanel.getCreator());
        componentInstanceRelationService.copy(oldPanelId, newPanelId, cisTocis, newPanel.getCreator());
		return newPanel;
	}


	public PanelWithAll getPanelWithAll(String projectId, String panelId, String userId, String token) {
		PanelWithAll panelWithAll = this.panelDetail(projectId, panelId, token);
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			PanelDao dao = sqlSession.getMapper(PanelDao.class);
			panelWithAll.setPanels(dao.getPanelsByProjectId(panelId, userId));
		}
		return panelWithAll;
	}

	/**
	 * 溯源，查询数据源在哪些面板中引用了
	 * @param id
	 * @param userId
	 * @return
	 */
	public List<Panel> sourceTrace(String id, String userId) {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			PanelDao dao = sqlSession.getMapper(PanelDao.class);
			return dao.getSourceTrace(id, userId);
		}
	}

	/**
	 * 多个数据源中每个数据源被多少个面板引用，以及是那些面板在引用，返回面板数量和面板信息（包括面板id，面板名称 和 项目id）。
	 * @param ids
	 * @param userId
	 * @return
	 */
	public Map<String, List<Panel>> someSourceTrace(String ids, String userId) {
		Map<String, List<Panel>> data = new HashMap<>();
		if (!StringUtils.isBlank(ids)) {
			String[] idArr = ids.split(",");
			for (int i = 0; i < idArr.length; i++) {
				String id = idArr[i];
				try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
					PanelDao dao = sqlSession.getMapper(PanelDao.class);
					data.put(id, dao.getSourceTrace(id, userId));
				}
			}
		}
		return data;
	}

	/**
	 * 获取该用户上一次访问面板详细信息
	 * @return
	 */
	public Map<String, String> getLastAccessPanel(String creator) {
		String id = null;
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			PanelDao dao = sqlSession.getMapper(PanelDao.class);
			id = dao.getLastAccessPanelId(creator);
		}
		if (!StringUtils.isBlank(id)) {
			try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
				PanelDao dao = sqlSession.getMapper(PanelDao.class);
				return (Map<String, String>)dao.get(id);
			}
		}
		return null;
	}

	/**
	 * 删除项目下的面板
	 * @param projectId
	 */
	public Integer deleteById(String projectId) {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			PanelDao dao = sqlSession.getMapper(PanelDao.class);
			// 查询项目下所有面板的ID
			List<String> panelIdList = dao.listIdByProjectId(projectId);
			if (!CollectionUtils.isEmpty(panelIdList)) {
				for (String panelId : panelIdList) {
					Panel temp = dao.get(panelId);
					if (null == temp) continue;
					PanelWithAll all = this.panelRunStatus(panelId);
					if (all.getStatus().equals(PanelState.RUNNING)) {
						return ExceptionConst.DELETE_PANEL_EXISTS_RUNNING_EXCEPTION;
					}
				}
				this.deleteByPanelIdList(panelIdList);
				dao.deleteById(projectId);
			}
			return ExceptionConst.Success;
		}
	}

	protected void deleteByPanelIdList(List<String> panelIdList) {
//		if (panelIdList.size() != 0) {
			// 查询项目下所有面板的所有组件实例ID
//			List<String> componentInstanceIdList = componentInstanceService.listIdByPanelIdList(panelIdList);
			// 删除所有面板下的组件实例
//			componentInstanceService.deleteByPanelIdList(panelIdList);

//jeq
			// 删除所有面板下的关联线
//			componentInstanceRelationService.deleteByPanelIdList(panelIdList);

			// 根据面板ID删除所有面板的运行实例、运行记录
//			processInstanceService.deleteByPanelIdList(panelIdList);
//			processRecordService.deleteByPanelIdList(panelIdList);

//			if (componentInstanceIdList.size() != 0) {
				// to do
//			}
//		}
	}

	/**
	 * 获取面板运行状态
	 * @param panelId
	 * @return
	 */
	public PanelWithAll panelRunStatus(String panelId) {
		ProcessInstance pi = null;
		PanelWithAll panelWithAll = null;
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			PanelDao dao = sqlSession.getMapper(PanelDao.class);
			Panel temp = dao.get(panelId);// 获得面板基本信息
			JAssert.isTrue(temp != null, "面板不存在！ID=" + panelId);
			// 包装面板以获得容纳其他信息的能力
			panelWithAll = PanelWithAll.wrap(temp);
			pi = dao.getLastByPanelId(panelId);
		}
		if (pi != null) {
			panelWithAll.setStatus(pi.getStatus());
			// 比对Panel的lastProcessInstanceId与最近一次的执行记录ID
//			if (panelWithAll.getLastProcessInstanceId() == null || !panelWithAll.getLastProcessInstanceId().equals(pi.getId())) {
				try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
					PanelDao dao = sqlSession.getMapper(PanelDao.class);
					dao.updateLastProcessInstanceId(panelWithAll.getId(), pi.getId());
					sqlSession.commit();
				}
				try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
					PanelDao dao = sqlSession.getMapper(PanelDao.class);
					dao.updateStatusByPanelId(panelWithAll.getId(), ComponentInstanceModified.RAW);
					sqlSession.commit();
				}
//			}
		} else {
			panelWithAll.setStatus(PanelState.PREPARED);// 还未执行过，就设置成“未开始”
		}
		return panelWithAll;
	}

	/**
	 * 封装面板中的组件
	 * @param panelWithAll
	 * @return
	 */
	public PanelWithAll getComponentInstanceAndWrap(PanelWithAll panelWithAll) {
		String id = panelWithAll.getId();
		// 给当前面板组件实例拼接最后一次运行信息
		// 更改组件实例状态#ComponentInstanceModified.MODIFIED变成#ComponentInstanceStatus.PREPARED
		// 更改组件实例状态#ComponentInstanceModified.RAW变成对应#{ProcessRecord}的status
		// 如果最后一次运行记录不存在，那么变成#ComponentInstanceStatus.PREPARED
		List<ComponentInstance> cis = componentInstanceService.getAllComponentInstancesWithPanel(id);
//		List<ProcessRecord> records = processRecordServiceImpl.getPanelComponentInstancesLatestRecord(id);
		List<ProcessRecord> records = new ArrayList<>();
		ProcessRecord pr0 = new ProcessRecord();
		pr0.setId("1");
		pr0.setPanelId(id);
		pr0.setComponentInstanceId("1");
		pr0.setUserId("2c67b97c597f4ee89e374a9354a867eb");
		pr0.setBeginTime(new Date(System.currentTimeMillis()));
		pr0.setEndTime(new Date(System.currentTimeMillis()+5000));
		records.add(pr0);
		Map<String, ProcessRecord> map = index(records);
		for (ComponentInstance ci : cis) {
			ProcessRecord pr = map.get(ci.getId());
			if (pr == null || ci.getStatus().equals(ComponentInstanceModified.MODIFIED)) {
				ci.setStatus(ComponentInstanceStatus.PREPARED);
			} else {
				ci.setStatus(pr.getStatus());
			}
			if (pr == null) {
//				pr = processRecordServiceImpl.getLatestRecord(ci.getId());
				pr = new ProcessRecord();
				pr.setId("1");
				pr.setPanelId(id);
				pr.setComponentInstanceId("1");
				pr.setUserId("2c67b97c597f4ee89e374a9354a867eb");
				pr.setBeginTime(new Date(System.currentTimeMillis()));
				pr.setEndTime(new Date(System.currentTimeMillis()+5000));
			}
			ci.setProcessRecord(pr);
		}

		// 组装当前面板组件实例列表
		panelWithAll.setComponentInstances(cis);
		// 组装当前面板组件关联列表
		panelWithAll.setComponentInstanceRelations(componentInstanceRelationService.getComponentInstanceRelationsByPanelId(panelWithAll.getId()));
		return panelWithAll;
	}

	public Map<String, ProcessRecord> index(List<ProcessRecord> records) {
		HashMap<String, ProcessRecord> map = new HashMap<>();
		for (ProcessRecord pr : records) {
			map.put(pr.getComponentInstanceId(), pr);
		}
		return map;
	}

	/**
	 * 查询项目列表  以创建时间排序
	 */
//	public List<Panel> getAllPanelList(Panel panel) {
//		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
//			PanelDao dao = sqlSession.getMapper(PanelDao.class);
//			List<Panel> panels = dao.listAll(panel.getCreator());
//            return panels;
//		}
//	}
    public List<Panel> getAllPanelList(Panel panel,String token) {
        String userId = exchangeSSOService.getUserId(token);//根据token获取用户信息(userId)
        if(userId == null) return null;
        //根据userId获取用户部门id
        List<String> departmentIds = exchangeSSOService.getCurDepAndSubDepIds(userId,token);
        if(departmentIds == null) return null;
        try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
            PanelDao dao = sqlSession.getMapper(PanelDao.class);
            Map<String,Object> params = new HashMap<>();
            params.put("panelName",panel.getPanelName());
            params.put("departmentIds",departmentIds);
            List<Panel> panels = dao.listAll(params);
            return panels;
        }
    }

	public String getComponentDetails(String id) {
		StringBuilder sb = new StringBuilder();
		List<ComponentInstance> cis = componentInstanceService.getAllComponentInstancesWithPanel(id);
		List<ComponentInstanceRelation> cirs = componentInstanceRelationService.getComponentInstanceRelationsByPanelId(id);
		return sb.append("面板中有"+cis.size()+"个组件实例，有"+cirs.size()+"个连线").toString();
	}

	/**4
	 * 根据项目id查找该项目下的面板列表
	 */
//	public Project getPanelById(String id) {
////		String userId = UserContextHolder.getUserContext().getOmSysUser().getUserId();
//		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
//			PanelDao dao = sqlSession.getMapper(PanelDao.class);
//			Project Panel = dao.getPanelById(id);
////			if(project != null) {
////				List panelList = getByProjectId(id,null);
////				if(panelList != null) {
////					project.setPanelList(panelList);
////				}
////			}
//			return Panel;// 获取面板信息，放入项目中
//		}
//	}

}
