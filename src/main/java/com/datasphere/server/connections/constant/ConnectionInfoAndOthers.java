package com.datasphere.server.connections.constant;

public class ConnectionInfoAndOthers extends ConnectionInfo {
    private String datas;
    private String batchSize;
    private String schema; //postgresql schema

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
