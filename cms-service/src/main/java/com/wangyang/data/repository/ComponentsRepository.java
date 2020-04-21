package com.wangyang.data.repository;

import com.wangyang.model.pojo.entity.Components;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ComponentsRepository extends JpaRepository<Components,Integer>
        , JpaSpecificationExecutor<Components> {

}
