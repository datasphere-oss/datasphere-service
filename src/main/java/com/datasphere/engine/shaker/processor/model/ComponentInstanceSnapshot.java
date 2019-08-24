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

package com.datasphere.engine.shaker.processor.model;

import com.datasphere.engine.shaker.processor.instance.model.ComponentInstance;

public class ComponentInstanceSnapshot extends ComponentInstance {
	
	private String processId;
	
	private String content;

	public ComponentInstanceSnapshot() {
		
	}
	
	public ComponentInstanceSnapshot(String processId) {
		this.processId = processId;
	}
	
	public ComponentInstanceSnapshot(String processId, String content) {
		this.processId = processId;
		this.content = content;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
