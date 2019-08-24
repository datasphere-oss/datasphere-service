package com.datasphere.engine.common.named;

import java.util.List;

public class NameGenerator {
	public String generate(String suffix, List<String> filter) {
		int index = 1;
		while(filter.contains(suffix + index)) {
			index++;
		}
		return suffix + index;
	}
}
