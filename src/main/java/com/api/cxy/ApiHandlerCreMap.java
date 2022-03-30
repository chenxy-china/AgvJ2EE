package com.api.cxy;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jni.cxy.ServiceTools;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiHandlerCreMap extends HttpServlet implements HttpHandler {
    Logger logger = LoggerFactory.getLogger(ApiHandlerCreMap.class);
    // 响应内容
    String response = "execution OK";

    private void executeCmd()
    {
        logger.info("******" + this.getClass().getName() + "******");

        // 开始建图
        if (!ServiceTools.getInstance().startCreateMap()) {
            response = "start Create Map NG";
            logger.info(response);
        } else {
            // 开始发送地图数据
            if (!ServiceTools.getInstance().startSendMapData()) {
                response = "Start Send MapData NG";
                logger.info(response);
            }
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {

        executeCmd();

        // 设置响应头
        resp.setHeader("Content-Type", "text/html; charset=UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Method", "POST,GET");

        // 设置响应code和内容长度
        resp.setStatus(200);
        resp.setBufferSize(response.length());

        // 设置响应内容
        try {
            OutputStream os = resp.getOutputStream();// 获取OutputStream输出流
            byte[] dataByteArr = response.getBytes("UTF-8");// 将字符转换成字节数组，指定以UTF-8编码进行转换
            os.write(dataByteArr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handle(HttpExchange exchange) throws IOException {

        executeCmd();

        // 设置响应头
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Method", "POST,GET");
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
