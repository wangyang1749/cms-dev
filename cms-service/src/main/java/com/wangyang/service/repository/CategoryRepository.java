package com.wangyang.service.repository;

import com.wangyang.pojo.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Integer>
        , JpaSpecificationExecutor<Category> {

    /**
     * 根据文章id找到所有的分类
     * @return
     */
//    @Query("select o from Category o where o.id in (select a.categoryId from ArticleCategory a where a.articleId=?1)")
//    List<Category> findCategoryByArticleId(int aid);

    @Query(value = "select o.id from Category o where status=true")
    List<Integer> findAllId();

    Category findByViewName(String viewName);
}
