package com.wangyang.service.service.impl;

import com.wangyang.common.utils.MarkdownUtils;
import com.wangyang.pojo.entity.Article;
import com.wangyang.service.repository.ArticleRepository;
import com.wangyang.service.service.IBaseArticleService;
import com.wangyang.service.service.IOptionService;
import com.wangyang.pojo.entity.base.BaseArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class BaseArticleServiceImpl<ARTICLE extends BaseArticle> implements IBaseArticleService<ARTICLE> {

    @Autowired
    IOptionService optionService;

    @Autowired
    ArticleRepository articleRepository;

    @Override
    public ARTICLE createOrUpdate(ARTICLE article) {

        MarkdownUtils.renderHtml(article);
        return article;
    }

//    @Override
//    public ARTICLE previewSave(ARTICLE article) {
//
//
//            String[] renderHtml = MarkdownUtils.renderHtml(article.getOriginalContent());
//
//            article.setFormatContent(renderHtml[1]);
//
//            article.setToc(renderHtml[0]);
//
//        return article;
//    }


}
