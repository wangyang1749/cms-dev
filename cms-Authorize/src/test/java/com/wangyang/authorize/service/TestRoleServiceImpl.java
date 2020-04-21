package com.wangyang.authorize.service;

import com.wangyang.model.pojo.entity.Role;
import com.wangyang.model.pojo.entity.User;
import com.wangyang.model.pojo.entity.UserRole;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class TestRoleServiceImpl extends AbstractServiceTest{

    @Test
    public void testFindRoleById(){
        //添加用户
        User user = userService.add(addUser());
        //添加角色
        Role role1 = roleService.add(addRole());
        //添加用户角色关系
        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(role1.getId());
        userRoleRepository.save(userRole);
        List<Role> roles = roleService.findByUserId(user.getId());
        Assert.assertEquals("ROLE_ADMIN", roles.get(0).getEnName());

    }
}
