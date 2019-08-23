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

package com.datasphere.datalayer.resource.manager.model;

import org.springframework.context.ApplicationEvent;

public class ResourceEvent extends ApplicationEvent {

	private static final long serialVersionUID = 4857585749410010866L;

	// resource id
	private long id;
	// action performed on resource
	private String action;
	// resource type for dispatch
	private String type;
	// userId of the OWNER, not of the user performing the request
	private String userId;
	// scope as defined in resource
	private String scopeId;

	public ResourceEvent(Object source, String scopeId, String userId, String type, long id, String action) {
		super(source);

		this.scopeId = scopeId;
		this.userId = userId;
		this.type = type;
		this.id = id;
		this.action = action;

	}

	public long getId() {
		return id;
	}

	public String getAction() {
		return action;
	}

	public String getType() {
		return type;
	}

	public String getUserId() {
		return userId;
	}

	public String getScopeId() {
		return scopeId;
	}
}
