package com.wangyang.authorize.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wangyang.authorize.jwt.JWTFilter;
import com.wangyang.authorize.jwt.TokenProvider;
import com.wangyang.model.pojo.entity.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class AuthenticationController {

    private  TokenProvider tokenProvider;

    private  AuthenticationManagerBuilder authenticationManagerBuilder;


    public AuthenticationController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/login")
    public String authorize(@Valid User loginDto,HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        AuthenticationManager authenticationManager = authenticationManagerBuilder.getObject();

        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            e.printStackTrace();

            return "redirect:/user/login";
        }

//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

//        boolean rememberMe = (loginDto.isRememberMe() == null) ? false : loginDto.isRememberMe();
        String jwt = tokenProvider.createToken(authentication, true);

//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        Cookie cookie = new Cookie(JWTFilter.AUTHORIZATION_HEADER,jwt);
//        cookie.setComment("auth purpose");
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
//        String redirect = request.getParameter("redirect");
//        String resUrl = "redirect:";
//        if(redirect!=null){
//            resUrl = resUrl+"/user/loginSuccess?redirect="+redirect;
//        }else {
//            resUrl = resUrl+"/user/loginSuccess";
//        }
        return "redirect:/user/info";
//        return new ResponseEntity<>(new AuthenticationController.JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    @ResponseBody
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody User loginDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        AuthenticationManager authenticationManager = authenticationManagerBuilder.getObject();

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

//        boolean rememberMe = (loginDto.isRememberMe() == null) ? false : loginDto.isRememberMe();
        String jwt = tokenProvider.createToken(authentication, true);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
