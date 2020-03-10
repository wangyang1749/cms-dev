package com.wangyang.authorize.config;

import com.wangyang.authorize.exception.NotFoundPageException;
import com.wangyang.authorize.pojo.dto.PermissionDto;
import com.wangyang.authorize.pojo.entity.Role;
import com.wangyang.authorize.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

@Component
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    //    @Autowired
//    MenuService menuService;
    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    IPermissionService permissionService;
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        List<PermissionDto> permissionDtos = permissionService.listAll();
        for (PermissionDto permissionDto : permissionDtos){
            if(antPathMatcher.match(permissionDto.getUrl(), requestUrl)){
                List<Role> roles = permissionDto.getRoles();
                String[] str = new String[roles.size()];
                for (int i = 0; i < roles.size(); i++) {
                    str[i] = roles.get(i).getEnName();
                }
                return SecurityConfig.createList(str);
            }
        }
//        return SecurityConfig.createList("ROLE_LOGIN");
        throw new AccessDeniedException("页面不存在");

    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}