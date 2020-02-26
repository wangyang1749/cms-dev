package com.wangyang.cms.service;

import com.wangyang.cms.pojo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {
    User save(User user);
    User update(int id,User updateUser);
    void deleteById(int id);
    User findById(int id);
    User findByUserName(String username);
    Page<User> list(Pageable pageable);
}
