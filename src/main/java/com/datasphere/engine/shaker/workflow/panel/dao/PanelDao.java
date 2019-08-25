package com.datasphere.engine.shaker.workflow.panel.dao;

import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.datasphere.engine.manager.resource.provider.mybatis.page.Pager;
import com.datasphere.engine.shaker.processor.model.ProcessInstance;
import com.datasphere.engine.shaker.workflow.panel.domain.Panel;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Singleton
public interface PanelDao {
    //获取“默认项目”项目下的所有自定义面板
//	@Select("select * from  app_panel where panel_name like '"+customPanelName+"%' and  project_id= #{0} and creator= #{1}")
//	@ResultMap("baseMap")
	List<Panel> getPanelByProjectId(String projectId, String creator);
	
	//删除项目下的面板
//	@Delete("delete from app_panel where project_id= #{0}")
	void deleteById(String projectId);
	
	 //根据projectId查询project, 关联将panel查询出来
	List<Panel> getPanelsByProjectId(String projectId, String creator);
	
	//根据面板Id查询panel，带项目信息
	Panel getPanelById(String id);
	
//	@Select("select id from app_panel where creator = #{0} and project_id = #{1} order by last_access desc offset 0 rows fetch next 1 rows only")
	String getLastAccessPanelIdBy(String creator, String projectId);

	List<Panel> getPanelsByProjectIdOrdey(String projectId, String creator, String sortField, String orderBy);

//	@Select("select id from app_panel where project_id = #{0}")
	List<String> listIdByProjectId(String projectId);

	@Select("select panel_name from app_panel where id = #{0}")
	List<String> listNameByPanelId(String id);

	boolean verifyName(String name, String projectId, String creator);

    List<Panel> getPanelsByProjectIdOrdeyPager(String projectId, String creator, String sortField, String orderBy, Pager pager);

	List<Panel> getSourceTrace(String id, String userId);

	List<String> getInstanceId(@Param(value = "panelId") String panelId);

	int deleteDataInstance(@Param(value = "instanceId") String instanceId);

	int deleteRelation(@Param(value = "panelId") String panelId);

	int deleteDefinition(@Param(value = "definitionId") String definitionId);

	List<String> getDefinitionIds(@Param(value = "panelId") String panelId);

    int insert(Panel panel);

    int update(Panel panel);

    int delete(String id);

    Panel get(String id);

    List<Panel> listBy(Panel panel);

    boolean exists(String panelId);

    ProcessInstance getLastByPanelId(String panelId);

	int updateStatusByPanelId(String panelId, String status);

	//查询所有的项目 (不带分页，不包含面板)
//	List<Panel> listAll(String creator);
	List<Panel> listAll(Map<String,Object> params);

	@Select("select count(1) from public.app_panel where id = #{panelId} and creator = #{creator}")
	Boolean existsByCreator(@Param(value = "panelId") String panelId, @Param(value = "creator") String creator);

	@Update("update public.app_panel set last_access = CURRENT_TIMESTAMP where id = #{0}")
	void updateLastAccess(String id);

	@Select("select id from public.app_panel where creator = #{0} and type=0 order by last_access desc limit 1")
	String getLastAccessPanelId(String creator);

//	@Select("select panel_name from public.app_panel where project_id = #{0}")
//	List<String> listNamesByProjectId(String projectId);

	@Update("update public.app_panel set last_process_instance_id = #{lastProcessInstanceId} where id = #{panelId}")
	int updateLastProcessInstanceId(@Param(value = "panelId") String panelId, @Param(value = "lastProcessInstanceId") String lastProcessInstanceId);
}
