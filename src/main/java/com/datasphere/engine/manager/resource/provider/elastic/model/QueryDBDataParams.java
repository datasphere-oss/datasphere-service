package com.datasphere.engine.manager.resource.provider.elastic.model;

public class QueryDBDataParams {
    private String id;
    private String daasId;
    private String daasName;
    private String dataBaseName;
    private String tableName;
    private String sql;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDaasId() {
        return daasId;
    }

    public void setDaasId(String daasId) {
        this.daasId = daasId;
    }

    public String getDaasName() {
        return daasName;
    }

    public void setDaasName(String daasName) {
        this.daasName = daasName;
    }

    public String getDatabaseName() {
        return dataBaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.dataBaseName = databaseName;
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
