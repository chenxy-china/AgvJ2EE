package com.api.cxy;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.jni.cxy.ServiceTools;
import com.sun.net.httpserver.HttpServer;

public class ApiCtrl {
    public static void Create(String[] args) throws Exception {
        try {

            // 创建 http 服务器, 绑定本地 8080 端口
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

            // 创建上下文监听, "/" 表示匹配所有 URI 请求
            server.createContext("/", new TestHandler());

            // 创建上下文监听, 处理 URI 以 "/agvinterface" 开头的请求
            server.createContext("/agvinterface", new ApiHandler());

            // 创建上下文监听, 处理 URI 以 "/agvinterface/createmap" 开头的请求
            server.createContext("/agvinterface/createmap", new ApiHandlerCreMap());

            // 创建上下文监听, 处理 URI 以 "/agvinterface/savemap" 开头的请求
            server.createContext("/agvinterface/savemap", new ApiHandlerSaveMap());

            // 创建上下文监听, 处理 URI 以 "/agvinterface/uploadmap" 开头的请求
            server.createContext("/agvinterface/uploadmap", new ApiHandlerUlMap());

            // 创建上下文监听, 处理 URI 以 "/agvinterface/getlaserdata" 开头的请求
            ApiHandlerGetLaser apiHandlerGetLaser = new ApiHandlerGetLaser();
            server.createContext("/agvinterface/getlaserdata", apiHandlerGetLaser);
            
            // 初始化AGV底层
            //ServiceTools.getInstance().startInit();
            //ServiceTools.getInstance().setScanListener(apiHandlerGetLaser);

            
            // 启动http服务
            server.start();
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            System.out.println(" ");
            System.out.println("反初始化AGV底层,释放资源");
            // 反初始化AGV底层,释放资源
            //ServiceTools.getInstance().startDeInit();
        }
    }
}
