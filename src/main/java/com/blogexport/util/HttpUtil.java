package com.blogexport.util;


import com.alibaba.fastjson.JSON;
import com.blogexport.model.Image;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HttpUtil {

    public static String post(String urlStr, Object param) {

        OutputStreamWriter out = null;
        InputStream is = null;
        try {
            URL url = new URL(urlStr);// 创建连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST"); // 设置请求方式
            connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
            connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
            connection.connect();
            out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8"); // utf-8编码
            out.append(JSON.toJSONString(param));
            out.flush();
            out.close();

            // 读取响应
            is = connection.getInputStream();
            int length = (int) connection.getContentLength();// 获取长度
            if (length != -1) {
                byte[] data = new byte[length];
                byte[] temp = new byte[512];
                int readLen = 0;
                int destPos = 0;
                while ((readLen = is.read(temp)) > 0) {
                    System.arraycopy(temp, 0, data, destPos, readLen);
                    destPos += readLen;
                }
                String result = new String(data, "UTF-8"); // utf-8编码
                System.out.println("主机返回:" + result);
                return result;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "";
    }

    public static void main(String[] args) {

        List<Image> list=new ArrayList<>();

        Image image=new Image();
        image.setFileName("9999");
        image.setUrl("https://mmbiz.qpic.cn/sz_mmbiz_png/kte008W0h6JmeycBpFkicibpF8V6zibmnTqFhUTZpWMSnpZdJhHkYcQfEwpA1KYdP1FLkSGAicrFCnChOYgQrQJvXw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1");
        list.add(image);

        post("http://1.117.86.142:8080/downloadImage",list);


    }

}