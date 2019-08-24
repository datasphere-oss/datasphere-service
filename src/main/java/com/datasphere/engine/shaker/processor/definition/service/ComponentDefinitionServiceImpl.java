package com.datasphere.engine.shaker.processor.definition.service;

import com.datasphere.resource.manager.module.panel.domain.ComponentDefinitionPanel;
import com.datasphere.server.manager.common.constant.GlobalDefine;
import com.datasphere.core.common.BaseService;
import com.datasphere.engine.manager.resource.provider.dictionary.model.DSSWord;
import com.datasphere.engine.shaker.processor.definition.ComponentDefinition;
import com.datasphere.engine.shaker.processor.definition.dao.ComponentDefinitionDao;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ComponentDefinitionServiceImpl extends BaseService {


	public List<ComponentDefinition> listDataSource() {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentDefinitionDao dictionaryDao = sqlSession.getMapper(ComponentDefinitionDao.class);
		return dictionaryDao.listDataSource();
		}
	}



	/**
	 * 以树的形式展现组件面板
	 * @return
	 */
	public List<DSSWord> listForTree(String creator, String name) {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentDefinitionDao dictionaryDao = sqlSession.getMapper(ComponentDefinitionDao.class);
			List<DSSWord> objects=new ArrayList<>();
			List<DSSWord> group;
			ComponentDefinition component=new ComponentDefinition();
			if(StringUtils.isEmpty(name)){
				group= dictionaryDao.listGroupNameIsNull(creator);
			}else{
				group=dictionaryDao.listGroup(creator,name);
			}
			DSSWord myDataSource = new DSSWord();
			myDataSource.setName("我的数据源");
			DSSWord myDataSource2 = new DSSWord();
			myDataSource2.setName("已订阅数据源");
			for (DSSWord classification:group) {
				if ("".equals(classification) || null == classification) continue;
				if(!StringUtils.isEmpty(name)){
					List<ComponentDefinition> components = dictionaryDao.listGroup1(name);
					for (ComponentDefinition component2 : components) {
						setOut(component2);
						component=component2;
						if(component2.getBusineesType().equals(classification.getName())){
							//判断name的模糊查询属于哪个二级菜单
							classification.addChild(component2);
						}
					}
					if ("DataWave".equals(component.getDataFrom())){
						myDataSource.addChild(classification);
					}else{
						myDataSource2.addChild(classification);
					}
				}else{
					List<ComponentDefinition> components = dictionaryDao.listGroup2(classification.getName());
					for (ComponentDefinition component2 : components) {
						setOut(component2);
						component=component2;
						classification.addChild(component2);
					}
					if ("DataWave".equals(component.getDataFrom())){
						myDataSource.addChild(classification);
					}else{
						myDataSource2.addChild(classification);
					}
				}
			}
			objects.add(myDataSource);
			objects.add(myDataSource2);
			return objects;
		}
	}


	public List<ComponentDefinitionPanel> listPanelCountGroupByComponentId(String creator) {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentDefinitionDao dictionaryDao = sqlSession.getMapper(ComponentDefinitionDao.class);
			return dictionaryDao.listPanelCountGroupByComponentId(creator);
		}
	}

	public List<ComponentDefinitionPanel> listComponentDefinitionPanel(String creator) {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentDefinitionDao dictionaryDao = sqlSession.getMapper(ComponentDefinitionDao.class);
			return dictionaryDao.listComponentDefinitionPanel(creator);
		}
	}

	public List<ComponentDefinition> listAllWithPager(ComponentDefinition pager) {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentDefinitionDao dictionaryDao = sqlSession.getMapper(ComponentDefinitionDao.class);
		return dictionaryDao.listAllWithPager(pager);
		}
	}

	/**
	 * 根据ID删除组件定义
	 * @param id
	 * @return
	 */
	public int delete(String id) {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentDefinitionDao dictionaryDao = sqlSession.getMapper(ComponentDefinitionDao.class);
			int flag = dictionaryDao.delete(id);
			sqlSession.commit();
			return flag;
		}
	}

	public Boolean exists(String id) {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentDefinitionDao dictionaryDao = sqlSession.getMapper(ComponentDefinitionDao.class);
			return dictionaryDao.exists(id);
		}
	}

	public int update(ComponentDefinition t) {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentDefinitionDao dictionaryDao = sqlSession.getMapper(ComponentDefinitionDao.class);
			return dictionaryDao.update(t);
		}
	}

	public void insert(ComponentDefinition t) {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentDefinitionDao dictionaryDao = sqlSession.getMapper(ComponentDefinitionDao.class);
			dictionaryDao.insert(t);
			sqlSession.commit();
		}
	}

	public ComponentDefinition getByCode(String code) {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentDefinitionDao dictionaryDao = sqlSession.getMapper(ComponentDefinitionDao.class);
		return dictionaryDao.getByCode(code);
		}
	}

	/**
	 *
	 */
	public List<ComponentDefinition> listAll(){
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentDefinitionDao dictionaryDao = sqlSession.getMapper(ComponentDefinitionDao.class);
		return dictionaryDao.listAll();
		}
	}

	public List<ComponentDefinition> listBy(ComponentDefinition componentDefinition){
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentDefinitionDao dictionaryDao = sqlSession.getMapper(ComponentDefinitionDao.class);
		return dictionaryDao.listBy(componentDefinition);
		}
	}

	public Object get(String id) {
		return null;
	}

	public ComponentDefinition getDFIDByName(String name) {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentDefinitionDao componentDefinition = sqlSession.getMapper(ComponentDefinitionDao.class);
			return componentDefinition.getDFIDByName(name);
		}
	}

	public List<ComponentDefinition> listDataProcessComponent(String creator) {
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentDefinitionDao componentDefinition = sqlSession.getMapper(ComponentDefinitionDao.class);
			return componentDefinition.getDataProcessComponent(creator,GlobalDefine.COMPONENT_CLASSIFICATION.PREPROCESS);
		}
	}

	protected List<Object> setOut(ComponentDefinition component){
		List<Object> list = new ArrayList();
		Map<String, Object> map = new HashMap<>();
		map.put("code",  "OUT001");
		map.put("displayName", "输出集");
		component.setIn_point(new ArrayList());
		component.setOut_point(list);
		component.setCode("SimpleDataSource");
		list.add(map);
		return list;
	}
}
