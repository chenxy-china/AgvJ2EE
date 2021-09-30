package com.jni.cxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jni.cxy.LaserMessage;
import com.jni.cxy.LaserMessage.ScanListener;
import com.ws.cxy.ServerManager;

public class ServiceTools {

    private boolean initStatus = false;
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
                AgvEngine.getInstance().start();
                initStatus = true;
            }
//          long consumingTime = System.currentTimeMillis() - startTime;
        }
    };

    public Runnable AgvDeInit_Runnable = new Runnable() {
        public void run() {
            System.out.println("Thread AgvDeInit_Runnable run");
            AgvDeInit_func();
        }
    };

    public void AgvDeInit_func() {
        System.out.println("AgvDeInit_func run");
//      long startTime = System.currentTimeMillis();
        AgvEngine.getInstance().stop();
        AgvEngine.getInstance().deinitAgvEngine();
//      long consumingTime = System.currentTimeMillis() - startTime;    
    }
    
    public boolean startCreateMap() {
        if (initStatus) {
            AgvEngine.getInstance().startCreateMap();
            return true;
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

    public void setScanListener(final ScanListener apiHandlerGetLaser) {
        AgvEngine.ScanCallback agvScanCallback = new AgvEngine.ScanCallback() {

            public void onScanCallback(LaserMessage msg) throws Exception {
                apiHandlerGetLaser.onScanListener(msg);
            }
        };
        AgvEngine.getInstance().setScanCallback(agvScanCallback);
    }
    
    public void setScanListener() {
        AgvEngine.ScanCallback agvScanCallback = new AgvEngine.ScanCallback() {
            ObjectMapper mapper = new ObjectMapper();
            
            public void onScanCallback(LaserMessage msg) throws Exception {
                String message = mapper.writeValueAsString(msg);
                //广播出去
                System.out.println("ws broadCast");
                ServerManager.broadCast(message);
            }
        };
        AgvEngine.getInstance().setScanCallback(agvScanCallback);
        System.out.println("setScanListener() ,set callback func ok");
    }
}
