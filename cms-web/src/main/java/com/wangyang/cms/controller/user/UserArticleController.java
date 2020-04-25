package com.wangyang.cms.controller.user;


import com.wangyang.data.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserArticleController {
    @Autowired
    IUserService userService;

    @GetMapping("/write")
    public String writeArticle(){
        return "user/write";
    }




}
