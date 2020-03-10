package com.wangyang.cms.service;


import com.wangyang.cms.pojo.entity.Article;
import com.wangyang.cms.pojo.enums.ArticleStatus;
import com.wangyang.cms.pojo.vo.ArticleDetailVO;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import javax.transaction.Transactional;
import java.util.List;
@Transactional
public class TestArticleService extends AbstractServiceTest{


    public void save(){
        Article article = articleService.saveArticle(null, addArticle(), tagIds(), categoryIds());
        Assert.assertEquals(ArticleStatus.DRAFT,article.getStatus());
    }
    @Test
    public void testSave(){
        Article article = articleService.saveArticle(null, addArticle(), tagIds(), categoryIds());
        Assert.assertEquals("Title",article.getTitle());
        Assert.assertEquals(ArticleStatus.DRAFT,article.getStatus());
        Article updateArticle = articleService.saveArticle(article.getId(), updateArticle(), tagIds(), categoryIds());
        Assert.assertEquals("updateTitle",updateArticle.getTitle());
        Assert.assertEquals(ArticleStatus.DRAFT,updateArticle.getStatus());
    }
    @Test
    public void testCreate(){
        ArticleDetailVO article = articleService.createArticle(addArticle(), tagIds(), categoryIds());
        Assert.assertEquals(ArticleStatus.PUBLISHED,article.getStatus());
    }


    @Test
    public void test(){
        int beforeSize = articleRepository.findAllId().size();
        testCreate();
        save();
        int afterSize = articleRepository.findAllId().size();
        Assert.assertEquals(beforeSize+1,afterSize);
        
    }

}
