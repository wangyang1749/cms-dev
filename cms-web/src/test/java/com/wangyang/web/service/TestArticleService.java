package com.wangyang.web.service;


import com.wangyang.pojo.entity.Article;
import com.wangyang.pojo.entity.Category;
import com.wangyang.pojo.enums.ArticleStatus;
import com.wangyang.pojo.vo.ArticleDetailVO;
import com.wangyang.pojo.params.ArticleParams;
import com.wangyang.pojo.params.ArticleQuery;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class TestArticleService extends AbstractServiceTest{


//    @Test
//    public void testAddCategory(){
//        Category category = categoryService.save(addCategory());
//
//    }
//
//    @Test
//    public void testCreate(){
//        ArticleDetailVO article = articleService.createArticleDetailVo(addArticle(), null);
//        Assert.assertEquals(ArticleStatus.PUBLISHED,article.getStatus());
//        Assert.assertEquals(true,article.getHaveHtml());
//        Assert.assertNotNull(article.getFormatContent());
//        Assert.assertNotNull(article.getSummary());
//    }
//    @Test
//    public void testCreateHaveCategory(){
//        Category category = categoryService.addOrUpdate(addCategory());
//
//        Article articleParams = addArticle();
//        articleParams.setCategoryId(category.getId());
//        ArticleDetailVO article = articleService.createArticleDetailVo(articleParams, null);
//
//        Assert.assertEquals(ArticleStatus.PUBLISHED,article.getStatus());
//        Assert.assertEquals(true,article.getHaveHtml());
//        Assert.assertNotNull(article.getFormatContent());
//        Assert.assertNotNull(article.getSummary());
//        Assert.assertEquals(article.getCategory().getName(),"TestCategory");
//    }
//
//    @Test
//    public void testCreateHaveCategoryAndTags(){
//
//
//
//    }
//
//    @Test
//    public void testUpdate(){
//        Category category = categoryService.addOrUpdate(addCategory());
//        Article articleParams = addArticle();
//        articleParams.setCategoryId(category.getId());
//        ArticleDetailVO article = articleService.createArticleDetailVo(articleParams, null);
//        Article findArticle = articleService.findArticleById(article.getId());
//        findArticle.setTitle("updateTitle");
//        ArticleDetailVO updateArticle = articleService.updateArticleDetailVo(findArticle, null);
//        Assert.assertEquals(updateArticle.getTitle(),"updateTitle");
//        Assert.assertEquals(updateArticle.getCategory().getName(),"TestCategory");
//    }
//
//    @Test
//    public void testUpdateAndCategory(){
//        Category category = categoryService.addOrUpdate(addCategory());
//        Category category2 = categoryService.addOrUpdate(addCategory2());
//
//        Article articleParams = addArticle();
//        articleParams.setCategoryId(category.getId());
//        ArticleDetailVO article = articleService.createArticleDetailVo(articleParams, null);
//
//        Article findArticle = articleService.findArticleById(article.getId());
//        findArticle.setTitle("updateTitle");
//        findArticle.setCategoryId(category2.getId());
//
//        ArticleDetailVO updateArticle = articleService.updateArticleDetailVo(findArticle, null);
//        Assert.assertEquals(updateArticle.getTitle(),"updateTitle");
//        Assert.assertEquals(updateArticle.getCategory().getName(),"TestCategory2");
//    }
//
//
//
//
//
////    @Test
//    public void testUpdateController(){
//        Category category = categoryService.addOrUpdate(addCategory());
//        Article articleParams = addArticle();
//        articleParams.setCategoryId(category.getId());
//        ArticleDetailVO article = articleService.createArticleDetailVo(articleParams, null);
//
//        articleParams.setTitle("updateTitle");
//
////        ArticleDetailVO articleDetailVO = articleController.updateArticleDetailVO(articleParams, article.getId());
////        String path = CmsConst.WORK_DIR+"/html/article/"+articleDetailVO.getViewName()+".html";
////
////        File file = new File(path);
////        Assert.assertEquals(true,file.exists());
////        file.delete();
////
////        String path2 = CmsConst.WORK_DIR+"/html/articleList/"+category.getViewName()+".html";
////        File file2 = new File(path2);
////        Assert.assertEquals(true,file2.exists());
////        file2.delete();
//    }
//
////    @Test
//    public void testUpdateControllerCategory(){
//        Category category = categoryService.addOrUpdate(addCategory());
//        Category category2 = categoryService.addOrUpdate(addCategory2());
//        ArticleParams articleParams = addArticleParam();
//        articleParams.setCategoryId(category.getId());
////        ArticleDetailVO article = articleController.createArticleDetailVO(articleParams);
////        String path3 = CmsConst.WORK_DIR+"/html/article/"+article.getViewName()+".html";
////        File file3 = new File(path3);
////        Assert.assertEquals(true,file3.exists());
////        file3.delete();
////
////        String path4 = CmsConst.WORK_DIR+"/html/articleList/"+category.getViewName()+".html";
////        File file4 = new File(path4);
////        Assert.assertEquals(true,file4.exists());
////        file4.delete();
////
////
////        ArticleParams articleParams2 = addArticleParam();
////        articleParams2.setTitle("updateTitle");
////        articleParams2.setCategoryId(category2.getId());
////        ArticleDetailVO articleDetailVO = articleController.updateArticleDetailVO(articleParams2, article.getId());
////
////        String path = CmsConst.WORK_DIR+"/html/article/"+articleDetailVO.getViewName()+".html";
////        File file = new File(path);
////        Assert.assertEquals(true,file.exists());
////        file.delete();
////
////        String path2 = CmsConst.WORK_DIR+"/html/articleList/"+category2.getViewName()+".html";
////        File file2 = new File(path2);
////        Assert.assertEquals(true,file2.exists());
////        file2.delete();
//    }
//
//
//
//    @Test
//    public void testListByCategory(){
//        ArticleQuery articleQuery  = new ArticleQuery();
//        articleQuery.setCategoryId(26);
//        Page<Article> articles = articleService.articleList(articleQuery, PageRequest.of(0, 10));
////        System.out.println(articles.getTotalElements());
//
//    }
//
//    @Test
//    public void testLike(){
////        System.out.println(articleService.increaseLikes(888));
//        System.out.println(articleService.getLikesNumber(888));
//    }



}
