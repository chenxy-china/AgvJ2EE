package com.jni.cxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jni.cxy.LaserMessage;
import com.jni.cxy.LaserMessage.ScanListener;
import com.ws.cxy.ServerManager;

public class ServiceTools {

    private boolean initStatus = false;
    private boolean agentStatus = false;
    private boolean mapSendStatus = false;
    private boolean mappingStatus = false;
    private static ServiceTools mInstance = new ServiceTools();

    private ServiceTools() {
        System.out.println("new ServiceTools()");
    }

    public static ServiceTools getInstance() {
        return mInstance;
    }

    public void startInit() {
        if (!initStatus) {
            Thread td = new Thread(ServiceTools.getInstance().AgvInit_Runnable);
            td.start();
        }
    }

    public void startDeInit() {
        if (initStatus) {
            initStatus = false;
//            Thread td = new Thread(ServiceTools.getInstance().AgvDeInit_Runnable);
//            td.start();
            AgvDeInit_func();
        }
    }

    public Runnable AgvInit_Runnable = new Runnable() {
        public void run() {
            System.out.println("Thread AgvInit_Runnable run");
//          long startTime = System.currentTimeMillis();
            if (AgvEngine.getInstance().initAgvEngine()) {
                AgvEngine.getInstance().startSensor();
                initStatus = true;
            }
//          long consumingTime = System.currentTimeMillis() - startTime;
        }
    };

//    public Runnable AgvDeInit_Runnable = new Runnable() {
//        public void run() {
//            System.out.println("Thread AgvDeInit_Runnable run");
//            AgvDeInit_func();
//        }
//    };

    public void AgvDeInit_func() {
        System.out.println("AgvDeInit_func run");
//      long startTime = System.currentTimeMillis();
        stopSendLaserData();
        stopSendMapData();
        stopCreateMap();
        AgvEngine.getInstance().stopSensor();
        AgvEngine.getInstance().deinitAgvEngine();
//      long consumingTime = System.currentTimeMillis() - startTime;    
    }
    
    public boolean startCreateMap() {
        if (initStatus) {
            if(!mappingStatus) {
                mappingStatus = AgvEngine.getInstance().startCreateMap();
            }
            return mappingStatus;
        }
        return false;
    }

    public boolean getLaserData() {
        if (initStatus) {
            AgvEngine.getInstance().GetLaserData();
            return true;
        }
        return false;
    }

    public boolean stopCreateMap() {
        if (mappingStatus) {
            if(AgvEngine.getInstance().stopCreateMap()) {
                mappingStatus = false;
                return true;
            }
        }
        return false;
    }

    public boolean saveMap() {
        if (initStatus) {
            return AgvEngine.getInstance().saveMap();
        }
        return false;
    }
    
    public boolean startSendLaserData() {
        if (initStatus) {
            if(!agentStatus) {
                agentStatus = AgvEngine.getInstance().startSendLaserData();
            }
            return agentStatus;
        }
        return false;
    }
    
    public boolean stopSendLaserData() {
        if(agentStatus) {
            if(AgvEngine.getInstance().stopSendLaserData()) {
                agentStatus = false;
                return true;
            }
        }
        return false;
    }
    
    public boolean startSendMapData() {
        if (initStatus) {
            if(!mapSendStatus) {
                mapSendStatus = AgvEngine.getInstance().startSendMapData();
            }
            return mapSendStatus;
        }
        return false;
    }
    
    public boolean stopSendMapData() {
        if(mapSendStatus) {
            if(AgvEngine.getInstance().stopSendMapData()) {
                mapSendStatus = false;
                return true;
            }
        }
        return false;
    }
    
    public void setScanListener(final ScanListener apiHandlerGetLaser) {
        AgvEngine.ScanCallback agvScanCallback = new AgvEngine.ScanCallback() {

            public void onScanCallback(Object obj) throws Exception {
                apiHandlerGetLaser.onScanListener((LaserMessage)obj);
            }
        };
        AgvEngine.getInstance().setScanCallback(agvScanCallback);
    }
    
    public void setScanListener() {
        AgvEngine.ScanCallback agvScanCallback = new AgvEngine.ScanCallback() {

            public void onScanCallback(Object obj) throws Exception {
                //广播出去
                System.out.println("ws broadCast");
                ServerManager.broadCast(obj);
            }
        };
        AgvEngine.getInstance().setScanCallback(agvScanCallback);
        System.out.println("setScanListener() ,set callback func ok");
    }
}
