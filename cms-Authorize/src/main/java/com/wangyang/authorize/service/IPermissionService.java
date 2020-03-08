package com.wangyang.authorize.service;

import com.wangyang.authorize.pojo.entity.Permission;

import java.util.List;

public interface IPermissionService {
    List<Permission> findByUserId(int id);
}
