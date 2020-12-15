package com.wangyang.service.service;

import com.wangyang.pojo.entity.ComponentsArticle;

public interface IComponentsArticleService {
    ComponentsArticle add(int articleId, int componentsId);

    ComponentsArticle add(String viewName, int componentsId);

    void delete(int id);

    ComponentsArticle delete(int articleId, int componentsId);
}
