package com.wangyang.cms.service;

//import com.wangyang.authorize.pojo.entity.Permission;
//import com.wangyang.authorize.pojo.entity.User;
//import com.wangyang.authorize.repository.PermissionRepository;
//import com.wangyang.authorize.repository.UserRepository;
//import com.wangyang.authorize.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;

import java.util.List;

@SpringBootTest
public class TestUserService {
//
//    @Autowired
//    IUserService userService;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    PermissionRepository permissionRepository;
//    @Test
//    public void testAdd(){
//        User user = userService.findByUsername("wangyang");
//        System.out.println(user.getUsername());
//    }
//
//    @Test
//    public void test2(){
//        User user = new User();
//        user.setUsername("wangyang");
//        Example<User> example = Example.of(user);
//
//        List<User> userList = userRepository.findAll(example);
//    }
//
//    @Test
//    public void test3(){
//        List<Permission> permissionList = permissionRepository.findByUserId(1);
//        permissionList.forEach(permission -> {
//            System.out.println(permission.getEnName());
//        });
//        System.out.println(permissionList.size());
//    }
}
