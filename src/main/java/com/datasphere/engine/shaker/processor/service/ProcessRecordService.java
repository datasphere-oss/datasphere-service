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

package com.datasphere.engine.shaker.processor.service;

import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.datasphere.core.common.BaseService;
import com.datasphere.core.common.utils.UUIDUtils;
import com.datasphere.engine.core.utils.JAssert;
import com.datasphere.engine.shaker.processor.dao.ProcessRecordDao;
import com.datasphere.engine.shaker.processor.model.ProcessInstance;
import com.datasphere.engine.shaker.processor.model.ProcessRecord;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessRecordService extends BaseService {

	@Autowired
	ProcessInstanceService processInstanceService;
	
	public void add(ProcessRecord processRecord){
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()){
			ProcessRecordDao processRecordDao = sqlSession.getMapper(ProcessRecordDao.class);
			processRecordDao.add(processRecord);
		}
	}
	
	public ProcessRecord getById(String id){
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ProcessRecordDao processRecordDao = sqlSession.getMapper(ProcessRecordDao.class);
			return processRecordDao.get(id);
		}
	}

	public ProcessRecord create_back(String processId,String panelId,String componentInstanceId,String userId){
		ProcessRecord record=new ProcessRecord();
		record.setId(UUIDUtils.random());
		record.setProcessId(processId);
		record.setPanelId(panelId);
		record.setComponentInstanceId(componentInstanceId);
		record.setUserId(userId);
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ProcessRecordDao processRecordDao = sqlSession.getMapper(ProcessRecordDao.class);
			processRecordDao.add(record);
			sqlSession.commit();
		}
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ProcessRecordDao processRecordDao = sqlSession.getMapper(ProcessRecordDao.class);
			return processRecordDao.get(record.getId());
		}
	}

	/**
	 * 添加 工作流运行日志记录
	 * @param processId
	 * @param panelId
	 * @param componentInstanceId
	 * @param userId
	 * @param msg
	 * @return
	 */
	public ProcessRecord create(String processId,String panelId,String componentInstanceId,String userId,JSONObject msg){
		ProcessRecord record = new ProcessRecord();
		record.setId(UUIDUtils.random());
		record.setBeginTime(new Date());
		record.setProcessId(processId);
		record.setPanelId(panelId);
		record.setComponentInstanceId(componentInstanceId);
		record.setUserId(userId);
		if (msg.getString("logsType").equals("1")) {
			record.setInfoMsg(msg.toJSONString());
		} else if (msg.getString("logsType").equals("2")){
			record.setErrorMsg(msg.toJSONString());
		} else {
			record.setWarnMsg(msg.toJSONString());
		}
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ProcessRecordDao processRecordDao = sqlSession.getMapper(ProcessRecordDao.class);
			processRecordDao.add(record);
			sqlSession.commit();
		}
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ProcessRecordDao processRecordDao = sqlSession.getMapper(ProcessRecordDao.class);
			return processRecordDao.get(record.getId());
		}
	}
	
	public void modify(ProcessRecord processRecord) {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ProcessRecordDao processRecordDao = sqlSession.getMapper(ProcessRecordDao.class);
			processRecordDao.modify(processRecord);
			sqlSession.commit();
		}
	}

	public List<ProcessRecord> getPanelComponentInstancesLatestRecord(String panelId) {
		ProcessInstance processInstance = processInstanceService.getLastByPanelId(panelId);
		if(processInstance == null) {// 未执行过，返回空集合
			return Collections.emptyList();
		}
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ProcessRecordDao processRecordDao = sqlSession.getMapper(ProcessRecordDao.class);
			return processRecordDao.listByProcessInstanceId(processInstance.getId());
		}
	}

	/**0
	 * 获取组件实例的log信息
	 * @param componentInstanceId
	 * @return
	 */
	public JSONObject getLatestRecord(String componentInstanceId) {
		JSONArray allArr = new JSONArray();
		JSONArray warnArr = new JSONArray();
		JSONArray errorArr = new JSONArray();
        JSONArray infoArr = new JSONArray();
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ProcessRecordDao processRecordDao = sqlSession.getMapper(ProcessRecordDao.class);
			List<ProcessRecord> prs = processRecordDao.getLatestRecord(componentInstanceId);
			for (ProcessRecord pr:prs) {
				if (!StringUtils.isBlank(pr.getErrorMsg())) {
					allArr.add(JSON.parse(pr.getErrorMsg()));
					errorArr.add(JSON.parse(pr.getErrorMsg()));
				}
				if (!StringUtils.isBlank(pr.getWarnMsg())) {
					allArr.add(JSON.parse(pr.getWarnMsg()));
					warnArr.add(JSON.parse(pr.getWarnMsg()));
				}
				if (!StringUtils.isBlank(pr.getInfoMsg())) {
					allArr.add(JSON.parse(pr.getInfoMsg()));
					infoArr.add(JSON.parse(pr.getInfoMsg()));
				}
			}
			JSONObject result = new JSONObject();
			result.put("All", allArr);
			result.put("Warning", warnArr);
			result.put("Error", errorArr);
			result.put("Info", infoArr);
			return result;
		}
	}

	public int deleteByPanelIdList(List<String> panelIdList) {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ProcessRecordDao processRecordDao = sqlSession.getMapper(ProcessRecordDao.class);
			return processRecordDao.deleteByPanelIdList(panelIdList);
		}
	}

	public void updateRecord(String id, String status, String message) {
		ProcessRecord record = null;
		// TODO Auto-generated method stub
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ProcessRecordDao processRecordDao = sqlSession.getMapper(ProcessRecordDao.class);
			record = processRecordDao.get(id);
		}
		JAssert.isTrue(record!=null, "运行记录不存在！");
		record.setStatus(status);
		record.setEndTime(new Date());
		record.setErrorMsg(message);
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ProcessRecordDao processRecordDao = sqlSession.getMapper(ProcessRecordDao.class);
			processRecordDao.modify(record);
			sqlSession.commit();
		}
	}
}
