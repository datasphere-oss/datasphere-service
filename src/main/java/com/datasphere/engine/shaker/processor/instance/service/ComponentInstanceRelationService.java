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

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datasphere.core.common.BaseService;
import com.datasphere.core.common.utils.UUIDUtils;
import com.datasphere.engine.core.utils.JAssert;
import com.datasphere.engine.shaker.processor.common.constant.ComponentClassification;
import com.datasphere.engine.shaker.processor.instance.dao.ComponentInstanceDao;
import com.datasphere.engine.shaker.processor.instance.dao.ComponentInstanceRelationDao;
import com.datasphere.engine.shaker.processor.instance.model.ComponentInstance;
import com.datasphere.engine.shaker.processor.instance.model.ComponentInstanceRelation;

/**
 * 组件实例关系服务接口
 */
@Service
public class ComponentInstanceRelationService extends BaseService {
	private final static Log logger = LogFactory.getLog(ComponentInstanceService.class);
//	@Autowired
//	ExchangeSSOService exchangeSSOService;

	public String insert(ComponentInstanceRelation cir, String userId) {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceDao componentInstanceDao = sqlSession.getMapper(ComponentInstanceDao.class);
			ComponentInstanceRelationDao dao = sqlSession.getMapper(ComponentInstanceRelationDao.class);
			// 判断线两端的组件实例都存在且是自己的，判断关联的组件实例在同一面板
			ComponentInstance source = componentInstanceDao.get(cir.getSourceComponentInstanceId());
			ComponentInstance dest = componentInstanceDao.get(cir.getDestComponentInstanceId());
			// 判断线两端的点都是存在的
			JAssert.isTrue(source != null && dest != null, "源组件实例或者目的组件实例不存在！");
			JAssert.isTrue(userId.equals(source.getCreator()) && userId.equals(dest.getCreator()), "不能关联他人的组件实例！");
			JAssert.isTrue(source.getPanelId().equals(dest.getPanelId()), "只能给同一面板的组件实例进行关联！");
			ComponentClassification sourceType = source.getComponentType();
			ComponentClassification destType = dest.getComponentType();
			if(sourceType.equals(ComponentClassification.Tag) && !destType.equals(ComponentClassification.SimpleDataSource)) {
				return "暂不支持该类型处理组件["+sourceType+"]和["+destType+"]的关联操作！";
			}
			cir.setId(UUIDUtils.random());
			cir.setCreator(userId);// 保存创建者
			cir.setPanelId(source.getPanelId());// 保存Panel的ID
			dao.insert(cir);
			sqlSession.commit();
		}
		return "success";
	}

	public int update(ComponentInstanceRelation t,String token) {
//		String userId = exchangeSSOService.getAccount(token);
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceDao componentInstanceDao = sqlSession.getMapper(ComponentInstanceDao.class);
			ComponentInstanceRelationDao baseDao = sqlSession.getMapper(ComponentInstanceRelationDao.class);
			// 判断线两端的组件实例都存在且是自己的，判断关联的组件实例在同一面板
			ComponentInstance source = componentInstanceDao.get(t.getSourceComponentInstanceId());
			ComponentInstance dest = componentInstanceDao.get(t.getDestComponentInstanceId());
			JAssert.isTrue(source != null && dest != null, "源组件实例或者目的组件实例不存在！");
//			JAssert.isTrue(userId.equals(source.getCreator()) && userId.equals(dest.getCreator()), "不能关联他人的组件实例！");
			JAssert.isTrue(source.getPanelId().equals(dest.getPanelId()), "只能给同一面板的组件实例进行关联！");
			// 判断线两端的点都是存在的
			int flag = baseDao.update(t);
			sqlSession.commit();
			return flag;
		}
	}

	public int delete(String id,String token) {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceRelationDao baseDao = sqlSession.getMapper(ComponentInstanceRelationDao.class);
			// 判断线的创建者是当前用户
			JAssert.isTrue(baseDao.existsByIdAndCreator(id, exchangeSSOService.getAccount(token)), "待删除的关联线不存在或者是他人的关联线！");
			int flag = baseDao.delete(id);
			sqlSession.commit();
			return flag;
		}
	}

	/**
	 * 根据源组件实例id,查询相关组件实例关系
	 * @param componentInstanceId
	 * @return
	 */
	public List<ComponentInstanceRelation> getComponentInstanceRelationsBySourceId(String componentInstanceId){
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceRelationDao baseDao = sqlSession.getMapper(ComponentInstanceRelationDao.class);
			ComponentInstanceRelation cir = new ComponentInstanceRelation();
			cir.setSourceComponentInstanceId(componentInstanceId);
			return baseDao.listBy(cir);
		}
	}

	/**
	 * 根据目的组件实例id,查询相关组件实例关系
	 * @param componentInstanceId
	 * @return
	 */
	public List<ComponentInstanceRelation> getComponentInstanceRelationsByDestinationId(String componentInstanceId){
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceRelationDao baseDao = sqlSession.getMapper(ComponentInstanceRelationDao.class);
			ComponentInstanceRelation cir = new ComponentInstanceRelation();
			cir.setDestComponentInstanceId(componentInstanceId);
			return baseDao.listBy(cir);
		}
	}

	/**
	 * 根据面板ID查询相关组件实例关系
	 * @param panelId
	 * @return
	 */
	public List<ComponentInstanceRelation> getComponentInstanceRelationsByPanelId(String panelId) {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceRelationDao baseDao = sqlSession.getMapper(ComponentInstanceRelationDao.class);
			return baseDao.listBy(new ComponentInstanceRelation(panelId));
		}
	}

	/**
	 * 删除这些面板上的关联线
	 * @param panelIdList
	 * @return
	 */
	public int deleteByPanelIdList(List<String> panelIdList) {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceRelationDao baseDao = sqlSession.getMapper(ComponentInstanceRelationDao.class);
			return baseDao.deleteByPanelIdList(panelIdList);
		}
	}

	public void copy(String oldPanelId, String newPanelId, Map<String, String> cisTocis, String creator) {
		List<ComponentInstanceRelation> cirs = null;
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceRelationDao baseDao = sqlSession.getMapper(ComponentInstanceRelationDao.class);
			cirs = baseDao.listBy(new ComponentInstanceRelation(oldPanelId));
		}

		for (ComponentInstanceRelation cir : cirs) {
			String sid = cir.getSourceComponentInstanceId();
			String did = cir.getDestComponentInstanceId();
			cir.setId(UUIDUtils.random());
			cir.setSourceComponentInstanceId(cisTocis.get(sid));
			cir.setDestComponentInstanceId(cisTocis.get(did));
			cir.setPanelId(newPanelId);
			cir.setCreator(creator);
			try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
				ComponentInstanceRelationDao baseDao = sqlSession.getMapper(ComponentInstanceRelationDao.class);
				baseDao.insert(cir);
				sqlSession.commit();
			}
		}
	}

	public List<ComponentInstanceRelation> listBy(ComponentInstanceRelation componentInstanceRelation){
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceRelationDao baseDao = sqlSession.getMapper(ComponentInstanceRelationDao.class);
			return baseDao.listBy(componentInstanceRelation);
		}
	}
}
