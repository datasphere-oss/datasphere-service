package com.datasphere.com.datasphere.engine.projects.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.datasphere.com.datasphere.engine.projects.dao.ProjectDao;
import com.datasphere.com.datasphere.engine.projects.model.Project;
import com.datasphere.core.common.BaseService;
import com.datasphere.engine.core.utils.JAssert;
import com.datasphere.engine.datasource.mybatis.page.Pager;
import com.datasphere.engine.manager.resource.provider.service.ExchangeSSOService;

import org.apache.commons.lang3.StringUtils;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl extends BaseService {
	public static String DEFAULT_PROJECT_NAME = "默认项目";

	@Autowired
	ExchangeSSOService exchangeSSOService;
	
	/**1
	 * 检验名称是否已经存在
	 * @param projectName
	 * @return false:没有重复命名，true：有重复命名
	 */
	public boolean veriftyName(String projectName) {
//		String userId = UserContextHolder.getUserContext().getOmSysUser().getUserId();
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ProjectDao dao = sqlSession.getMapper(ProjectDao.class);
			return dao.veriftyName(projectName,null);
		}
	}

	/**2
	 * 创建项目，如果项目不存在，则自定义草稿项目（草稿项目存储资自动保存面板）
	 * @param project
	 */
	public int create(Project project, String token) {
		project.setCreator(exchangeSSOService.getAccount(token));
		project.setId(UUID.randomUUID().toString());
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ProjectDao dao = sqlSession.getMapper(ProjectDao.class);
			int flag = dao.insert(project);
			sqlSession.commit();
			return flag;
		}
	}

	/**3
	 * 更新项目
	 * @param project
	 * @return
	 */
	public int update(Project project, String token) {
		JAssert.isTrue(!DEFAULT_PROJECT_NAME.equals(getProjectById(project.getId(), token).getProjectName()), "默认项目不能修改！");
		Project dbProject = getProjectById(project.getId(), token);
		if(!dbProject.getProjectName().equals(project.getProjectName())) {
			JAssert.isTrue(!veriftyName(project.getProjectName()), "项目名称已存在！");
		}
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ProjectDao dao = sqlSession.getMapper(ProjectDao.class);
			int flag = dao.update(project);
			sqlSession.commit();
			return flag;
		}
	}

	/**4
	 * 删除项目及面板
	 * 1:删除成功;    0:删除失败;   2:有正在运行的面板，删除失败
	 */
	public int delete(String id, String token) {
//		String userId = UserContextHolder.getUserContext().getOmSysUser().getUserId();
		JAssert.isTrue(getProjectById(id, token) != null, id+":项目不存在！");
		JAssert.isTrue(!DEFAULT_PROJECT_NAME.equals(getProjectById(id, token).getProjectName()), "默认项目不能删除！");
		//删除时，调用删除该项目下的所有面板  ，当面板正在运行时，不能删除！
		int resultDel=deletePanelByProjectId(id,null);
		if(resultDel==0){
			try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
				ProjectDao dao = sqlSession.getMapper(ProjectDao.class);
				int flag = dao.delete(id);
				sqlSession.commit();
				return flag;
			}
		}else if(resultDel==2){
			return 2;
		}else{
			return 0;
		}
	}

	/**4
	 * 根据项目id查找该项目下的面板列表
	 */
	public Project getProjectById(String id, String token) {
//		String userId = UserContextHolder.getUserContext().getOmSysUser().getUserId();
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ProjectDao dao = sqlSession.getMapper(ProjectDao.class);
			Project project = dao.getProjectById(id,exchangeSSOService.getAccount(token));
			if(project != null) {
				List panelList = getByProjectId(id,null);
				if(panelList != null) {
					project.setPanelList(panelList);
				}
			}
			return project;// 获取面板信息，放入项目中
		}
	}

	/**5
	 * 查找所有项目,不分页
	 */
	public List<Project> getAllProjectList(Project project) {
//		String userId = UserContextHolder.getUserContext().getOmSysUser().getUserId();
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ProjectDao dao = sqlSession.getMapper(ProjectDao.class);
			List<Project> projects = dao.listAll(null, project.getSortField(), project.getOrderBy());
			if(projects.size()>0){ //获取面板信息，放入项目中
				return getPanelByProjectIdList(projects, project.getSortField(), project.getOrderBy(),project.getPanelPageSize(),project.getPanelPageNumber(),null);
			} else {
				return null;
			}
		}
	}

	/**6
	 * 带面板和分页的的项目列表
	 * @return
	 */
	public Project getAllWithPanelsListByPage(Project project){
//		String userId = UserContextHolder.getUserContext().getOmSysUser().getUserId();
		if(StringUtils.isBlank(project.getSortField())) {
			project.setSortField("create_time");
		}
		if(StringUtils.isBlank(project.getOrderBy())) {
			project.setOrderBy("desc");
		}
		if(project.getPager() == null) {
			Pager pager=new Pager();
			pager.setPageNumber(1);
			pager.setPageSize(20);
			project.setPager(pager);
		}
		List<Project> projectWithPanelList = null;
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ProjectDao dao = sqlSession.getMapper(ProjectDao.class);
			List<Project> projectList = dao.listAllWithPanelsPager(null, project.getSortField(), project.getOrderBy(),project.getPager());
			if(projectList.size()>0){
				//将面板list加入各个项目中
				projectWithPanelList=getPanelByProjectIdList(projectList,project.getSortField(), project.getOrderBy(),project.getPanelPageSize(),project.getPanelPageNumber(),null);
				project.getPager().setList(projectWithPanelList);
			} else {
				project = null;
			}
			return project;
		}
	}

	/**
	 * 根据条件查询项目列表,如按名称查询，按描述查询
	 * @param project
	 * @return
	 */
	public List<Project> getProjectByField(Project project) {
//		String userId = UserContextHolder.getUserContext().getOmSysUser().getUserId();
		project.setCreator(null);
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ProjectDao dao = sqlSession.getMapper(ProjectDao.class);
			List<Project> projectList = dao.getProjectByField(project);
			JAssert.isTrue(projectList.size() <= 1, "该用户的项目重名。名称:" + project.getProjectName());
			if (projectList.size() == 1) {
				// 将面板信息放入项目中
				List panelList = getByProjectId(projectList.get(0).getId(), null);
				if (panelList != null) {
					projectList.get(0).setPanelList(panelList);
				}
				return projectList;
			} else {
				return null;
			}
		}
	}
	
	/**
	 * 项目名称模糊查询列表
	 * @param project
	 * @return
	 */
	public Project findByName(Project project) {
//		String userId = UserContextHolder.getUserContext().getOmSysUser().getUserId();
		if(StringUtils.isBlank(project.getSortField())) {
			project.setSortField("create_time");
		}
		if(StringUtils.isBlank(project.getOrderBy())) {
			project.setOrderBy("desc");
		}
		if(project.getPager()==null){
			Pager pager=new Pager();
			pager.setPageNumber(1);
			pager.setPageSize(20); //项目默认20条
			project.setPager(pager);
		}

		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ProjectDao dao = sqlSession.getMapper(ProjectDao.class);
			List<Project> projectsList = dao.findByNamePager(project.getProjectName(), null, project.getSortField(), project.getOrderBy(), project.getPager());

			//将面板list加入各个项目中
			if (projectsList.size() > 0) {
				List<Project> projectWithPanelList = getPanelByProjectIdList(projectsList, project.getSortField(), project.getOrderBy(), project.getPanelPageSize(), project.getPanelPageNumber(), null);
				project.getPager().setList(projectWithPanelList);
			} else {
				project = null;
			}
			return project;
		}
	}

	//默认项目
	public Project getDefultProject(String token) {
		Project project = new Project();
		project.setCreator(null);
		project.setProjectName("默认项目");
		List<Project> projectlist = getProjectByField(project);
		if (projectlist.size() > 0) {
			return projectlist.get(0);
		} else {
			project.setProjectDesc("该项目存储自定义面板!");
			create(project,token);
			return project;
		}
	}
	
	public List<Project> listByCreator(String creator) {
		Project p = new Project();
		p.setCreator(creator);
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ProjectDao dao = sqlSession.getMapper(ProjectDao.class);
			return dao.getProjectByField(p);
		}
	}

	public Boolean exists(String projectId) {
//		String userId = UserContextHolder.getUserContext().getOmSysUser().getUserId();
		try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
			ProjectDao dao = sqlSession.getMapper(ProjectDao.class);
			return dao.existsByCreator(projectId, null);
		}
	}

	/**
	 * 组装多个项目下的面板列表
	 * @param projectList
	 * @param userId
	 * @param sortField
	 * @param orderBy
	 * @return
	 */
	public List<Project> getPanelByProjectIdList(List<Project> projectList,String sortField,String orderBy,String panelPageSize,String panelPageNumber,String userId){
		//组装所有项目id
//		String projectIdStr="";
//		if(projectList.size()>0){
//			for(Project project:projectList){
//				projectIdStr=projectIdStr+project.getId()+",";
//			}
//			projectIdStr=projectIdStr.substring(0, projectIdStr.length()-1);
//		}
//
//		try {
//			String params=null;
//			if(!StringUtils.isBlank(panelPageSize) &&!StringUtils.isBlank(panelPageNumber) ){
//				params="projectId="+projectIdStr+"&sortField="+sortField+"&orderBy="+orderBy+"&pager.pageSize="+panelPageSize+"&pager.pageNumber="+panelPageNumber+"&creator="+userId;
//
//			}else{
//				params="projectId="+projectIdStr+"&sortField="+sortField+"&orderBy="+orderBy+"&creator="+userId;
//			}
//			//访问工作台的url，获取面板详细信息
//			Map<String, Object>  panelMap= (Map)WebsiteTerminalUtils.sendRequest(terminal,HttpMethod.POST, "/panel/getByProjectIdList", params);
//			if(panelMap!=null && panelMap.size()>0){
//				for (Project project:projectList) {
//					Map<String, Object> panelMapPager = ObjectMapperUtils.readValue(ObjectMapperUtils.writeValue(panelMap.get(project.getId())),  Map.class);
//					project.setPanelTotal(panelMapPager.get("totalRecords").toString());
//					List panels=(List) panelMapPager.get("list");
//					if(panels!=null){
//						project.setPanelList(panels);
//						O.json(panels);
//					}
//
//				}
//			}
//		} catch (HttpClientException e) {
//			e.printStackTrace();
//			throw new JIllegalOperationException(e.getErrorCode(),e.getMessage());
//		}
		return projectList;
	}
	
	/**
	 * 组装单个项目下的面板列表
	 * @param projectId
	 * @param userId
	 * @return
	 */
	public List getByProjectId(String projectId,String userId){
		List panels=null;
//		try {
			String params="projectId="+projectId+"&sortField=create_time&orderBy=desc"+"&creator="+userId;
//			panels= (List)WebsiteTerminalUtils.sendRequest(terminal,HttpMethod.POST, "/panel/getByProjectId", params);
//			O.json(panels);
//		} catch (HttpClientException e) {
//			e.printStackTrace();
//			throw new JIllegalOperationException(e.getErrorCode(),e.getMessage());
//		}
		return panels;
	}
	
	/**
	 * 删除一个项目下的面板列表
	 * @param projectId
	 * @return
	 */
	public int deletePanelByProjectId(String projectId,String userId){
		 int  delFlag = 0; //0:删除成功;删除失败;
//		try {
			String params="projectId="+projectId+"&creator="+userId;
//			String panelLists= (String)WebsiteTerminalUtils.sendRequest(terminal,HttpMethod.POST, "/panel/deletePanelByProjectId", params);
//			if(panelLists!=null){
//				delFlag=0;
//			}
//		} catch (HttpClientException e) {
//			delFlag=e.getErrorCode();
//			if(delFlag==2){
//				return delFlag;
//			}else{
//				throw new JIllegalOperationException(e.getErrorCode(),e.getMessage());
//			}
//		}
		return delFlag;
	}

	/**
	 * 获取面板详细信息
	 * @param id
	 * @param projectId
	 * @return
	 */
	public Map<String, Object> panelPageDetail(String projectId, String id) {
//		String userId = UserContextHolder.getUserContext().getOmSysUser().getUserId();
		Map<String, Object> panelList=null;
		//项目不存在，面板不存在
		if(StringUtils.isBlank(projectId) && StringUtils.isBlank(id)) {
			//取该用户上一次访问面板详细信息
			/*Map<String, String> lastPanel = getLastAccessPanel(null);
			if(lastPanel == null) {
				// 如果还没创建面板，在“默认项目”下创建一个面板
				// 该用户上一次访问面板详细信息不存在，则创建默认面板
				projectId=getDefultProject().getId();
				Map<String, String> panel = createPanel(projectId,null);
				JAssert.isTrue(panel!=null,"创建默认面板--面板不能为空！");
				id=panel.get("id");
			} else {
				//该用户上一次访问面板详细信息存在，则获取上次的面板流程详细信息
				id=lastPanel.get("id");
				projectId=lastPanel.get("projectId");
			}*/
		} else if (!StringUtils.isBlank(projectId) && StringUtils.isBlank(id)){
			//该项目下没有面板时，提示没有面板
			return null;
		}
		//获取面板及流程详细信息，并将该用户下的全部项目信息放入结果中。
//		try {
//			String params="id="+id+"&projectId="+projectId+"&creator="+userId;
//			List panelLists= (List)WebsiteTerminalUtils.sendRequest(terminal,HttpMethod.POST, "/panel/panelDetailCyl", params);
//			if(panelLists.size()>0){
//				panelList=(Map<String, Object>) panelLists.get(0);
//				panelList.put("projects", listByCreator(userId));
//			}
//		} catch (HttpClientException e) {
//			e.printStackTrace();
//			throw new JIllegalOperationException(e.getErrorCode(),e.getMessage());
//		}
		return panelList;
	}
}

