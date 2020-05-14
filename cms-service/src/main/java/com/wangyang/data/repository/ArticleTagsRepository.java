package com.wangyang.data.repository;

import com.wangyang.model.pojo.entity.Article;
import com.wangyang.model.pojo.entity.ArticleTags;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ArticleTagsRepository extends JpaRepository<ArticleTags,Integer> {

    List<ArticleTags> findAllByArticleIdIn(Collection<Integer> articleIds);
    List<ArticleTags> findByArticleId(int articleId);

    List<ArticleTags> deleteByArticleId(int id);



}
