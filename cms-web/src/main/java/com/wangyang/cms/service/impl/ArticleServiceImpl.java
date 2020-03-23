package com.wangyang.cms.service.impl;

import com.wangyang.authorize.pojo.entity.User;
import com.wangyang.authorize.service.IUserService;
import com.wangyang.cms.expection.ArticleException;
import com.wangyang.cms.expection.ObjectException;
import com.wangyang.cms.pojo.dto.ArticleDto;
import com.wangyang.cms.pojo.dto.CategoryArticleListDao;
import com.wangyang.cms.pojo.dto.CategoryDto;
import com.wangyang.cms.pojo.dto.TagsDto;
import com.wangyang.cms.pojo.entity.*;
import com.wangyang.cms.pojo.entity.base.BaseCategory;
import com.wangyang.cms.pojo.enums.ArticleStatus;
import com.wangyang.cms.pojo.enums.PropertyEnum;
import com.wangyang.cms.pojo.params.ArticleParams;
import com.wangyang.cms.pojo.params.ArticleQuery;
import com.wangyang.cms.pojo.support.CmsConst;
import com.wangyang.cms.pojo.support.TemplateOptionMethod;
import com.wangyang.cms.pojo.vo.ArticleDetailVO;
import com.wangyang.cms.pojo.vo.ArticleVO;
import com.wangyang.cms.pojo.vo.SheetDetailVo;
import com.wangyang.cms.repository.*;
import com.wangyang.cms.service.*;
import com.wangyang.cms.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ArticleServiceImpl extends BaseArticleServiceImpl<Article> implements IArticleService {

    @Value("${cms.workDir}")
    private String workDir;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    ITemplateService templateService;
    @Autowired
    ArticleTagsRepository articleTagsRepository;
    @Autowired
    TagsRepository tagsRepository;

    @Autowired
    ICategoryService categoryService;
    @Autowired
    CommentRepository commentRepository;



    @Autowired
    IOptionService optionService;

    @Autowired
    IUserService userService;

    @Autowired
    IChannelService channelService;

    @Autowired
    ISheetService sheetService;

    @Autowired
    BaseCategoryRepository baseCategoryRepository;

    /**
     * create article
     * @param tagsIds
     * @return
     */
    @Override
    public ArticleDetailVO createArticleDetailVo(Article article, Set<Integer> tagsIds) {

        article.setStatus(ArticleStatus.PUBLISHED);
        // 文章发布默认生成HTML
        if(article.getHaveHtml()==null){
            article.setHaveHtml(true);
        }



        ArticleDetailVO articleDetailVO = createOrUpdateArticle(article, tagsIds);
        return articleDetailVO;

    }

    @Override
    public ArticleDetailVO updateArticleDetailVo(Article article,  Set<Integer> tagsIds) {
        article.setPdfPath(null);
        article.setStatus(ArticleStatus.PUBLISHED);
        article.setUpdateDate(new Date());
        // 文章发布默认生成HTML
        if(article.getHaveHtml()==null){
            article.setHaveHtml(true);
        }

        //TODO temp delete all tags and category before update
        articleTagsRepository.deleteByArticleId(article.getId());

        ArticleDetailVO articleDetailVO = createOrUpdateArticle(article, tagsIds);
        return articleDetailVO;
    }


    @Override
    public ArticleDetailVO updateArticleDetailVo(Article article) {
        article.setPdfPath(null);
        article.setStatus(ArticleStatus.PUBLISHED);
        // 文章发布默认生成HTML
        if(article.getHaveHtml()==null){
            article.setHaveHtml(true);
        }


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
    public Article saveOrUpdateArticleDraft(Article article){
        if(article.getUserId()==null){
            throw new ArticleException("文章用户不能为空!!");
        }
        if(article.getCategoryId()==null){
            throw new ArticleException("文章类别不能为空!!");
        }
        article.setHaveHtml(false);
        // 获取默认文章模板Id
        if(article.getTemplateName()==null|| "".equals(article.getTemplateName())){
            article.setTemplateName(CmsConst.DEFAULT_ARTICLE_TEMPLATE);
        }
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
    public List<ArticleDto> listBy(int categoryId){
        Specification<Article> specification = new Specification<Article>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("categoryId"),categoryId);
            }
        };
        return  articleRepository.findAll(specification).stream().map(article -> {
            ArticleDto articleDto = new ArticleDto();
            BeanUtils.copyProperties(article,articleDto);
            return articleDto;
        }).collect(Collectors.toList());

    }





    @Override
    public String  generatePdf(Integer articleId) {

        Article article = findArticleById(articleId);
        if(article.getStatus()!=ArticleStatus.PUBLISHED){
            throw new ArticleException("文章没有发布不能生成PDF!!");
        }
        String pdfPath= article.getPath()+"/"+article.getViewName()+".pdf";
        String absolutePath = workDir+"/html/"+pdfPath;
        File file = new File(absolutePath);
        if(file.exists()){
            if(!pdfPath.equals(article.getPdfPath())){
                article.setPdfPath(pdfPath);
                Article updateArticle = articleRepository.save(article);
                return updateArticle.getPdfPath();
            }
            return article.getPdfPath();
        }else {
            String url = "http://localhost:8080/preview/pdf/"+articleId;
            String result = NodeJsUtil.execNodeJs("node", workDir+"/templates/nodejs/generatePdf.js", url, workDir + "/html/" + pdfPath);
            System.out.println(result);
            article.setPdfPath(pdfPath);
            Article saveArticle = articleRepository.save(article);
            return  saveArticle.getPdfPath();
        }
    }

    @Override
    public ModelAndView previewPdf(int articleId){
        ArticleDetailVO articleDetailVo = findArticleAOById(articleId);
//        Optional<Template> templateOptional = templateRepository.findById(articleDetailVo.getTemplateId());
//        if(!templateOptional.isPresent()){
//            throw new TemplateException("Template not found in preview !!");
//        }
        Template template = templateService.findByEnName(articleDetailVo.getTemplateName());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("view",articleDetailVo);
        modelAndView.addObject("notPdf",true);
        modelAndView.setViewName(template.getTemplateValue());
        return modelAndView;
    }

    /**
     * 用户前端预览
     * @param articleId
     * @return
     */
    @Override
    public ModelAndView previewForSave(int articleId) {
        Article article = findArticleById(articleId);
        article = super.previewSave(article);
        ArticleDetailVO articleDetailVo = convert(article);
//        Optional<Template> templateOptional = templateRepository.findById(articleDetailVo.getTemplateId());
//        if(!templateOptional.isPresent()){
//            throw new TemplateException("Template not found in preview !!");
//        }
        Template template = templateService.findByEnName(articleDetailVo.getTemplateName());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("view",articleDetailVo);
        modelAndView.setViewName(template.getTemplateValue());
        return modelAndView;
    }
    @Override
    public ModelAndView preview(int articleId) {
        Article article = findArticleById(articleId);
        if(article.getStatus()!=ArticleStatus.PUBLISHED){
            article = super.createOrUpdate(article);
        }
        ArticleDetailVO articleDetailVo = convert(article);
//        Optional<Template> templateOptional = templateRepository.findById(articleDetailVo.getTemplateId());
//        if(!templateOptional.isPresent()){
//            throw new TemplateException("Template not found in preview !!");
//        }
        Template template = templateService.findByEnName(articleDetailVo.getTemplateName());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("view",articleDetailVo);
        modelAndView.setViewName(template.getTemplateValue());
        return modelAndView;
    }



    @Override
    public Article deleteByArticleId(int id) {
        Article article = findArticleById(id);
        log.debug(">>> delete comment");
        commentRepository.deleteByArticleId(id);
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
        ArticleDetailVO articleDetailVO = new ArticleDetailVO();

        String viewName = article.getViewName();
        if(viewName==null||"".equals(viewName)){
            viewName = CMSUtils.randomViewName();
            log.debug("!!! view name not found, use "+viewName);
            article.setViewName(viewName);
        }

        Optional<BaseCategory> baseCategory = baseCategoryRepository.findById(article.getCategoryId());
        if(baseCategory.isPresent()){
            if(baseCategory.get() instanceof Category){
                Category category = (Category)baseCategory.get();
                articleDetailVO.setCategory(category);
            }else{
                Channel channel = (Channel)baseCategory.get();
                article.setPath(channel.getPath()+"/"+channel.getName());
                //栏目类型的文章由栏目统一管理文章模板
                article.setTemplateName(channel.getArticleTemplateName());
                if(channel.getFirstArticle()==null||"".equals(channel.getFirstArticle())){
                    channel.setFirstArticle(article.getViewName());
                    channel = channelService.save(channel);
                    articleDetailVO.setUpdateChannelFirstName(true);
                }

                articleDetailVO.setCategory(channel);
            }
        }else {
            throw new ObjectException("文章发布的分类没有找到!!");
        }


        article = super.createOrUpdate(article);

        generateSummary(article);

        // 获取默认文章模板Id
        if(article.getTemplateName()==null|| "".equals(article.getTemplateName())){
            article.setTemplateName(CmsConst.DEFAULT_ARTICLE_TEMPLATE);
        }
//        保存文章
        Article saveArticle = articleRepository.saveAndFlush(article);

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
            throw  new ArticleException("请先为文章 id:["+article.getId()+"] title: "+article.getTitle()+"指定类别再执行全部更新!!");
        }

        User user = userService.findById(article.getUserId());
        articleDetailVo.setUser(user);
        BaseCategory baseCategory = channelService.findBaseCategoryById(article.getCategoryId());
        articleDetailVo.setCategory(baseCategory);
        return articleDetailVo;
    }




    @Override
    public  List<Article>  updateAllArticleHtml(Boolean more){
        List<Article> articles = articleRepository.findAll();
        return articles;
    }

    @Override
    public ArticleDetailVO findArticleAOById(int id) {

        return convert(findArticleById(id));
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
    public Page<Article> articleList(ArticleQuery articleQuery,Pageable pageable){
        return  articleRepository.findAll(buildSpecByQuery(articleQuery),pageable);
    }

    @Override
    public Page<ArticleDto> convertToSimple(Page<Article> articlePage) {
        List<Article> articles = articlePage.getContent();
        Set<Integer> userIds = ServiceUtil.fetchProperty(articles, Article::getUserId);
        List<User> users = userService.findAllById(userIds);

        Map<Integer, User> userMap = ServiceUtil.convertToMap(users, User::getId);

        return  articlePage.map(article -> {
            ArticleDto articleVO = new ArticleDto();
            articleVO.setUser(userMap.get(article.getUserId()));
            BeanUtils.copyProperties(article,articleVO);
            return articleVO;
        });

    }

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
//        List<ArticleCategory> articleCategorys = articleCategoryRepository.findAllByArticleIdIn(categorys);
//        Set<Integer> categoryIds = ServiceUtil.fetchProperty(articleCategorys, ArticleCategory::getCategoryId);
//        List<Category> categorys = categoryRepository.findAllById(categoryIds);
//        Map<Integer, Category> categoryMap = ServiceUtil.convertToMap(categorys, Category::getId);
//        Map<Integer,List<Category>> categoryListMap = new HashMap<>();
//        articleCategorys.forEach(
//                articleCategory -> {
//                    categoryListMap.computeIfAbsent(articleCategory.getArticleId(),
//                            categoryId->new LinkedList<>()).add(categoryMap.get(articleCategory.getCategoryId()));
//                }
//        );

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

    private Specification<Article> buildSpecByQuery(ArticleQuery articleQuery) {
        return (Specification<Article>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();

            if (articleQuery.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), articleQuery.getStatus()));
            }

            if (articleQuery.getCategoryId() != null) {
//                Subquery<Article> articleSubQuery = query.subquery(Article.class);
//                Root<ArticleCategory> postCategoryRoot = articleSubQuery.from(ArticleCategory.class);
//                articleSubQuery.select(postCategoryRoot.get("articleId"));
//                articleSubQuery.where(
//                        criteriaBuilder.equal(root.get("id"), postCategoryRoot.get("articleId")),
//                        criteriaBuilder.equal(postCategoryRoot.get("categoryId"), articleQuery.getCategoryId()));
//                predicates.add(criteriaBuilder.exists(articleSubQuery));
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

            return query.where(predicates.toArray(new Predicate[0])).getRestriction();
        };
    }

    @Override
    public Page<ArticleDto> articleShow(Specification<Article> specification, Pageable pageable){
        Page<Article> articles = articleRepository.findAll(specification, pageable);
        return articles.map(article -> {
            ArticleDto articleDto = new ArticleDto();
            BeanUtils.copyProperties(article, articleDto);
            return articleDto;
        });
    }
    @Override
    public List<ArticleDto> articleShow(Specification<Article> specification,Sort sort){
        List<Article> articles = articleRepository.findAll(specification,sort);
        return articles.stream().map(article -> {
            ArticleDto articleDto = new ArticleDto();
            BeanUtils.copyProperties(article,articleDto);
            return articleDto;
        }).collect(Collectors.toList());
    }


    @Override
    @TemplateOptionMethod(name = "New Article",templateValue = "templates/components/@newArticle",viewName="newArticle",path = "components",event = "ACAU")
    public Page<ArticleDto> articleShowLatest(){
        Specification<Article> specification = new Specification<Article>() {
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Path path = root.get("likes");
                return null;
            }
        };
        return articleShow(specification,PageRequest.of(0,5,Sort.by(Sort.Order.desc("createDate"))));
    }

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




    @Override
    public Article haveHtml(int id){
        Article article = findArticleById(id);
        if(article.getStatus()==ArticleStatus.DRAFT){
            //草稿文章要生成Html,将文章状态改为发布
            article.setStatus(ArticleStatus.PUBLISHED);
            article =super.createOrUpdate(article);
            article.setUpdateDate(new Date());
            // 生成摘要
            generateSummary(article);
        }
        if(article.getHaveHtml()){
            article.setHaveHtml(false);
        }else {
            article.setHaveHtml(true);
        }
        articleRepository.save(article);
        return article;
    }


    @Override
    public void generateSummary(Article article){
        if(article.getSummary()==null||"".equals(article.getSummary())){
            String text = MarkdownUtils.getText(article.getFormatContent());
            String summary ;
            if(text.length()>100){
                summary = text.substring(0,100);
            }else {
                summary = text;
            }
            article.setSummary(summary+"....");
        }
    }

    @Override
    public ArticleDetailVO updateCategory(Article article, int categoryId){

        if(article.getStatus()!=ArticleStatus.PUBLISHED){
            throw new ArticleException("文章没有发布不能更改类别!!");
        }

//        article.setTitle(updateArticle.getTitle());
//        article.setOriginalContent(updateArticle.getOriginalContent());
//        article.setUserId(updateArticle.getUserId());
        BaseCategory baseCategory = channelService.findBaseCategoryById(categoryId);
        if(baseCategory instanceof  Channel) {
            article.setPath(baseCategory.getPath() + "/" + baseCategory.getName());
            article.setTemplateName(((Channel) baseCategory).getArticleTemplateName());
        }else {
            article.setPath("article");
            article.setTemplateName(CmsConst.DEFAULT_ARTICLE_TEMPLATE);
        }
        article.setCategoryId(categoryId);
        Article saveArticle = articleRepository.save(article);
        ArticleDetailVO articleDetailVO = conventToAddTags(saveArticle);
        articleDetailVO.setCategory(baseCategory);

        return articleDetailVO;
    }

    /**
     * 动态的获取第二页
     * @param categoryId
     * @param page
     * @return
     */
    @Override
    public ModelAndView getArticleListByCategory(int categoryId, int page) {
        ModelAndView modelAndView = new ModelAndView();
        Category category = categoryService.findById(categoryId);

        // 分页
        CategoryArticleListDao articleListByCategory = getArticleListByCategory(category, page);
//        Template template = templateService.findById(category.getTemplateId());
        Template template = templateService.findByEnName(category.getTemplateName());
        modelAndView.setViewName(template.getTemplateValue());
        modelAndView.addObject("view", articleListByCategory);
        return modelAndView;
    }



    /**
     * 获取第一页文章和分类, 用于生成HTML
     *  PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"))
     * @param category
     * @return
     */
    @Override
    public CategoryArticleListDao getArticleListByCategory(Category category){
        return getArticleListByCategory(category,0);
    }


//    public CategoryArticleListDao getArticleListByCategory(Category category,Integer page){
//        Integer pageSize = optionService.getPropertyIntegerValue(PropertyEnum.CATEGORY_PAGE_SIZE);
//        CategoryArticleListDao categoryArticleListDao = getArticleListByCategory(category, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id")));
//        return categoryArticleListDao;
//    }

    /**
     * 动态分页使用
     * @param category
     * @return
     */
    public CategoryArticleListDao getArticleListByCategory(Category category, int page){
        CategoryArticleListDao articleListVo = new CategoryArticleListDao();
        Page<ArticleDto> articleDtoPage = findArticleListByCategoryId(category.getId(), page);
        articleListVo.setPage(articleDtoPage);
        articleListVo.setCategory(category);
        articleListVo.setViewName(category.getViewName());
        return articleListVo;
    }

    /**
     * 分类页文章展示设置,可以通过Option动态设置分页大小, 排序
     * @param categoryId
     * @param page
     * @return
     */
    @Override
    public Page<ArticleDto> findArticleListByCategoryId(int categoryId, int page) {
        Integer pageSize = optionService.getPropertyIntegerValue(PropertyEnum.CATEGORY_PAGE_SIZE);
        return findArticleListByCategoryId(categoryId,PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id")));
    }

    /**
     * 查找分类第一页的文章,用于该分类下文章的静态化
     * @param categoryId
     * @param pageable
     * @return
     */
    public Page<ArticleDto> findArticleListByCategoryId(int categoryId,Pageable pageable) {
        Specification<Article> specification = new Specification<Article>() {
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                return criteriaQuery.where(criteriaBuilder.equal(root.get("categoryId"),categoryId)
                        ,criteriaBuilder.isTrue(root.get("haveHtml"))
                ).getRestriction();
            }
        };
        Page<Article> articles = articleRepository.findAll(specification, pageable);
        return  convertToSimple(articles);
    }

//
//    @Override
//    public ArticleDetailVO addArticleToChannel(Article article, int channelId){
//
//        article.setCategoryId(channel.getId());
//        Article saveArticle = articleRepository.save(article);
//        ArticleDetailVO articleDetailVO = findArticleAOById(articleId);
//
//        Article article = findArticleById(articleId);
//        Channel channel = channelService.findById(channelId);
//
//        Sheet sheet = new Sheet();
//        BeanUtils.copyProperties(article,sheet);
//        sheet.setChannelId(channelId);
//        sheet.setPath(channel.getPath()+"/"+channel.getName());
//        Sheet saveSheet = sheetService.save(sheet);
//
//        SheetDetailVo sheetDetailVo = new SheetDetailVo();
//        BeanUtils.copyProperties(saveSheet,sheetDetailVo);
//        sheetDetailVo.setChannel(channel);
//        return sheetDetailVo;
//    }
}
