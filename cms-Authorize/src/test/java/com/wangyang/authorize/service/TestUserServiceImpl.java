package com.wangyang.authorize.service;

import com.wangyang.pojo.entity.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import javax.transaction.Transactional;

@Transactional
public class TestUserServiceImpl extends AbstractServiceTest{

    @Test
    public void testAdd(){
        User user = userService.add(addUser());
        Assert.assertNotNull(user);
    }

    @Test
    public void testFindUserByUserName(){
        User user = userService.add(addUser());
        User findUser = userRepository.findByUsername(user.getUsername());
        Assert.assertEquals(user,findUser);
    }

}
