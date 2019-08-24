package com.datasphere.engine.shaker.processor.service;

import com.datasphere.core.common.BaseService;
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
