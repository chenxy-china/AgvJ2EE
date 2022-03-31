package com.api.cxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jni.cxy.ServiceTools;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiHandler extends HttpServlet implements HttpHandler {
    Logger logger = LoggerFactory.getLogger(ApiHandler.class);

    public void init(ServletConfig config) {
        logger.info("******" + this.getClass().getName() + "******初始化AGV底层******");

        // 初始化AGV底层
        ServiceTools.getInstance().startInit();
        ServiceTools.getInstance().setScanListener();
    }

    public void destroy() {
        logger.info("******" + this.getClass().getName() + "******反初始化AGV底层,释放资源******");
        // 反初始化AGV底层,释放资源
        ServiceTools.getInstance().startDeInit();
    }

    public void service(HttpServletRequest req, HttpServletResponse resp) {
        logger.info("******" + this.getClass().getName() + "******");

        // 获取body中的String数据
        InputStream is = null;
        try {
            is = req.getInputStream();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        // 将资料解码
        String body = sb.toString();
        logger.info("body: " + body);
        logger.info(" ");

        String response = "";
        try {
            // 将body中的String数据json反序列化
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(body);
            String ip = jsonNode.get("IP").asText();
            logger.info("IP: " + ip);
            // 响应内容
            response = "find IP in body";
        } catch (JsonMappingException e) {
            e.printStackTrace();
            // 响应内容
            response = "JsonMappingException";
        } catch (IOException e) {
            e.printStackTrace();
            // 响应内容
            response = "IOException";
        }

        logger.info(response);

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
        logger.info("******" + this.getClass().getName() + "******");

        // 获取body中的String数据
        InputStream is = exchange.getRequestBody();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        // 将资料解码
        String body = sb.toString();
        logger.info("body: " + body);

        String response = "";
        // 将body中的String数据json反序列化
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(body);
            String ip = jsonNode.get("IP").asText();
            logger.info("IP: " + ip);
            // 响应内容
            response = "find IP in body";
        } catch (JsonMappingException e) {
            e.printStackTrace();
            // 响应内容
            response = "JsonMappingException";
        } catch (Exception e) {
            e.printStackTrace();
            // 响应内容
            response = "Exception";
        } 

        logger.info(response);

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
