package com.wangyang.data.service;

import com.wangyang.model.pojo.dto.PermissionDto;
import com.wangyang.model.pojo.entity.Permission;

import java.util.List;

public interface IPermissionService {
    List<Permission> findByUserId(int id);

    List<PermissionDto> listAll();
}
