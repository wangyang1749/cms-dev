package com.wangyang.cms.controller.user;

import com.wangyang.authorize.jwt.JWTFilter;
import com.wangyang.authorize.jwt.TokenProvider;
import com.wangyang.authorize.pojo.entity.User;
import com.wangyang.cms.controller.api.AuthenticationController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserArticleController {

    private TokenProvider tokenProvider;

    private AuthenticationManagerBuilder authenticationManagerBuilder;


    public UserArticleController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }
    @GetMapping("/write")
    public String writeArticle(){
        return "user/write";
    }

    @GetMapping("/login")
    public String login(){
        return "templates/user/login";
    }

    @PostMapping("/login")
    public String authorize(@Valid User loginDto, HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        AuthenticationManager authenticationManager = authenticationManagerBuilder.getObject();

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

//        boolean rememberMe = (loginDto.isRememberMe() == null) ? false : loginDto.isRememberMe();
        String jwt = tokenProvider.createToken(authentication, true);

//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        Cookie cookie = new Cookie(JWTFilter.AUTHORIZATION_HEADER,jwt);
//        cookie.setComment("auth purpose");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
        return "templates/user/login";
//        return new ResponseEntity<>(new AuthenticationController.JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }
}
