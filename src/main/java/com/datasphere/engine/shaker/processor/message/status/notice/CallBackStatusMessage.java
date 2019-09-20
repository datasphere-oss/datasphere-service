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

package com.datasphere.engine.shaker.processor.message.status.notice;

import java.util.concurrent.ConcurrentHashMap;

public class CallBackStatusMessage {
	private static CallBackStatusMessage message = new CallBackStatusMessage();

	private CallBackStatusMessage() {
	}

	public static CallBackStatusMessage getInstance() {
		return message;
	}

	private static ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<String, Object>();

	public Object get(String key) {
		return cache.get(key);
	}

	public void add(String key, Object obj) {
		cache.put(key, obj);
	}

	public void remove(String key) {
		if (cache.containsKey(key))
			cache.remove(key);
	}

	

}
