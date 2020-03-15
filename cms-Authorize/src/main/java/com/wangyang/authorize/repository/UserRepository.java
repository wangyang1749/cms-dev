package com.wangyang.authorize.repository;

import com.wangyang.authorize.pojo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

    User findByUsername(String username);
}
