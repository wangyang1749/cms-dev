package com.wangyang.service.repository;

import com.wangyang.pojo.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission,Integer> {

    @Query(value = "select p from User as u " +
            "Left join UserRole as ur     on u.id=ur.userId " +
            "Left join Role as r          on r.id=ur.roleId " +
            "Left join RolePermission as rp  on r.id=rp.roleId " +
            "Left join Permission as p on p.id=rp.permissionId " +
            "where u.id=  ?1")
    List<Permission> findByUserId(int id);

}
