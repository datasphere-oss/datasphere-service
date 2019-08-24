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
