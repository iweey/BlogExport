package com.blogexport.util;



import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageUtil {

    /**
     * 下载图片
     * @param imgUrl  要下载的图片链接
     * @param filePath  保存图片的路径
     * @throws Exception
     */
    public static void downloadImage(String imgUrl,String filePath) throws Exception {
        String uri = String.format(imgUrl);
        URL url = new URL(uri);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //破解防盗链
        conn.setRequestProperty("referer", url.getProtocol()+"://"+url.getHost());
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5 * 1000);
        InputStream inStream = conn.getInputStream();
        readInputStream(inStream, filePath);
    }

    /**
     * 保存图片
     * @param inStream
     * @param path
     * @throws Exception
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