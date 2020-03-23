package com.wangyang.cms.service;

import com.wangyang.cms.controller.api.ArticleController;
import com.wangyang.cms.core.jms.consumer.ArticleConsumerServiceImpl;
import com.wangyang.cms.pojo.entity.Article;
import com.wangyang.cms.pojo.entity.Category;
import com.wangyang.cms.pojo.entity.base.BaseCategory;
import com.wangyang.cms.pojo.params.ArticleParams;
import com.wangyang.cms.pojo.params.CategoryParam;
import com.wangyang.cms.repository.ArticleRepository;
import com.wangyang.cms.repository.BaseCategoryRepository;
import com.wangyang.cms.repository.CategoryRepository;
import com.wangyang.cms.repository.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public abstract class AbstractServiceTest {
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    IOptionService optionService;

    @Autowired
    OptionRepository optionRepository;

    @Autowired
    ITemplateService templateService;
    @Autowired
    IArticleService articleService;

    @Autowired
    ICategoryService categoryService;

    @Autowired
    BaseCategoryRepository baseCategoryRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    IHtmlService htmlService;

    @Autowired
    ArticleController articleController;

    public Category addCategory(){
        Category categoryParam = new Category();
        categoryParam.setName("TestCategory");

        return categoryParam;
    }
    public Category addCategory2(){
        Category categoryParam = new Category();
        categoryParam.setName("TestCategory2");

        return categoryParam;
    }
    public Article addArticle(){
        Article articleParams = new Article();
        articleParams.setTitle("Title");
        articleParams.setOriginalContent("test_test");
        return articleParams;
    }
    public ArticleParams addArticleParam(){
        ArticleParams articleParams = new ArticleParams();
        articleParams.setTitle("Title");
        articleParams.setOriginalContent("test_test");
        return articleParams;
    }
    public Article updateArticle(){
        Article articleParams = new Article();
        articleParams.setTitle("updateTitle");
        articleParams.setOriginalContent("test_test");
        return articleParams;
    }
    public Set<Integer> tagIds(){
        Set<Integer> set = new HashSet<>();
        set.add(1);
        return set;
    }
    public Set<Integer> categoryIds(){
        Set<Integer> set = new HashSet<>();
        set.add(1);
        return set;
    }
}
