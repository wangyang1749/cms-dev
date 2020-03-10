package com.wangyang.authorize.service;

import com.wangyang.authorize.pojo.dto.PermissionDto;
import com.wangyang.authorize.pojo.entity.Permission;

import java.util.List;

public interface IPermissionService {
    List<Permission> findByUserId(int id);

    List<PermissionDto> listAll();
}
