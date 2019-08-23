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

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import com.datasphere.datalayer.resource.manager.common.ConsumerException;
import com.datasphere.datalayer.resource.manager.common.NoSuchConsumerException;

public interface ConsumerBuilder {

	public String getId();

	public String getType();

	public boolean isAvailable();

	public Consumer build() throws NoSuchConsumerException, ConsumerException;

	public Consumer build(Map<String, Serializable> properties) throws NoSuchConsumerException, ConsumerException;

	public Consumer build(Registration reg) throws NoSuchConsumerException, ConsumerException;

	/*
	 * Properties
	 */
	public abstract Set<String> listProperties();
}
