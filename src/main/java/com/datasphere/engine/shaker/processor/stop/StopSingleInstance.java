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

package com.datasphere.engine.shaker.processor.stop;

import java.util.concurrent.ConcurrentHashMap;

public class StopSingleInstance {
	private static StopSingleInstance stopSingleInstance = new StopSingleInstance();

	private StopSingleInstance() {
	}

	public static StopSingleInstance getInstances() {
		return stopSingleInstance;
	}



	private static ConcurrentHashMap<String, Boolean> cache = new ConcurrentHashMap<String, Boolean>();

	public void stop(String panelId) {
		cache.put(panelId, true);
	}

	public void start(String panelId) {
		cache.put(panelId, false);
	}

	public Boolean get(String panelId) {
		return cache.get(panelId) == null ? false : cache.get(panelId);
	}
	
	public void remove(String panelId){
		cache.remove(panelId);
	}
}
