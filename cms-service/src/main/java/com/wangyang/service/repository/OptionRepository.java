package com.wangyang.service.repository;

import com.wangyang.pojo.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option,Integer> {

    Option findByKey(String key);
}
