package com.ws.cxy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.jni.cxy.LaserMessage;
import com.jni.cxy.MapMessage;
import com.jni.cxy.ServiceTools;

public class ServerManager {
    private static Collection<LaserDataServer> laserdataservers = Collections.synchronizedCollection(new ArrayList<LaserDataServer>());
    private static Collection<MapDataServer> mapdataservers = Collections.synchronizedCollection(new ArrayList<MapDataServer>());
    private static ObjectMapper mapper = new ObjectMapper();
    
    public static void broadCast(Object obj) throws JsonProcessingException{
        String msg = mapper.writeValueAsString(obj);

        if(obj instanceof  LaserMessage) {
            for (LaserDataServer ldServer : laserdataservers) {
                try {
                    ldServer.sendMessage(msg);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }else if(obj instanceof  MapMessage) {
            for (MapDataServer mdServer : mapdataservers) {
                try {
                    mdServer.sendMessage(msg);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        else {
            System.out.println("unknown msg type");
            for (LaserDataServer ldServer : laserdataservers) {
                try {
                    ldServer.sendMessage(msg);
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

    public static void add(DataServer server){
        if(server instanceof  LaserDataServer) {
            laserdataservers.add((LaserDataServer) server);
            System.out.println("有新连接加入LaserDataServer！ 当前连接数是："+ laserdataservers.size());
            
            // agv开始发送激光数据
            if (!ServiceTools.getInstance().startSendLaserData()) {
                String msg = "Start send laser data NG";
                System.out.println(msg);
                try {
                    server.sendMessage(msg);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }else {
                System.out.println("Start send laser data OK");
            }
            
        }else if(server instanceof  MapDataServer) {
            mapdataservers.add((MapDataServer) server);
            System.out.println("有新连接加入MapDataServer！ 当前总连接数是："+ mapdataservers.size());
            
            // agv开始发送地图数据
            if (!ServiceTools.getInstance().startSendMapData()) {
                String msg = "Start send map data NG";
                System.out.println(msg);
                try {
                    server.sendMessage(msg);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }else {
                System.out.println("Start send map data OK");
            }
            
        }
        System.out.println("有新连接加入！ 当前总连接数是："+ getTotal());
    }

    
    public static void remove(DataServer server){
        if(server instanceof  LaserDataServer) {
            laserdataservers.remove((LaserDataServer) server);
            System.out.println("有连接退出LaserDataServer！ 当前连接数是："+ laserdataservers.size());
            
            if(laserdataservers.size() == 0) {
                // agv停止发送激光数据
                if(ServiceTools.getInstance().stopSendLaserData()){
                    System.out.println("Stop send laser data OK");
                }else {
                    System.out.println("Stop send laser data NG");
                }
            }
        }else if(server instanceof  MapDataServer) {
            mapdataservers.remove((MapDataServer) server);
            System.out.println("有连接退出MapDataServer！ 当前连接数是："+ mapdataservers.size());
            
            if(mapdataservers.size() == 0) {
                // agv停止发送地图数据
                if(ServiceTools.getInstance().stopSendMapData()) {
                    System.out.println("Stop send map data OK");
                }else {
                    System.out.println("Stop send map data NG");
                }
            }
        }
        System.out.println("有连接退出！ 当前总连接数是："+ getTotal());
    }
}
