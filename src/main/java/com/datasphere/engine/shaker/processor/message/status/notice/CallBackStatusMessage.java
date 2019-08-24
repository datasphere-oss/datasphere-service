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
