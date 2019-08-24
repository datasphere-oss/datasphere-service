package com.datasphere.engine.shaker.processor.service;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.datasphere.resource.manager.module.dal.buscommon.utils.ObjectMapperUtils;
import com.datasphere.core.common.BaseService;
import com.datasphere.engine.shaker.processor.dao.ComponentInstanceSnapshotDao;
import com.datasphere.engine.shaker.processor.model.ComponentInstanceSnapshot;
import com.datasphere.resource.manager.module.component.instance.service.ComponentInstanceRelationService;
import com.datasphere.resource.manager.module.component.instance.service.ComponentInstanceService;

import org.apache.ibatis.session.SqlSession;

/**
 * 组件实例快照服务实现类
 */
@Singleton
public class ComponentInstanceSnapshotService extends BaseService {
//	@Inject
//	private ComponentInstanceRelationSnapshotService componentInstanceRelationSnapshotService;
	@Inject
	ComponentInstanceService componentInstanceService;
	@Inject
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
