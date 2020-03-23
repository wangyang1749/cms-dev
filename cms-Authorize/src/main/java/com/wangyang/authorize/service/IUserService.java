package com.wangyang.authorize.service;

import com.wangyang.authorize.pojo.dto.UserDto;
import com.wangyang.authorize.pojo.entity.User;

import java.util.Collection;
import java.util.List;

public interface IUserService {

    UserDto findByUsername(String username);

    User add(User user);

    User findById(int id);


    List<User> findAllById(Collection<Integer> ids);
}
