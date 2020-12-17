package com.wangyang.service.service;

import com.wangyang.pojo.dto.ArticleDto;
import com.wangyang.pojo.dto.CategoryArticleListDao;
import com.wangyang.pojo.entity.Category;
import com.wangyang.pojo.entity.Components;
import com.wangyang.pojo.entity.Sheet;
import com.wangyang.pojo.vo.ArticleDetailVO;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletRequest;

public interface IHtmlService {

    /**
     * 生成文章的html
     * @param articleVO
     */
    void conventHtml(ArticleDetailVO articleVO);
    /**
     * 生成该栏目下所有文章的列表, 用于动态添加到文章详情的旁边
     * @param channel
     * @return
     */
//    ChannelVo conventHtml(Channel channel);

    /**
     * 生成该栏目下文章列表, 只展示文章列表
     * @param category
     */
    CategoryArticleListDao convertArticleListBy(Category category);

//    void generateCategoryArticleListByCategory(Category category);

    Components generateHome();

//    void updateCategoryPage(Integer oldCategoryId);

//    void deleteTempFileByCategory(Category category);

    @Async
        //异步执行
    void conventHtmlNoCategoryList(ArticleDetailVO articleVO);

//    void addOrRemoveArticleToCategoryListByCategoryId(int baseCategoryId);

//    void generateCategoryArticleListByCategory(Integer id);

    /**
     * 生成分类列表的html, 用于首页显示
     */
    void generateCategoryListHtml();


    /**
     * 生成菜单的html
     */
    void generateMenuListHtml();



    void commonTemplate(String option);

    String convertArticleListBy(Category category, int page);

    /**
     * 生成最新文章
     */
    void newArticleListHtml();

    String convertArticlePageBy(HttpServletRequest request, Page<ArticleDto> articleDtoPage, String viewName);


    String previewArticlePageBy(HttpServletRequest request, Page<ArticleDto> articleDtoPage);

    CategoryArticleListDao convertArticleListBy(int categoryId);

    void convertArticleListBy(Sheet sheet);


//    void generateSheetListByChannelId(int id);


    void generateCommentHtmlByArticleId(int articleId);




    void articleTopListByCategoryId(int id);
}
