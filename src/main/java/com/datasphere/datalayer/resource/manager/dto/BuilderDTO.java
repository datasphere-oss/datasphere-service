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

import java.util.Set;

import com.datasphere.datalayer.resource.manager.model.ConsumerBuilder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Builder")
public class BuilderDTO {

    @ApiModelProperty(notes = "Builder id", example = "dremio")
    public String id;

    @ApiModelProperty(notes = "Builder resource type", example = "sql")
    public String type;

    @ApiModelProperty(notes = "Builder consumer name", example = "dremio")
    public String consumer;

    @ApiModelProperty(notes = "Consumer properties map - class specific", example = "{}")
    public String[] properties;

    public BuilderDTO() {

    }

    public BuilderDTO(String type, String consumer) {
        super();
        this.id = consumer;
        this.type = type;
        this.consumer = consumer;
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

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public String[] getProperties() {
        return properties;
    }

    public void setProperties(String[] properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "BuilderDTO [type=" + type + ", consumer=" + consumer + "]";
    }

    public static BuilderDTO fromBuilder(ConsumerBuilder cb) {

        BuilderDTO dto = new BuilderDTO(cb.getType(), cb.getId());
        Set<String> prop = cb.listProperties();

        dto.properties = prop.toArray(new String[0]);

        return dto;

    }
}
