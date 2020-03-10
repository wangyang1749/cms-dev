package com.wangyang.authorize.repository;

import com.wangyang.authorize.pojo.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolePermissionRepository extends JpaRepository<RolePermission,Integer> {
}
