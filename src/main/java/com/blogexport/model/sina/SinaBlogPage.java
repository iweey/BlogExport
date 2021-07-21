package com.blogexport.model.sina;

import com.geccocrawler.gecco.annotation.*;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.spider.HtmlBean;

import java.util.List;

@Gecco(matchUrl = "http://blog.sina.com.cn/s/articlelist_{userid}_0_{currPage}.html", pipelines = "SinaBlogPipeline", timeout = 3000)
public class SinaBlogPage implements HtmlBean {

    private static final long serialVersionUID = -7127412585200687225L;

    @Request
    private HttpRequest request;


    @RequestParameter("currPage")
    private String currPage;


    @Href(click = true)
    @HtmlField(cssPath = ".article_blk .atc_title a")
    private List<String> urls;

    @Text(own = false)
    @HtmlField(cssPath = ".article_blk .atc_title a")
    private List<String> titles;

    @Text(own = false)
    @HtmlField(cssPath = " .atc_tm")
    private List<String> times;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public String getCurrPage() {
        return currPage;
    }

    public void setCurrPage(String currPage) {
        this.currPage = currPage;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public List<String> getTimes() {
        return times;
    }

    public void setTimes(List<String> times) {
        this.times = times;
    }

}
