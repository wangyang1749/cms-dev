package com.wangyang.authorize.service;

import com.wangyang.authorize.pojo.entity.User;

public interface IUserService {

    User findByUsername(String username);
}
