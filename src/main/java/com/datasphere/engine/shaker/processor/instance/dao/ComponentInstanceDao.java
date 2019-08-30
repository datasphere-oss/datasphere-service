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

package com.datasphere.engine.shaker.processor.instance.dao;

import java.util.List;

import javax.inject.Singleton;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.datasphere.engine.shaker.processor.instance.model.ComponentInstance;

@Singleton
public interface ComponentInstanceDao  {
	
	/**
	 * The component instance is queried according to the panel ID and the component classification. The panel ID condition is an exact match, and the component classification condition is a regular match.
	 * @param panelId
	 * @param
	 * @return
	 */
	//	@ResultMap("baseMap")
//	@Select("select * from component_instance where panel_id = #{0} and component_classification like '001%'")
//	List<ComponentInstance> listDataSourceByPanelId(String panelId);
	
	/**
	 * Query the number of panels that reference this component based on the component ID
	 * @param componentDefinitionId
	 * @return
	 */
//	long countDistinctPanelsByComponentId(String componentDefinitionId);
	
	/**
	 * List the names of all component instances under the current panel
	 * @param panelId
	 * @retur
	 */
	@Select("select ci_name from public.component_instance where panel_id = #{0}")
	List<String> listNamesByPanelId(String panelId);
	
	@Select("select count(1) from public.component_instance where id = #{param1} and creator = #{param2}")
	Boolean belong(String componentInstanceId, String userId);
	
	@Select("select count(1) from public.component_instance where component_definition_id = #{0}")
	Boolean existsBy(String componentDefinitionId);

	@Update("update public.component_instance set status = #{status} where panel_id = #{panelId}")
	int updateStatusByPanelId(@Param("panelId") String panelId, @Param("status") String status);
	
	int batchUpdateStatus(List<String> idList);

	List<String> listIdByPanelIdList(List<String> panelIdList);

	int deleteByPanelIdList(List<String> panelIdList);

	@ResultMap("baseMap")
	@Select("select distinct ci.* from public.component_instance ci left join public.component_instance_relation cir on(ci.id = cir.dest_component_instance_id) where ci.panel_id = '${_parameter}' and cir.id is null")
	List<ComponentInstance> listBeginComponentInstancesByPanelId(String panelId);
	
	ComponentInstance getInstanceBySubPanelId(ComponentInstance instance);

	int insert(ComponentInstance componentInstance);

	int delete(String id);

	int update(ComponentInstance componentInstance);


	ComponentInstance get(String id);

	List<ComponentInstance> listBy(ComponentInstance componentInstance);

	int getCountByCFId(String cFId);
}
