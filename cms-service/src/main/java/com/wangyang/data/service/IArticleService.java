package com.wangyang.data.service;

import com.wangyang.model.pojo.dto.ArticleDto;
import com.wangyang.model.pojo.dto.CategoryArticleListDao;
import com.wangyang.model.pojo.entity.Article;
import com.wangyang.model.pojo.entity.Category;
import com.wangyang.model.pojo.vo.ArticleDetailVO;
import com.wangyang.model.pojo.vo.ArticleVO;
import com.wangyang.model.pojo.params.ArticleQuery;
import com.wangyang.model.pojo.support.TemplateOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Set;


@TemplateOption
public interface IArticleService extends IBaseArticleService<Article> {

    ArticleDetailVO updateArticleDetailVo(Article article, Set<Integer> tagsIds);
    ArticleDetailVO createArticleDetailVo(Article article,Set<Integer> tagsIds);

    ArticleDetailVO updateArticleDetailVo(Article article);

    Article save(Article article);

    /**
     * 保存或者更新文章草稿,只是保存文章内容
     * @param article
     * @return
     */
    Article saveOrUpdateArticleDraft(Article article);

    Page<ArticleDto> pageDtoBy(int categoryId, int page, int size);

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

    Article findByIdAndUserId(int id, int userId);

    Page<Article> articleList(ArticleQuery articleQuery, Pageable pageable);

    Page<ArticleDto> convertToSimple(Page<Article> articlePage);

    Page<ArticleVO> convertToAddCategory(Page<Article> articlePage);

    Page<ArticleVO> convertToListVo(Page<Article> articlePage);

    Page<ArticleDto> articleShow(Specification<Article> specification, Pageable pageable);

//    List<ArticleDto> articleShow(Specification<Article> specification, Sort sort);

    Page<ArticleDto> articleShowLatest();

    /**
     * Increase article like
     * @param id
     */
    int increaseLikes(int id);


    String  generatePdf(Integer articleId);




    Integer getLikesNumber(int id);

    int increaseVisits(int id);

    Integer getVisitsNumber(int id);

    Article haveHtml(int id);

    Article openComment(int id);

    void generateSummary(Article article);

    ArticleDetailVO updateCategory(Article article, int categoryId);

    CategoryArticleListDao findCategoryArticleBy(Category category, int page);



    Page<ArticleDto> pageDtoBy(int categoryId, int page);

    Page<ArticleDto> pageDtoBy(Category category, int page);

    Page<ArticleDto> pageDtoBy(int categoryId, Pageable pageable);

    Integer getCommentNum(int id);

    void updateCommentNum(int id, int num);

    Article updateOrder(int articleId, int order);

    List<Article> carousel();

    Page<ArticleDto> pageByTagId(int tagId, int size);

    Page<ArticleDto> pageByTagId(int tagId, Pageable pageable);

    Page<Article>  pageByUserId(int userId, Pageable pageable,ArticleQuery articleQuery);

    Page<Article>  pageBy(Pageable pageable, ArticleQuery articleQuery);

    Page<ArticleDto>  pageDtoBy(Pageable pageable, ArticleQuery articleQuery);

    List<ArticleDto> listByTitle(String title);

    List<Article> listByIds(Set<Integer> ids);

    List<ArticleDto> listByComponentsId(int componentsId);

    Article findByViewName(String viewName);


//    ArticleDetailVO addArticleToChannel(Article article, int channelId);

//    Page<ArticleDto> findArticleListByCategoryId(int categoryId,Pageable pageable);
}