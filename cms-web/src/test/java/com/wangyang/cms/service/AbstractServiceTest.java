package com.wangyang.cms.service;

import com.wangyang.cms.pojo.entity.Article;
import com.wangyang.cms.pojo.params.ArticleParams;
import com.wangyang.cms.repository.ArticleRepository;
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
    IArticleService articleService;

    public ArticleParams addArticle(){
        ArticleParams articleParams = new ArticleParams();
        articleParams.setTitle("Title");
        articleParams.setOriginalContent("test_test");
        return articleParams;
    }
    public ArticleParams updateArticle(){
        ArticleParams articleParams = new ArticleParams();
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
