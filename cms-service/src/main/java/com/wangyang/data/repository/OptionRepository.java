package com.wangyang.data.repository;

import com.wangyang.model.pojo.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option,Integer> {

    Option findByKey(String key);
}
