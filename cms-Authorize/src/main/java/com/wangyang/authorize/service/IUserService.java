package com.wangyang.authorize.service;

import com.wangyang.authorize.pojo.dto.UserDto;
import com.wangyang.authorize.pojo.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IUserService {

    UserDto findByUsername(String username);

    User add(User user);

    User findById(int id);


    Optional<User> findOptionalBy(int userId);

    UserDto getCurrentUser();

    String getCurrentUserName();

    List<User> findAllById(Collection<Integer> ids);
}
