package com.wangyang.cms.repository;

import com.wangyang.cms.pojo.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseCategoryRepository extends JpaRepository<Category,Integer> {
}
