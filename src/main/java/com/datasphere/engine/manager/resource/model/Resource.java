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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@EntityListeners({ AuditingEntityListener.class, ResourceListener.class })
//@JsonSerialize(using = ResourceSerializer.class)
//@JsonDeserialize(using = ResourceDeserializer.class)
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String type;
    private String provider;
    private String name;
    
    @Length(max = 500)
    @Column
    private String uri;

    private String userId;
    // example scope=tenant/project/user
    private String scopeId;
    private String properties;

    // 由资源管理器托管
    // flag if managed by resourcemanager
    private boolean managed;

    // flag if updates are propagated
    private boolean subscribed;

    // tags - use a converter to persist as single string field
    @Convert(converter = StringListConverter.class)
    private List<String> tags;

    /*
     * Audit
     */
    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date createdDate;

    @Column(name = "modified_date")
    @LastModifiedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Column(name = "created_by")
    @CreatedBy
    protected String createdBy;

    @Column(name = "modified_by")
    @LastModifiedBy
    protected String lastModifiedBy;

    public Resource() {
        super();
        this.managed = true;
        this.subscribed = true;
        this.tags = new ArrayList<>();
    }

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

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public boolean isManaged() {
        return managed;
    }

    public void setManaged(boolean managed) {
        this.managed = managed;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Resource [id=" + id + ", type=" + type + ", provider=" + provider + ", name=" + name + ", uri=" + uri
                + ", userId=" + userId + ", scopeId=" + scopeId + ", properties=" + properties + ", managed=" + managed
                + ", subscribed=" + subscribed + ", tags=" + tags + ", createdDate=" + createdDate + ", modifiedDate="
                + modifiedDate + ", createdBy=" + createdBy + ", lastModifiedBy=" + lastModifiedBy + ", map=" + map
                + "]";
    }

    @Transient
    @JsonIgnore
    private Map<String, Serializable> map;

    @JsonIgnore
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
            JSONObject json = Resource.jsonFromMap(map);
            // serialize to string
            properties = json.toString();
        } else {
            properties = "{}";
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

    public static Resource clone(Resource source) {
        Resource res = new Resource();
        res.id = source.id;
        res.type = source.type;
        res.provider = source.provider;
        res.name = source.name;
        res.uri = source.uri;

        res.userId = source.userId;
        res.scopeId = source.scopeId;

        res.properties = source.properties;
        res.managed = source.managed;
        res.subscribed = source.subscribed;

        res.tags.addAll(source.getTags());

        res.createdDate = source.createdDate;
        res.modifiedDate = source.modifiedDate;
        res.createdBy = source.createdBy;
        res.lastModifiedBy = source.lastModifiedBy;

        return res;
    }

}
