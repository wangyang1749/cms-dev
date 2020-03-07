package com.wangyang.cms.service.impl;

import com.wangyang.cms.pojo.entity.base.BaseArticle;
import com.wangyang.cms.pojo.enums.ArticleStatus;
import com.wangyang.cms.service.IBaseArticleService;
import com.wangyang.cms.utils.MarkdownUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

public abstract class BaseArticleServiceImpl<ARTICLE extends BaseArticle> implements IBaseArticleService<ARTICLE> {
    @Override
    public ARTICLE createOrUpdate(ARTICLE article) {

        if(article.getStatus()== ArticleStatus.PUBLISHED){
            article.setFormatContent(MarkdownUtils.renderHtml(article.getOriginalContent()));
        }
        return article;
    }


}
