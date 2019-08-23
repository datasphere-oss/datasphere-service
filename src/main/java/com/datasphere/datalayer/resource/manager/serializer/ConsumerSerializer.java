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

import com.datasphere.datalayer.resource.manager.dto.ConsumerDTO;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class ConsumerSerializer extends StdSerializer<ConsumerDTO> {

    private static final long serialVersionUID = 2765900697671425958L;

    public ConsumerSerializer() {
        this(null);
    }

    public ConsumerSerializer(Class<ConsumerDTO> t) {
        super(t);
    }

    @Override
    public void serialize(
            ConsumerDTO consumer, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        jgen.writeStartObject();
        jgen.writeNumberField("id", consumer.getId());

        jgen.writeStringField("type", consumer.getType());
        jgen.writeStringField("consumer", consumer.getConsumer());

        jgen.writeStringField("scopeId", consumer.getScopeId());
        jgen.writeStringField("userId", consumer.getUserId());

        jgen.writeStringField("url", consumer.getUrl());
        jgen.writeStringField("status", consumer.getStatus());

        // write properties json
        jgen.writeFieldName("properties");
        jgen.writeRawValue(consumer.getProperties());

        // write tags as json array
        jgen.writeFieldName("tags");
        jgen.writeStartArray();
        for (String tag : consumer.tags) {
            jgen.writeString(tag);
        }
        jgen.writeEndArray();

        // close
        jgen.writeEndObject();
    }
}
