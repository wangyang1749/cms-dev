package com.wangyang.authorize.service;

import com.wangyang.authorize.pojo.entity.Role;

import java.util.List;

public interface IRoleService {
    List<Role> findByUserId(Integer id);

    Role add(Role role);
}
