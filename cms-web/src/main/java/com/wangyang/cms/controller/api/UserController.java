package com.wangyang.cms.controller.api;

import com.wangyang.authorize.pojo.dto.UserDto;
import com.wangyang.authorize.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    IUserService userService;

    @GetMapping("/getCurrent")
    public UserDto getCurrentUser(){
        return userService.getCurrentUser();
    }
}
