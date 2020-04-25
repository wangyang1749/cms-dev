package com.wangyang.authorize.controller;

import com.wangyang.authorize.config.service.UserDetailServiceImpl;
import com.wangyang.authorize.pojo.dto.UserDto;
import com.wangyang.data.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserDetailServiceImpl userService;

    @GetMapping("/login")
    public String login(){
        Optional<String> userName = userService.getCurrentUserName();
        if(userName.isPresent()){
            return "redirect:/";
        }
        return "templates/user/login";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(){
        return "templates/user/loginSuccess";
    }


}
