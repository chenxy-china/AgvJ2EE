package com.ws.cxy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServerManager {
    private static Collection<LaserDataServer> laserdataservers = Collections.synchronizedCollection(new ArrayList<LaserDataServer>());
    private static Collection<MapDataServer> mapdataservers = Collections.synchronizedCollection(new ArrayList<MapDataServer>());
    private static ObjectMapper mapper = new ObjectMapper();
    
    public static void broadCast(Object obj) throws JsonProcessingException{
        String msg = mapper.writeValueAsString(obj);

        if(obj instanceof  LaserDataServer) {
            for (LaserDataServer ldServer : laserdataservers) {
                try {
                    ldServer.sendMessage(msg);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }else if(obj instanceof  MapDataServer) {
            for (MapDataServer mdServer : mapdataservers) {
                try {
                    mdServer.sendMessage(msg);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }
     
    public static int getTotal(){
        return laserdataservers.size()+mapdataservers.size();
    }
    
    public static void add(Object server){
        if(server instanceof  LaserDataServer) {
            laserdataservers.add((LaserDataServer) server);
            System.out.println("有新连接加入LaserDataServer！ 当前连接数是："+ laserdataservers.size());
        }else if(server instanceof  MapDataServer) {
            mapdataservers.add((MapDataServer) server);
            System.out.println("有新连接加入MapDataServer！ 当前总连接数是："+ mapdataservers.size());
        }
        System.out.println("有新连接加入！ 当前总连接数是："+ getTotal());
    }

    
    public static void remove(Object server){
        if(server instanceof  LaserDataServer) {
            laserdataservers.remove((LaserDataServer) server);
            System.out.println("有连接退出LaserDataServer！ 当前连接数是："+ laserdataservers.size());
        }else if(server instanceof  MapDataServer) {
            mapdataservers.remove((MapDataServer) server);
            System.out.println("有连接退出MapDataServer！ 当前连接数是："+ mapdataservers.size());
        }
        System.out.println("有连接退出！ 当前总连接数是："+ getTotal());
    }
}
