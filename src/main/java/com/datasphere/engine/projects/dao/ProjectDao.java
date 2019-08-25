package com.datasphere.engine.projects.dao;

import java.util.List;

import com.datasphere.engine.manager.resource.provider.mybatis.page.Pager;
import com.datasphere.engine.projects.model.Project;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ProjectDao {
    //名称模糊查询
	public List<Project> findByNamePager(String projectName, String creator, String sortField, String orderBy, @Param("pager") Pager pager);

	@Select("select count(1) from app_project where id = #{0} and creator = #{1}")
	public Boolean existsByCreator(String projectId, String creator);

	boolean veriftyName(@Param(value = "project_name") String project_name, @Param(value = "creator") String creator);

	int insert(Project project);
	int update(Project project);
	int delete(@Param(value = "id") String id);

	//通过id查询的项目
	Project getProjectById(@Param(value = "id") String id, @Param(value = "creator") String creator);

	//查询所有的项目 (不带分页，不包含面板)
	List<Project> listAll(String creator, String sortField, String orderBy);

	//查询所有的项目（带面板列表）
	List<Project> listAllWithPanelsPager(String creator, String sortField, String orderBy, @Param("pager") Pager pager);

	List<Project> getProjectByField(Project project);
}
