package com.datasphere.engine.shaker.processor.instance.componentparams;

import com.alibaba.fastjson.annotation.JSONField;

public class BaseComponentParams implements AbstractMethodParams{
	
	/**
	 * 组件编码
	 */
	private String code;
	/**
	 * 组件参数
	 */
	@JSONField(name = "algmAttrs")
	private Object algmAttrs;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Object getAlgmAttrs() {
		return algmAttrs;
	}
	public void setAlgmAttrs(Object algmAttrs) {
		this.algmAttrs = algmAttrs;
	}
	
}
