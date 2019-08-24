package com.datasphere.engine.manager.resource.provider.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DBQuery {

	private String id;

	private String query;

	private String classification;

	private String databaseName;
	
	private String schemaName;
	
	private String tableName;
	
	@JsonIgnore
	private int page;
	
	@JsonIgnore
	private int rows;

	@JsonIgnore
	private String searchName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	@Override
	public String toString() {
		return "DBQuery [databaseName=" + databaseName + ", schemaName=" + schemaName + ", tableName=" + tableName
				+ ", page=" + page + ", rows=" + rows + ", searchName=" + searchName + "]";
	}
	
}
