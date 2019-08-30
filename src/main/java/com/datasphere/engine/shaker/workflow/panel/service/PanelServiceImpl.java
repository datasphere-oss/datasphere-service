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

package com.datasphere.engine.shaker.workflow.panel.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.datasphere.core.common.BaseService;
import com.datasphere.core.common.utils.UUIDUtils;
import com.datasphere.engine.common.exception.JIllegalOperationException;
import com.datasphere.engine.common.named.NameGenerator;
import com.datasphere.engine.core.utils.ExceptionConst;
import com.datasphere.engine.core.utils.JAssert;
import com.datasphere.engine.manager.resource.provider.mybatis.page.Pager;
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

@Service
public class PanelServiceImpl extends BaseService {
	public static final String CustomPanelName = "Default Project";// Custom panel default name

//	@@Autowired
//    ProjectServiceImpl projectService;
	@Autowired
    ComponentInstanceService componentInstanceService;
	@Autowired
    ComponentInstanceRelationService componentInstanceRelationService;
	@Autowired
	NameGenerator nameGenerator;
//	@@Autowired
//	ProcessRecordService processRecordServiceImpl;
//	@Autowired
//	RedisServiceImpl redisService;

	/**
	 * Create a panel (created with a custom panel that contains "Auto Save", named draft1, draft2,....)
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
		}

//		departmentId = departmentId.substring(0, departmentId.length()-1);
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			PanelDao dao = sqlSession.getMapper(PanelDao.class);
//			panel.setCreator(exchangeSSOService.getAccount(token));
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
	 * Get panel information based on id (loaded project) select * from app_panel where id = ?
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
		// When the old and new names are different, it is judged whether there is a panel with some names under the project.
		if (panel.getPanelName().equals(newPanel.getPanelName())) {
			JAssert.isTrue(!verifyName(newPanel.getPanelName(), null, newPanel.getCreator()), ExceptionConst.NAME_REPEAT, "The panel name already exists!");
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
	 * Verify that the name already exists
	 * @return
	 */
	public boolean verifyName(String panelName, String projectId, String userId) {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			PanelDao dao = sqlSession.getMapper(PanelDao.class);
			return dao.verifyName(panelName, projectId, userId);
		}
	}

	/**
	 * Query the panel accessed by the last word
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
	 * Fuzzy query
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
	 * Query the project according to the id, the association will query the panel, with sorting, adding pages
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
	 * Find the list of panels under each project based on multiple project ids. The paging and sorting functions need to be implemented. Returns the items that contain the items (each panel id + corresponding panel information)
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
	 * Get the panel details. Case 1: There are projects and panels, normal processing; Case 2: There are no panels in the project, return null, prompt no panel; Case 3: no project has no panel, create default project,
	 * Reource.manager: Case 1: There is a project panel, returning null, prompting no panel; Case 2: No project panel, creating default project panel, returning blank panel information
	 * @param projectId
	 * @param panelId
	 * @return
	 */
	public PanelWithAll panelDetail(String projectId, String panelId, String token) {
		JAssert.isTrue(panelId != null, "Project panel ID cannot be empty!");
//		id = getPanelById(panelId).getId();
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			PanelDao dao = sqlSession.getMapper(PanelDao.class);
			String username = exchangeSSOService.getAccount(token);
			boolean root = false;
			try {
				Object obj = redisService.get(token);
				if (obj != null) {
					String allInfo = obj.toString();
					JSONObject allInfoJson = JSON.parseObject(allInfo);
					JSONArray roles = allInfoJson.getJSONArray("roles");
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
			if (!root) JAssert.isTrue(dao.existsByCreator(panelId, username), "The panel does not exist!ID="+panelId+"，user："+username);
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
		if(StringUtils.isBlank(panelId)) {
			Map<String, String> lastPanel = getLastAccessPanel(userId);
			if(lastPanel == null) {
				Panel panel = new Panel();
				panel = create(panel, token);
				JAssert.isTrue(panel != null,"Create default panel - panel can't be empty!");
				panelId = panel.getId();
			} else {
				panelId = lastPanel.get("id");
			}
		}

		//Get the panel and process details and put all the project information under the user into the results.
		getPanelWithAll(projectId, panelId, userId, token);

		return panelList;
	}

	//Default project panel
	public Panel getDefaultPanel(String token) {
		Panel panel = new Panel();
		panel.setCreator(exchangeSSOService.getAccount(token));
		panel.setProjectName("Default project");
		List<Panel> panelList = null;
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			PanelDao dao = sqlSession.getMapper(PanelDao.class);
			panelList = dao.listBy(panel);
		}
		if (panelList.size() > 0) {
			return panelList.get(0);
		} else {
			panel.setProjectDesc("This project stores custom panels!");
			try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
				PanelDao dao = sqlSession.getMapper(PanelDao.class);
				dao.insert(panel);
				sqlSession.commit();
			}
			return panel;
		}
	}

	/**
	 * Save as
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
	 * save as
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
		JAssert.isTrue(oldPanel != null, "The panel does not exist!ID=" + oldPanelId);
//		List<String> panelNames = null;
		if (StringUtils.isBlank(newPanel.getPanelName())) {
//			panelNames = dao.listNamesByProjectId(newPanel.getProjectId());
			newPanel.setPanelName(newPanel.getPanelName());
		} else {
			if (!StringUtils.isBlank(newPanel.getPanelName()) && verifyName(newPanel.getPanelName(), newPanel.getProjectId(), newPanel.getCreator())) {
				throw new JIllegalOperationException("Panel rename!");
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
	 * Traceability, in which panels the query data source is referenced
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
	 * How many panels are referenced by each of the multiple data sources, and those panels are referenced, returning the number of panels and panel information (including panel id, panel name, and project id).
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
	 * Get the last time the user accessed the panel details
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
	 * Delete the panel contained in the project
	 * @param projectId
	 */
	public Integer deleteById(String projectId) {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			PanelDao dao = sqlSession.getMapper(PanelDao.class);
			
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

	}

	/**
	 * Get the panel running status
	 * @param panelId
	 * @return
	 */
	public PanelWithAll panelRunStatus(String panelId) {
		ProcessInstance pi = null;
		PanelWithAll panelWithAll = null;
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			PanelDao dao = sqlSession.getMapper(PanelDao.class);
			Panel temp = dao.get(panelId);
			JAssert.isTrue(temp != null, "The panel does not exist!ID=" + panelId);
			panelWithAll = PanelWithAll.wrap(temp);
			pi = dao.getLastByPanelId(panelId);
		}
		if (pi != null) {
			panelWithAll.setStatus(pi.getStatus());
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
			panelWithAll.setStatus(PanelState.PREPARED);// Has not been executed, it is set to "not started"
		}
		return panelWithAll;
	}

	/**
	 * Components in the package panel
	 * @param panelWithAll
	 * @return
	 */
	public PanelWithAll getComponentInstanceAndWrap(PanelWithAll panelWithAll) {
		String id = panelWithAll.getId();
		// splicing the last run information for the current panel component instance
		// Change the component instance state #ComponentInstanceModified.MODIFIED to #ComponentInstanceStatus.PREPARED
		// Change the component instance state #ComponentInstanceModified.RAW to become the status corresponding to #{ProcessRecord}
		// If the last run record does not exist, then become #ComponentInstanceStatus.PREPARED
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

		// Assemble the current panel component instance list
		panelWithAll.setComponentInstances(cis);
		// Assemble the current panel component association list
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

    public List<Panel> getAllPanelList(Panel panel,String token) {
        String userId = exchangeSSOService.getUserId(token);
        if(userId == null) return null;
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
		return sb.append("There are "+cis.size()+" component instances in the panel, with "+cirs.size()+" connections").toString();
	}

}
