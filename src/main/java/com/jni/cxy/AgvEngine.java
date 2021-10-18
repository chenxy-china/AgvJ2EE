package com.jni.cxy;

public class AgvEngine {

    public interface ScanCallback {
        public void onScanCallback(Object msg) throws Exception;
    }

    private static ScanCallback mScanCallback;

    private static AgvEngine mInstance = new AgvEngine();

    private AgvEngine() {
        System.out.println("new AgvEngine()");
    }

    public static AgvEngine getInstance() {
        return mInstance;
    }

    static private boolean flaglibso=false;
    static {
        try {
            // 载入本地库
            System.loadLibrary("AgvCtrlJni");
            flaglibso=true;
        } catch (Throwable e) {
            System.out.println("load libAgvCtrlJni.so failed");
            e.printStackTrace();
        }
    }

    public boolean initAgvEngine() {
        System.out.println("initAgvEngine() start");
        if(flaglibso) {
            try {
                if (JniInit()) {
                    String nativeInfVer = JniGetVersion();
                    System.out.println("Jni Version :"+nativeInfVer);
                    
                    JniSetLaserScanCallbakFunc("sendScanResultFromNative");
                    System.out.println("initAgvEngine() successfully");
                    return true;
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                JniDeInit();
            }
        }

        System.out.println("initAgvEngine() unsuccessfully");
        return false;
    }

    public boolean deinitAgvEngine() {
        System.out.println("deinitAgvEngine() start");
        if (JniDeInit()) {
            System.out.println("deinitAgvEngine() successfully");
            return true;
        }
        System.out.println("deinitAgvEngine() unsuccessfully");
        return false;
    }

    public void startSensor() {
        System.out.println("Start IMU");
        JniStartIMU("127.0.0.1");
        
        System.out.println("Start Laser");
        JniStartLaser("127.0.0.1");
    }

    public void stopSensor() {
        System.out.println("Stop IMU");
        JniStopIMU("127.0.0.1");
        System.out.println("Stop Laser");
        JniStopLaser("127.0.0.1");
    }

    public boolean startCreateMap() {
        System.out.println("Start Create Map");
        return JniStartCreateMap();
    }

    public void GetLaserData() {
        JniGetLaserData();
    }

    public boolean stopCreateMap() {
        System.out.println("Stop Create Map");
        return JniStopCreateMap();
    }
    
    public boolean saveMap() {
        System.out.println("Save Map");
        return JniSaveMap();
    }
    
    public boolean startSendLaserData() {
        System.out.println("Start send laser data");
        return JniStartSendLaserData();
    }

    public boolean stopSendLaserData() {
        System.out.println("Stop send laser data");
        return JniStopSendLaserData();
    }

    public boolean startSendMapData() {
        System.out.println("Start send map data");
        return JniStartSendMapData();
    }

    public boolean stopSendMapData() {
        System.out.println("Stop send map data");
        return JniStopSendMapData();
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

    private native boolean JniSaveMap();

    private native String JniGetMap();

    private native boolean JniGetLaserData();

    private native boolean JniSetLaserScanCallbakFunc(String callbackFunc);
    
    private native boolean JniStartSendLaserData();
    
    private native boolean JniStopSendLaserData();
    
    private native boolean JniStartSendMapData();
    
    private native boolean JniStopSendMapData();
}
