package com.wangyang.authorize.service;

import com.wangyang.authorize.Application;
import com.wangyang.authorize.pojo.entity.Role;
import com.wangyang.authorize.pojo.entity.User;
import com.wangyang.authorize.repository.UserRepository;
import com.wangyang.authorize.repository.UserRoleRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@TestPropertySource (locations = {"classpath:application.properties"})
@SpringBootTest(classes = Application.class)
//@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractServiceTest {
    @Autowired
    IUserService userService;

    @Autowired
    IRoleService roleService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    IPermissionService permissionService;

    public User addUser(){
        User user = new User();
        user.setUsername("wangyang1749748955555");
        user.setPassword("123456");
        return user;
    }

    public Role addRole(){
        Role role = new Role();
        role.setEnName("ROLE_ADMIN");
        return role;
    }

}
