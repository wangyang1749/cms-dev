package com.wangyang.cms.service;

import com.wangyang.cms.pojo.entity.ArticleCategory;
import com.wangyang.cms.repository.ArticleCategoryRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest
public class TestArticleCategoey {

    @Autowired
    ArticleCategoryRepository articleCategoryRepository;

    @Test
//    @Transactional
    public void test1(){
    }
}
