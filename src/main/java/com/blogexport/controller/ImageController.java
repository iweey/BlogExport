package com.blogexport.controller;

import com.blogexport.model.Image;
import com.blogexport.pipeline.WeixinPipeline;
import com.blogexport.util.ImageUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class ImageController {


    @Value("${image.path}")
    private String imagePath;


    private ConcurrentHashMap<String,Boolean> imageStatus=new ConcurrentHashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(WeixinPipeline.class);


    @PostMapping("/downloadImage")
    public boolean downloadImage(@RequestBody List<Image> images) {

        //开多线程下载  TODO

        if (images == null || images.size() == 0) {
            return false;
        }
        for (Image image : images) {

            String filePath = imagePath + File.separator + image.getFileName();

            LOGGER.info("download image url:"+image.getUrl());
            LOGGER.info("download image filePath:"+filePath);

            try {
                ImageUtil.downloadImage(image.getUrl(), filePath);
            }catch (Exception e){
                LOGGER.error("download image error",e);
            }

        }

        return true;
    }

    @GetMapping(value = "/image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getImage(@PathVariable String id) throws Exception {
        try {

            String filePath = imagePath + File.separator + id;

            return FileUtils.readFileToByteArray(new File(filePath));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new byte[]{};

    }


}