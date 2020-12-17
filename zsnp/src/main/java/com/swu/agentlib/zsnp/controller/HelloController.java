package com.swu.agentlib.zsnp.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器
 *
 * @author: @我没有三颗心脏
 * @create: 2018-05-08-下午 16:46
 */
@Controller
@RequestMapping(value = "")
public class HelloController {
//    @Value("${name}")
//    private String name;
//    @Value("${age}")
//    private String age;
//
//    @RequestMapping("/hello")
//    public String hello() {
//        return name + age;
//    }
    @RequestMapping(value = "/index")
    public String index() {
        return "index";
    }

}