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

package com.datasphere.engine.manager.resource.consumer.log;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datasphere.engine.manager.resource.SystemKeys;
import com.datasphere.engine.manager.resource.common.ConsumerException;
import com.datasphere.engine.manager.resource.model.Consumer;
import com.datasphere.engine.manager.resource.model.Registration;
import com.datasphere.engine.manager.resource.model.Resource;

public class LogNoSqlConsumer extends Consumer {

    private final static Logger _log = LoggerFactory.getLogger(LogNoSqlConsumer.class);

    public static final String TYPE = SystemKeys.TYPE_NOSQL;
    public static final String ID = "logNoSql";

    private int STATUS;

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getUrl() {
        return "";
    }

    /*
     * Init method - POST constructor since spring injects properties *after
     * creation*
     */
    @PostConstruct
    public void init() {
        _log.debug("init called");
        STATUS = SystemKeys.STATUS_READY;
    }

    @Override
    public int getStatus() {
        return STATUS;
    }

    @Override
    public void addResource(String scopeId, String userId, Resource resource) throws ConsumerException {
        _log.debug("add resource " + resource.toString());

    }

    @Override
    public void updateResource(String scopeId, String userId, Resource resource) throws ConsumerException {
        _log.debug("update resource " + resource.toString());

    }

    @Override
    public void deleteResource(String scopeId, String userId, Resource resource) throws ConsumerException {
        _log.debug("delete resource " + resource.toString());

    }

    @Override
    public void checkResource(String scopeId, String userId, Resource resource) throws ConsumerException {
        _log.debug("check resource " + resource.toString());

    }

    @Override
    public Registration getRegistration() {
        // not supported
        return null;
    }

}
