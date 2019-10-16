/*
 * Copyright 2019, Huahuidata, Inc.
 * DataSphere is licensed under the Mulan PSL v1.
 * You can use this software according to the terms and conditions of the Mulan PSL v1.
 * You may obtain a copy of Mulan PSL v1 at:
 * http://license.coscl.org.cn/MulanPSL
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v1 for more details.
 */

package com.datasphere.engine.manager.resource.provider.webhdfs.model;

import java.util.List;


public class WebHDFSDataSourceInfo {

    // 主键
    private String id;

    // 数据源类型
    private String dataDSType;

    // 数据源业务类型
    private String businessType;

    // 主机IP地址
    private String hostIP;
    // 端口
    private String hostPort;
    // 路径
    private String descriptor;
    // 用户
    private String userName;
    // 密码
    private String password;

    // 数据源类型
    private Integer dataType;

    //数据源表信息
    private List<WebHDFSTableInfo> dataSources;

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

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public List<WebHDFSTableInfo> getDataSources() {
        return dataSources;
    }

    public void setDataSources(List<WebHDFSTableInfo> dataSources) {
        this.dataSources = dataSources;
    }
}
