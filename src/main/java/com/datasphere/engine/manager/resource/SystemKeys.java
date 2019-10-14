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

package com.datasphere.engine.manager.resource;

public class SystemKeys {

    public final static String ENTITY_RESOURCE = "RESOURCE";
    public final static String ENTITY_REGISTRATION = "REGISTRATION";
    public final static String SCOPE = "SCOPE";

    public final static String TYPE_SQL = "sql";
    public final static String TYPE_NOSQL = "nosql";
    public final static String TYPE_OBJECT = "object";
    public final static String TYPE_FILE = "file";
    public final static String TYPE_ODBC = "odbc";

    public final static int STATUS_READY = 0;
    public final static int STATUS_INIT = 1;
    public final static int STATUS_ERROR = 2;
    public final static int STATUS_UNKNOWN = 3;
    public final static int STATUS_ENABLED = -1;
    public final static int STATUS_DISABLED = -2;

    public final static String ACTION_CREATE = "create";
    public final static String ACTION_UPDATE = "update";
    public final static String ACTION_DELETE = "delete";
    public final static String ACTION_CHECK = "check";

    public final static String CONFIG_PROVIDERS = "system.providers";
    public final static String CONFIG_CONSUMERS = "system.consumers";

    public final static String ORDER_ASC = "asc";
    public final static String ORDER_DESC = "desc";
    // 角色管理
    public final static String ROLE_ADMIN = "ROLE_ADMIN";
    public final static String ROLE_RESOURCE_ADMIN = "ROLE_RESOURCE_ADMIN";
    public final static String ROLE_CONSUMER_ADMIN = "ROLE_CONSUMER_ADMIN";
    public final static String ROLE_USER = "ROLE_USER";
    // 资源管理
    public final static String PERMISSION_RESOURCE_CREATE = "CREATE_RESOURCE";
    public final static String PERMISSION_RESOURCE_UPDATE = "UPDATE_RESOURCE";
    public final static String PERMISSION_RESOURCE_DELETE = "DELETE_RESOURCE";
    public final static String PERMISSION_RESOURCE_CHECK = "CHECK_RESOURCE";
    public final static String PERMISSION_RESOURCE_VIEW = "VIEW_RESOURCE";
    // 消费者管理
    public final static String PERMISSION_CONSUMER_CREATE = "CREATE_CONSUMER";
    public final static String PERMISSION_CONSUMER_UPDATE = "UPDATE_CONSUMER";
    public final static String PERMISSION_CONSUMER_DELETE = "DELETE_CONSUMER";
    public final static String PERMISSION_CONSUMER_VIEW = "VIEW_CONSUMER";

}
