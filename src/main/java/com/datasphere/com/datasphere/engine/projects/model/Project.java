package com.datasphere.com.datasphere.engine.projects.model;

import java.util.List;

import com.datasphere.core.common.BaseEntity;
import com.datasphere.engine.shaker.workflow.panel.domain.Panel;


public class Project extends BaseEntity {
	private String projectName;
	//项目描述
	private String projectDesc;
	//创建人id
	private String creator;
	//面板总数
	private String panelTotal;
	//面板条数：0：查询全部，4：返回4条
	private String  panelPageSize;
	//面板第几页
	private String  panelPageNumber;
	//该项目下的面板列表
    List<Panel>  panelList;
    
	public String getPanelPageNumber() {
		return panelPageNumber;
	}

	public void setPanelPageNumber(String panelPageNumber) {
		this.panelPageNumber = panelPageNumber;
	}

	public String getPanelPageSize() {
		return panelPageSize;
	}

	public void setPanelPageSize(String panelPageSize) {
		this.panelPageSize = panelPageSize;
	}

	public String getPanelTotal() {
		return panelTotal;
	}

	public void setPanelTotal(String panelTotal) {
		this.panelTotal = panelTotal;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectDesc() {
		return projectDesc;
	}

	public void setProjectDesc(String projectDesc) {
		this.projectDesc = projectDesc;
	}
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	public List<Panel> getPanelList() {
		return panelList;
	}

	public void setPanelList(List<Panel> panelList) {
		this.panelList = panelList;
	}

	@Override
	public String toString() {
		return "ProjectQuery [projectName=" + projectName + ", projectDesc=" + projectDesc + ",id=" + this.getId()+",creator="+creator
				+ ", createTime=" + this.getCreateTime() + ", lastModified=" + this.getLastModified() + "]";
	}
}
