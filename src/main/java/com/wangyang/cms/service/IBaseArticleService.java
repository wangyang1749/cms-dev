package com.wangyang.cms.service;

import com.wangyang.cms.pojo.entity.base.BaseArticle;

public interface IBaseArticleService<ARTICLE extends BaseArticle> {
    ARTICLE createOrUpdate(ARTICLE article);
}
