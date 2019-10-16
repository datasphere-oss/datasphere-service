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

package com.datasphere.engine.projects.model;

import java.util.List;

import com.datasphere.core.common.BaseEntity;
import com.datasphere.engine.shaker.workflow.panelboard.model.Panel;


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
