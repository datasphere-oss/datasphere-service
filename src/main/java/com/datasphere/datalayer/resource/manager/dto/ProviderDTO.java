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

package com.datasphere.datalayer.resource.manager.dto;

import java.util.Arrays;
import java.util.Set;

import com.datasphere.datalayer.resource.manager.model.ResourceProvider;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Provider")
public class ProviderDTO {

    @ApiModelProperty(notes = "Provider id", example = "postgresSql")
    public String id;

    @ApiModelProperty(notes = "Provider resource type", example = "sql")
    public String type;

    @ApiModelProperty(notes = "Provider name", example = "postgres")
    public String provider;

    @ApiModelProperty(notes = "Provider properties map - class specific", example = "{}")
    public String[] properties;

    public ProviderDTO() {
    }

    public ProviderDTO(String type, String provider) {
        super();
        this.id = provider;
        this.type = type;
        this.provider = provider;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String[] getProperties() {
        return properties;
    }

    public void setProperties(String[] properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "ProviderDTO [type=" + type + ", provider=" + provider + ", properties=" + Arrays.toString(properties)
                + "]";
    }

    public static ProviderDTO fromProvider(ResourceProvider p) {

        ProviderDTO dto = new ProviderDTO(p.getType(), p.getId());
        Set<String> prop = p.listProperties();

        dto.properties = prop.toArray(new String[0]);

        return dto;

    }

}
