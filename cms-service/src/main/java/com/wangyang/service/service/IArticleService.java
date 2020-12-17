package com.wangyang.service.service;

import com.wangyang.pojo.dto.ArticleAndCategoryMindDto;
import com.wangyang.pojo.dto.ArticleDto;
import com.wangyang.pojo.dto.CategoryArticleListDao;
import com.wangyang.pojo.entity.Article;
import com.wangyang.pojo.entity.Category;
import com.wangyang.pojo.vo.ArticleDetailVO;
import com.wangyang.pojo.vo.ArticleVO;
import com.wangyang.pojo.params.ArticleQuery;
import com.wangyang.pojo.support.TemplateOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Set;


@TemplateOption
public interface IArticleService extends IBaseArticleService<Article> {

    /**
     * 为文章添加类别和标签
     * @param article
     * @return
     */
    ArticleDetailVO convert(Article article);
    /**
     * 为文章只添加标签
     * @param article
     * @return
     */
    ArticleDetailVO conventToAddTags(Article article);
    /**
     * Find article vo  by id
     * @param id article id
     * @return
     */
    ArticleDetailVO findArticleAOById(int id);
    ArticleDetailVO updateArticleDetailVo(Article article, Set<Integer> tagsIds);
    ArticleDetailVO createArticleDetailVo(Article article,Set<Integer> tagsIds);
    ArticleDetailVO updateArticleDetailVo(Article article);
    ArticleDetailVO updateCategory(Article article, int categoryId);


    /**
     * 编辑文章时判断，该文章是否属于该用户
     * @param id 文章id
     * @param userId 用户 id
     * @return
     */
    Article findByIdAndUserId(int id, int userId);
    Article findArticleById(int id);
    Article save(Article article);
    /**
     * 保存或者更新文章草稿,只是保存文章内容
     * @param article
     * @return
     */
    Article updateArticleDraft(Article article);
    Article saveArticleDraft(Article article);
    Article deleteByArticleId(int id);
    Article haveHtml(int id);
    Article openComment(int id);
    Article updateOrder(int articleId, int order);
    Article findByViewName(String viewName);
    /**
     * 发送或取消置顶
     * @param id
     * @return
     */
   Article sendOrCancelTop(int id);


    /**
     *
     * @param id
     * @return
     */
    List<ArticleDto> listTopByCategoryId(int id);
    /**
     * 同过标题查找
     * @param title
     * @return
     */
    List<ArticleDto> listByTitle(String title);
    List<Article> listByIds(Set<Integer> ids);

    /**
     * 查找自定义组件里的article
     * @param componentsId
     * @return
     */
    List<ArticleDto> listByComponentsId(int componentsId);

    /**
     * 根据user查找article
     * @param userId
     * @return
     */
    List<Article>listByUserId(int userId);
    /**
     *
     * @param categoryId
     * @return
     */
    List<ArticleDto> listArticleDtoBy(int categoryId);
    /**
     * 更新本地所有文章html
     */
    List<Article>  listHaveHtml();

    /**
     * 根据category查找article
     * @param categoryId
     * @return
     */
    List<Article> listArticleBy(int categoryId);





    Page<Article> articleList(ArticleQuery articleQuery, Pageable pageable);
    Page<ArticleDto> convertToSimple(Page<Article> articlePage);
    Page<ArticleVO> convertToAddCategory(Page<Article> articlePage);
    Page<ArticleVO> convertToListVo(Page<Article> articlePage);
    Page<ArticleDto> articleShow(Specification<Article> specification, Pageable pageable);
    Page<ArticleDto> pageDtoBy(int categoryId, int page);
    Page<ArticleDto> pageDtoBy(Category category, int page);

    /**
     * 分页查找ArticleDto去掉top的article
     * @param category
     * @param page
     * @return
     */
    Page<ArticleDto> pageArticleDtoNoTopByCategoryAndPage(Category category, int page);
    Page<ArticleDto> pageDtoBy(int categoryId, Pageable pageable);
    Page<ArticleDto> pageByTagId(int tagId, int size);
    Page<ArticleDto> pageByTagId(int tagId, Pageable pageable);
    Page<ArticleDto> pageDtoBy(Pageable pageable, ArticleQuery articleQuery);
    Page<Article>  pageByUserId(int userId, Pageable pageable,ArticleQuery articleQuery);
    Page<Article>  pageBy(Pageable pageable, ArticleQuery articleQuery);




    /**
     * Increase article like
     * @param id
     */
    int increaseLikes(int id);
    Integer getLikesNumber(int id);
    int increaseVisits(int id);
    Integer getVisitsNumber(int id);
    void generateSummary(Article article);
    Integer getCommentNum(int id);
    /**
     * 动态分页使用
     * @param category
     * @param page
     * @return
     */
    CategoryArticleListDao findCategoryArticleBy(Category category, int page);
    void updateCommentNum(int id, int num);
    ArticleAndCategoryMindDto listArticleMindDto(int categoryId);
    String jsMindFormat( ArticleAndCategoryMindDto articleAndCategoryMindDto);


}