package com.wangyang.data.repository;

import com.wangyang.model.pojo.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseCategoryRepository extends JpaRepository<Category,Integer> {
}
