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

package com.datasphere.engine.manager.resource.consumer.local;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.datasphere.engine.manager.resource.common.NoSuchConsumerException;
import com.datasphere.engine.manager.resource.model.Consumer;
import com.datasphere.engine.manager.resource.model.ConsumerBuilder;
import com.datasphere.engine.manager.resource.model.Registration;

@Component
public class NullSqlBuilder implements ConsumerBuilder {

	@Value("${consumers.null.enable}")
	private boolean enabled;

	@Value("${consumers.null.properties}")
	private List<String> properties;

	@Override
	public String getType() {
		return NullSqlConsumer.TYPE;
	}

	@Override
	public String getId() {
		return NullSqlConsumer.ID;
	}

	@Override
	public Set<String> listProperties() {
		return new HashSet<String>(properties);
	}

	@Override
	public boolean isAvailable() {
		return enabled;
	}

	@Override
	public Consumer build() throws NoSuchConsumerException {
		if (!enabled) {
			throw new NoSuchConsumerException();
		}

		NullSqlConsumer consumer = new NullSqlConsumer();
		// explicitly call init() since @postconstruct won't work here
		consumer.init();

		return consumer;
	}

	@Override
	public Consumer build(Map<String, Serializable> properties) throws NoSuchConsumerException {
		// properties supported
		NullSqlConsumer consumer = new NullSqlConsumer(properties);
		// explicitly call init() since @postconstruct won't work here
		consumer.init();

		return consumer;
	}

	@Override
	public Consumer build(Registration reg) throws NoSuchConsumerException {
		// properties supported
		NullSqlConsumer consumer = new NullSqlConsumer(reg.getPropertiesMap());
		// explicitly call init() since @postconstruct won't work here
		consumer.init();

		return consumer;
	}

}