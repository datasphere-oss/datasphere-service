package com.datasphere.engine.manager.resource.provider.hbase.model;

import java.util.List;

public class HbaseDataSourceInfo {

    //主键
    private String id;
    //数据源类型（mysql，oracle...）
    private String dataDSType;
    //数据源业务类型
    private String businessType;
    //数据源ip
    private String pathXML;
//    private String databaseName; // 数据库名称
    //数据源类型(文件系统，数据库...)
    private Integer dataType;
    //具体表信息（一个数据源）
    private List<HbaseTableInfo> dataSources;

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

    public String getPathXML() {
        return pathXML;
    }

    public void setPathXML(String pathXML) {
        this.pathXML = pathXML;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public List<HbaseTableInfo> getDataSources() {
        return dataSources;
    }

    public void setDataSources(List<HbaseTableInfo> dataSources) {
        this.dataSources = dataSources;
    }
}
