package com.datasphere.engine.shaker.workflow.panel.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

public class ComponentDefinitionPanel {
	
	@JsonProperty(access=Access.WRITE_ONLY)
	String componentDefinitionId;
	
	String panelId;
	
	String panelName;
	
	String projectId;
	
	String projectName;
	
	@JsonInclude(value=Include.NON_NULL)
	Long count;

	public String getComponentDefinitionId() {
		return componentDefinitionId;
	}

	public void setComponentDefinitionId(String componentDefinitionId) {
		this.componentDefinitionId = componentDefinitionId;
	}

	public String getPanelId() {
		return panelId;
	}

	public void setPanelId(String panelId) {
		this.panelId = panelId;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public String getPanelName() {
		return panelName;
	}

	public void setPanelName(String panelName) {
		this.panelName = panelName;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
}
