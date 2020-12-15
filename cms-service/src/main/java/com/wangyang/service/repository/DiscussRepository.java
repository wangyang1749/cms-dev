package com.wangyang.service.repository;

import com.wangyang.pojo.entity.Discuss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DiscussRepository extends JpaRepository<Discuss,Integer>
        , JpaSpecificationExecutor<Discuss> {
}
