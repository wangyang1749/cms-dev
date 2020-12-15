package com.wangyang.service.repository;

import com.wangyang.pojo.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseCategoryRepository extends JpaRepository<Category,Integer> {
}
