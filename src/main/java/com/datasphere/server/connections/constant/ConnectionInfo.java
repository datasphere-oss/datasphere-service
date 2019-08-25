package com.datasphere.server.connections.constant;

public class ConnectionInfo {
	private String typeName;
	private String hostIP;
	private String hostPort;
	private String userName;
	private String userPassword;
	private String databaseName;
	private String tableName;
	private String datas;
	private String batchSize;
	private String schema;

    public String getTypeName() {
    return typeName;
    }

    public void setTypeName(String typeName) {
    this.typeName = typeName;
    }

    public String getHostIP() {
    return hostIP;
    }

    public void setHostIP(String hostIP) {
    this.hostIP = hostIP;
    }

    public String getHostPort() {
    return hostPort;
    }

    public void setHostPort(String hostPort) {
    this.hostPort = hostPort;
    }

    public String getUserName() {
    return userName;
    }

    public void setUserName(String userName) {
    this.userName = userName;
    }

    public String getUserPassword() {
    return userPassword;
    }

    public void setUserPassword(String userPassword) {
    this.userPassword = userPassword;
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
    
    public String getDatas() {
        return datas;
    }

    public void setDatas(String datas) {
        this.datas = datas;
    }

    public String getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(String batchSize) {
        this.batchSize = batchSize;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
}

