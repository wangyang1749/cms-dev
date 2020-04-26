package com.wangyang.schedule.util;

import com.wangyang.common.CmsConst;
import com.wangyang.common.utils.TemplateUtil;
import com.wangyang.data.service.IArticleService;
import com.wangyang.data.service.ITemplateService;
import com.wangyang.model.pojo.dto.ArticleDto;
import com.wangyang.model.pojo.entity.Article;
import com.wangyang.model.pojo.entity.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Component
public class ArticleJob {

    @Autowired
    IArticleService articleService;

    @Autowired
    ITemplateService templateService;
    //每天凌晨执行
    @ArticleJobAnnotation(jobName = "hotArticle",jobGroup = "ArticleJob",cornExpression = "0 0 0 * * ?")
    public void hotArticle(){
        System.out.println("生成最新文章！！");
        Specification<Article> specification = new Specification<Article>() {
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return null;
            }
        };
        Page<ArticleDto> articleDtos = articleService.articleShow(specification, PageRequest.of(0, 5, Sort.by(Sort.Order.desc("visits"))));
        Template template = templateService.findByEnName(CmsConst.ARTICLE_LIST);
        TemplateUtil.convertHtmlAndSave("components","hotArticle",articleDtos,template);
    }

}
