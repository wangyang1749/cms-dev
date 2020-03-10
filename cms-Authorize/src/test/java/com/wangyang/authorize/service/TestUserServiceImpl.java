package com.wangyang.authorize.service;

import com.wangyang.authorize.pojo.entity.User;
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

}
