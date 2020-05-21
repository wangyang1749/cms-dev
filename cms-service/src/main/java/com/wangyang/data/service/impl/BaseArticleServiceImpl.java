package com.wangyang.data.service.impl;

import com.wangyang.common.utils.MarkdownUtils;
import com.wangyang.data.repository.ArticleRepository;
import com.wangyang.data.service.IBaseArticleService;
import com.wangyang.data.service.IOptionService;
import com.wangyang.model.pojo.entity.base.BaseArticle;
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

//        if(article.getStatus()== ArticleStatus.PUBLISHED){
            String[] renderHtml = MarkdownUtils.renderHtml(article.getOriginalContent());
            article.setFormatContent(renderHtml[1]);
            article.setToc(renderHtml[0]);
//        }

        return article;
    }

    @Override
    public ARTICLE previewSave(ARTICLE article) {


            String[] renderHtml = MarkdownUtils.renderHtml(article.getOriginalContent());

            article.setFormatContent(renderHtml[1]);

            article.setToc(renderHtml[0]);

        return article;
    }


}
