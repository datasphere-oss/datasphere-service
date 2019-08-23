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
import java.util.ArrayList;
import java.util.List;

import com.datasphere.datalayer.resource.manager.dto.ConsumerDTO;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class ConsumerDeserializer extends StdDeserializer<ConsumerDTO> {

    private static final long serialVersionUID = 7325201711034533646L;

    public ConsumerDeserializer() {
        this(null);
    }

    public ConsumerDeserializer(Class<?> t) {
        super(t);
    }

    @Override
    public ConsumerDTO deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        JsonNode node = jp.getCodec().readTree(jp);
        ConsumerDTO dto = new ConsumerDTO();

        if (node.has("id")) {
            dto.id = node.get("id").asLong();
        }
        if (node.has("scopeId")) {
            dto.scopeId = node.get("scopeId").asText();
        }
        if (node.has("userId")) {
            dto.userId = node.get("userId").asText();
        }

        if (node.has("type")) {
            dto.type = node.get("type").asText();
        }
        if (node.has("consumer")) {
            dto.consumer = node.get("consumer").asText();
        }

        JsonNode properties = node.get("properties");
        if (properties != null) {
            dto.setProperties(properties.toString());
        }

        JsonNode tags = node.get("tags");
        List<String> list = new ArrayList<>();
        if (tags != null && tags.isArray()) {
            for (JsonNode tag : tags) {
                list.add(tag.asText());
            }
        }

        dto.tags = list.toArray(new String[0]);
        
        return dto;
    }
}
