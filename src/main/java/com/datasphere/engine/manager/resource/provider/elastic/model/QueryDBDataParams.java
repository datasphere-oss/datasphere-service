package com.datasphere.engine.manager.resource.provider.elastic.model;

public class QueryDBDataParams {
    private String id;
	private String dssId;
    private String dssName;
    private String databaseName;
    private String tableName;
    private String sql;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

  
    public String getDssId() {
		return dssId;
	}

	public void setDssId(String dssId) {
		this.dssId = dssId;
	}

	public String getDssName() {
		return dssName;
	}

	public void setDssName(String dssName) {
		this.dssName = dssName;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
 
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
