package com.blogexport.pipeline;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.blogexport.model.sina.SinaBlog;
import com.blogexport.model.sina.SinaComment;
import com.geccocrawler.gecco.annotation.PipelineName;
import com.geccocrawler.gecco.pipeline.Pipeline;
import com.geccocrawler.gecco.spider.SpiderBean;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@PipelineName("SinaBlogPipeline")
public class SinaBlogPipeline implements Pipeline<SpiderBean> {

    //处理入口
    @Override
    public void process(SpiderBean bean) {
        if (bean.getClass().equals(SinaBlog.class)) {
            try {
                exeSinaBlog(bean);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void exeSinaBlog(SpiderBean bean) throws IOException {

        SinaBlog sinaBlog = (SinaBlog) bean;

        if (StringUtils.isBlank(sinaBlog.getTitle())) {
            return;
        }

        if (sinaBlog.getTitle().equals("相关博文")) {
            sinaBlog.setTitle(sinaBlog.getSubtitle());
        }

        //设置博客发布时间
        String time = sinaBlog.getTime();
        time = time.replace("(", "");
        time = time.replace(")", "");
        sinaBlog.setTime(time);

        //根据文章模板生成文章的html
        String templatePath = this.getClass().getClassLoader().getResource("static/html/SinaBlogTemplate.html").getPath();
        String html = FileUtils.readFileToString(new File(templatePath), "UTF-8");
        html = html.replace("#title#", sinaBlog.getTitle());
        html = html.replace("#time#", sinaBlog.getTime());
        html = html.replace("#content#", sinaBlog.getContent());

        String imageDic = sinaBlog.getRequest().getParameter("homeDic") + File.separator + "image" + File.separator;
        html = html.replace("#imageDic#", imageDic + File.separator);

        List<SinaComment> commentList = getSinaComment(sinaBlog.getId());

        String comments = "";

        for (SinaComment comment : commentList) {

            String content = comment.getContent();

            content = content.replaceAll("(\\\\r\\\\n|\\\\r|\\\\n|\\\\n\\\\r)", "<br/>");

            String s = "<div class=\"msgBody\"><p class=\"userName\">" + comment.getNick()
                    + "</p><p class=\"replyBody\">" + content
                    + "</p><div class=\"msgBodyReply\"><div class=\"msgBodyReplyList\"></div></div></div>";

            comments += s;
        }

        html = html.replace("#comments#", comments);

        //保存html到本地
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(sinaBlog.getTime());
            String sTime = String.valueOf(date.getTime() / 1000);
            String htmlDic = sinaBlog.getRequest().getParameter("homeDic") + File.separator + "html" + File.separator;
            String filePath = htmlDic + File.separator + sTime + ".html";
            FileUtils.writeStringToFile(new File(filePath), html, "UTF-8", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取博客评论
     *
     * @param blogId 博客文章Id
     * @return
     */
    public List<SinaComment> getSinaComment(String blogId) {
        List<SinaComment> comments = new ArrayList<>();
        try {
            Thread.sleep(3000);
            HttpClient client = HttpClients.createDefault();
            String url = "http://comment5.news.sina.com.cn/page/info?channel=blog&newsid=" + blogId + "&_=16238567454251&page=1&page_size=200&oe=utf-8&score=&fake=1&thread=1&list=asc&t_size=1000&varname=requestId_78648559";
            HttpResponse response = client.execute(new HttpGet(url));
            HttpEntity entity = response.getEntity();
            if (response.getStatusLine().getStatusCode() == 200) {
                String res = EntityUtils.toString(entity);
                JSONObject jsonObject1 = (JSONObject) JSON.parse(res);
                JSONObject jsonObject2 = (JSONObject) JSON.parse(jsonObject1.getString("result"));
                List<SinaComment> tmpSinaComments = JSON.parseObject(jsonObject2.getString("cmntlist"), new TypeReference<List<SinaComment>>() {
                });
                if (tmpSinaComments != null && tmpSinaComments.size() > 0) {
                    comments = tmpSinaComments;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return comments;
    }

}
