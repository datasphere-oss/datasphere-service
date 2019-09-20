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

package com.datasphere.engine.shaker.workflow.panel.model;

import java.util.List;

import com.datasphere.engine.projects.model.Project;
import com.datasphere.engine.shaker.processor.instance.model.ComponentInstance;
import com.datasphere.engine.shaker.processor.instance.model.ComponentInstanceRelation;

public class PanelWithAll extends com.datasphere.engine.shaker.workflow.panel.model.Panel {
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private String status;

	List<Project> projects;
	
	List<com.datasphere.engine.shaker.workflow.panel.model.Panel> panels;
	
	List<ComponentInstance> componentInstances;
	
	List<ComponentInstanceRelation> componentInstanceRelations;
	
	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public List<com.datasphere.engine.shaker.workflow.panel.model.Panel> getPanels() {
		return panels;
	}

	public void setPanels(List<com.datasphere.engine.shaker.workflow.panel.model.Panel> panels) {
		this.panels = panels;
	}

	public List<ComponentInstance> getComponentInstances() {
		return componentInstances;
	}

	public void setComponentInstances(List<ComponentInstance> componentInstances) {
		this.componentInstances = componentInstances;
	}

	public List<ComponentInstanceRelation> getComponentInstanceRelations() {
		return componentInstanceRelations;
	}

	public void setComponentInstanceRelations(List<ComponentInstanceRelation> componentInstanceRelations) {
		this.componentInstanceRelations = componentInstanceRelations;
	}
	
	public static PanelWithAll wrap(Panel p) {
		PanelWithAll t = new PanelWithAll();
		t.id = p.getId();
		t.createTime = p.getCreateTime();
		t.lastModified = p.getLastModified();
		t.panelName = p.getPanelName();
		t.panelDesc = p.getPanelDesc();
		t.projectId = p.getProjectId();
		t.lastProcessInstanceId = p.lastProcessInstanceId;
		return t;
	}
}
