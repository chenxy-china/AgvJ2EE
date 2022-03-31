package com.ws.cxy;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataServer {
    Logger logger = LoggerFactory.getLogger(DataServer.class);
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    
    @OnOpen
    public void onOpen(Session session){
        logger.info("new ws connection " + session.getId() +  this.getClass().getName());
        this.session = session;
        ServerManager.add(this);
    }
    
    @OnClose
    public void onClose(){
        logger.info("close ws connection " +  this.getClass().getName());
        ServerManager.remove(this);
    }
    
    public void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
    }
    
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("message from client:" + message);
    }
 
    @OnError
    public void onError(Session session, Throwable error){
        logger.info("error");
        error.printStackTrace();
    }
}
