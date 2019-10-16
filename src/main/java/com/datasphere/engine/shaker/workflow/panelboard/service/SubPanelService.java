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

package com.datasphere.engine.shaker.workflow.panelboard.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datasphere.engine.shaker.processor.definition.service.ComponentDefinitionServiceImpl;
import com.datasphere.engine.shaker.processor.instance.service.ComponentInstanceRelationService;
import com.datasphere.engine.shaker.processor.instance.service.ComponentInstanceService;

@Service
public class SubPanelService {
	@Autowired
	ComponentInstanceService componentInstanceService;
	@Autowired
	ComponentInstanceRelationService componentInstanceRelationService;
	@Autowired
	ComponentDefinitionServiceImpl componentDefinitionService;
	@Autowired
	PanelServiceImpl panelService;

//	public Object preDataProcessWarp(PreDataProcessEntity preDataProcessEntity) {
//
//		JAssert.isTrue(preDataProcessEntity != null && preDataProcessEntity.getComponentId() != null, "组件ID不存在");
//
//		ComponentInstance preComponentInstance = componentInstanceService.get(preDataProcessEntity.getComponentId());
//
//		JAssert.isTrue(preComponentInstance != null, "预处理组件实例不存在！ID=" + preDataProcessEntity.getComponentId());
//
//		Panel subPanel = panelService.get(preComponentInstance.getSubPanelId());
//
//		JAssert.isTrue(subPanel != null, "预处理面板不存在！ID=" + preComponentInstance.getSubPanelId());
//
//		if (preComponentInstance.getComponentType() != ComponentClassification.DataPreProcess) {
//			throw new JRuntimeException(GlobalDefine.ERROR_CODE.COMPONENT_NAME_CONSISTENCY,
//					"组件名称与应用内部定义不一致！NAME＝" + ComponentClassification.DataPreProcess);
//		}
//		String panelId = preComponentInstance.getSubPanelId();
//		// delete componentinstance and relations with current subpanel
//		deleteSubPanelComponentInstances(panelId);
//
//		// set precomponent version
//		preComponentInstance.setVersion(preDataProcessEntity.getVersion());
//
//
//		PanelWithAll all = this.warp(preDataProcessEntity.getOperates(), panelId, preDataProcessEntity.getCreator());
//		System.out.println(JSONObject.toJSONString(all));
//		preComponentInstance.setCiParams(JSONArray.toJSONString(preDataProcessEntity.getColumns()));
//		componentInstanceService.update(preComponentInstance);
//		return null;
//	}

	/**
	 * 删除子流程
	 * 
	 * @author kevin 2017年7月18日 下午4:34:57
	 * @param panelId
	 * @return
	 */
	public boolean deleteSubPanelComponentInstances(String panelId) {
		List<String> panels = new ArrayList<>();
		panels.add(panelId);
		componentInstanceRelationService.deleteByPanelIdList(panels);
		componentInstanceService.deleteByPanelIdList(panels);
		return true;
	}

	/**
	 * 根据list封装组件实例和关系
	 * 
	 * @author kevin 2017年7月18日 下午4:38:33
	 * @param operates
	 * @return
	 */
//	public PanelWithAll warp(String operateStr, String panelId, String creator) {
//		List<ComponentInstance> componentInstances = new ArrayList<>();
//		List<ComponentParams> operates=JSONArray.parseArray(operateStr, ComponentParams.class);
//		for (ComponentParams operate : operates) {
//			String type = operate.getCode();
//			ComponentDefinition cd = componentDefinitionService.getByCode(type);
//			JAssert.isTrue(cd != null, "组件类型不存在！operateCode=" + type);
//			ComponentInstance instance = this.warpComponentInstanceByOpearate(operate, cd, panelId, creator);
//			componentInstances.add(instance);
//		}
//		List<ComponentInstanceRelation> relations = warpComponentInstanceRelationByInstances(componentInstances,
//				panelId, creator);
//		PanelWithAll all = new PanelWithAll();
//		all.setComponentInstances(componentInstances);
//		all.setComponentInstanceRelations(relations);
//		return all;
//
//	}

//	public ComponentInstance warpComponentInstanceByOpearate(ComponentParams operate, ComponentDefinition cd, String panelId,
//			String creator) {
//		ComponentInstance instance = new ComponentInstance();
//		instance.setCiParams(JSONObject.toJSONString(operate.getAlgmAttrs()));
//		instance.setComponentDefinitionId(cd.getId());
//		instance.setPanelId(panelId);
//		instance.setCreator(creator);
//		componentInstanceService.insert(instance);
//		return instance;
//
//	}

//	public List<ComponentInstanceRelation> warpComponentInstanceRelationByInstances(
//			List<ComponentInstance> componentInstances, String panelId, String creator) {
//		List<ComponentInstanceRelation> list = new ArrayList<>();
//		for (int i = 0; i < componentInstances.size(); i++) {
//			if (i == 0) {
//				continue;
//			}
//			ComponentInstanceRelation relation = new ComponentInstanceRelation();
//			relation.setCreator(creator);
//			relation.setPanelId(panelId);
//			relation.setSourceComponentInstanceId(componentInstances.get(i - 0).getId());
//			relation.setSourceOutputName("OUT001");
//			if (i != componentInstances.size() - 1) {
//				relation.setDestComponentInstanceId(componentInstances.get(i + 1).getId());
//				relation.setDestInputName("IN001");
//			}
//			componentInstanceRelationService.insert(relation, creator);
//			list.add(relation);
//		}
//		return list;
//
//	}

}
