package com.datasphere.engine.manager.resource.provider.database.model;

public class DBTableInfo {
	private String name;
	private int rows;
	private int columns;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getColumns() {
		return columns;
	}
	public void setColumns(int columns) {
		this.columns = columns;
	}
	
	@Override
	public String toString() {
		return "DBTableInfo [name=" + name + ", rows=" + rows + ", columns=" + columns + "]";
	}
}
