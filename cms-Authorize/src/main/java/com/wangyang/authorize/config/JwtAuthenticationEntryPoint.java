package com.wangyang.authorize.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangyang.common.BaseResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // This is invoked when user tries to access a secured REST resource without supplying any credentials
        // We should just send a 401 Unauthorized response because there is no 'login page' to redirect to
        // Here you can place any message you want

        String requestURI = request.getRequestURI();
        if(requestURI.startsWith("/api")){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setMessage(authException.getMessage());
            baseResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write(new ObjectMapper().writeValueAsString(baseResponse));
            out.flush();
            out.close();

//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
        }else {
            response.sendRedirect("/login?redirect="+requestURI);
        }
    }
}