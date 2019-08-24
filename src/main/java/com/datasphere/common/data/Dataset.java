package com.datasphere.common.data;

import java.util.Date;

public class Dataset {

	String dataKey;
	Column[] columnsMeta;
	String[][] data;
	String message;
	String model;
	Date createTime;
	Date lastModified;

	public String getDataKey() {
		return dataKey;
	}

	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}

	public Column[] getColumnsMeta() {
		return columnsMeta;
	}

	public void setColumnsMeta(Column[] columnsMeta) {
		this.columnsMeta = columnsMeta;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String[][] getData() {
		return data;
	}

	public void setData(String[][] data) {
		this.data = data;
	}
	
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
}
