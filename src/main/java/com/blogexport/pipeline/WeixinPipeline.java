package com.blogexport.pipeline;

import com.blogexport.model.Image;
import com.blogexport.model.weixin.Weixin;
import com.blogexport.util.HttpUtil;
import com.geccocrawler.gecco.annotation.PipelineName;
import com.geccocrawler.gecco.pipeline.Pipeline;
import com.geccocrawler.gecco.spider.SpiderBean;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@PipelineName("WeixinPipeline")
public class WeixinPipeline implements Pipeline<SpiderBean> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeixinPipeline.class);

    @Override
    public void process(SpiderBean bean) {
        if (bean.getClass().equals(Weixin.class)) {
            exeWeixin(bean);
        }
    }


    public void exeWeixin(SpiderBean bean) {

        Weixin weixin = (Weixin) bean;

        String imageServer = weixin.getRequest().getParameter("imageServer");

        //发送下载图片到远程服务器
        //微信禁用代理访问图片
        List<String> images = weixin.getImages();
        if (images != null && images.size() > 0) {
            List<Image> list = new ArrayList<>();
            for (String imgUrl : images) {
                Image image = new Image();
                String[] arr = imgUrl.split("/");
                String fileName = arr[4];
                image.setFileName(fileName);
                image.setUrl(imgUrl);
                list.add(image);
            }
            HttpUtil.post(imageServer+"/downloadImage", list);
        }

        //TODO 判断图片是否下载完成



        try {

            String templatePath = this.getClass().getClassLoader().getResource("static/html/WeixinTemplate.html").getPath();

            String html = FileUtils.readFileToString(new File(templatePath), "UTF-8");

            html = html.replaceAll("#title#", weixin.getTitle());

            String postTime = weixin.getRequest().getParameter("postTime");

            String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(postTime)*1000));

            html = html.replace("#time#", time);
            html = html.replace("#content#", weixin.getContent());
            html = html.replace("#imageDic#", imageServer + File.separator+"image"+File.separator);

            String htmlDic = weixin.getRequest().getParameter("htmlDic");
            String filePath = htmlDic + File.separator + postTime + ".html";
            FileUtils.writeStringToFile(new File(filePath), html, "UTF-8", false);

        } catch (Exception e) {
            LOGGER.error("微信保存html错误",e);
        }

    }


}
