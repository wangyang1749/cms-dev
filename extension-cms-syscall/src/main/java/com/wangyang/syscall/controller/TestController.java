package com.wangyang.syscall.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("syscallTest")
@RequestMapping("/api")
public class TestController {

    @GetMapping("/test1")
    public String test(){
        return "Hello Word";
    }
}
