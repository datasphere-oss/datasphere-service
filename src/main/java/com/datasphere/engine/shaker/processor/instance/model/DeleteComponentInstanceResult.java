package com.datasphere.engine.shaker.processor.instance.model;

import java.util.List;

/**
 * 结果集
 */
public class DeleteComponentInstanceResult {
	
	String componentInstanceId;
	
	List<String> componentInstanceRelationIds;

	public String getComponentInstanceId() {
		return componentInstanceId;
	}

	public void setComponentInstanceId(String componentInstanceId) {
		this.componentInstanceId = componentInstanceId;
	}

	public List<String> getComponentInstanceRelationIds() {
		return componentInstanceRelationIds;
	}

	public void setComponentInstanceRelationIds(List<String> componentInstanceRelationIds) {
		this.componentInstanceRelationIds = componentInstanceRelationIds;
	}
}
