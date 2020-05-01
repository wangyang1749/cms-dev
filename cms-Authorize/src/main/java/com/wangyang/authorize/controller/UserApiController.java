package com.wangyang.authorize.controller;

import com.wangyang.authorize.config.service.UserDetailServiceImpl;
import com.wangyang.authorize.pojo.dto.SpringUserDto;
import com.wangyang.data.service.IUserService;
import com.wangyang.model.pojo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/user")
public class UserApiController {

    @Autowired
    UserDetailServiceImpl userDetailService;

    @Autowired
    IUserService userService;

    @GetMapping
    public Page<User> pageBy(@PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        return userService.pageBy(pageable);
    }


    @PostMapping
    public User addUser(@RequestBody User user){
        return userDetailService.addUser(user);
    };

    @GetMapping("/getCurrent")
    @ResponseBody
    public SpringUserDto getCurrentUser(){
        return userDetailService.getCurrentUser();
    }
}
