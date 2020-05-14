package com.wangyang.data.service;

import com.wangyang.model.pojo.entity.ComponentsArticle;

public interface IComponentsArticleService {
    ComponentsArticle add(int articleId, int componentsId);

    ComponentsArticle add(String viewName, int componentsId);

    void delete(int id);

    ComponentsArticle delete(int articleId, int componentsId);
}
