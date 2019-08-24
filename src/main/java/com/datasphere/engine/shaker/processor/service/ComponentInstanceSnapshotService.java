package com.datasphere.engine.shaker.processor.service;

import java.util.HashMap;
import java.util.Map;

import com.datasphere.resource.manager.module.dal.buscommon.utils.ObjectMapperUtils;
import com.datasphere.core.common.BaseService;
import com.datasphere.engine.shaker.processor.dao.ComponentInstanceSnapshotDao;
import com.datasphere.engine.shaker.processor.model.ComponentInstanceSnapshot;
import com.datasphere.resource.manager.module.component.instance.service.ComponentInstanceRelationService;
import com.datasphere.resource.manager.module.component.instance.service.ComponentInstanceService;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 组件实例快照服务实现类
 */
@Service
public class ComponentInstanceSnapshotService extends BaseService {
	@Autowired
	ComponentInstanceService componentInstanceService;
	@Autowired
	ComponentInstanceRelationService componentInstanceRelationService;
	
	public void createComponentInstanceSnapshotsByPanelId(String panelId,String processId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("componentInstances", componentInstanceService.getAllComponentInstancesWithPanel(panelId));
		map.put("componentInstanceRelations", componentInstanceRelationService.getComponentInstanceRelationsByPanelId(panelId));
		try (SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ComponentInstanceSnapshotDao cisd = sqlSession.getMapper(ComponentInstanceSnapshotDao.class);
			ComponentInstanceSnapshot cis = new ComponentInstanceSnapshot();
			cis.setProcessId(processId);
			cis.setContent(ObjectMapperUtils.writeValue(map));
			cisd.add(cis);
			sqlSession.commit();
		}
	}
}
