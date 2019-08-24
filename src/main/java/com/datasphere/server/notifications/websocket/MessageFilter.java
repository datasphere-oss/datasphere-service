package com.datasphere.server.notifications.websocket;

import com.datasphere.server.notifications.websocket.WSPacket;

public interface MessageFilter
{
    boolean supports(final String p0);
    
    boolean matches(final Object p0, final WSPacket p1);
}
