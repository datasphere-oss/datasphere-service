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

import com.datasphere.datalayer.resource.manager.dto.ResourceDTO;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class ResourceDeserializer extends StdDeserializer<ResourceDTO> {

    private static final long serialVersionUID = 5583408623910364345L;

    public ResourceDeserializer() {
        this(null);
    }

    public ResourceDeserializer(Class<?> t) {
        super(t);
    }

    @Override
    public ResourceDTO deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        JsonNode node = jp.getCodec().readTree(jp);
        ResourceDTO res = new ResourceDTO();

        if (node.has("id")) {
            res.id = node.get("id").asLong();
        }

        if (node.has("type")) {
            res.type = node.get("type").asText();
        }
        if (node.has("provider")) {
            res.provider = node.get("provider").asText();
        }
        if (node.has("name")) {
            res.name = node.get("name").asText();
        }
        if (node.has("uri")) {
            res.uri = node.get("uri").asText();
        }

        if (node.has("userId")) {
            res.userId = node.get("userId").asText();
        }
        if (node.has("scopeId")) {
            res.scopeId = node.get("scopeId").asText();
        }

        JsonNode properties = node.get("properties");
        if (properties != null) {
            res.setProperties(properties.toString());
        }

        if (node.has("managed")) {
            res.managed = node.get("managed").asBoolean();
        }
        if (node.has("subscribed")) {
            res.subscribed = node.get("subscribed").asBoolean();
        }

        JsonNode tags = node.get("tags");
        List<String> list = new ArrayList<>();
        if (tags != null && tags.isArray()) {
            for (JsonNode tag : tags) {
                list.add(tag.asText());
            }
        }

        res.tags = list.toArray(new String[0]);

        return res;
    }

}
