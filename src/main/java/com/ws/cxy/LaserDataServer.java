package com.ws.cxy;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/ws/getlaserdata")
public class LaserDataServer extends DataServer{
    
}
