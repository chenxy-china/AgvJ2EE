package com.jni.cxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jni.cxy.LaserMessage;
import com.jni.cxy.LaserMessage.ScanListener;
import com.ws.cxy.ServerManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceTools {
    Logger logger = LoggerFactory.getLogger(ServiceTools.class);
    private boolean initStatus = false;
    private boolean agentStatus = false;
    private boolean mapSendStatus = false;
    private boolean mappingStatus = false;
    private static ServiceTools mInstance = new ServiceTools();

    private ServiceTools() {
        logger.info("new ServiceTools()");
    }

    public static ServiceTools getInstance() {
        return mInstance;
    }

    public void startInit() {
        if (!initStatus) {
            // Thread td = new Thread(ServiceTools.getInstance().AgvInit_Runnable);
            // td.start();
            AgvInit_func();
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
            logger.info("Thread AgvInit_Runnable run");
//          long startTime = System.currentTimeMillis();
            if (AgvEngine.getInstance().initAgvEngine()) {
                //AgvEngine.getInstance().startSensor();
                initStatus = true;
            }
//          long consumingTime = System.currentTimeMillis() - startTime;
        }
    };

    public void AgvInit_func() {
        logger.info("AgvInit_func run");
//      long startTime = System.currentTimeMillis();
        if (AgvEngine.getInstance().initAgvEngine()) {
            //AgvEngine.getInstance().startSensor();
            initStatus = true;
        }
//      long consumingTime = System.currentTimeMillis() - startTime;
    }

//    public Runnable AgvDeInit_Runnable = new Runnable() {
//        public void run() {
//            logger.info("Thread AgvDeInit_Runnable run");
//            AgvDeInit_func();
//        }
//    };

    public void AgvDeInit_func() {
        logger.info("AgvDeInit_func run");
//      long startTime = System.currentTimeMillis();
        // stopSendLaserData();
        // stopSendMapData();
        // stopCreateMap();
        // AgvEngine.getInstance().stopSensor();
        AgvEngine.getInstance().deinitAgvEngine();
//      long consumingTime = System.currentTimeMillis() - startTime;    
    }

    public boolean startCreateMap() {
        if (initStatus) {
            if (!mappingStatus) {
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
            if (AgvEngine.getInstance().stopCreateMap()) {
                mappingStatus = false;
                return true;
            }
        }
        return false;
    }

    public boolean saveMap(String fn) {
        if (initStatus) {
            return AgvEngine.getInstance().saveMap(fn);
        }
        return false;
    }

    public boolean startSendLaserData() {
        if (initStatus) {
            if (!agentStatus) {
                agentStatus = AgvEngine.getInstance().startSendLaserData();
            }
            return agentStatus;
        }
        return false;
    }

    public boolean stopSendLaserData() {
        if (agentStatus) {
            if (AgvEngine.getInstance().stopSendLaserData()) {
                agentStatus = false;
                return true;
            }
        }
        return false;
    }

    public boolean startSendMapData() {
        if (initStatus) {
            if (!mapSendStatus) {
                mapSendStatus = AgvEngine.getInstance().startSendMapData();
            }
            return mapSendStatus;
        }
        return false;
    }

    public boolean stopSendMapData() {
        if (mapSendStatus) {
            if (AgvEngine.getInstance().stopSendMapData()) {
                mapSendStatus = false;
                return true;
            }
        }
        return false;
    }

    public boolean setPubMap(String fn) {
        if (initStatus) {
            return AgvEngine.getInstance().setPubMap(fn);
        }
        return false;
    }

    public boolean setImuLocate(float arr[]) {
        if (initStatus) {
            return AgvEngine.getInstance().setImuLocate(arr);
        }
        return false;
    }

    public boolean startRelocate(String fn,float arr[]) {
        if (initStatus) {
            return AgvEngine.getInstance().startRelocate(fn,arr);
        }
        return false;
    }

    public boolean stopRelocate() {
        if (initStatus) {
            return AgvEngine.getInstance().stopRelocate();
        }
        return false;
    }

    public void setScanListener(final ScanListener apiHandlerGetLaser) {
        AgvEngine.ScanCallback agvScanCallback = new AgvEngine.ScanCallback() {

            public void onScanCallback(Object obj) throws Exception {
                apiHandlerGetLaser.onScanListener((LaserMessage) obj);
            }
        };
        AgvEngine.getInstance().setScanCallback(agvScanCallback);
    }

    public void setScanListener() {
        AgvEngine.ScanCallback agvScanCallback = new AgvEngine.ScanCallback() {

            public void onScanCallback(Object obj) throws Exception {
                // 广播出去
                ServerManager.broadCast(obj);
            }
        };
        AgvEngine.getInstance().setScanCallback(agvScanCallback);
        logger.info("set callback func ok\n");
    }
}
