package com.datasphere.server.notifications.websocket;

import com.datasphere.server.notifications.websocket.WSPacket;

public interface WSPacketHandler
{
    void beforeSend(final com.datasphere.server.notifications.websocket.WSPacket p0);
    
    void afterSend(final WSPacket p0);
}
