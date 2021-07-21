package com.blogexport.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class TestController {


    @RequestMapping("/test")
    public String test(HttpServletResponse httpServletResponse, HttpServletRequest request) {
        return "index";
    }


}
