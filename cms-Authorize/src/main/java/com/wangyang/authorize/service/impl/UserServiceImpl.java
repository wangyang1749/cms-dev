package com.wangyang.authorize.service.impl;

import com.wangyang.authorize.pojo.entity.User;
import com.wangyang.authorize.repository.UserRepository;
import com.wangyang.authorize.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

//    @Autowired
//    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public User findByUsername(String username) {
        User user = new User();
        user.setUsername("wangyang");
        user.setPassword(passwordEncoder.encode("123456"));
        return user;
    }
}
