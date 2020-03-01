package com.wangyang.cms.repository;

import com.wangyang.cms.pojo.entity.ArticleCategory;
import com.wangyang.cms.pojo.entity.ArticleTags;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ArticleCategoryRepository extends JpaRepository<ArticleCategory,Integer> {
    List<ArticleCategory> findAllByArticleIdIn(Collection<Integer> articleIds);

    List<ArticleCategory> deleteByArticleId(int articleId);

    List<ArticleCategory> deleteByCategoryId(int categoryId);
}
