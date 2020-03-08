package com.wangyang.authorize.service.impl;

import com.wangyang.authorize.pojo.entity.Permission;
import com.wangyang.authorize.repository.PermissionRepository;
import com.wangyang.authorize.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class PermissionServiceImpl implements IPermissionService {

//    @Autowired
//    PermissionRepository permissionRepository;


    @Override
    public List<Permission> findByUserId(int id) {
        Permission permission = new Permission();
        permission.setEnName("ROLE_ADMIN");
        return Arrays.asList(permission);
    }
}
