package com.wangyang.authorize.jwt;

import com.wangyang.authorize.pojo.dto.SpringUserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;


public class JWTFilter extends GenericFilterBean {

    private static final Logger LOG = LoggerFactory.getLogger(JWTFilter.class);

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private TokenProvider tokenProvider;



    public JWTFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();
        String token = httpServletRequest.getHeader("Cms-Token");
        if(tokenProvider.validateTokenCustomize(token)){
            SecurityContextHolder.getContext().setAuthentication(tokenProvider.getAuthenticationCustomize(token));
        } else  if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) { //验证jwt
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            SpringUserDto springUserDto = (SpringUserDto)authentication.getPrincipal();
            httpServletRequest.setAttribute("userId", springUserDto.getId());
            httpServletRequest.setAttribute("username",authentication.getName());
            LOG.debug("set Authentication to security context for '{}', uri: {}", authentication.getName(), requestURI);
        } else {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_ANONYMOUS");
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            grantedAuthorities.add(grantedAuthority);
//            User principal = new User("匿名用户", "", grantedAuthorities);

//            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(principal,"",grantedAuthorities));
            SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken("doesNotMatter","anonymousUser",grantedAuthorities));
            LOG.debug("no valid JWT token found, uri: {}", requestURI);
        }


        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        String requestURI = request.getRequestURI();


        String header = request.getHeader("accept");
        String headerAccept =" ";
        if(header!=null){
          headerAccept= header.split(",")[0];
        }

        String type = "";
        if(request.getHeader("AuthorizeType")!=null){
            type = request.getHeader("AuthorizeType");

        }


        if(headerAccept.equals("text/html")||type.equals("Cookie")){
            Cookie[] cookies = request.getCookies();
            if(cookies!=null){
                for (int i = 0;i<cookies.length;i++){
                    Cookie cookie = cookies[i];
                    if(cookie.getName().equals("Authorization")){
                        return cookie.getValue();
                    }
                }
            }
        } else{

            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
                return bearerToken.substring(7);
            }
        }
        return null;
    }
}
