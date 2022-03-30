package com.jni.cxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgvEngine {
    Logger logger = LoggerFactory.getLogger(AgvEngine.class);

    public interface ScanCallback {
        public void onScanCallback(Object msg) throws Exception;
    }

    private static ScanCallback mScanCallback;

    private static AgvEngine mInstance = new AgvEngine();

    private AgvEngine() {
        logger.info("new AgvEngine()");
    }

    public static AgvEngine getInstance() {
        return mInstance;
    }

    static private boolean flaglibso = false;
    static {
        Logger logger = LoggerFactory.getLogger(AgvEngine.class);
        try {
            // 载入本地库
            System.loadLibrary("AgvCtrlJni");
            flaglibso = true;
        } catch (Throwable e) {
            logger.info("load libAgvCtrlJni.so failed");
            e.printStackTrace();
        }
    }

    public boolean initAgvEngine() {
        logger.info("initAgvEngine() start");
        if (flaglibso) {
            try {
                String nativeInfVer = JniGetVersion();
                logger.info("Jni Version :" + nativeInfVer);
                if (JniInit()) {
                    JniSetLaserScanCallbakFunc("sendScanResultFromNative");
                    logger.info("initAgvEngine() successfully");
                    return true;
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                JniDeInit();
            }
        }

        logger.error("initAgvEngine() unsuccessfully");
        return false;
    }

    public boolean deinitAgvEngine() {
        logger.info("deinitAgvEngine() start");
        if (JniDeInit()) {
            logger.info("deinitAgvEngine() successfully");
            return true;
        }
        logger.error("deinitAgvEngine() unsuccessfully");
        return false;
    }

    public void startSensor() {
        logger.info("Start IMU");
        JniStartIMU("127.0.0.1");

        logger.info("Start Laser");
        JniStartLaser("127.0.0.1");
    }

    public void stopSensor() {
        logger.info("Stop IMU");
        JniStopIMU("127.0.0.1");
        logger.info("Stop Laser");
        JniStopLaser("127.0.0.1");
    }

    public boolean startCreateMap() {
        logger.info("Start Create Map");
        return JniStartCreateMap();
    }

    public void GetLaserData() {
        JniGetLaserData();
    }

    public boolean stopCreateMap() {
        logger.info("Stop Create Map");
        return JniStopCreateMap();
    }

    public boolean saveMap(String fn) {
        logger.info("Save Map");
        return JniSaveMap(fn);
    }

    public boolean startSendLaserData() {
        logger.info("Start send laser data");
        return JniStartSendLaserData();
    }

    public boolean stopSendLaserData() {
        logger.info("Stop send laser data");
        return JniStopSendLaserData();
    }

    public boolean startSendMapData() {
        logger.info("Start send map data");
        return JniStartSendMapData();
    }

    public boolean stopSendMapData() {
        logger.info("Stop send map data");
        return JniStopSendMapData();
    }

    public boolean setPubMap(String fn) {
        logger.info("set publish map");
        return JniSetPubMap(fn);
    }

    public boolean setImuLocate(float arr[]) {
        logger.info("set IMU locate");
        return JniSetImuLocate(arr);
    }

    public boolean startRelocate(String fn,float arr[]) {
        logger.info("Start relocate");
        return JniStartRelocate(fn,arr);
    }

    public boolean stopRelocate() {
        logger.info("Stop relocate");
        return JniStopRelocate();
    }

    public static void sendScanResultFromNative(Object msg) throws Exception {
        // Native层中Laser扫描一次结束，触发本方法，发送Laser扫描结果
        // 或者发送建图中的map数据
        mScanCallback.onScanCallback(msg);
    }

    public void setScanCallback(ScanCallback scanCallback) {
        this.mScanCallback = scanCallback;
    }

    private native String JniGetVersion();

    private native boolean JniInit() throws Exception;

    private native boolean JniDeInit();

    private native boolean JniStartIMU(String ip);

    private native boolean JniStopIMU(String ip);

    private native boolean JniStartLaser(String ip);

    private native boolean JniStopLaser(String ip);

    private native boolean JniStartCreateMap();

    private native boolean JniStopCreateMap();

    private native boolean JniSaveMap(String fn);

    private native String JniGetMap();

    private native boolean JniGetLaserData();

    private native boolean JniSetLaserScanCallbakFunc(String callbackFunc);

    private native boolean JniStartSendLaserData();

    private native boolean JniStopSendLaserData();

    private native boolean JniStartSendMapData();

    private native boolean JniStopSendMapData();

    private native boolean JniSetPubMap(String fn);

    private native boolean JniStartRelocate(String fn,float arr[]);

    private native boolean JniStopRelocate();

    private native boolean JniSetImuLocate(float arr[]);
}
