package com.wangyang.cms.service;

import com.wangyang.cms.pojo.dto.CategoryArticleListDao;
import com.wangyang.cms.pojo.entity.*;
import com.wangyang.cms.pojo.vo.ArticleDetailVO;
import com.wangyang.cms.pojo.vo.ChannelVo;
import com.wangyang.cms.pojo.vo.CommentVo;
import com.wangyang.cms.pojo.vo.SheetDetailVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    ChannelVo conventHtml(Channel channel);

    /**
     * 生成该栏目下文章列表, 只展示文章列表
     * @param category
     */
    CategoryArticleListDao convertHtml(Category category);

//    void generateCategoryArticleListByCategory(Category category);

    Components generateHome();

//    void updateCategoryPage(Integer oldCategoryId);

    void addOrRemoveArticleToCategoryListByCategoryId(int baseCategoryId);

//    void generateCategoryArticleListByCategory(Integer id);

    /**
     * 生成分类列表的html, 用于首页显示
     */
    void generateCategoryListHtml();

    /**
     * 生成栏目列表的html, 用于首页显示
     */
    void generateChannelListHtml();

    /**
     * 生成菜单的html
     */
    void generateMenuListHtml();



    void commonTemplate(String option);

    void convertHtml(Sheet sheet);


    void generateSheetListByChannelId(int id);

    void generateCommentHtmlByComment(Comment comment);

    void generateCommentHtmlByArticleId(int articleId);
}
