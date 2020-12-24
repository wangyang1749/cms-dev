package com.wangyang.authorize.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wangyang.authorize.jwt.JWTFilter;
import com.wangyang.authorize.jwt.TokenProvider;
import com.wangyang.authorize.pojo.dto.CmsToken;
import com.wangyang.pojo.entity.User;
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
@RequestMapping
public class AuthenticationController {

    private  TokenProvider tokenProvider;

    private  AuthenticationManagerBuilder authenticationManagerBuilder;


    public AuthenticationController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/login")
    public String authorize(@Valid User loginDto,HttpServletResponse response,HttpServletRequest request) {

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
        SecurityContextHolder.getContext().setAuthentication(authentication);

//        boolean rememberMe = (loginDto.isRememberMe() == null) ? false : loginDto.isRememberMe();
        CmsToken token = tokenProvider.createToken(authentication, true);
        CmsToken refreshToken = tokenProvider.refreshToken(authentication,true);

        Cookie cookie = new Cookie(JWTFilter.AUTHORIZATION_HEADER,token.getToken());
        cookie.setPath("/");
//        cookie.setMaxAge(3600*24);
        Cookie refreshCookie = new Cookie(JWTFilter.REFRESH_HEAD,refreshToken.getToken());
        refreshCookie.setPath("/");
//        refreshCookie.setMaxAge(3600*24);
        response.addCookie(cookie);
        response.addCookie(refreshCookie);
//        response.addHeader(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + token);
//        response.addHeader(JWTFilter.REFRESH_HEAD, "Bearer " + refreshToken);
//        response.addHeader("expires_in", String.valueOf(token.getExp()));

//        String redirect = request.getParameter("redirect");
//        if(redirect!=null){
//            return "redirect:"+redirect;
//        }
        return "redirect:/user/info";
    }

    @PostMapping("/user/authenticate")
    @ResponseBody
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody User loginDto,HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        AuthenticationManager authenticationManager = authenticationManagerBuilder.getObject();

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

//        boolean rememberMe = (loginDto.isRememberMe() == null) ? false : loginDto.isRememberMe();
        CmsToken token = tokenProvider.createToken(authentication, true);
        CmsToken refreshToken = tokenProvider.refreshToken(authentication,true);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + token.getToken());
        httpHeaders.add(JWTFilter.REFRESH_HEAD, "Bearer " + refreshToken.getToken());
        httpHeaders.add("expires_in", String.valueOf(token.getExp()));
        Cookie cookie = new Cookie(JWTFilter.AUTHORIZATION_HEADER,token.getToken());
        cookie.setPath("/");
        Cookie refreshCookie = new Cookie(JWTFilter.REFRESH_HEAD,refreshToken.getToken());
        refreshCookie.setPath("/");
        response.addCookie(cookie);
        response.addCookie(refreshCookie);
        return new ResponseEntity<>(new JWTToken(token.getToken(),refreshToken.getToken(),String.valueOf(token.getExp())), httpHeaders, HttpStatus.OK);
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;
        private String refreshToken;
        private  String expires;



        public JWTToken(String idToken, String refreshToken, String expires) {
            this.idToken = idToken;
            this.refreshToken = refreshToken;
            this.expires = expires;
        }

        @JsonProperty("expires_in")
        public String getExpires() {
            return expires;
        }

        public void setExpires(String expires) {
            this.expires = expires;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }
}
