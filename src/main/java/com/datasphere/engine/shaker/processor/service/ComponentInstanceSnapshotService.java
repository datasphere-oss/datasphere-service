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

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datasphere.core.common.BaseService;
import com.datasphere.engine.core.utils.ObjectMapperUtils;
import com.datasphere.engine.shaker.processor.dao.ComponentInstanceSnapshotDao;
import com.datasphere.engine.shaker.processor.instance.service.ComponentInstanceRelationService;
import com.datasphere.engine.shaker.processor.instance.service.ComponentInstanceService;
import com.datasphere.engine.shaker.processor.model.ComponentInstanceSnapshot;

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
