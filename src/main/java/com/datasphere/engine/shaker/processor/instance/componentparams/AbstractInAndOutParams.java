package com.datasphere.engine.shaker.processor.instance.componentparams;

import com.alibaba.fastjson.annotation.JSONField;

public abstract class AbstractInAndOutParams {
	private String code;
	@JSONField(name = "dataKey")
	private String dataKey;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDataKey() {
		return dataKey;
	}

	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}

}
