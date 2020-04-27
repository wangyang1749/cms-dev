package com.wangyang.authorize.controller;

import com.wangyang.authorize.config.service.UserDetailServiceImpl;
import com.wangyang.authorize.pojo.dto.UserDto;
import com.wangyang.data.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @GetMapping("/info")
    public String info(){
        return "templates/user/info";
    }
    @GetMapping("/logout")
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


    @GetMapping("/loginSuccess")
    public String loginSuccess(Model model, HttpServletRequest request){
        String redirect = request.getParameter("redirect");
        model.addAttribute("url",redirect);
        return "templates/user/loginSuccess";
    }


}
