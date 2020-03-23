package com.wangyang.cms.service;

import com.wangyang.cms.pojo.dto.ArticleDto;
import com.wangyang.cms.pojo.dto.CategoryArticleListDao;
import com.wangyang.cms.pojo.entity.Article;
import com.wangyang.cms.pojo.entity.Category;
import com.wangyang.cms.pojo.params.ArticleParams;
import com.wangyang.cms.pojo.params.ArticleQuery;
import com.wangyang.cms.pojo.support.TemplateOption;
import com.wangyang.cms.pojo.vo.ArticleDetailVO;
import com.wangyang.cms.pojo.vo.ArticleVO;
import com.wangyang.cms.pojo.vo.SheetDetailVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Set;


@TemplateOption
public interface IArticleService extends IBaseArticleService<Article> {

    ArticleDetailVO  updateArticleDetailVo(Article article, Set<Integer> tagsIds);
    ArticleDetailVO createArticleDetailVo(Article article,Set<Integer> tagsIds);

    ArticleDetailVO updateArticleDetailVo(Article article);

    Article save(Article article);

    /**
     * 保存或者更新文章草稿,只是保存文章内容
     * @param article
     * @return
     */
    Article saveOrUpdateArticleDraft(Article article);

    List<ArticleDto> listBy(int categoryId);

//    Article updateCategory(int articleId, int categoryId);




    Article deleteByArticleId(int id);

//    ArticleDetailVO createOrUpdateArticleVo(Article article, Set<Integer> tagsIds);


    /**
     * 为文章只添加标签
     * @param article
     * @return
     */
    ArticleDetailVO conventToAddTags(Article article);

    /**
     * 为文章添加类别和标签
     * @param article
     * @return
     */
    ArticleDetailVO convert(Article article);

    /**
     * 更新本地所有文章html
     */
    List<Article>  updateAllArticleHtml(Boolean more);

    /**
     * Find article vo  by id
     * @param id article id
     * @return
     */
    ArticleDetailVO findArticleAOById(int id);
    Article findArticleById(int id);

    Page<Article> articleList(ArticleQuery articleQuery,Pageable pageable);

    Page<ArticleDto> convertToSimple(Page<Article> articlePage);

    Page<ArticleVO> convertToListVo(Page<Article> articlePage);

    Page<ArticleDto> articleShow(Specification<Article> specification, Pageable pageable);

    List<ArticleDto> articleShow(Specification<Article> specification, Sort sort);

    Page<ArticleDto> articleShowLatest();

    /**
     * Increase article like
     * @param id
     */
    int increaseLikes(int id);


    String  generatePdf(@PathVariable("articleId") Integer articleId);

    ModelAndView previewPdf(int articleId);

    ModelAndView previewForSave(int articleId);

    ModelAndView preview(int articleId);

    Integer getLikesNumber(int id);

    int increaseVisits(int id);

    Integer getVisitsNumber(int id);

    Article haveHtml(int id);

    void generateSummary(Article article);

    ArticleDetailVO updateCategory(Article article, int categoryId);

    ModelAndView getArticleListByCategory(int categoryId, int page);

    CategoryArticleListDao getArticleListByCategory(Category category);

    Page<ArticleDto> findArticleListByCategoryId(int categoryId, int page);

//    ArticleDetailVO addArticleToChannel(Article article, int channelId);

//    Page<ArticleDto> findArticleListByCategoryId(int categoryId,Pageable pageable);
}