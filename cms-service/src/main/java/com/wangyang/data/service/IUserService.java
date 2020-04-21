package com.wangyang.data.service;

import com.wangyang.model.pojo.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IUserService {



    User add(User user);

    User findById(int id);


    Optional<User> findOptionalBy(int userId);


    List<User> findAllById(Collection<Integer> ids);
}
