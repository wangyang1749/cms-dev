package com.wangyang.cms.service;

import com.wangyang.cms.pojo.dto.ArticleDto;
import com.wangyang.cms.pojo.entity.Article;
import com.wangyang.cms.pojo.params.ArticleParams;
import com.wangyang.cms.pojo.params.ArticleQuery;
import com.wangyang.cms.pojo.support.BaseResponse;
import com.wangyang.cms.pojo.support.TemplateOption;
import com.wangyang.cms.pojo.support.TemplateOptionMethod;
import com.wangyang.cms.pojo.vo.ArticleDetailVO;
import com.wangyang.cms.pojo.vo.ArticleVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Set;


@TemplateOption
public interface IArticleService extends IBaseArticleService<Article> {

    ArticleDetailVO  updateArticle(int articleId, ArticleParams updateArticle,  Set<Integer> tagsIds, Set<Integer> categoryIds);
    ArticleDetailVO createArticle(ArticleParams articleParams, Set<Integer> tagsIds, Set<Integer> categoryIds);


    Article deleteByArticleId(int id);

    ArticleDetailVO createOrUpdateArticle(Article article, Set<Integer> tagsIds, Set<Integer> categoryIds);


    /**
     * 跟新本地所有文章html
     */
    List<Integer>  updateAllArticleHtml();

    /**
     * Find article vo  by id
     * @param id article id
     * @return
     */
    ArticleDetailVO findArticleAOById(int id);
    Article findArticleById(int id);

    Page<Article> articleList(ArticleQuery articleQuery,Pageable pageable);

    Page<ArticleVO> convertToSimple(Page<Article> articlePage);

    Page<ArticleVO> convertToListVo(Page<Article> articlePage);

    Page<ArticleDto> articleShow(Specification<Article> specification, Pageable pageable);

    List<ArticleDto> articleShow(Specification<Article> specification, Sort sort);

    Page<ArticleDto> articleShowLatest();

    /**
     * Increase article like
     * @param id
     */
    void increaseLikes(int id);


    ModelAndView preview(int articleId);

//    Page<ArticleDto> findArticleListByCategoryId(int categoryId,Pageable pageable);
}