package com.blogexport.controller;

import com.blogexport.common.SingletonQueue;
import com.blogexport.pipeline.WeixinPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class WeixinController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeixinPipeline.class);


    @RequestMapping("/weixin")
    public void weixin(String link) {
        if (link.startsWith("https://mp.weixin.qq.com/mp/profile_ext?action=getmsg")) {
            System.out.println(link);
            LOGGER.debug("request weixin url:"+link);
            SingletonQueue.getInstance().add(link);
        }
    }

}
