package com.api.cxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class TestHandler extends HttpServlet  implements HttpHandler {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    {}
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    {}
    
    public void service(HttpServletRequest request, HttpServletResponse response)
    {
        System.out.println("******"+this.getClass().getName()+"******");
        
        System.out.println("addr: " + request.getRemoteAddr() + // 客户端IP地址
                "; protocol: " + request.getProtocol() + // 请求协议: HTTP/1.1
                "; method: " + request.getMethod() + // 请求方法: GET, POST 等
                "; URI: " + request.getRequestURI()); // 请求 URI
        
        // 获取请求头
        String userAgent = request.getHeader("User-Agent");
        System.out.println("User-Agent: " + userAgent);

        String ContentType = request.getHeader("Content-Type");
        System.out.println("Content-Type: " + ContentType);
        
        // 获取body中的String数据
        InputStream is=null;
        try {
            is = request.getInputStream();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
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
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        
        // 将资料解码
        String body = sb.toString();
        System.out.println("body: " + body);
        System.out.println(" ");
        
        try {
            response.setHeader("Content-Type", "text/html; charset=UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Method", "POST,GET");
            
            response.getWriter().println("connect AGV http server OK");
            response.getWriter().println(new Date().toLocaleString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("******"+this.getClass().getName()+"******");
        
        System.out.println("addr: " + exchange.getRemoteAddress() + // 客户端IP地址
                "; protocol: " + exchange.getProtocol() + // 请求协议: HTTP/1.1
                "; method: " + exchange.getRequestMethod() + // 请求方法: GET, POST 等
                "; URI: " + exchange.getRequestURI()); // 请求 URI

        // 获取请求头
        String userAgent = exchange.getRequestHeaders().getFirst("User-Agent");
        System.out.println("User-Agent: " + userAgent);

        String ContentType = exchange.getRequestHeaders().getFirst("Content-Type");
        System.out.println("Content-Type: " + ContentType);

        InputStream is = exchange.getRequestBody();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        String body = sb.toString();
        System.out.println("body: " + body);
        System.out.println(" ");

        // 响应内容
        String response = "connect AGV http server OK";

        // 设置响应头
        exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
        // 防止前后端分离的跨域问题
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Method", "POST,GET");

        // 设置响应code和内容长度
        exchange.sendResponseHeaders(200, response.length());

        // 设置响应内容
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());

        // 关闭处理器, 同时将关闭请求和响应的输入输出流（如果还没关闭）
        os.close();
    }
}
