package com.wangyang.schedule.util;

import com.google.common.base.Joiner;
import com.wangyang.common.CmsConst;
import com.wangyang.common.utils.FileUtils;
import com.wangyang.common.utils.TemplateUtil;
import com.wangyang.data.repository.ArticleRepository;
import com.wangyang.data.service.IArticleService;
import com.wangyang.model.pojo.entity.Article;
import com.wangyang.model.pojo.entity.Components;
import com.wangyang.model.pojo.enums.ArticleStatus;
import com.wangyang.model.pojo.support.ScheduleOption;
import com.wangyang.model.pojo.support.TemplateOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@ScheduleOption
public class SiteMapJob {

    @Autowired
    ArticleRepository articleRepository;

    @ArticleJobAnnotation(jobName = "generateSiteMap",jobGroup = "SiteMapJob",cornExpression = "0 0 0 * * ?")
    public void generateSiteMap(){
        Specification<Article> specification = new Specification<Article>() {
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("status"), ArticleStatus.PUBLISHED)).getRestriction();
            }
        };
        List<Article> articles = articleRepository.findAll(specification);
        StringBuffer sb = new StringBuffer();
        articles.forEach(article -> {
            sb.append( "http://www.bioinfo.online/"+article.getPath()+"/"+article.getViewName()+".html\n");
        });
//        System.out.println(sb);
        File file = new File(CmsConst.WORK_DIR+"/html/siteMap.txt");
        FileUtils.saveFile(file,sb.toString());
    }

}
