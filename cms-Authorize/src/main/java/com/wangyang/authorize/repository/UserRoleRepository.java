package com.wangyang.authorize.repository;

import com.wangyang.authorize.pojo.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole,Integer> {
}
