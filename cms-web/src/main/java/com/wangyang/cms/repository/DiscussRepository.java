package com.wangyang.cms.repository;

import com.wangyang.cms.pojo.entity.Discuss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DiscussRepository extends JpaRepository<Discuss,Integer>
        , JpaSpecificationExecutor<Discuss> {
}
