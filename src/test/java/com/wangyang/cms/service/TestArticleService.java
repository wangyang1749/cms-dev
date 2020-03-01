package com.wangyang.cms.service;

import com.wangyang.cms.pojo.entity.Article;
import com.wangyang.cms.pojo.enums.ArticleStatus;
import com.wangyang.cms.pojo.params.ArticleParams;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public class TestArticleService
{
    @Autowired
    IArticleService articleService;

    @Test
    public void test1(){
        articleService.articleShowLatest().forEach(articleDto -> System.out.println(articleDto));
    }


    @Test
    public void test2(){

    }
    @Test
    public void testCreateArticle(){
    }

    @Test
    public void testCreateArticleAndHtml(){

    }

    @Test
    public void testMarkDown(){

    }

    @Test
    @Transactional
    public void testUpdateLikes(){

    }

    @Test
    public void testPage(){
        articleService.findArticleListByCategoryId(3, PageRequest.of(0,5));
    }
}
