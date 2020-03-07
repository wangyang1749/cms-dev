package com.wangyang.cms.service;

import com.wangyang.cms.pojo.entity.Article;
import com.wangyang.cms.pojo.enums.ArticleStatus;
import com.wangyang.cms.pojo.params.ArticleParams;
import com.wangyang.cms.repository.ArticleRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
public class TestArticleService
{
    @Autowired
    IArticleService articleService;

    @Autowired
    ArticleRepository articleRepository;

    @Test
    public void test1(){
        articleService.articleShowLatest().forEach(articleDto -> System.out.println(articleDto));
    }


    @Test
    public void test2(){
        List<Integer> list = articleRepository.findAllId();
//        Assert.assertNotNull(list);
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
    }
}
