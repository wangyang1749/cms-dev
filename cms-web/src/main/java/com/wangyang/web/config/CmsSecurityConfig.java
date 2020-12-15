package com.wangyang.web.config;

//import org.springframework.security.authentication.*;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;


//@EnableWebSecurity
@Deprecated
public class CmsSecurityConfig {//  extends WebSecurityConfigurerAdapter {

//    @Autowired
//    UserDetailsService userDetailsService;
//
//    @Bean
//    PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
////    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService);
//    }
//
////    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/index.html","/hello","hello6");
//    }
//
////    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.cors().
//                and().authorizeRequests()
//                .antMatchers("/api/**")
//                .authenticated()
//                .and()
//                .formLogin()
//                .usernameParameter("username")
//                .passwordParameter("password")
//                .loginProcessingUrl("/login")
//                .successHandler((req, resp, authentication) -> {
//                    resp.setContentType("application/json;charset=utf-8");
//                    PrintWriter out = resp.getWriter();
//                    out.write(new ObjectMapper().writeValueAsString(BaseResponse.ok("Login success!!")));
//                    out.flush();
//                    out.close();
//                })
//                .failureHandler((req, resp, exception)->{
//                    resp.setContentType("application/json;charset=utf-8");
//                    PrintWriter out = resp.getWriter();
//                    BaseResponse baseResponse = new BaseResponse();
//                    baseResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//                    if (exception instanceof LockedException) {
//                        baseResponse.setMessage("账户被锁定，请联系管理员!");
//                    } else if (exception instanceof CredentialsExpiredException) {
//                        baseResponse.setMessage("密码过期，请联系管理员!");
//                    } else if (exception instanceof AccountExpiredException) {
//                        baseResponse.setMessage("账户过期，请联系管理员!");
//                    } else if (exception instanceof DisabledException) {
//                        baseResponse.setMessage("账户被禁用，请联系管理员!");
//                    } else if (exception instanceof BadCredentialsException) {
//                        baseResponse.setMessage("用户名或者密码输入错误，请重新输入!");
//                    }
//                    out.write(new ObjectMapper().writeValueAsString(baseResponse));
//                    out.flush();
//                    out.close();
//                })
//                .and()
//                .logout()
//                .logoutUrl("/logout")
//                .logoutSuccessHandler((request,resp,authentication) ->{
//                    resp.setContentType("application/json;charset=utf-8");
//                    PrintWriter out = resp.getWriter();
//                    out.write(new ObjectMapper().writeValueAsString(BaseResponse.ok("logout success!!")));
//                    out.flush();
//                    out.close();
//                })
//                .and().exceptionHandling()
//                .authenticationEntryPoint((req, resp, authException) -> {
//                    resp.setContentType("application/json;charset=utf-8");
//                    resp.setStatus(401);
//                    PrintWriter out = resp.getWriter();
//                    out.write(new ObjectMapper().writeValueAsString(BaseResponse.error("not authentication!!")));
//                    out.flush();
//                    out.close();
//                });
//
//        http.csrf().disable().exceptionHandling();
//    }
}
