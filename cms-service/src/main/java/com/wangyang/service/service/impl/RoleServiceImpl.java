package com.wangyang.service.service.impl;

import com.wangyang.service.repository.RoleRepository;
import com.wangyang.service.service.IRoleService;
import com.wangyang.pojo.entity.Role;
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
