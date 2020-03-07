package com.wangyang.cms.repository;

import com.wangyang.cms.pojo.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option,Integer> {

    Option findByKey(String key);
}
