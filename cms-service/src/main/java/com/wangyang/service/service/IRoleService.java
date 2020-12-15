package com.wangyang.service.service;

import com.wangyang.pojo.entity.Role;

import java.util.List;

public interface IRoleService {
    List<Role> findByUserId(Integer id);

    Role add(Role role);
}
