package com.wangyang.authorize.config;

import com.wangyang.pojo.dto.PermissionDto;
import com.wangyang.service.service.IPermissionService;
import com.wangyang.pojo.entity.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

@Component
@Slf4j
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    //    @Autowired
//    MenuService menuService;
    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    IPermissionService permissionService;
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
//        log.info("开始获取请求地址需要的权限");
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        List<PermissionDto> permissionDtos = permissionService.listAll();
        for (PermissionDto permissionDto : permissionDtos){
            if(antPathMatcher.match(permissionDto.getUrl(), requestUrl)){
                List<Role> roles = permissionDto.getRoles();
                String[] str = new String[roles.size()];
                for (int i = 0; i < roles.size(); i++) {
                    str[i] = roles.get(i).getEnName();
                }
//                log.info("请求需要"+str);
                return SecurityConfig.createList(str);
            }
        }
        return SecurityConfig.createList("ROLE_ANONYMOUS");

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