package com.wangyang.cms.service;


import com.wangyang.model.pojo.entity.Article;
import com.wangyang.model.pojo.entity.Category;
import com.wangyang.model.pojo.enums.ArticleStatus;
import com.wangyang.model.pojo.vo.ArticleDetailVO;
import com.wangyang.model.pojo.params.ArticleParams;
import com.wangyang.model.pojo.params.ArticleQuery;
import com.wangyang.common.CmsConst;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.transaction.Transactional;
import java.io.File;
import java.util.List;

@Transactional
public class TestArticleService extends AbstractServiceTest{


    @Test
    public void testTags(){
//        Page<Article> page = articleService.pageByTagId(4, 2);
//        System.out.println(page);
    }

    @Test
    public void testUpdateCommentNum(){
//        articleRepository.updateCommentNum(10,-1);
//        System.out.println(articleRepository.getCommentNum(10));
    }

    public void save(){
        Article article = articleService.saveOrUpdateArticleDraft(addArticle());
        Assert.assertEquals(ArticleStatus.DRAFT,article.getStatus());
    }
    @Test
    public void testSave(){
        Article article = articleService.saveOrUpdateArticleDraft(addArticle());
        Assert.assertEquals("Title",article.getTitle());
        Assert.assertEquals(ArticleStatus.DRAFT,article.getStatus());
//        Article updateArticle = articleService.saveOrUpdateArticleDraft(updateArticle());
//        Assert.assertEquals("updateTitle",updateArticle.getTitle());
//        Assert.assertEquals(ArticleStatus.DRAFT,updateArticle.getStatus());
    }

    @Test
    public void testAddCategory(){
        Category category = categoryService.save(addCategory());

    }

    @Test
    public void testCreate(){
        ArticleDetailVO article = articleService.createArticleDetailVo(addArticle(), null);
        Assert.assertEquals(ArticleStatus.PUBLISHED,article.getStatus());
        Assert.assertEquals(true,article.getHaveHtml());
        Assert.assertNotNull(article.getFormatContent());
        Assert.assertNotNull(article.getSummary());
    }
    @Test
    public void testCreateHaveCategory(){
        Category category = categoryService.addOrUpdate(addCategory());

        Article articleParams = addArticle();
        articleParams.setCategoryId(category.getId());
        ArticleDetailVO article = articleService.createArticleDetailVo(articleParams, null);

        Assert.assertEquals(ArticleStatus.PUBLISHED,article.getStatus());
        Assert.assertEquals(true,article.getHaveHtml());
        Assert.assertNotNull(article.getFormatContent());
        Assert.assertNotNull(article.getSummary());
        Assert.assertEquals(article.getCategory().getName(),"TestCategory");
    }

    @Test
    public void testCreateHaveCategoryAndTags(){

        

    }

    @Test
    public void testUpdate(){
        Category category = categoryService.addOrUpdate(addCategory());
        Article articleParams = addArticle();
        articleParams.setCategoryId(category.getId());
        ArticleDetailVO article = articleService.createArticleDetailVo(articleParams, null);
        Article findArticle = articleService.findArticleById(article.getId());
        findArticle.setTitle("updateTitle");
        ArticleDetailVO updateArticle = articleService.updateArticleDetailVo(findArticle, null);
        Assert.assertEquals(updateArticle.getTitle(),"updateTitle");
        Assert.assertEquals(updateArticle.getCategory().getName(),"TestCategory");
    }

    @Test
    public void testUpdateAndCategory(){
        Category category = categoryService.addOrUpdate(addCategory());
        Category category2 = categoryService.addOrUpdate(addCategory2());

        Article articleParams = addArticle();
        articleParams.setCategoryId(category.getId());
        ArticleDetailVO article = articleService.createArticleDetailVo(articleParams, null);

        Article findArticle = articleService.findArticleById(article.getId());
        findArticle.setTitle("updateTitle");
        findArticle.setCategoryId(category2.getId());

        ArticleDetailVO updateArticle = articleService.updateArticleDetailVo(findArticle, null);
        Assert.assertEquals(updateArticle.getTitle(),"updateTitle");
        Assert.assertEquals(updateArticle.getCategory().getName(),"TestCategory2");
    }


    @Test
    public void testAddHtml(){
        ArticleDetailVO article = articleService.createArticleDetailVo(addArticle(), null);
        htmlService.conventHtml(article);
        String path = CmsConst.WORK_DIR+"/html/"+article.getPath()+"/"+article.getViewName()+".html";
        File file = new File(path);
        Assert.assertEquals(true,file.exists());
        file.delete();
    }
    @Test
    public void testAddHtmlAndViewName(){
        Article articleParams = addArticle();
        articleParams.setViewName("1749748955");
        ArticleDetailVO article = articleService.createArticleDetailVo(articleParams, null);
        htmlService.conventHtml(article);
        String path = CmsConst.WORK_DIR+"/html/"+article.getPath()+"/"+article.getViewName()+".html";
        File file = new File(path);
        Assert.assertEquals(true,file.exists());
        Assert.assertEquals("1749748955",article.getViewName());
        file.delete();
    }

    @Test
    public void testAddArticleAndCategoryHtml(){
        Category category = categoryService.addOrUpdate(addCategory());
//        category.setPath("articleList");
        Article articleParams = addArticle();
        articleParams.setCategoryId(category.getId());
        ArticleDetailVO article = articleService.createArticleDetailVo(articleParams, null);

        htmlService.conventHtml(article);
        String path = CmsConst.WORK_DIR+"/html/"+article.getPath()+"/"+article.getViewName()+".html";
        File file = new File(path);
        Assert.assertEquals(true,file.exists());
        file.delete();

        String path2 = CmsConst.WORK_DIR+"/html/"+category.getPath()+"/"+category.getViewName()+".html";
        File file2 = new File(path2);
        Assert.assertEquals(true,file2.exists());
        file2.delete();
    }

//    @Test
    public void testUpdateController(){
        Category category = categoryService.addOrUpdate(addCategory());
        Article articleParams = addArticle();
        articleParams.setCategoryId(category.getId());
        ArticleDetailVO article = articleService.createArticleDetailVo(articleParams, null);

        articleParams.setTitle("updateTitle");

//        ArticleDetailVO articleDetailVO = articleController.updateArticleDetailVO(articleParams, article.getId());
//        String path = CmsConst.WORK_DIR+"/html/article/"+articleDetailVO.getViewName()+".html";
//
//        File file = new File(path);
//        Assert.assertEquals(true,file.exists());
//        file.delete();
//
//        String path2 = CmsConst.WORK_DIR+"/html/articleList/"+category.getViewName()+".html";
//        File file2 = new File(path2);
//        Assert.assertEquals(true,file2.exists());
//        file2.delete();
    }

//    @Test
    public void testUpdateControllerCategory(){
        Category category = categoryService.addOrUpdate(addCategory());
        Category category2 = categoryService.addOrUpdate(addCategory2());
        ArticleParams articleParams = addArticleParam();
        articleParams.setCategoryId(category.getId());
//        ArticleDetailVO article = articleController.createArticleDetailVO(articleParams);
//        String path3 = CmsConst.WORK_DIR+"/html/article/"+article.getViewName()+".html";
//        File file3 = new File(path3);
//        Assert.assertEquals(true,file3.exists());
//        file3.delete();
//
//        String path4 = CmsConst.WORK_DIR+"/html/articleList/"+category.getViewName()+".html";
//        File file4 = new File(path4);
//        Assert.assertEquals(true,file4.exists());
//        file4.delete();
//
//
//        ArticleParams articleParams2 = addArticleParam();
//        articleParams2.setTitle("updateTitle");
//        articleParams2.setCategoryId(category2.getId());
//        ArticleDetailVO articleDetailVO = articleController.updateArticleDetailVO(articleParams2, article.getId());
//
//        String path = CmsConst.WORK_DIR+"/html/article/"+articleDetailVO.getViewName()+".html";
//        File file = new File(path);
//        Assert.assertEquals(true,file.exists());
//        file.delete();
//
//        String path2 = CmsConst.WORK_DIR+"/html/articleList/"+category2.getViewName()+".html";
//        File file2 = new File(path2);
//        Assert.assertEquals(true,file2.exists());
//        file2.delete();
    }

    @Test
    public void test(){
        int beforeSize = articleRepository.findAllId().size();
        testCreate();
        save();
        int afterSize = articleRepository.findAllId().size();
        Assert.assertEquals(beforeSize+1,afterSize);
        
    }

    @Test
    public void testListByCategory(){
        ArticleQuery articleQuery  = new ArticleQuery();
        articleQuery.setCategoryId(26);
        Page<Article> articles = articleService.articleList(articleQuery, PageRequest.of(0, 10));
//        System.out.println(articles.getTotalElements());

    }

    @Test
    public void testLike(){
//        System.out.println(articleService.increaseLikes(888));
        System.out.println(articleService.getLikesNumber(888));
    }


    @Test
    public void testCarousel(){
        List<Article> articles = articleService.carousel();
        System.out.println(articles.size());
    }
}
