package com.datasphere.engine.shaker.processor.instance.dao;

import java.util.List;

//import org.apache.ibatis.annotations.ResultMap;
import io.micronaut.context.annotation.Parameter;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.datasphere.resource.manager.module.component.instance.domain.ComponentInstance;

import javax.inject.Singleton;

@Singleton
public interface ComponentInstanceDao  {
	
	/**
	 * 根据面板ID和组件分类查询出组件实例，面板ID条件是精确匹配，组件分类条件是正则匹配
	 * @param panelId
	 * @param
	 * @return
	 */
	//	@ResultMap("baseMap")
//	@Select("select * from component_instance where panel_id = #{0} and component_classification like '001%'")
//	List<ComponentInstance> listDataSourceByPanelId(String panelId);
	
	/**
	 * 根据组件ID查询引用了此组件的面板数量
	 * @param componentDefinitionId
	 * @return
	 */
//	long countDistinctPanelsByComponentId(String componentDefinitionId);
	
	/**
	 * 列出当前面板下所有组件实例的名称
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
