package com.blogexport.model.weixin;

public class ArticleJson {

    private Integer ret;
    private Integer can_msg_continue;
    private String general_msg_list;
    private Integer next_offset;

    public Integer getRet() {
        return ret;
    }

    public void setRet(Integer ret) {
        this.ret = ret;
    }

    public Integer getCan_msg_continue() {
        return can_msg_continue;
    }

    public void setCan_msg_continue(Integer can_msg_continue) {
        this.can_msg_continue = can_msg_continue;
    }

    public String getGeneral_msg_list() {
        return general_msg_list;
    }

    public void setGeneral_msg_list(String general_msg_list) {
        this.general_msg_list = general_msg_list;
    }

    public Integer getNext_offset() {
        return next_offset;
    }

    public void setNext_offset(Integer next_offset) {
        this.next_offset = next_offset;
    }
}
