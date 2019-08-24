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

package com.datasphere.engine.manager.resource.model;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PreRemove;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datasphere.engine.manager.resource.SystemKeys;
import com.datasphere.engine.manager.resource.event.ResourceEventHandler;

@Component
public class ResourceListener {
	/*
	 * Callbacks
	 */
	@PostPersist
	private void postPersist(final Resource entity) {
		service.notifyAction(entity.getScopeId(), entity.getUserId(), entity.getType(), entity.getId(),
				SystemKeys.ACTION_CREATE);
	}

	@PostUpdate
	private void postUpdate(final Resource entity) {
		service.notifyAction(entity.getScopeId(), entity.getUserId(), entity.getType(), entity.getId(),
				SystemKeys.ACTION_UPDATE);
	}

	@PreRemove
	private void preRemove(final Resource entity) {
	    //disabled due to async dispatch
//		service.notifyAction(entity.getScopeId(), entity.getUserId(), entity.getType(), entity.getId(),
//				SystemKeys.ACTION_DELETE);
	}

	/*
	 * Service
	 */
	private ResourceEventHandler service;

	@Autowired
	public ResourceListener(ResourceEventHandler rs) {
		service = rs;
	}
}
