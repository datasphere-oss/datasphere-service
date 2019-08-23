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

package com.datasphere.datalayer.resource.manager.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.datasphere.datalayer.resource.manager.SystemKeys;

@Component
@ConfigurationProperties(prefix = "consumers.static")
public class ConsumerConfiguration {

    private List<String> sql = new ArrayList<String>();
    private List<String> nosql = new ArrayList<String>();
    private List<String> file = new ArrayList<String>();
    private List<String> object = new ArrayList<String>();
    private List<String> odbc = new ArrayList<String>();

    public List<String> getSql() {
        return sql;
    }

    public void setSql(List<String> sql) {
        this.sql = sql;
    }

    public List<String> getNosql() {
        return nosql;
    }

    public void setNosql(List<String> nosql) {
        this.nosql = nosql;
    }

    public List<String> getFile() {
        return file;
    }

    public void setFile(List<String> file) {
        this.file = file;
    }

    public List<String> getObject() {
        return object;
    }

    public void setObject(List<String> object) {
        this.object = object;
    }

    public List<String> getOdbc() {
        return odbc;
    }

    public void setOdbc(List<String> odbc) {
        this.odbc = odbc;
    }

    public List<String> get(String type) {
        List<String> res = null;
        switch (type) {
        case SystemKeys.TYPE_SQL:
            res = getSql();
            break;
        case SystemKeys.TYPE_NOSQL:
            res = getNosql();
            break;
        case SystemKeys.TYPE_FILE:
            res = getFile();
            break;
        case SystemKeys.TYPE_OBJECT:
            res = getObject();
            break;
        case SystemKeys.TYPE_ODBC:
            res = getOdbc();
            break;
        }

        return res;
    }

}
