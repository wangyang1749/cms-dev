package com.wangyang.authorize.repository;

import com.wangyang.authorize.pojo.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission,Integer> {
}
