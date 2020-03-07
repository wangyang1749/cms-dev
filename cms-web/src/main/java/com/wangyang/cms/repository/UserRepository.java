package com.wangyang.cms.repository;

import com.wangyang.cms.pojo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

    User findByUsername(String  username);
}
