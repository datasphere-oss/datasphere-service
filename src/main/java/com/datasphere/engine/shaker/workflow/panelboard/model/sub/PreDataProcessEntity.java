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

package com.datasphere.engine.shaker.workflow.panel.model.sub;

import com.alibaba.fastjson.annotation.JSONField;

public class PreDataProcessEntity {
	@JSONField(name = "processId")
	private String componentId;
	@JSONField(name = "programId")
	private String version;
	@JSONField(name = "operates")
	private String operates;
	@JSONField(name = "columns")
	private String columns;
	private String creator;

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getComponentId() {
		return componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getOperates() {
		return operates;
	}

	public void setOperates(String operates) {
		this.operates = operates;
	}

	public String getColumns() {
		return columns;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}

}
