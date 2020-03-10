package com.wangyang.authorize.service.impl;

import com.wangyang.authorize.exception.UserException;
import com.wangyang.authorize.pojo.entity.User;
import com.wangyang.authorize.repository.UserRepository;
import com.wangyang.authorize.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public User findByUsername(String username) {
        User user = new User();
        user.setUsername(username);
        List<User> userList = userRepository.findAll(Example.of(user));
        if (CollectionUtils.isEmpty(userList)){
            throw new UserException("用户名不存在!!!");
        }
        return userList.get(0);
    }
}
