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

import com.datasphere.engine.manager.resource.common.ConsumerException;

public abstract class Consumer {

	/*
	 * Consumer
	 */
	public abstract String getId();

	public abstract String getType();
	
	public abstract String getUrl();

	public abstract int getStatus();

	public abstract Registration getRegistration();

	/*
	 * Resources - 添加、检查、更新、删除资源
	 */
	public abstract void addResource(String scopeId, String userId, Resource resource) throws ConsumerException;

	public abstract void checkResource(String scopeId, String userId, Resource resource) throws ConsumerException;

	public abstract void updateResource(String scopeId, String userId, Resource resource) throws ConsumerException;

	public abstract void deleteResource(String scopeId, String userId, Resource resource) throws ConsumerException;

}