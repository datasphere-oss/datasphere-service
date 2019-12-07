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

package com.datasphere.engine.core.common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datasphere.engine.manager.resource.provider.MyBatisSqlSessionFactoryService;

@Service
public class BaseService {
    @Autowired
    protected MyBatisSqlSessionFactoryService sqlSessionFactoryService;

    {
        this.dataServiceOnPrestoServerRootUrl = PropertyUtil.getProperty("dataservice-on-presto.server.root.url");
        this.OpenAPIServerRootUrl = PropertyUtil.getProperty("open.api.server.root.url");

        this.DSSServerAPIV2RootUrl = PropertyUtil.getProperty("dss.server.apiv2.root.url");
        this.DSSServerAPIV3RootUrl = PropertyUtil.getProperty("dss.server.apiv3.root.url");
        this.DSSClassName = PropertyUtil.getProperty("dss.class.name");
        this.DSSJdbcUrl = PropertyUtil.getProperty("dss.jdbc.url");
        this.DSSUserName = PropertyUtil.getProperty("dss.username");
        this.DSSPassWord = PropertyUtil.getProperty("dss.password");

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
    // TODO 待删除部分
    protected String dataServiceOnPrestoServerRootUrl;
    protected String OpenAPIServerRootUrl;

    // datasphere
    protected String DSSServerAPIV2RootUrl;
    protected String DSSServerAPIV3RootUrl;
    protected String DSSClassName;
    protected String DSSJdbcUrl;
    protected String DSSUserName;
    protected String DSSPassWord;

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
