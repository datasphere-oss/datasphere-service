package com.datasphere.server.connections.utils;

public class Assert {
	
	public static void isTrue(Boolean b) {
		isTrue(b, "Illegal arguments!");
	}
	
	public static void isTrue(Boolean b, String msg) {
		if(b == null || !b) {
			throw new RuntimeException(msg);
		}
	}
}
