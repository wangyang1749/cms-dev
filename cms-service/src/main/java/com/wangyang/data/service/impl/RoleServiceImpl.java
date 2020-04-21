package com.wangyang.data.service.impl;

import com.wangyang.data.repository.RoleRepository;
import com.wangyang.data.service.IRoleService;
import com.wangyang.model.pojo.entity.Role;
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
