package com.blogexport.task;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blogexport.common.SingletonQueue;
import com.blogexport.model.weixin.AllArticle;
import com.blogexport.model.weixin.Article;
import com.blogexport.model.weixin.ArticleJson;
import com.blogexport.pipeline.WeixinPipeline;
import com.blogexport.util.PdfUtil;
import com.geccocrawler.gecco.GeccoEngine;
import com.geccocrawler.gecco.request.HttpGetRequest;
import com.google.common.reflect.TypeToken;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
public class WeixinScheduleTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeixinPipeline.class);


    @Value("${image.server}")
    private String imageServer;

    @Scheduled(fixedRate = 5000)
    public void exeTask() {
        try {
            //要考虑去重 TODO
            Object object = SingletonQueue.getInstance().take();
            if (object != null) {
                String url = object.toString();
                if (StringUtils.isNotBlank(url)) {
                    LOGGER.debug("take weixin url:" + url);
                    List<Article> articles = getArticles(url);
                    LOGGER.debug("获取所有历史文章链接结束，文章数目:" + articles.size());
                    LOGGER.debug("爬虫启动");
                    start(articles);
                    LOGGER.debug("爬虫结束");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  List<Article> getArticles(String url) {

        List<Article> articles = new ArrayList<>();

        LOGGER.debug("start weixin");

        String[] s = url.split("offset=");
        if (s == null || s.length != 2) {
            LOGGER.debug("微信链接错误，url:" + url);
            return articles;
        }

        String preurl = s[0] + s[1];

        //分页查询的起点，每次取10条
        int offset = 0;

        try {
            while (true) {
                HttpClient client = HttpClients.createDefault();
                String tmpurl = preurl + "&offset=" + offset;
                LOGGER.debug("offset:{}", offset);
                LOGGER.debug("weixin url:{}", tmpurl);
                HttpResponse response = client.execute(new HttpGet(tmpurl));
                HttpEntity entity = response.getEntity();
                if (response.getStatusLine().getStatusCode() == 200) {
                    String res = EntityUtils.toString(entity);
                    ArticleJson articleJson = JSON.parseObject(res, ArticleJson.class);
                    JSONObject jsonObject = JSON.parseObject(articleJson.getGeneral_msg_list());
                    List<AllArticle> allArticleList = JSON.parseObject(jsonObject.getString("list"), new TypeToken<List<AllArticle>>() {
                    }.getType());

                    for (AllArticle allArticle : allArticleList) {
                        Article tmpArticle = allArticle.getApp_msg_ext_info();
                        Long datetime = Long.parseLong(allArticle.getComm_msg_info().getDatetime());
                        tmpArticle.setPostTime(datetime);
                        if (tmpArticle != null) {
                            articles.add(tmpArticle);
                            if (tmpArticle.getIs_multi() == 1) {
                                List<Article> multi_app_msg_item_list = allArticle.getApp_msg_ext_info().getMulti_app_msg_item_list();
                                if (multi_app_msg_item_list != null) {
                                    for (Article article : multi_app_msg_item_list) {
                                        //设置公众号文章发布时间，一次发布多条的话，时间是一样的，所以需要自增保持唯一，方便后续排序
                                        datetime++;
                                        article.setPostTime(datetime);
                                        articles.add(article);
                                    }
                                }
                            }
                        }
                    }

                    if (articleJson.getCan_msg_continue() == 0) {
                        break;
                    }

                    //设置下一次分页的开始记录
                    offset = articleJson.getNext_offset();

                } else {
                    LOGGER.debug("http请求错误");
                    break;
                }
                TimeUnit.SECONDS.sleep(3);
            }

            LOGGER.debug("获取微信文章列表结束");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return articles;

    }

    public  void start(List<Article> articles) throws Exception {

        if (articles == null || articles.size() == 0) {
            LOGGER.debug("参数错误");
            return;
        }

        String homeDic = System.getProperty("user.dir") + File.separator + "export";

        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String mergeFilePath = homeDic + File.separator + "weixin_" + date + ".pdf";

        List<HttpGetRequest> requests = new ArrayList<>();

        for (Article article : articles) {
            HttpGetRequest request = new HttpGetRequest(article.getContent_url());
            Map<String, String> map = new HashMap<>();
            map.put("homeDic", homeDic);
            map.put("imageServer", imageServer);
            map.put("postTime", article.getPostTime() + "");
            request.setParameters(map);
            requests.add(request);
        }

        startEngine(requests);

        String htmlDic=homeDic + File.separator + "html" + File.separator;
        String pdfDic=homeDic + File.separator + "pdf" + File.separator;

        PdfUtil.dirHtmlToPdf(htmlDic, pdfDic);

        PdfUtil.mergePdf(pdfDic, mergeFilePath);

        clean(homeDic);

    }

    public static void clean(String homeDic){
        try {
            //删除临时文件夹
            FileUtils.deleteDirectory(new File(homeDic + File.separator + "pdf" + File.separator));
            FileUtils.deleteDirectory(new File(homeDic + File.separator + "html" + File.separator));
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
