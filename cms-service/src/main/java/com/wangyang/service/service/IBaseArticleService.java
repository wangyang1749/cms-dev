package com.wangyang.service.service;

import com.wangyang.pojo.entity.base.BaseArticle;

public interface IBaseArticleService<ARTICLE extends BaseArticle> {
    ARTICLE createOrUpdate(ARTICLE article);

//    ARTICLE previewSave(ARTICLE article);
}
