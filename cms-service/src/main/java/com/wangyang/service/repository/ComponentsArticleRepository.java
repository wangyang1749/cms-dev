package com.wangyang.service.repository;

import com.wangyang.pojo.entity.ComponentsArticle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComponentsArticleRepository extends JpaRepository< ComponentsArticle,Integer> {

    List<ComponentsArticle> findByComponentId(Integer componentId);

    ComponentsArticle findByArticleIdAndComponentId(int articleId,int componentId);
}
