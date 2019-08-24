package com.datasphere.server.connections.model;

import com.datasphere.common.dmpbase.data.Column;

public class TableMetaData {
	
	String tableName;
	
	Column[] columns;
	
	String[][] data;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Column[] getColumns() {
		return columns;
	}

	public void setColumns(Column[] columns) {
		this.columns = columns;
	}

	public String[][] getData() {
		return data;
	}

	public void setData(String[][] data) {
		this.data = data;
	}
}
