package com.wangyang.authorize.controller;

import com.wangyang.authorize.config.service.UserDetailServiceImpl;
import com.wangyang.authorize.pojo.dto.UserDto;
import com.wangyang.data.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserDetailServiceImpl userService;

    @GetMapping("/getCurrent")
    public UserDto getCurrentUser(){
        return userService.getCurrentUser();
    }
}
