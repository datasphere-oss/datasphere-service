package com.datasphere.server.connections.dao.impl;

import com.datasphere.server.connections.dao.TableNameGenerator;

public class DefaultTableNameGenerator implements TableNameGenerator {
	public static final String PREFIX = "DATASET_";
	
	@Override
	public String generate(String factor) {
		return PREFIX + factor;
	}
}
