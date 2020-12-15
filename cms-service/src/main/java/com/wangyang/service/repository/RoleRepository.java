package com.wangyang.service.repository;

import com.wangyang.pojo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role,Integer> {

    @Query("select r from Role as r , " +
            "UserRole as ur " +
            "where r.id=ur.roleId and ur.userId=?1 ")
    List<Role> findByUserId(Integer id);
}
