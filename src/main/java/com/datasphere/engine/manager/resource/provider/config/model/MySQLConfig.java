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

package com.datasphere.engine.manager.resource.provider.config.model;

public class MySQLConfig {

    private String username;
    private String password;
    private String hostname;
    private String port;
    private String[] authenticationType;
    private Integer fetchSize;
    private String database;
    private Boolean showOnlyConnectiondatabase;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String[] getAuthenticationType() {
        return authenticationType;
    }

    public void setAuthenticationType(String[] authenticationType) {
        this.authenticationType = authenticationType;
    }

    public Integer getFetchSize() {
        return fetchSize;
    }

    public void setFetchSize(Integer fetchSize) {
        this.fetchSize = fetchSize;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public Boolean getShowOnlyConnectiondatabase() {
        return showOnlyConnectiondatabase;
    }

    public void setShowOnlyConnectiondatabase(Boolean showOnlyConnectiondatabase) {
        this.showOnlyConnectiondatabase = showOnlyConnectiondatabase;
    }
}
