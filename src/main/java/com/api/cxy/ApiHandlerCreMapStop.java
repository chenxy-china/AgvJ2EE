package com.api.cxy;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jni.cxy.ServiceTools;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ApiHandlerCreMapStop extends HttpServlet implements HttpHandler{
    
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
    {
        System.out.println("******"+this.getClass().getName()+"******");
        
        // 响应内容
        String response = "execution OK";

        // 停止发送地图数据
        if (!ServiceTools.getInstance().stopSendMapData()) {
            response = "Stop Send Map failed";
            System.out.println(response);
        }
        // 停止建图
        if (!ServiceTools.getInstance().stopCreateMap()) {
            response = "AgvEngine initialize unfinished";
            System.out.println(response);
        }

        // 设置响应头
        resp.setHeader("Content-Type", "text/html; charset=UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Method", "POST,GET");

        // 设置响应code和内容长度
        resp.setStatus(200);
        resp.setBufferSize(response.length());
        
        // 设置响应内容
        try {
            OutputStream os = resp.getOutputStream();//获取OutputStream输出流
            byte[] dataByteArr = response.getBytes("UTF-8");//将字符转换成字节数组，指定以UTF-8编码进行转换
            os.write(dataByteArr);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void handle(HttpExchange exchange) throws IOException {
        // 响应内容
        String response = "execution OK";

        // 开始建图
        if (ServiceTools.getInstance().stopCreateMap()) {
            response = "initialize unfinished";
        }

        // 设置响应头
        exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");

        // 设置响应code和内容长度
        exchange.sendResponseHeaders(200, response.length());

        // 设置响应内容
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());

        // 关闭处理器, 同时将关闭请求和响应的输入输出流（如果还没关闭）
        os.close();

    }
}