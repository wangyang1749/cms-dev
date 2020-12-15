package com.wangyang.authorize.service;

import com.wangyang.authorize.Application;
import com.wangyang.service.repository.UserRepository;
import com.wangyang.service.repository.UserRoleRepository;
import com.wangyang.service.service.IPermissionService;
import com.wangyang.service.service.IRoleService;
import com.wangyang.service.service.IUserService;
import com.wangyang.pojo.entity.Role;
import com.wangyang.pojo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;


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
