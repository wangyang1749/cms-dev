package com.wangyang.service.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wangyang.common.exception.ArticleException;
import com.wangyang.common.exception.ObjectException;
import com.wangyang.common.utils.*;
import com.wangyang.pojo.entity.base.BaseEntity;
import com.wangyang.service.service.*;
import com.wangyang.pojo.dto.*;
import com.wangyang.pojo.entity.*;
import com.wangyang.pojo.enums.ArticleStatus;
import com.wangyang.pojo.vo.ArticleDetailVO;
import com.wangyang.pojo.vo.ArticleVO;

import com.wangyang.pojo.params.ArticleQuery;
import com.wangyang.common.CmsConst;
import com.wangyang.service.repository.*;
import com.wangyang.service.util.FormatUtil;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;


/**
 * ArticleServiceImpl
 *
 * @author wangyang
 */
@Service
@Transactional
@Slf4j
public class ArticleServiceImpl extends BaseArticleServiceImpl<Article> implements IArticleService {
    enum ArticleList{
        INCLUDE_TOP,
        NO_INCLUDE_TOP,
        ALL_PUBLISH_MODIFY_ARTICLE,
        ALL_ARTICLE
    }

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ArticleTagsRepository articleTagsRepository;
    @Autowired
    TagsRepository tagsRepository;

    @Autowired
    ICategoryService categoryService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    ComponentsArticleRepository componentsArticleRepository;
    @Autowired
    IUserService userService;

    private  List<Predicate> listPredicate(ArticleQuery articleQuery,Root<Article> root,CriteriaBuilder criteriaBuilder, CriteriaQuery<?> query){
        List<Predicate> predicates = new LinkedList<>();



        if (articleQuery.getCategoryId()!=null) {
            predicates.add(criteriaBuilder.equal(root.get("categoryId"),articleQuery.getCategoryId()));
        }

        if (articleQuery.getKeyword() != null) {
            // Format like condition
            String likeCondition = String.format("%%%s%%",articleQuery.getKeyword());

            // Build like predicate
            Predicate titleLike = criteriaBuilder.like(root.get("title"), likeCondition);
            Predicate originalContentLike = criteriaBuilder.like(root.get("originalContent"), likeCondition);

            predicates.add(criteriaBuilder.or(titleLike, originalContentLike));
        }
//            if(articleQuery.getHaveHtml()!=null){
//                predicates.add(criteriaBuilder.equal(root.get("haveHtml"),articleQuery.getHaveHtml()));
//            }
        if(articleQuery.getUserId()!=null){
            predicates.add(criteriaBuilder.equal(root.get("userId"), articleQuery.getUserId()));
        }
        if(articleQuery.getStatus()!=null){
            predicates.add(criteriaBuilder.equal(root.get("status"),articleQuery.getStatus()));
        }
        if(articleQuery.getTagsId()!=null){

            Subquery<Article> subquery = query.subquery(Article.class);
            Root<ArticleTags> subRoot = subquery.from(ArticleTags.class);
            subquery = subquery.select(subRoot.get("articleId")).where(criteriaBuilder.equal(subRoot.get("tagsId"),articleQuery.getTagsId()));
            predicates.add(criteriaBuilder.in(root.get("id")).value(subquery));
        }
        if(articleQuery.getTop()!=null){
            if(articleQuery.getTop()){
                predicates.add(criteriaBuilder.isTrue(root.get("top")));
            }else {
                predicates.add(criteriaBuilder.isFalse(root.get("top")));
            }

        }
        return predicates;
    }

    private Specification<Article> buildPublishByQuery(ArticleQuery articleQuery) {
        return (Specification<Article>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = listPredicate(articleQuery, root, criteriaBuilder, query);
            predicates.add(criteriaBuilder.or(criteriaBuilder.equal(root.get("status"), ArticleStatus.PUBLISHED),
                    criteriaBuilder.equal(root.get("status"), ArticleStatus.MODIFY)));

            query.where(predicates.toArray(new Predicate[0]));
            if(articleQuery.getDesc()!=null){
                if(articleQuery.getDesc()){
                    query.orderBy(criteriaBuilder.desc(root.get("order")),criteriaBuilder.desc(root.get("id")));
                }else {
                    query.orderBy(criteriaBuilder.asc(root.get("order")),criteriaBuilder.desc(root.get("id")));

                }
            }

            return query.getRestriction();
        };
    }
    private Specification<Article> buildAllByQuery(ArticleQuery articleQuery) {
        return (Specification<Article>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = listPredicate(articleQuery, root, criteriaBuilder, query);
            if (articleQuery.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), articleQuery.getStatus()));
            }
            query.where(predicates.toArray(new Predicate[0]));
            if(articleQuery.getDesc()!=null){
                if(articleQuery.getDesc()){
                    query.orderBy(criteriaBuilder.desc(root.get("order")),criteriaBuilder.desc(root.get("id")));
                }else {
                    query.orderBy(criteriaBuilder.asc(root.get("order")),criteriaBuilder.desc(root.get("id")));

                }
            }

            return query.getRestriction();
        };
    }
    private Specification<Article> articleSpecification(int  categoryId,ArticleList articleList){
        Category category =new Category();
        category.setId(categoryId);
        category.setDesc(true);
        return articleSpecification(category,articleList);
    }
    private Specification<Article> articleSpecification(Category category,ArticleList articleList){
        Specification<Article> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();
            predicates.add(criteriaBuilder.equal(root.get("categoryId"),category.getId()));

            if(articleList.equals(ArticleList.INCLUDE_TOP)){
                predicates.add( criteriaBuilder.isTrue(root.get("top")));
                predicates.add(  criteriaBuilder.or(criteriaBuilder.equal(root.get("status"), ArticleStatus.PUBLISHED),
                        criteriaBuilder.equal(root.get("status"), ArticleStatus.MODIFY)));

            }else if(articleList.equals(ArticleList.NO_INCLUDE_TOP)){
                predicates.add( criteriaBuilder.isFalse(root.get("top")));
                predicates.add(  criteriaBuilder.or(criteriaBuilder.equal(root.get("status"), ArticleStatus.PUBLISHED),
                        criteriaBuilder.equal(root.get("status"), ArticleStatus.MODIFY)));

            }else if(articleList.equals(ArticleList.ALL_PUBLISH_MODIFY_ARTICLE)){
                predicates.add(  criteriaBuilder.or(criteriaBuilder.equal(root.get("status"), ArticleStatus.PUBLISHED),
                        criteriaBuilder.equal(root.get("status"), ArticleStatus.MODIFY)));
            }else if(articleList.equals(ArticleList.ALL_ARTICLE)){

            }
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
            if(category.getDesc()){
//                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("updateDate")));
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("order")),criteriaBuilder.desc(root.get("id")));

            }else {
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("order")),criteriaBuilder.desc(root.get("id")));

//                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("updateDate")));
            }
            return criteriaQuery.getRestriction();
        };
        return specification;
    }


//    /**
//     * @param categoryId
//     * @return
//     */
//    private Specification<Article> queryByCategory(int categoryId){
//        Specification<Article> specification = new Specification<Article>() {
//            @Override
//            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//
//                criteriaQuery.where(criteriaBuilder.equal(root.get("categoryId"),categoryId),
//                                criteriaBuilder.or(criteriaBuilder.equal(root.get("status"), ArticleStatus.PUBLISHED),
//                                        criteriaBuilder.equal(root.get("status"), ArticleStatus.MODIFY))
//                );
//                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("order")),criteriaBuilder.desc(root.get("id")));
//                return  criteriaQuery.getRestriction();
//            }
//        };
//        return specification;
//    }
//    /**
//     * @param category
//     * @return
//     */
//    private Specification<Article> queryByCategory(Category category){
//        Specification<Article> specification = new Specification<Article>() {
//            @Override
//            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//
//                criteriaQuery.where(criteriaBuilder.equal(root.get("categoryId"),category.getId()),
//                        criteriaBuilder.or(criteriaBuilder.equal(root.get("status"), ArticleStatus.PUBLISHED),
//                                criteriaBuilder.equal(root.get("status"), ArticleStatus.MODIFY))
////                        criteriaBuilder.notEqual(root.get("status"),ArticleStatus.RECYCLE)
//                );
//                if(category.getDesc()){
//                    criteriaQuery.orderBy(criteriaBuilder.desc(root.get("order")),criteriaBuilder.desc(root.get("id")));
//                }else {
//                    criteriaQuery.orderBy(criteriaBuilder.asc(root.get("order")),criteriaBuilder.desc(root.get("id")));
//
//                }
//                return  criteriaQuery.getRestriction();
//            }
//        };
//        return specification;
//    }
//
//
//
//    /**
//     * 除去置顶文章查询条件
//     * @param category
//     * @return
//     */
//    private Specification<Article> queryArticleDtoNoTopByCategory(Category category ){
//        Specification<Article> specification = new Specification<Article>() {
//            @Override
//            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//
//                criteriaQuery.where(criteriaBuilder.equal(root.get("categoryId"),category.getId()),
//                        criteriaBuilder.isFalse(root.get("top")),
//                        criteriaBuilder.or(criteriaBuilder.equal(root.get("status"), ArticleStatus.PUBLISHED),
//                                criteriaBuilder.equal(root.get("status"), ArticleStatus.MODIFY))
//
//                );
//                if(category.getDesc()){
//                    criteriaQuery.orderBy(criteriaBuilder.desc(root.get("order")),criteriaBuilder.desc(root.get("id")));
//                }else {
//                    criteriaQuery.orderBy(criteriaBuilder.asc(root.get("order")),criteriaBuilder.desc(root.get("id")));
//
//                }
//
//                return  criteriaQuery.getRestriction();
//            }
//        };
//        return specification;
//    }
//
//    /**
//     * 置顶文章查询条件
//     * @param categoryId
//     * @return
//     */
//    private Specification<Article> queryListByCategory(int categoryId){
//        Specification<Article> specification = new Specification<Article>() {
//            @Override
//            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//
//                criteriaQuery.where(criteriaBuilder.equal(root.get("categoryId"),categoryId),
////                        ,criteriaBuilder.isTrue(root.get("haveHtml")),
//                        criteriaBuilder.isTrue(root.get("top"))
//                );
//                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("order")),criteriaBuilder.desc(root.get("id")));
//                return  criteriaQuery.getRestriction();
//            }
//        };
//        return specification;
//    }
//


    /**
     * create article
     * @param tagsIds
     * @return
     */
    @Override
    public ArticleDetailVO createArticleDetailVo(Article article, Set<Integer> tagsIds) {
        if(article.getOrder()==null){
            int count = articleRepository.countBycategoryId(article.getCategoryId());
            article.setOrder(count+1);
        }
        if(article.getParentId()==null){
            article.setParentId(0);
        }
        if(article.getDirection()==null){
            article.setDirection("right");// 脑图方向向左
        }
        if(article.getExpanded()== null){
            article.setExpanded(false);// 脑图默认不展开
        }

//        article.setStatus(ArticleStatus.PUBLISHED);
//        article.setHaveHtml(true);
        article.setTop(false);
        ArticleDetailVO articleDetailVO = createOrUpdateArticle(article, tagsIds);
        return articleDetailVO;
    }



    @Override
    public ArticleDetailVO updateArticleDetailVo(Article article,  Set<Integer> tagsIds) {
        if(article.getOrder()==null){
            int count = articleRepository.countBycategoryId(article.getCategoryId());
            article.setOrder(count+1);
        }
        article.setPdfPath(null);
//        article.setStatus(ArticleStatus.PUBLISHED);
//        article.setHaveHtml(true);
        article.setUpdateDate(new Date());


        //TODO temp delete all tags and category before update
        articleTagsRepository.deleteByArticleId(article.getId());

        ArticleDetailVO articleDetailVO = createOrUpdateArticle(article, tagsIds);
        return articleDetailVO;
    }


    @Override
    public ArticleDetailVO updateArticleDetailVo(Article article) {
        article.setPdfPath(null);
//        article.setStatus(ArticleStatus.PUBLISHED);
        // 文章发布默认生成HTML
//        if(article.getHaveHtml()==null){
//            article.setHaveHtml(true);
//        }
        ArticleDetailVO articleDetailVO = createOrUpdateArticle(article, null);
        return articleDetailVO;
    }


    @Override
    public Article save(Article article){
        return  articleRepository.save(article);
    }

    /**
     * 保存或者更新文章草稿
     * @param article
     * @return
     */
    @Override
    public Article updateArticleDraft(Article article){

        if(article.getUserId()==null){
            throw new ArticleException("文章用户不能为空!!");
        }

        String viewName = article.getViewName();
        if(viewName==null||"".equals(viewName)){
            viewName = CMSUtils.randomViewName();
            log.debug("!!! view name not found, use "+viewName);
            article.setViewName(viewName);
        }
//        article.setStatus(ArticleStatus.DRAFT);
        return  articleRepository.save(article);
    }

    @Override
    public Article saveArticleDraft(Article article){
        article.setPath(CMSUtils.getArticlePath());

        if(article.getParentId()==null){
            article.setParentId(0);
        }
        if(article.getDirection()==null){
            article.setDirection("right");// 脑图方向向左
        }
        if(article.getExpanded()== null){
            article.setExpanded(false);// 脑图默认不展开
        }

        if(article.getUserId()==null){
            throw new ArticleException("文章用户不能为空!!");
        }
        article.setTop(false);

        String viewName = article.getViewName();
        if(viewName==null||"".equals(viewName)){
            viewName = CMSUtils.randomViewName();
            log.debug("!!! view name not found, use "+viewName);
            article.setViewName(viewName);
        }
        article.setStatus(ArticleStatus.DRAFT);
        return  articleRepository.save(article);
    }

    @Override
    public Article recycle(int id) {
        Article article = findArticleById(id);
        Article findArticle = new Article();
        BeanUtils.copyProperties(article,findArticle);
        article.setStatus(ArticleStatus.RECYCLE);
        article.setTop(false);
        article.setOpenComment(false);
        articleRepository.save(article);
        return findArticle;
    }

    @Override
    public Article deleteByArticleId(int id) {
        Article article = findArticleById(id);
        log.debug(">>> delete comment");
//        commentRepository.deleteByArticleId(id);
        log.debug(">>> delete article tags");
        articleTagsRepository.deleteByArticleId(id);
        log.debug("delete article");
        articleRepository.deleteById(id);
        return article;
    }

//    @Override
//    public ArticleDetailVO createOrUpdateArticleVo(Article article, Set<Integer> tagsIds) {
//        ArticleDetailVO saveArticle = createOrUpdateArticle(article, tagsIds);
//        // crate value object
////        ArticleDetailVO articleVO = convert(saveArticle,tagsIds);
//        return saveArticle;
//    }

    private ArticleDetailVO createOrUpdateArticle(Article article, Set<Integer> tagsIds) {
        if(article.getUserId()==null){
            throw new ArticleException("文章用户不能为空!!");
        }
        if(article.getCategoryId()==null){
            throw new ArticleException("文章类别不能为空!!");
        }
        if(article.getStatus()!=ArticleStatus.INTIMATE){
            article.setStatus(ArticleStatus.PUBLISHED);
        }
        ArticleDetailVO articleDetailVO = new ArticleDetailVO();

        String viewName = article.getViewName();
        if(viewName==null||"".equals(viewName)){
            viewName = CMSUtils.randomViewName();
            log.debug("!!! view name not found, use "+viewName);
            article.setViewName(viewName);
        }
//        article.setHaveHtml(true);

        //设置评论模板
        if(article.getCommentTemplateName()==null){
            article.setCommentTemplateName(CmsConst.DEFAULT_COMMENT_TEMPLATE);
        }
        Category category = categoryService.findById(article.getCategoryId());
        article.setPath(CMSUtils.getArticlePath());

        if(article.getTemplateName()==null){
            //由分类管理文章的模板，这样设置可以让文章去维护自己的模板
            article.setTemplateName(category.getArticleTemplateName());
        }

        article = super.createOrUpdate(article);
        //图片展示
        if(article.getPicPath()==null|| "".equals(article.getPicPath())){
            String imgSrc = ImageUtils.getImgSrc(article.getOriginalContent());
            article.setPicPath(imgSrc);
        }
        generateSummary(article);

//        保存文章
        Article saveArticle = articleRepository.save(article);
        articleDetailVO.setCategory(category);
//        articleDetailVO.setUpdateChannelFirstName(true);
        BeanUtils.copyProperties(saveArticle,articleDetailVO);
        // 添加标签
        if (!CollectionUtils.isEmpty(tagsIds)) {
            // Get Article tags
            List<ArticleTags> articleTagsList = tagsIds.stream().map(tagId -> {
                ArticleTags articleTags = new ArticleTags();
                articleTags.setTagsId(tagId);
                articleTags.setArticleId(saveArticle.getId());
                return articleTags;
            }).collect(Collectors.toList());
            //save article tags
            articleTagsRepository.saveAll(articleTagsList);
            articleDetailVO.setTagIds(tagsIds);
            List<Tags> tags = tagsRepository.findAllById(tagsIds);
            articleDetailVO.setTags(tags);

        }
        //添加用户
        User user = userService.findById(article.getUserId());
        articleDetailVO.setUser(user);
        return articleDetailVO;
    }


    private ArticleDetailVO convert(Article saveArticle, Set<Integer> tagsIds) {
        ArticleDetailVO articleDetailVO = new ArticleDetailVO();
        BeanUtils.copyProperties(saveArticle,articleDetailVO);

        //find tags
        if(!CollectionUtils.isEmpty(tagsIds)){
            articleDetailVO.setTagIds(tagsIds);
            List<Tags> tags = tagsRepository.findAllById(tagsIds);
            articleDetailVO.setTags(tags);
        }

        if(saveArticle.getCategoryId()!=null){
            Category category = categoryService.findById(saveArticle.getCategoryId());
            articleDetailVO.setCategory(category);
        }

        return articleDetailVO;
    }

    /**
     * 为文章只添加标签
     * @param article
     * @return
     */
    @Override
    public ArticleDetailVO conventToAddTags(Article article){
        ArticleDetailVO articleDetailVo = new ArticleDetailVO();
        BeanUtils.copyProperties(article,articleDetailVo);
        //find tags
        List<Tags> tags = tagsRepository.findTagsByArticleId(article.getId());
        if(!CollectionUtils.isEmpty(tags)){
            articleDetailVo.setTags(tags);
            articleDetailVo.setTagIds( ServiceUtil.fetchProperty(tags, Tags::getId));
        }
        //添加用户
        User user = userService.findById(article.getUserId());
        articleDetailVo.setUser(user);

        return articleDetailVo;
    }

    /**
     * 为文章添加类别和标签
     * @param article
     * @return
     */
    @Override
    public ArticleDetailVO convert(Article article) {
        ArticleDetailVO articleDetailVo = new ArticleDetailVO();
        BeanUtils.copyProperties(article,articleDetailVo);
        
        //find tags
        List<Tags> tags = tagsRepository.findTagsByArticleId(article.getId());
        if(!CollectionUtils.isEmpty(tags)){
            articleDetailVo.setTags(tags);
            articleDetailVo.setTagIds( ServiceUtil.fetchProperty(tags, Tags::getId));
        }
        if(article.getCategoryId()==null){
            throw  new ArticleException("文章["+article.getTitle()+"]的没有指定类别!!");
        }

        User user = userService.findById(article.getUserId());
        articleDetailVo.setUser(user);
        Optional<Category> optionalCategory = categoryService.findOptionalById(article.getCategoryId());
        if(optionalCategory.isPresent()){
//            throw new ObjectException("文章为名称："+article.getTitle()+" 文章为Id："+article.getId()+"分类没有找到！");
            if(articleDetailVo.getTemplateName()==null){
                articleDetailVo.setTemplateName(optionalCategory.get().getArticleTemplateName());
            }
            articleDetailVo.setCategory(optionalCategory.get());
        }


        return articleDetailVo;
    }




    @Override
    public  List<Article>  listHaveHtml(){
        List<Article> articles = articleRepository.findAll(new Specification<Article>() {
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.or(criteriaBuilder.equal(root.get("status"), ArticleStatus.PUBLISHED),
                        criteriaBuilder.equal(root.get("status"), ArticleStatus.MODIFY));
            }
        });
        return articles;
    }

    @Override
    public ArticleDetailVO findArticleAOById(int id) {

        return conventToAddTags(findArticleById(id));
//        return convert(findArticleById(id));
    }

    @Override
    public Article findArticleById(int id) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if(!optionalArticle.isPresent()){
            throw new ObjectException("Article is not found");
        }
        return optionalArticle.get();
    }




    @Override
    public Article findByIdAndUserId(int id, int userId){
        Article article = articleRepository.findByIdAndUserId(id, userId);
        if(article==null){
            throw new ObjectException("用户为"+userId+"的文章不存在！");
        }
        return article;
    }



    @Override
    public Page<ArticleDto> convertToSimple(Page<Article> articlePage) {
        List<Article> articles = articlePage.getContent();
        Set<Integer> userIds = ServiceUtil.fetchProperty(articles, Article::getUserId);
        List<User> users = userService.findAllById(userIds);

        Map<Integer, User> userMap = ServiceUtil.convertToMap(users, User::getId);

        return  articlePage.map(article -> {
            ArticleDto articleDto = new ArticleDto();
            articleDto.setUser(userMap.get(article.getUserId()));
            BeanUtils.copyProperties(article,articleDto);
            articleDto.setLinkPath(FormatUtil.articleListFormat(article));
            return articleDto;
        });

    }




    /**
     * 添加为分裂数据添加category
     * @param articlePage
     * @return
     */
//    @Override
//    public Page<ArticleVO> convertToAddCategory(Page<Article> articlePage) {
//        List<Article> articles = articlePage.getContent();
//        Set<Integer> categories = ServiceUtil.fetchProperty(articles, Article::getCategoryId);
//        List<CategoryDto> categoryDtos = categoryService.findAllById(categories).stream().map(category -> {
//            CategoryDto categoryDto = new CategoryDto();
//            BeanUtils.copyProperties(category, categoryDto);
//            return categoryDto;
//        }).collect(Collectors.toList());
//        Map<Integer, CategoryDto> categoryMap = ServiceUtil.convertToMap(categoryDtos, CategoryDto::getId);
//        Page<ArticleVO> articleVOS = articlePage.map(article -> {
//            ArticleVO articleVO = new ArticleVO();
//            BeanUtils.copyProperties(article,articleVO);
//            if(categoryMap.containsKey(article.getCategoryId())){
//                articleVO.setCategory( categoryMap.get(article.getCategoryId()));
//            }
//            return articleVO;
//        });
//        return articleVOS;
//    }

    @Override
    public Page<ArticleVO> convertToListVo(Page<Article> articlePage) {
        List<Article> articles = articlePage.getContent();
        //Get article Ids
        Set<Integer> articleIds = ServiceUtil.fetchProperty(articles, Article::getId);

        List<ArticleTags> articleTags = articleTagsRepository.findAllByArticleIdIn(articleIds);

        Set<Integer> tagIds = ServiceUtil.fetchProperty(articleTags, ArticleTags::getTagsId);
        List<Tags> tags = tagsRepository.findAllById(tagIds);
        Map<Integer, Tags> tagsMap = ServiceUtil.convertToMap(tags, Tags::getId);
        Map<Integer,List<Tags>> tagsListMap = new HashMap<>();
        articleTags.forEach(
                articleTag->{
                    tagsListMap.computeIfAbsent(articleTag.getArticleId(),
                            tagsId->new LinkedList<>())
                            .add(tagsMap.get(articleTag.getTagsId()));
                }

        );
        Set<Integer> categories = ServiceUtil.fetchProperty(articles, Article::getCategoryId);
        List<CategoryDto> categoryDtos = categoryService.findAllById(categories).stream().map(category -> {
            CategoryDto categoryDto = new CategoryDto();
            BeanUtils.copyProperties(category, categoryDto);
            return categoryDto;
        }).collect(Collectors.toList());
        Map<Integer, CategoryDto> categoryMap = ServiceUtil.convertToMap(categoryDtos, CategoryDto::getId);


        Page<ArticleVO> articleVOS = articlePage.map(article -> {
            ArticleVO articleVO = new ArticleVO();
            BeanUtils.copyProperties(article,articleVO);

            if(categoryMap.containsKey(article.getCategoryId())){
                articleVO.setCategory( categoryMap.get(article.getCategoryId()));

            }

            articleVO.setTags(Optional.ofNullable(tagsListMap.get(article.getId()))
                    .orElseGet(LinkedList::new)
                    .stream()
                    .filter(Objects::nonNull)
                    .map(tag->{
                        TagsDto tagsDto = new TagsDto();
                        BeanUtils.copyProperties(tag,tagsDto);
                        return tagsDto;
                    })
                    .collect(Collectors.toList()));
//            articleVO.setTags(tagsListMap.get(article.getId()));
            return articleVO;
        });
        return articleVOS;
    }


//    @Override
//    public Page<ArticleDto> articleShow(Specification<Article> specification, Pageable pageable){
//        Page<Article> articles = articleRepository.findAll(specification, pageable);
//        return convertArticle2ArticleDto(articles);
//    }
//    @Override
//    public List<ArticleDto> articleShow(Specification<Article> specification,Sort sort){
//        List<Article> articles = articleRepository.findAll(specification,sort);
//        return articles.stream().map(article -> {
//            ArticleDto articleDto = new ArticleDto();
//            BeanUtils.copyProperties(article,articleDto);
//            return articleDto;
//        }).collect(Collectors.toList());
//    }

    @Override
    public int increaseLikes(int id) {
        int affectedRows = articleRepository.updateLikes(id);
        return affectedRows;
    }

    @Override
    public Integer getLikesNumber(int id){
        Integer likesNumber = articleRepository.getLikesNumber(id);

        return likesNumber;
    }
    @Override
    public int increaseVisits(int id) {
        int affectedRows = articleRepository.updateVisits(id);
        return affectedRows;
    }

    @Override
    public Integer getVisitsNumber(int id){
        Integer likesNumber = articleRepository.getVisitsNumber(id);

        return likesNumber;
    }



//
//    @Override
//    public Article haveHtml(int id){
//        Article article = findArticleById(id);
//        if(article.getCategoryId()==null){
//            throw new ArticleException("文章别为空不能生成文章，请编辑添加类别！");
//        }
//        if(article.getStatus()==ArticleStatus.DRAFT){
//            //草稿文章要生成Html,将文章状态改为发布
//            article.setStatus(ArticleStatus.PUBLISHED);
//            article =super.createOrUpdate(article);
//            article.setUpdateDate(new Date());
//            Category category = categoryService.findById(article.getCategoryId());
//            article.setPath(category.getPath());
//            article.setTemplateName(category.getArticleTemplateName());
//            // 生成摘要
//            generateSummary(article);
//        }
//        if(article.getHaveHtml()){
//            article.setHaveHtml(false);
//        }else {
//            article.setHaveHtml(true);
//        }
//        articleRepository.save(article);
//        return article;
//    }


    /**
     * 打开或者关闭评论
     * @param id
     * @return
     */
    @Override
    public Article openComment(int id){
        Article article = findArticleById(id);
        if(article.getOpenComment()==null|| article.getCommentTemplateName()==null){
            article.setOpenComment(true);
            article.setCommentTemplateName(CmsConst.DEFAULT_COMMENT_TEMPLATE);
            return  articleRepository.save(article);
        }

        if(article.getOpenComment()){
            article.setOpenComment(false);
        }else {
            article.setOpenComment(true);
        }
        articleRepository.save(article);
        return article;
    }


    @Override
    public void generateSummary(Article article){
//        if(article.getSummary()==null||"".equals(article.getSummary())){
//
//        }
        String text = MarkdownUtils.getText(article.getFormatContent());
        String summary ;
        if(text.length()>100){
            summary = text.substring(0,100);
        }else {
            summary = text;
        }
        article.setSummary(summary+"....");
    }

    /**
     * 更改文章类别
     * @param article
     * @param categoryId
     * @return
     */
    @Override
    public ArticleDetailVO updateCategory(Article article, int categoryId){
        if(article.getUserId()==null){
            throw new ArticleException("文章用户不能为空!!");
        }
        if(article.getCategoryId()==null){
            throw new ArticleException("文章类别不能为空!!");
        }
//        if(article.getStatus()!=ArticleStatus.PUBLISHED){
//            throw new ArticleException("文章没有发布不能更改类别!!");
//        }

//        article.setTitle(updateArticle.getTitle());
//        article.setOriginalContent(updateArticle.getOriginalContent());
//        article.setUserId(updateArticle.getUserId());
        Category category = categoryService.findById(categoryId);
        //文章路径
//        article.setPath();
        article.setTemplateName(category.getArticleTemplateName());

//        if(baseCategory instanceof  Channel) {
//            article.setPath(baseCategory.getPath() + "/" + baseCategory.getName());
//            article.setTemplateName(((Channel) baseCategory).getArticleTemplateName());
//        }else {
//            article.setPath("article");
//            article.setTemplateName(CmsConst.DEFAULT_ARTICLE_TEMPLATE);
//        }
        article.setCategoryId(categoryId);
        Article saveArticle = articleRepository.save(article);
        ArticleDetailVO articleDetailVO = conventToAddTags(saveArticle);
        articleDetailVO.setCategory(category);

        return articleDetailVO;
    }


    /**
     * 动态分页使用
     * @param category
     * @return
     */
    @Override
    public CategoryArticleListDao findCategoryArticleBy(Category category, int page){
        CategoryArticleListDao articleListVo = new CategoryArticleListDao();

        Page<Article> articles = articleRepository.findAll(articleSpecification(category,ArticleList.NO_INCLUDE_TOP),PageRequest.of(page,category.getArticleListSize()));
        Page<ArticleDto> articleDtoPage = convertToSimple(articles);
        articleListVo.setPage(articleDtoPage);
        articleListVo.setCategory(category);
        articleListVo.setViewName(category.getViewName());
        articleListVo.setPath(category.getPath());
        /**
         * 分页路径的格式生成
         */
        articleListVo.setLinkPath(FormatUtil.categoryList2Format(category));
        return articleListVo;
    }





    private List<ArticleDto> convertArticle2ArticleDto(List<Article> articles){
        return articles.stream().map(article -> {
            ArticleDto articleDto = new ArticleDto();
            BeanUtils.copyProperties(article,articleDto);
            articleDto.setLinkPath(FormatUtil.articleListFormat(article));
            return articleDto;
        }).collect(Collectors.toList());
    }

    @Override
    public Page<ArticleDto> convertArticle2ArticleDto(Page<Article> articles){
        return articles.map(article -> {
            ArticleDto articleDto = new ArticleDto();
            BeanUtils.copyProperties(article, articleDto);
            articleDto.setLinkPath(FormatUtil.articleListFormat(article));
            return articleDto;
        });
    }


    @Override
    public List<Article> listArticleDtoBy(int categoryId){
        return articleRepository.findAll(articleSpecification(categoryId,ArticleList.ALL_ARTICLE));
    }

    @Override
    public List<Article> listArticleBy(int categoryId){
        return  articleRepository.findAll(articleSpecification(categoryId,ArticleList.ALL_PUBLISH_MODIFY_ARTICLE));
    }







    @Override
    public Page<Article> pageAllBy(Pageable pageable,ArticleQuery articleQuery){
        return  articleRepository.findAll(buildAllByQuery(articleQuery),pageable);
    }
    @Override
    public Page<Article>  pageByUserId(int userId, Pageable pageable,ArticleQuery articleQuery){
        articleQuery.setUserId(userId);
        return  pageAllBy(pageable,articleQuery);
    }

    @Override
    public Page<Article>  pagePublishBy(Pageable pageable,ArticleQuery articleQuery){
        return  articleRepository.findAll(buildPublishByQuery(articleQuery),pageable);
    }
    @Override
    public Page<Article>  pagePublishBy(Pageable pageable){
        return  articleRepository.findAll(new Specification<Article>() {
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.or(criteriaBuilder.equal(root.get("status"), ArticleStatus.PUBLISHED),
                        criteriaBuilder.equal(root.get("status"), ArticleStatus.MODIFY));
            }
        },pageable);
    }



//    public Page<ArticleDto>  pageDtoBy(Pageable pageable,ArticleQuery articleQuery){
//        return  convertToSimple(pageUserBy(pageable,articleQuery));
//    }
    /**
     * 查找分类第一页的文章,用于该分类下文章的静态化
     * @param category
     * @param pageable
     * @return
     */
    @Override
    public Page<ArticleDto> pageDtoByCategory(Category category, Pageable pageable) {
        ArticleQuery articleQuery = new ArticleQuery();
        articleQuery.setCategoryId(category.getId());
        articleQuery.setDesc(category.getDesc());
        Page<Article> articles = pagePublishBy(pageable, articleQuery);
        return  convertToSimple(articles);
    }
    /**
     * 分类页文章展示设置,可以通过Option动态设置分页大小, 排序
     * @param categoryId
     * @param page
     * @return
     */
    @Override
    public Page<ArticleDto> pageDtoByCategoryId(int categoryId, int page) {
        Category category = categoryService.findById(categoryId);
        return pageDtoByCategory(category, PageRequest.of(page, category.getArticleListSize()));
    }











    @Override
    public Article updateOrder(int articleId, int order){
        Article article = findArticleById(articleId);
        article.setOrder(order);
        Article saveArticle = articleRepository.save(article);
        return saveArticle;
    }






    @Override
    public Page<ArticleDto> pageByTagId(int tagId, int size){
        return pageByTagId(tagId,PageRequest.of(0,size,Sort.by(Sort.Order.desc("createDate"))));
    }

    @Override
    public Page<ArticleDto> pageByTagId(int tagId, Pageable pageable){

        Specification<Article> specification = new Specification<Article>() {
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                Subquery<Article> subquery = criteriaQuery.subquery(Article.class);
                Root<ArticleTags> subRoot = subquery.from(ArticleTags.class);
                subquery = subquery.select(subRoot.get("articleId")).where(criteriaBuilder.equal(subRoot.get("tagsId"),tagId));
                return criteriaQuery.where(criteriaBuilder.in(root.get("id")).value(subquery)).getRestriction();
            }
        };
        Page<Article> articles = articleRepository.findAll(specification, pageable);
        return   convertArticle2ArticleDto(articles);
    }




    @Override
    public List<ArticleDto> listByTitle(String title){
       Specification<Article> specification = new Specification<Article>() {
           @Override
           public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
               String likeCondition = String.format("%%%s%%",title);
               return criteriaQuery.where(criteriaBuilder.like(root.get("title"),likeCondition)).getRestriction();
           }
       };
        return articleRepository.findAll(specification).stream().map(article -> {
            ArticleDto articleDto = new ArticleDto();
            BeanUtils.copyProperties(article, articleDto);
            return articleDto;
        }).collect(Collectors.toList());
    }





    /**
     * 根据id查找文章
     * @param ids
     * @return
     */
    @Override
    public List<Article> listByIds(Set<Integer> ids){
        List<Article> articles = articleRepository.findAllById(ids);
        return articles;
    }

    @Override
    public List<ArticleDto> listByComponentsId(int componentsId){
        List<ComponentsArticle> componentsArticles = componentsArticleRepository.findByComponentId(componentsId);
        Set<Integer> articleIds = ServiceUtil.fetchProperty(componentsArticles, ComponentsArticle::getArticleId);
        List<Article> articles = articleRepository.findAllById(articleIds);
        return convertArticle2ArticleDto(articles);
    }

    @Override
    public List<Article>listByUserId(int userId){
        ArticleQuery articleQuery = new ArticleQuery();
        articleQuery.setUserId(userId);
        return articleRepository.findAll(buildAllByQuery(articleQuery));
    }
    @Override
    public Article findByViewName(String viewName){
        return articleRepository.findByViewName(viewName);
    }

    @Override
    public ArticleAndCategoryMindDto listArticleMindDto(int categoryId){

        ArticleAndCategoryMindDto articleAndCategoryMindDto = new ArticleAndCategoryMindDto();
        List<ArticleMindDto> articleMindDtoList = listArticleBy(categoryId).stream().map(article -> {
            ArticleMindDto articleMindDto = new ArticleMindDto();
            BeanUtils.copyProperties(article, articleMindDto);
            return articleMindDto;
        }).collect(Collectors.toList());
        Category category = categoryService.findById(categoryId);

        articleAndCategoryMindDto.setCategory(category);
        articleAndCategoryMindDto.setList(articleMindDtoList);
        articleAndCategoryMindDto.setLinkPath(FormatUtil.categoryList2Format(category));
        return  articleAndCategoryMindDto;
    }


    @Override
    public String jsMindFormat( ArticleAndCategoryMindDto articleAndCategoryMindDto ){
        JSONArray root = new JSONArray();
        Category category = articleAndCategoryMindDto.getCategory();
        JSONObject item = new JSONObject();
        item.put("id",String.valueOf(0));
        item.put("topic",category.getName());
        item.put("isroot",true);
        root.add(item);
        for (ArticleMindDto articleMindDto:articleAndCategoryMindDto.getList()){
            item = new JSONObject();
            item.put("id",String.valueOf(articleMindDto.getId()));
            item.put("topic",articleMindDto.getTitle());
            item.put("direction",articleMindDto.getDirection());
            item.put("expanded",articleMindDto.getExpanded());
            item.put("parentid",String.valueOf(articleMindDto.getParentId()));
            item.put("data","/"+articleMindDto.getPath()+"/"+articleMindDto.getViewName()+".html");
            root.add(item);
        }

//        articleMindDtoList.add(new ArticleMindDto(0,category.getName(),category.getViewName(),
//                category.getPath(),-1));
        return  root.toString();
    }


    @Override
    public List<ArticleDto> listTopByCategoryId(Category category) {
        List<Article> articles = articleRepository.findAll(articleSpecification(
                category,ArticleList.INCLUDE_TOP));
        return convertArticle2ArticleDto(articles);
    }

    @Override
    public Article sendOrCancelTop(int id) {
        Article article = findArticleById(id);
        if(article.getStatus().equals(ArticleStatus.DRAFT)||article.getStatus().equals(ArticleStatus.RECYCLE)){
            throw new ArticleException("该文章没有发布不能置顶！");
        }
        if(article.getTop()){
            article.setTop(false);
        }else {
            article.setTop(true);
        }
        return  article;
    }

    @Override
    public  Integer getCommentNum(int id){
        return  articleRepository.getCommentNum(id);
    }

    @Override
    public void updateCommentNum(int id, int num){
        articleRepository.updateCommentNum(id,num);
    }


}
