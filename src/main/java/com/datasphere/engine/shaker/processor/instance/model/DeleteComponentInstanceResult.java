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
