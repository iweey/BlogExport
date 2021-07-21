package com.blogexport;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blogexport.model.weixin.AllArticle;
import com.blogexport.model.weixin.Article;
import com.blogexport.model.weixin.ArticleJson;
import com.geccocrawler.gecco.GeccoEngine;
import com.geccocrawler.gecco.request.HttpPostRequest;
import com.geccocrawler.gecco.utils.UrlMatcher;
import com.google.common.reflect.TypeToken;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws IOException {

//        String url = "https://mp.weixin.qq.com/mp/profile_ext?action=getmsg&__biz=MzI5NzA5MDEzNg==&f=json&offset=0&count=10&is_ok=1&scene=124&uin=ODcyOTUzNzIw&key=6d2826151309341c43bb1a9449ca60f81aac85fe99f50a2abe792d20d5facb7b5baeb30e22f654fbf306e58f69d1c72fb4f463ead7c82500d0aaa5fc63157915e3a874536ced626761ba1e21cb5d35ba9f3bdf729d8eb4264838f3ca11642b21c270b769324ea866b4e28a92f84430636467b871de492a626a6a4a78a530800b&pass_ticket=3RbwFp%2Fsq0nJvqKDjc5rcUTbY7ty2KTfnwqg8C%2BmF4zSd7ZpzC4z8%2BumpodlawsL&wxtoken=&appmsg_token=1122_nqcyy55LWQOk60NHoUt5RvKY7hgzWxE_eJ0Rlw~~&x5=0&f=json";
//
//
//       String[] s= url.split("offset=");
//
//       url=s[0]+s[1]+"&offset=0";
//
//
//        System.out.println(url);

//        String url="https://mp.weixin.qq.com/s?__biz=MzI5NzA5MDEzNg==&amp;mid=2649984204&amp;idx=1&amp;sn=0c837ce66647948d1893a53550ed4284&amp;chksm=f4bd41fbc3cac8eddba46f04041450b3eda1f4b04d576711172b74dea60fa9bfa17381ea4742&amp;scene=27#wechat_redirect";
//
////        String urlPattern="https://mp.weixin.qq.com/s/*";
//        String urlPattern="*";
//
//        Map<String, String> params = UrlMatcher.match(url, urlPattern);
//
//        System.out.println(params);



//        GeccoEngine.create()
//                .classpath("com.blogexport")
//                //开始抓取的页面地址
////                .seed("https://mp.weixin.qq.com/s/2rQJ9o30pNal9rlp5XlOqw")
//                .seed("https://mp.weixin.qq.com/s?__biz=MzI5NzA5MDEzNg==&amp;mid=2649984204&amp;idx=1&amp;sn=0c837ce66647948d1893a53550ed4284&amp;chksm=f4bd41fbc3cac8eddba46f04041450b3eda1f4b04d576711172b74dea60fa9bfa17381ea4742&amp;scene=27#wechat_redirect")
//                //开启几个爬虫线程,线程数量最好不要大于seed request数量
//                .thread(1)
//                //单个爬虫每次抓取完一个请求后的间隔时间
//                .interval(5000)
//                //循环抓取
//                .loop(false)
//                //采用pc端userAgent
//                .mobile(false)
//                //是否开启debug模式，跟踪页面元素抽取
//                .debug(true)
//                //非阻塞方式运行
//                .run();

//        List<Article> articles=new ArrayList<>();
//
//
//        String res = FileUtils.readFileToString(new File("/Users/laijiawei/Documents/source/java/blog-export/src/main/resources/weixin.json"));
//        ArticleJson articleJson = JSON.parseObject(res, ArticleJson.class);
//        JSONObject jsonObject = JSON.parseObject(articleJson.getGeneral_msg_list());
//        List<AllArticle> allArticleList = JSON.parseObject(jsonObject.getString("list"), new TypeToken<List<AllArticle>>() {}.getType());
//        for (AllArticle allArticle : allArticleList) {
//            Article tmpArticle = allArticle.getApp_msg_ext_info();
//
//            Long datetime = Long.parseLong(allArticle.getComm_msg_info().getDatetime());
//
//            tmpArticle.setDatetime(datetime);
//
//            if (tmpArticle != null) {
//                articles.add(tmpArticle);
////                            write(tmpArticle);
//                if (tmpArticle.getIs_multi() == 1) {
//                    List<Article> multi_app_msg_item_list = allArticle.getApp_msg_ext_info().getMulti_app_msg_item_list();
//                    if (multi_app_msg_item_list != null) {
//
////                                    articles.addAll(multi_app_msg_item_list);
//
//                        for (Article article : multi_app_msg_item_list) {
//                            datetime++;
//                            article.setDatetime(datetime);
//                            articles.add(article);
//
////                                        write(article);
//                        }
//                    }
//                }
//            }
//        }


//        String sp1 = System.getProperty("user.dir");
//
//        System.out.println(sp1);
//
//        System.out.println("end");


        Document doc = Jsoup.connect("http://blog.sina.com.cn/s/articlelist_1413142663_0_1.html").get();
        Elements elements = doc.select(".SG_pages span");
        Integer pageCount=  Integer.parseInt(elements.text().replace("共","").replace("页",""));

        System.out.println(pageCount);

    }

    public void getName() throws IOException {

        String fileName = "/Users/laijiawei/Documents/source/java/blog-export/src/main/resources/static/html/SinaBlogTemplate.html";
        String html = FileUtils.readFileToString(new File(fileName), "UTF-8");

        System.out.println(html);
    }
}
