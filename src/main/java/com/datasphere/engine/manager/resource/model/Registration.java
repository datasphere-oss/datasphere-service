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

package com.datasphere.engine.manager.resource.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String type;
    private String consumer;

    private String userId;
    private String scopeId;

    private String properties;

    // tags - use a converter to persist as single string field
    @Convert(converter = StringListConverter.class)
    private List<String> tags;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getScopeId() {
        return scopeId;
    }

    public void setScopeId(String scopeId) {
        this.scopeId = scopeId;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Registration [id=" + id + ", type=" + type + ", consumer=" + consumer + ", userId=" + userId
                + ", scopeId=" + scopeId + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Registration other = (Registration) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Transient
    @JsonIgnore
    private Map<String, Serializable> map;

    public Map<String, Serializable> getPropertiesMap() {
        if (this.map == null) {
            // read map from properties
            map = Resource.propertiesFromValue(properties);
        }
        return map;
    }

    public void setPropertiesMap(Map<String, Serializable> map) {
        this.map = map;
        sync();
    }
    
    @PrePersist
    @PreUpdate
    private void sync() {
        if (map != null) {
            // custom build json from map
            JSONObject json = jsonFromMap(map);
            // serialize to string
            properties = json.toString();
        }
    }

    public static Map<String, Serializable> propertiesFromValue(String value) {
        // read map from string as json
        Map<String, Serializable> map = new HashMap<>();
        JSONObject json = new JSONObject(value);
        // build map from json
        for (String key : json.keySet()) {
            JSONArray arr = json.optJSONArray(key);
            if (arr != null) {
                // value is array of String
                String[] ss = new String[arr.length()];
                for (int i = 0; i < arr.length(); i++) {
                    String s = arr.optString(i);
                    ss[i] = s;
                }

                map.put(key, ss);
            } else {
                // get as String
                String s = json.optString(key);
                map.put(key, s);
            }
        }

        return map;
    }

    public static JSONObject jsonFromMap(Map<String, Serializable> map) {
        // custom build json from map
        JSONObject json = new JSONObject();
        for (String key : map.keySet()) {
            Serializable value = map.get(key);
            // support only String or String[]
            if (value instanceof String) {
                json.put(key, value);
            } else if (value instanceof String[]) {
                JSONArray arr = new JSONArray();
                for (String s : (String[]) value) {
                    arr.put(s);
                }

                json.put(key, arr);
            }
        }

        return json;
    }
}
