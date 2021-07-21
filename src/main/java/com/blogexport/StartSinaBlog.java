package com.blogexport;

import com.blogexport.util.PdfUtil;
import com.geccocrawler.gecco.GeccoEngine;
import com.geccocrawler.gecco.request.HttpGetRequest;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class StartSinaBlog {


    public static void main(String[] args) throws Exception {

        //要爬取的新浪博客的用户id
        //必须修改这个配置，其他参数都可以用默认配置
        String userId = "1613879330";

        //设置工作目录
        String homeDic = System.getProperty("user.dir") + File.separator + "export";

        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String mergeFilePath = homeDic + File.separator + "blog_" + userId + "_" + date + ".pdf";

        //获取博客目录的总页数
        Integer pageCount = getPageCount(userId);
        if(pageCount==0){
            System.out.println("解析总页数失败，请手动填写");
            return;
        }

        //所有文章
        List<HttpGetRequest> requests = new ArrayList<>();
        for (int i = 0; i < pageCount; i++) {
            HttpGetRequest request = new HttpGetRequest("http://blog.sina.com.cn/s/articlelist_" + userId + "_0_" + i + ".html");
            Map<String, String> map = new HashMap<>();
            map.put("homeDic", homeDic);
            request.setParameters(map);
            requests.add(request);
        }


        //单个文章
//        HttpGetRequest request = new HttpGetRequest("http://blog.sina.com.cn/s/blog_4b0f52990102z7do.html");
//        Map<String, String> map = new HashMap<>();
//        map.put("homeDic", homeDic);
//        request.setParameters(map);
//        requests.add(request);


        //开始爬虫
        startEngine(requests);

        String htmlDic=homeDic + File.separator + "html" + File.separator;
        String pdfDic=homeDic + File.separator + "pdf" + File.separator;

        //把生成的文章html转换成pdf
        PdfUtil.dirHtmlToPdf(htmlDic, pdfDic);

        //合并pdf
        PdfUtil.mergePdf(pdfDic, mergeFilePath);

        clean(homeDic);

    }

    public static Integer getPageCount(String userId){
        Integer pageCount = 0;
        try {
            if (pageCount == 0) {
                Document doc = Jsoup.connect("http://blog.sina.com.cn/s/articlelist_" + userId + "_0_1.html").get();
                Elements elements = doc.select(".SG_pages span");
                pageCount = Integer.parseInt(elements.text().replace("共", "").replace("页", ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageCount;
    }

    public static void clean(String homeDic){
        try {
            //删除临时文件夹
            FileUtils.deleteDirectory(new File(homeDic + File.separator + "pdf" + File.separator));
            FileUtils.deleteDirectory(new File(homeDic + File.separator + "html" + File.separator));
            FileUtils.deleteDirectory(new File(homeDic + File.separator + "image" + File.separator));
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("清除临时文件失败");
        }

    }


    public static void startEngine(List<HttpGetRequest> requests) {
        GeccoEngine.create()
                .classpath("com.blogexport")
                .seed(requests)
                .thread(10)
                .interval(3000)
                .loop(false)
                .mobile(false)
                .debug(false)
                .run();
    }

}