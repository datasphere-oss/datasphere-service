package com.datasphere.engine.manager.resource.provider.database.entity;

import java.util.List;

public class DBTableDataList {
	private List<String> unsupportColumns;
	private List<String> columns;
	private List<String> typeList;
	private List<List<String>> dataList;
	public List<String> getUnsupportColumns() {
		return unsupportColumns;
	}
	public void setUnsupportColumns(List<String> unsupportColumns) {
		this.unsupportColumns = unsupportColumns;
	}
	public List<String> getColumns() {
		return columns;
	}
	public void setColumns(List<String> columns) {
		this.columns = columns;
	}
	public List<List<String>> getDataList() {
		return dataList;
	}
	public void setDataList(List<List<String>> dataList) {
		this.dataList = dataList;
	}
	public List<String> getTypeList() {
		return typeList;
	}
	public void setTypeList(List<String> typeList) {
		this.typeList = typeList;
	}
}
