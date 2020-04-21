package com.wangyang.data.service;

import com.wangyang.model.pojo.entity.base.BaseArticle;

public interface IBaseArticleService<ARTICLE extends BaseArticle> {
    ARTICLE createOrUpdate(ARTICLE article);

    ARTICLE previewSave(ARTICLE article);
}
