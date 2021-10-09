package com.ws.cxy;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

public class DataServer {
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    
    @OnOpen
    public void onOpen(Session session){
        System.out.println("client ws connect");  
        this.session = session;
        ServerManager.add(this);    
    }
    
    @OnClose
    public void onClose(){
        System.out.println("client ws disconnect");
        ServerManager.remove(this); 
    }
    
    public void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
    }
    
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
    }
 
    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("发生错误");
        error.printStackTrace();
    }
}
