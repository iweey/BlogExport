package com.blogexport.model.weixin;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class Article {

    private String title;
    private String content_url;
    @JsonIgnore
    private Integer is_multi;

    @JsonIgnore
    //文章发布时间戳 单位 秒
    private Long postTime;

    private List<Article> multi_app_msg_item_list;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent_url() {
        return content_url;
    }

    public void setContent_url(String content_url) {
        this.content_url = content_url;
    }

    public Integer getIs_multi() {
        return is_multi;
    }

    public void setIs_multi(Integer is_multi) {
        this.is_multi = is_multi;
    }

    public Long getPostTime() {
        return postTime;
    }

    public void setPostTime(Long postTime) {
        this.postTime = postTime;
    }

    public List<Article> getMulti_app_msg_item_list() {
        return multi_app_msg_item_list;
    }

    public void setMulti_app_msg_item_list(List<Article> multi_app_msg_item_list) {
        this.multi_app_msg_item_list = multi_app_msg_item_list;
    }
}
