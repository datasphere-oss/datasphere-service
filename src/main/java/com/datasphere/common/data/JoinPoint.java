package com.datasphere.common.data;

import java.io.Serializable;

public class JoinPoint implements Serializable{
	
	private String code;
	private String dataKey;
	private String[] attrNames;
	
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
	public String[] getAttrNames() {
		return attrNames;
	}
	public void setAttrNames(String[] attrNames) {
		this.attrNames = attrNames;
	}

}
