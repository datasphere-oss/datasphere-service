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
