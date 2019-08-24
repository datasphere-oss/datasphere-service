package com.datasphere.engine.shaker.processor.instance.componentparams;

import com.alibaba.fastjson.annotation.JSONField;

public class InputParams extends AbstractInAndOutParams {
	@JSONField(name = "attrNames")
	private String columns;

	public String getColumns() {
		return columns;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}
}
