package com.wangyang.authorize.controller;

import com.wangyang.authorize.config.service.UserDetailServiceImpl;
import com.wangyang.service.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
@RequestMapping
public class UserController {

    @Autowired
    UserDetailServiceImpl userDetailService;

    @Autowired
    IUserService userService;


    @GetMapping("/login")
    public String login(){
        Optional<String> userName = userDetailService.getCurrentUserName();
        if(userName.isPresent()&&!userName.get().equals("anonymousUser")){
            return "redirect:/";
        }
        return "templates/user/login";
    }


    @GetMapping("/user/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
    //获取cookie
        Cookie[] cookies=request.getCookies();
        for(Cookie cookie: cookies){
            cookie.setMaxAge(0);
            cookie.setPath("/");  //路径一定要写上，不然销毁不了
            response.addCookie(cookie);
        }
        return "redirect:/";
    }


    @GetMapping("/user/loginSuccess")
    public String loginSuccess(Model model, HttpServletRequest request){
        String redirect = request.getParameter("redirect");
        model.addAttribute("url",redirect);
        return "templates/user/loginSuccess";
    }


}
