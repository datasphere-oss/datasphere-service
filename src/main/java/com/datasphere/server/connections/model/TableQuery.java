package com.datasphere.server.connections.model;

public class TableQuery {

	String tabelName;
	
	String[] columnNames;
	
	Pager pager;
	
	boolean rowBase = true;

	public String getTabelName() {
		return tabelName;
	}

	public void setTabelName(String tabelName) {
		this.tabelName = tabelName;
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}

	public boolean isRowBase() {
		return rowBase;
	}

	public void setRowBase(boolean rowBase) {
		this.rowBase = rowBase;
	}
}
