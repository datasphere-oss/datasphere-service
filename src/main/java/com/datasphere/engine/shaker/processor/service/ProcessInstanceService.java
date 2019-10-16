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

import com.datasphere.engine.core.common.BaseService;
import com.datasphere.engine.shaker.processor.dao.ProcessInstanceDao;
import com.datasphere.engine.shaker.processor.model.ProcessInstance;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProcessInstanceService extends BaseService {
	
	public void add(ProcessInstance processInstance){
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ProcessInstanceDao processInstanceDao = sqlSession.getMapper(ProcessInstanceDao.class);
			processInstanceDao.add(processInstance);
			sqlSession.commit();
		}
	}
	
	public void modify(ProcessInstance processInstance){
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ProcessInstanceDao processInstanceDao = sqlSession.getMapper(ProcessInstanceDao.class);
			processInstanceDao.modify(processInstance);
			sqlSession.commit();
		}
	}

	public ProcessInstance getLastByPanelId(String panelId) {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ProcessInstanceDao processInstanceDao = sqlSession.getMapper(ProcessInstanceDao.class);
			return processInstanceDao.getLastByPanelId(panelId);
		}
	}

	public int deleteByPanelIdList(List<String> panelIdList) {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ProcessInstanceDao processInstanceDao = sqlSession.getMapper(ProcessInstanceDao.class);
			return processInstanceDao.deleteByPanelIdList(panelIdList);
		}
	}

}
