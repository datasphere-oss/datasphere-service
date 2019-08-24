package com.datasphere.engine.manager.resource.provider.hive.model;

import java.util.List;

import com.datasphere.engine.datasource.hadoop.hive.HiveTableInfo;

public class HiveDataSourceInfo {

    //主键
    private String id;
    //数据源类型（mysql，oracle...）
    private String dataDSType;
    //数据源业务类型
    private String businessType;
    //数据源ip
    private String hostIP;
    //数据源port
    private String hostPort;
    //数据源用户名
    private String userName;
    //数据源密码
    private String userPassword;
    //JDBC URL
    private String jdbcURL;
    //数据源库名称
    private String databaseName;
    //数据源类型(文件系统，数据库...)
    private Integer dataType;
    //具体表信息（一个数据源）
    private List<HiveTableInfo> dataSources;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDataDSType() {
        return dataDSType;
    }

    public void setDataDSType(String dataDSType) {
        this.dataDSType = dataDSType;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
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

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public List<HiveTableInfo> getDataSources() {
        return dataSources;
    }

    public void setDataSources(List<HiveTableInfo> dataSources) {
        this.dataSources = dataSources;
    }

    public String getJdbcURL() {
        return jdbcURL;
    }

    public void setJdbcURL(String jdbcURL) {
        this.jdbcURL = jdbcURL;
    }
}
