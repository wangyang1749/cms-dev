package com.wangyang.cms.repository;

import com.wangyang.cms.pojo.entity.Category;
import com.wangyang.cms.pojo.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
    @Query("select o from Category o where o.id in (select a.categoryId from ArticleCategory a where a.articleId=?1)")
    List<Category> findCategoryByArticleId(int aid);
}
