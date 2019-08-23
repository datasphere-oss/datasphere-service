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

package com.datasphere.datalayer.resource.manager.serializer;

import java.io.IOException;

import com.datasphere.datalayer.resource.manager.dto.ResourceDTO;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ResourceSerializer extends StdSerializer<ResourceDTO> {

    private static final long serialVersionUID = 8183246348939784272L;

    public ResourceSerializer() {
        this(null);
    }

    public ResourceSerializer(Class<ResourceDTO> t) {
        super(t);
    }

    @Override
    public void serialize(
            ResourceDTO resource, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        jgen.writeStartObject();
        jgen.writeNumberField("id", resource.getId());

        jgen.writeStringField("type", resource.getType());
        jgen.writeStringField("provider", resource.getProvider());
        jgen.writeStringField("name", resource.getName());
        jgen.writeStringField("uri", resource.getUri());

        jgen.writeStringField("userId", resource.getUserId());
        jgen.writeStringField("scopeId", resource.getScopeId());

        // write properties json
        jgen.writeFieldName("properties");
        jgen.writeRawValue(resource.getProperties());

        jgen.writeBooleanField("managed", resource.isManaged());
        jgen.writeBooleanField("subscribed", resource.isSubscribed());

        // write tags as json array
        jgen.writeFieldName("tags");
        jgen.writeStartArray();
        for (String tag : resource.tags) {
            jgen.writeString(tag);
        }
        jgen.writeEndArray();

        // close
        jgen.writeEndObject();
    }
}
