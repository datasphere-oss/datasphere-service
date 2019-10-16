package com.datasphere.engine.manager.resource.provider.hive.model;

public class HiveConnectionInfo {
  // 名称信息
  private String name;
  // 业务类型
  private String businessType;
  // 类型名称
  private String typeName;
  private String hostIP;
  private String hostPort;
  private String userName;
  private String userPassword;
  // 数据库名称 或 Oracle的SID
  private String databaseName; 
  private String tableName;
  // 列信息
  private String columns;
  // 行信息
  private String rows;
  // URL信息
  private String jdbcURL;
  private String query;


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBusinessType() {
    return businessType;
  }

  public void setBusinessType(String businessType) {
    this.businessType = businessType;
  }

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

  public String getColumns() {
    return columns;
  }

  public void setColumns(String columns) {
    this.columns = columns;
  }

  public String getRows() {
    return rows;
  }

  public void setRows(String rows) {
    this.rows = rows;
  }

  public String getJdbcURL() {
    return jdbcURL;
  }

  public void setJdbcURL(String jdbcURL) {
    this.jdbcURL = jdbcURL;
  }

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }
}
