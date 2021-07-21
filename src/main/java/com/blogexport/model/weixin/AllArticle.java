package com.blogexport.model.weixin;

import java.util.List;

public class AllArticle {

    private CommonMsg comm_msg_info;
    private Article app_msg_ext_info;

    public CommonMsg getComm_msg_info() {
        return comm_msg_info;
    }

    public void setComm_msg_info(CommonMsg comm_msg_info) {
        this.comm_msg_info = comm_msg_info;
    }

    public Article getApp_msg_ext_info() {
        return app_msg_ext_info;
    }

    public void setApp_msg_ext_info(Article app_msg_ext_info) {
        this.app_msg_ext_info = app_msg_ext_info;
    }

}
