package com.blogexport.util;



import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageUtil {

    public static void downloadImage(String imgUrl,String filePath) throws Exception {
        String uri = String.format(imgUrl);
        URL url = new URL(uri);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestProperty("referer", url.getProtocol()+"://"+url.getHost()); //这是破解防盗链添加的参数
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5 * 1000);
        InputStream inStream = conn.getInputStream();
        readInputStream(inStream, filePath);
    }

    /**
     * 保存图片
     *
     */
    public static void readInputStream(InputStream inStream, String path) throws Exception{
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(path));
            byte[] buffer = new byte[102400];
            int len;
            while( (len=inStream.read(buffer)) != -1 ){
                fos.write(buffer, 0, len);
            }
            fos.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            inStream.close();
            fos.close();
        }
        
      
    }
}