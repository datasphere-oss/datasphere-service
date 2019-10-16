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

package com.datasphere.engine.shaker.workflow.panelboard.model;

import com.datasphere.engine.core.common.BaseEntity;

public class Panel extends BaseEntity {
	protected String id;

	protected String projectId;//项目id
	protected String projectName;//项目名称
	protected String projectRef;//项目引用
	protected String projectDesc;//项目描述

	protected String panelName;// 面板名称	@Size(min = 1, max = 64
	protected String panelDesc;// 面板描述 	@Size(min = 0, max = 1024

	protected String lastProcessInstanceId;
	protected Integer type; //面板类型：0 面板、1 模版 、2 子面板、3 模型
	protected String creator;// 创建人

    protected int componentInstanceCount;// 组件实例总数
    protected int componentInstanceRelationCount;// 组件实例关系总数

	protected String departmentId;
	protected String departmentName;
	protected String userId;

	public Panel() {}

	public Panel(String projectId) {
		this.projectId = projectId;
	}

	public Panel(String projectId, String userId) {
		this.projectId = projectId;
		this.creator = userId;
	}

    public int getComponentInstanceCount() {
        return componentInstanceCount;
    }

    public void setComponentInstanceCount(int componentInstanceCount) {
        this.componentInstanceCount = componentInstanceCount;
    }

    public int getComponentInstanceRelationCount() {
        return componentInstanceRelationCount;
    }

    public void setComponentInstanceRelationCount(int componentInstanceRelationCount) {
        this.componentInstanceRelationCount = componentInstanceRelationCount;
    }

	public String getProjectRef() {
		return projectRef;
	}

	public void setProjectRef(String projectRef) {
		this.projectRef = projectRef;
	}

	public String getProjectDesc() {
		return projectDesc;
	}

	public void setProjectDesc(String projectDesc) {
		this.projectDesc = projectDesc;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getPanelName() {
		return panelName;
	}

	public void setPanelName(String panelName) {
		this.panelName = panelName;
	}

	public String getPanelDesc() {
		return panelDesc;
	}

	public void setPanelDesc(String panelDesc) {
		this.panelDesc = panelDesc;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getLastProcessInstanceId() {
		return lastProcessInstanceId;
	}

	public void setLastProcessInstanceId(String lastProcessInstanceId) {
		this.lastProcessInstanceId = lastProcessInstanceId;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "Panel [panelName=" + panelName + ", panelDesc=" + panelDesc + ", projectId=" + projectId + ", creator="
				+ creator + ", projectRef=" + projectId + "]";
	}
}
