package com.ws.cxy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        Logger logger = LoggerFactory.getLogger(ServerManager.class);
        String msg = mapper.writeValueAsString(obj);

        if(obj instanceof  LaserMessage) {
            //logger.info("LaserMessage msg type");
            for (LaserDataServer ldServer : laserdataservers) {
                try {
                    ldServer.sendMessage(msg);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }else if(obj instanceof  MapMessage) {
            logger.info("MapMessage msg type");
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
            //logger.info("unknown msg type");
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
        Logger logger = LoggerFactory.getLogger(ServerManager.class);
        if(server instanceof  LaserDataServer) {
            laserdataservers.add((LaserDataServer) server);
            //logger.info("new connection to LaserDataServer！ current connections total:"+ laserdataservers.size());
            
            // agv开始发送激光数据
            if (!ServiceTools.getInstance().startSendLaserData()) {
                String msg = "Start send laser data NG";
                logger.info(msg);
                try {
                    server.sendMessage(msg);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }else {
                //logger.info("Start send laser data OK");
            }
            
        }else if(server instanceof  MapDataServer) {
            mapdataservers.add((MapDataServer) server);
            //logger.info("new connection to MapDataServer！ current connections total:"+ mapdataservers.size());
            
            // agv开始发送地图数据
            if (!ServiceTools.getInstance().startSendMapData()) {
                String msg = "Start send map data NG";
                logger.info(msg);
                try {
                    server.sendMessage(msg);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }else {
                //logger.info("Start send map data OK");
            }
            
        }
        logger.info("new connection to DataServer！current connections total:"+ getTotal());
    }

    
    public static void remove(DataServer server){
        Logger logger = LoggerFactory.getLogger(ServerManager.class);
        if(server instanceof  LaserDataServer) {
            laserdataservers.remove((LaserDataServer) server);
            //logger.info("disconnect to LaserDataServer！ current connections total:"+ laserdataservers.size());
            
            if(laserdataservers.size() == 0) {
                // agv停止发送激光数据
                if(ServiceTools.getInstance().stopSendLaserData()){
                    logger.info("Stop send laser data OK");
                }else {
                    logger.info("Stop send laser data NG");
                }
            }
        }else if(server instanceof  MapDataServer) {
            mapdataservers.remove((MapDataServer) server);
            //logger.info("disconnect to MapDataServer！ current connections total:"+ mapdataservers.size());
            
            if(mapdataservers.size() == 0) {
                // agv停止发送地图数据
                if(ServiceTools.getInstance().stopSendMapData()) {
                    logger.info("Stop send map data OK");
                }else {
                    logger.info("Stop send map data NG");
                }
            }
        }
        logger.info("disconnect to DataServer！current connections total:"+ getTotal());
    }
}
