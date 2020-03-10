package com.wangyang.authorize.service.impl;

import com.wangyang.authorize.pojo.entity.Role;
import com.wangyang.authorize.repository.RoleRepository;
import com.wangyang.authorize.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public List<Role> findByUserId(Integer id) {
        return roleRepository.findByUserId(id);
    }

    @Override
    public Role add(Role role){
        return roleRepository.save(role);
    }
}
