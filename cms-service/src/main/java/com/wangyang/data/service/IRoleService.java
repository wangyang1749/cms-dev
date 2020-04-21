package com.wangyang.data.service;

import com.wangyang.model.pojo.entity.Role;

import java.util.List;

public interface IRoleService {
    List<Role> findByUserId(Integer id);

    Role add(Role role);
}
