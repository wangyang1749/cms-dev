package com.wangyang.authorize.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.PrintWriter;
//
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/oauth/check_token");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password(passwordEncoder().encode("123456"))
                .roles("ADMIN");
    }
//
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/index.html","/hello","hello6");
//    }
//
//    //    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.cors().
//                and().authorizeRequests()
//                .antMatchers("/api/**")
//                .hasRole("ADMIN")
//                .and()
//                .formLogin()
//                .usernameParameter("username")
//                .passwordParameter("password")
//                .loginProcessingUrl("/login")
//                .successHandler((req, resp, authentication) -> {
//                    resp.setContentType("application/json;charset=utf-8");
//                    PrintWriter out = resp.getWriter();
//                    out.flush();
//                    out.close();
//                })
//                .failureHandler((req, resp, exception)->{
//                    resp.setContentType("application/json;charset=utf-8");
//                    PrintWriter out = resp.getWriter();
//
//                    out.flush();
//                    out.close();
//                })
//                .and()
//                .logout()
//                .logoutUrl("/logout")
//                .logoutSuccessHandler((request,resp,authentication) ->{
//                    resp.setContentType("application/json;charset=utf-8");
//                    PrintWriter out = resp.getWriter();
//                    out.flush();
//                    out.close();
//                })
//                .and().exceptionHandling()
//                .authenticationEntryPoint((req, resp, authException) -> {
//                    resp.setContentType("application/json;charset=utf-8");
//                    resp.setStatus(401);
//                    PrintWriter out = resp.getWriter();
//                    out.flush();
//                    out.close();
//                });
//
//        http.csrf().disable().exceptionHandling();
//    }
}
