package com.blogexport.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blogexport.model.weixin.AllArticle;
import com.blogexport.model.weixin.Article;
import com.blogexport.model.weixin.ArticleJson;
import com.google.common.reflect.TypeToken;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.util.List;

public class WeixinUtil {

    public static String username = "唐书房";

    public static void main(String[] args) {



    }


    public static void test(){
        System.out.println("start weixin");

        int offset = 0;

        try {
            while (true) {
                HttpClient client = HttpClients.createDefault();
//                String url = "";
                String url = "https://mp.weixin.qq.com/mp/profile_ext?action=getmsg&__biz=MjM5NzA0NjA0MA==&f=json&count=10&is_ok=1&scene=124&uin=ODcyOTUzNzIw&key=0f34bd7853d92dfa66746086299e41a1b0f8cef6e71d7ee89cecb6543dc07ce8659b91a756be957cd56504afd8a29fcbfad226e61c5cebc910bdf3b4578de8e73adcfc4f986ef4019813dafa01572fa9289fbbd4006a99d7dfa6cfa894853df7635efb943c9022964a32e698e60ba9bd545adc60c687800bd6c376993ece0d0b&pass_ticket=SZWADiH%2BtqOhYidH51ZGIoNmZx2JYHZKWmYhYGwf1QwkgSwwjF%2F7KcWJ9%2B7Gn7Zx&wxtoken=&appmsg_token=1118_AEEKOf6pBJ2GEID4mBWZJ_VtfbuI4ooo84n6WA~~&x5=0&f=json";
//                            https:mp.weixin.qq.com/mp/profile_ext?action=getmsg&__biz=MzUzNzYxNjAzMg==&f=json&offset=32&count=10&is_ok=1&scene=124&uin=ODcyOTUzNzIw&key=17188743a22509203cd5b54285723dd40620c8843680577fe2c4f4784ac5ccb93d5ccf298e52eef8ffeeb10908be1db331164c65e90aacde250c28927d370694b1b916f3a43fa44d1883d56a20dbbb06f6c4e91d1770bcbef23d367e05f8ab6efd7cd03b241bdda6882a4e52a6e67da2c851a95303050ce4c34d65355f37ea37&pass_ticket=3RbwFp%2Fsq0nJvqKDjc5rcUTbY7ty2KTfnwqg8C%2BmF4zSd7ZpzC4z8%2BumpodlawsL&wxtoken=&appmsg_token=1122_Q2Dk1ZOCrl4Tyif3Y8IhmLBT6yj2QB4Jvr-m9w~~&x5=0&f=json
                url += "&offset=" + offset;
                System.out.println("offset:" + offset);
                HttpResponse response = client.execute(new HttpGet(url));
                HttpEntity entity = response.getEntity();
                if (response.getStatusLine().getStatusCode() == 200) {
                    String res = EntityUtils.toString(entity);
                    ArticleJson articleJson = JSON.parseObject(res, ArticleJson.class);
                    JSONObject jsonObject = JSON.parseObject(articleJson.getGeneral_msg_list());
                    List<AllArticle> allArticles = JSON.parseObject(jsonObject.getString("list"), new TypeToken<List<AllArticle>>() {
                    }.getType());
                    for (AllArticle allArticle : allArticles) {
                        Article tmpArticle = allArticle.getApp_msg_ext_info();
                        if (tmpArticle != null) {
                            write(tmpArticle);
                            if (tmpArticle.getIs_multi() == 1) {
                                List<Article> multi_app_msg_item_list = allArticle.getApp_msg_ext_info().getMulti_app_msg_item_list();
                                if (multi_app_msg_item_list != null) {
                                    for (Article article : multi_app_msg_item_list) {
                                        write(article);
                                    }
                                }
                            }
                        }
                    }
                    if (articleJson.getCan_msg_continue() == 0) {
                        break;
                    }

                    offset = articleJson.getNext_offset();

                } else {
                    System.out.println("http请求错误");
                    break;
                }
                Thread.sleep(5000);
            }

            System.out.println("end");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void write(Article article) {
        System.out.println(article.getContent_url());
        String dir = "/Users/laijiawei/Documents/test/" + username + "/";
        String fileName = dir + "all.json";
        try {
            FileUtils.writeStringToFile(new File(fileName), article.getContent_url() + "\r\n", "UTF-8", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

