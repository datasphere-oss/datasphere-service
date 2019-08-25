package com.datasphere.engine.manager.resource.provider.db.model;

public class DBTableField {

	private String name;
	private String value;
	private int type;
	private String businessDataType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getBusinessDataType() {
		return businessDataType;
	}

	public void setBusinessDataType(String businessDataType) {
		this.businessDataType = businessDataType;
	}

	@Override
	public String toString() {
		return "DBTableField [name=" + name + ", value=" + value + ", type=" + type + ", businessDataType="
				+ businessDataType + "]";
	}
}
