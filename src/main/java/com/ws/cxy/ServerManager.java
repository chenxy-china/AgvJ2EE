package com.ws.cxy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ServerManager {
    private static Collection<LaserDataServer> servers = Collections.synchronizedCollection(new ArrayList<LaserDataServer>());
    
    public static void broadCast(String msg){
        for (LaserDataServer ldServer : servers) {
            try {
                ldServer.sendMessage(msg);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
     
    public static int getTotal(){
        return servers.size();
    }
    public static void add(LaserDataServer server){
        System.out.println("有新连接加入！ 当前总连接数是："+ servers.size());
        servers.add(server);
    }
    public static void remove(LaserDataServer server){
        System.out.println("有连接退出！ 当前总连接数是："+ servers.size());
        servers.remove(server);
    }
}
