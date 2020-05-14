package com.wangyang.schedule.util;

import com.wangyang.common.CmsConst;
import com.wangyang.common.utils.TemplateUtil;
import com.wangyang.data.service.*;
import com.wangyang.model.pojo.dto.ArticleDto;
import com.wangyang.model.pojo.dto.CategoryDto;
import com.wangyang.model.pojo.entity.*;
import com.wangyang.model.pojo.enums.TemplateType;
import com.wangyang.model.pojo.params.CategoryQuery;
import com.wangyang.model.pojo.support.TemplateOption;
import com.wangyang.model.pojo.support.TemplateOptionMethod;
import com.wangyang.model.pojo.vo.IndexVo;
import lombok.extern.slf4j.Slf4j;
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
import java.util.*;

@Component
@Slf4j
@TemplateOption
public class ArticleJob {

    @Autowired
    IArticleService articleService;

    @Autowired
    ITagsService tagsService;

    @Autowired
    ICategoryService categoryService;

    @Autowired
    IMenuService menuService;

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
        Map<String,Object> map = new HashMap<>();
        map.put("view",articleDtos);
        map.put("showUrl","/articleList?sort=visits,DESC");
        map.put("name","热门文章");
        Template template = templateService.findByEnName(CmsConst.ARTICLE_LIST);
        TemplateUtil.convertHtmlAndSave("components","hotArticle",map,template);
    }

    @ArticleJobAnnotation(jobName = "likeArticle",jobGroup = "ArticleJob",cornExpression = "0 0 0 * * ?")
    public void likeArticle(){
        Specification<Article> specification = new Specification<Article>() {
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return null;
            }
        };
        Page<ArticleDto> articleDtos = articleService.articleShow(specification, PageRequest.of(0, 5, Sort.by(Sort.Order.desc("likes"))));
        Map<String,Object> map = new HashMap<>();
        map.put("view",articleDtos);
        map.put("showUrl","/articleList?sort=likes,DESC");
        map.put("name","点赞最多");
        Template template = templateService.findByEnName(CmsConst.ARTICLE_LIST);
        TemplateUtil.convertHtmlAndSave("components","likeArticle",map,template);
    }

    //每天凌晨执行
    @ArticleJobAnnotation(jobName = "newInformation",jobGroup = "ArticleJob",cornExpression = "0 0 0 * * ?")
    public void newInformation(){
        Optional<Tags> tags = tagsService.findBy(CmsConst.TAGS_INFORMATION);
        if(tags.isPresent()){
            log.info("Schedule 生成推荐文章,在"+tags.get().getName());
            Page<ArticleDto> articleDtos = articleService.pageByTagId(tags.get().getId(), 5);
            Map<String,Object> map = new HashMap<>();
            map.put("view",articleDtos);
            map.put("showUrl","/articleList?tagsId="+tags.get().getId());
            map.put("name","最新资讯");
            Template template = templateService.findByEnName(CmsConst.ARTICLE_LIST);
            TemplateUtil.convertHtmlAndSave("components","newInformation",map,template);
        }else {
            log.info("Schedule 不能找到Tags"+CmsConst.TAGS_INFORMATION);
        }
    }

    //每天凌晨执行
    @ArticleJobAnnotation(jobName = "recommendArticle",jobGroup = "ArticleJob",cornExpression = "0 0 0 * * ?")
    public void recommendArticle(){
        Optional<Tags> tags = tagsService.findBy(CmsConst.TAGS_RECOMMEND);
        if(tags.isPresent()){
            log.info("Schedule 生成推荐文章,在"+tags.get().getName());
            Page<ArticleDto> articleDtos = articleService.pageByTagId(tags.get().getId(), 5);
            Map<String,Object> map = new HashMap<>();
            map.put("view",articleDtos);
            map.put("showUrl","/articleList?tagsId="+tags.get().getId());
            map.put("name","推荐阅读");
            Template template = templateService.findByEnName(CmsConst.ARTICLE_LIST);
            TemplateUtil.convertHtmlAndSave("components","recommendArticle",map,template);
        }else {
            log.info("Schedule 不能找到Tags"+CmsConst.TAGS_RECOMMEND);
        }
    }

//    @TemplateOptionMethod(name = "Carousel",templateValue = "templates/components/@carousel",viewName="carousel",path = "components")
//    public List<Article> carousel(Set<Integer> ids){
//        return articleService.listByIds(ids);
//    }

    @TemplateOptionMethod(name = "New Article",templateValue = "templates/components/@newArticleIndex",viewName="newArticleIndex",path = "components")
    public Page<ArticleDto> articleShowLatest(){
        return articleService.articleShowLatest();
    }


    @TemplateOptionMethod(name = "Footer",templateValue = "templates/components/@footer",viewName="footer",path = "components")
    public Map<String, String> footer() {
        return null;
    }

    @TemplateOptionMethod(name = "Index",templateValue = "templates/components/@index",viewName="index",event = "ACAU")
    public IndexVo index() {
//        List<CategoryDto> recommend = categoryService.listRecommend();
//        List<Template> templates = templateService.listByAndStatusTrue(TemplateType.CATEGORY);
        IndexVo indexVo = new IndexVo();
//        indexVo.setTemplates(templates);
//        indexVo.setRecommend(recommend);
        return indexVo;
    }

    @TemplateOptionMethod(name = "Category List", templateValue = "templates/components/@categoryList", viewName = "categoryList", path = "components")
    public List<Category> listCategory() {
       return categoryService.list();
    }

    @TemplateOptionMethod(name = "Menu",templateValue = "templates/components/@header",viewName="header",path = "components")
    public List<Menu> listMenu(){
        return menuService.list();
    }

}
