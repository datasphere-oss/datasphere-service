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

package com.datasphere.datalayer.resource.manager.model;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import com.datasphere.datalayer.resource.manager.common.DuplicateNameException;
import com.datasphere.datalayer.resource.manager.common.InvalidNameException;
import com.datasphere.datalayer.resource.manager.common.ResourceProviderException;

public abstract class ResourceProvider {

    /*
     * Provider
     */
    public abstract String getId();

    public abstract String getType();

    public abstract int getStatus();

    /*
     * Resources
     */
    public abstract Resource createResource(String scopeId, String userId, String name,
            Map<String, Serializable> properties)
            throws ResourceProviderException, InvalidNameException, DuplicateNameException;

    public abstract void updateResource(Resource resource) throws ResourceProviderException;

    public abstract void deleteResource(Resource resource) throws ResourceProviderException;

    public abstract void checkResource(Resource resource) throws ResourceProviderException;

    /*
     * Properties
     */
    public abstract Set<String> listProperties();

}
