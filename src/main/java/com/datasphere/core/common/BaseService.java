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

package com.datasphere.core.common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datasphere.engine.datasource.MyBatisSqlSessionFactoryService;

@Service
public class BaseService {
    @Autowired
    protected MyBatisSqlSessionFactoryService sqlSessionFactoryService;

    {
        this.dataServiceOnPrestoServerRootUrl = PropertyUtil.getProperty("dataservice-on-presto.server.root.url");
        this.OpenAPIServerRootUrl = PropertyUtil.getProperty("open.api.server.root.url");

        this.daasServerAPIV2RootUrl = PropertyUtil.getProperty("daas.server.apiv2.root.url");
        this.daasServerAPIV3RootUrl = PropertyUtil.getProperty("daas.server.apiv3.root.url");
        this.daasClassName = PropertyUtil.getProperty("daas.class.name");
        this.daasJdbcUrl = PropertyUtil.getProperty("daas.jdbc.url");
        this.daasUsername = PropertyUtil.getProperty("daas.username");
        this.daasPassword = PropertyUtil.getProperty("daas.password");

        this.userLoginToken = PropertyUtil.getProperty("user.login.token");
        this.userInfoKey = PropertyUtil.getProperty("user.info.key");

        this.projectName = PropertyUtil.getProperty("sso.project.app.code");
        this.permissionServer = PropertyUtil.getProperty("sso.permission.server");
        this.permissionServerLoginUrl = PropertyUtil.getProperty("sso.permission.server.login.url");

        this.getUserLoginInfoByTokenApi = PropertyUtil.getProperty("sso.get.user.login.info");
        this.getUIPermissions = PropertyUtil.getProperty("sso.get.ui.permissions");

        this.getUserDepartmentInfo = PropertyUtil.getProperty("sso.get.user.department.info");
        this.getUserDepartmentIds = PropertyUtil.getProperty("sso.get.user.department.ids");

        this.redisHost = PropertyUtil.getProperty("redis.host");
        this.redisPort = Integer.parseInt(PropertyUtil.getProperty("redis.port"));
    }

    protected String dataServiceOnPrestoServerRootUrl;
    protected String OpenAPIServerRootUrl;

    // daas
    protected String daasServerAPIV2RootUrl;
    protected String daasServerAPIV3RootUrl;
    protected String daasClassName;
    protected String daasJdbcUrl;
    protected String daasUsername;
    protected String daasPassword;

    // redis
    protected String redisHost;
    protected int redisPort;

    protected  String userLoginToken;
    protected  String userInfoKey;


    protected String projectName;
    protected String permissionServer;
    protected String permissionServerLoginUrl;

    protected String getUIPermissions;
    protected String getUserLoginInfoByTokenApi;

    protected String getUserDepartmentIds;
    protected String getUserDepartmentInfo;


}
