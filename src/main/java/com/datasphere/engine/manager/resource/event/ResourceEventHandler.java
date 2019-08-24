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

package com.datasphere.engine.manager.resource.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.datasphere.engine.manager.resource.model.ResourceEvent;

@Component
public class ResourceEventHandler {
	private final static Logger _log = LoggerFactory.getLogger(ResourceEventHandler.class);

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	public void notifyAction(String scopeId, String userId, String type, long id, String action) {

		_log.debug("create message for " + type + " with payload " + action + ":" + String.valueOf(id));

		// create message
		ResourceEvent event = new ResourceEvent(this, scopeId, userId, type, id, action);
		applicationEventPublisher.publishEvent(event);

	}
}
