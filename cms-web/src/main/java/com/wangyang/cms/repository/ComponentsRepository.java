package com.wangyang.cms.repository;

import com.wangyang.cms.pojo.entity.Components;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ComponentsRepository extends JpaRepository<Components,Integer>
        , JpaSpecificationExecutor<Components> {

}
