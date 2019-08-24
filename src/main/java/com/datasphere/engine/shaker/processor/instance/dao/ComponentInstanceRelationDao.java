package com.datasphere.engine.shaker.processor.instance.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.datasphere.engine.shaker.processor.instance.model.ComponentInstanceRelation;


public interface ComponentInstanceRelationDao {
	
	@Delete("delete from public.component_instance_relation where source_component_instance_id = #{0} or dest_component_instance_id = #{0}")
	int deleteBySourceIdOrDestId(String id);
	
	@Select("select id from public.component_instance_relation where source_component_instance_id = #{0} or dest_component_instance_id = #{0}")
	List<String> listBySourceIdOrDestId(String id);

	@Select("select dest_component_instance_id from public.component_instance_relation where source_component_instance_id = #{0}")
	List<String> getDestIdBySourceId(String id);

	@Select("select source_component_instance_id from public.component_instance_relation where dest_component_instance_id = #{0}")
	List<String> getSouceCIIdsByDestCIIds(String id);
	
	@Select("select count(1) from public.component_instance_relation where id = #{id} and creator = #{creator}")
	Boolean existsByIdAndCreator(@Param(value = "id") String id, @Param(value = "creator") String creator);

	int deleteByPanelIdList(List<String> panelIdList);

    void insert(ComponentInstanceRelation componentInstanceRelation);

    int delete(String id);

	int update(ComponentInstanceRelation componentInstanceRelation);

	List<ComponentInstanceRelation> listBy(ComponentInstanceRelation componentInstanceRelation);


}
