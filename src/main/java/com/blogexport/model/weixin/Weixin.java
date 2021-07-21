package com.blogexport.model.weixin;

import com.geccocrawler.gecco.GeccoEngine;
import com.geccocrawler.gecco.annotation.*;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.spider.HtmlBean;

import java.util.List;

//@Gecco(matchUrl={"https://mp.weixin.qq.com/s/{queryString}"}, pipelines="WeixinPipeline")
@Gecco(matchUrl={"https://mp.weixin.qq.com/s?{queryString}"}, pipelines="WeixinPipeline")
public class Weixin implements HtmlBean {

    private static final long serialVersionUID = -7127412585200687225L;

    @Request
    private HttpRequest request;

    @RequestParameter("id")
    private String id;


    @Text(own=false)
    @HtmlField(cssPath="#activity-name")
    private String title;

    @Text(own=false)
    @HtmlField(cssPath="#copyright_logo")
    private String copyright;


    @Html
    @HtmlField(cssPath="#js_content")
    private String content;


    @Html()
    @HtmlField(cssPath="#publish_time")
    private String time;

    @Image(value={"data-src"})
    @HtmlField(cssPath="#js_content img")
    private List<String> images;


    @Override
    public String toString() {
        return "WeixinBlog{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", copyright='" + copyright + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

}

