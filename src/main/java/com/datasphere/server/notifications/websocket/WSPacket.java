package com.datasphere.server.notifications.websocket;

import com.fasterxml.jackson.annotation.*;

public class WSPacket
{
    String type;
    Object data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String userId;
    
    public WSPacket() {
    }
    
    public WSPacket(final String type) {
        this.type = type;
    }
    
    public WSPacket(final String type, final Object data) {
        this.type = type;
        this.data = data;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public Object getData() {
        return this.data;
    }
    
    public void setData(final Object data) {
        this.data = data;
    }
    
    public String getUserId() {
        return this.userId;
    }
    
    public void setUserId(final String userId) {
        this.userId = userId;
    }
}
