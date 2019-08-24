package com.datasphere.server.connections.utils;

public class StringUtils {

	public static Boolean isBlank(String str) {
		if(str == null || str.trim().length() == 0) {
			return true;
		}
		return false;
	}
}
