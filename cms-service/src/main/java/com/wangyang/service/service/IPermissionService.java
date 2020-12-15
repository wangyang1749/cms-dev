package com.wangyang.service.service;

import com.wangyang.pojo.dto.PermissionDto;
import com.wangyang.pojo.entity.Permission;

import java.util.List;

public interface IPermissionService {
    List<Permission> findByUserId(int id);

    List<PermissionDto> listAll();
}
