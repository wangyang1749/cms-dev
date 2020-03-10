package com.wangyang.cms.service.impl;

import com.wangyang.cms.core.jms.CmsService;
import com.wangyang.cms.expection.ArticleException;
import com.wangyang.cms.expection.ObjectException;
import com.wangyang.cms.expection.TemplateException;
import com.wangyang.cms.pojo.dto.ArticleDto;
import com.wangyang.cms.pojo.dto.CategoryDto;
import com.wangyang.cms.pojo.dto.TagsDto;
import com.wangyang.cms.pojo.entity.*;
import com.wangyang.cms.pojo.enums.ArticleStatus;
import com.wangyang.cms.pojo.params.ArticleParams;
import com.wangyang.cms.pojo.params.ArticleQuery;
import com.wangyang.cms.pojo.support.BaseResponse;
import com.wangyang.cms.pojo.support.CmsConst;
import com.wangyang.cms.pojo.support.TemplateOptionMethod;
import com.wangyang.cms.pojo.vo.ArticleDetailVO;
import com.wangyang.cms.pojo.vo.ArticleVO;
import com.wangyang.cms.repository.*;
import com.wangyang.cms.service.IArticleService;
import com.wangyang.cms.service.ICategoryService;
import com.wangyang.cms.utils.CMSUtils;
import com.wangyang.cms.utils.NodeJsUtil;
import com.wangyang.cms.utils.ServiceUtil;
import com.wangyang.cms.utils.TemplateUtil;
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
import org.springframework.web.bind.annotation.PathVariable;
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
    TemplateRepository templateRepository;
    @Autowired
    ArticleTagsRepository articleTagsRepository;
    @Autowired
    TagsRepository tagsRepository;
    @Autowired
    ArticleCategoryRepository articleCategoryRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CmsService cmsService;


    /**
     * create article
     * @param articleParams
     * @param tagsIds
     * @param categoryIds
     * @return
     */
    @Override
    public ArticleDetailVO createArticle(ArticleParams articleParams, Set<Integer> tagsIds, Set<Integer> categoryIds) {
        Article article = new Article();
        article.setStatus(ArticleStatus.PUBLISHED);
        BeanUtils.copyProperties(articleParams,article);
        ArticleDetailVO articleDetailVO = createOrUpdateArticleVo(article, tagsIds, categoryIds);
        return articleDetailVO;

    }

    @Override
    public Article saveArticle(Integer articleId, ArticleParams articleParams, Set<Integer> tagsIds, Set<Integer> categoryIds){
        Article article;
        if(articleId!=null){
            article = findArticleById(articleId);
            BeanUtils.copyProperties(articleParams,article);
            articleTagsRepository.deleteByArticleId(articleId);
            articleCategoryRepository.deleteByArticleId(articleId);
        }else{
            article = new Article();
            BeanUtils.copyProperties(articleParams,article);
        }
        article.setStatus(ArticleStatus.DRAFT);
        return createOrUpdateArticle(article,tagsIds,categoryIds);
    }


    @Override
    public ArticleDetailVO updateArticle(int articleId, ArticleParams articleParams,  Set<Integer> tagsIds, Set<Integer> categoryIds) {
        Article article = findArticleById(articleId);
        article.setPdfPath(null);
        article.setStatus(ArticleStatus.PUBLISHED);
        TemplateUtil.deleteTemplateHtml(article.getViewName(),article.getPath());
        BeanUtils.copyProperties(articleParams,article);

        //TODO temp delete all tags and category before update
        articleTagsRepository.deleteByArticleId(articleId);
        articleCategoryRepository.deleteByArticleId(articleId);
        ArticleDetailVO articleDetailVO = createOrUpdateArticleVo(article, tagsIds, categoryIds);
        return articleDetailVO;
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
        Optional<Template> templateOptional = templateRepository.findById(articleDetailVo.getTemplateId());
        if(!templateOptional.isPresent()){
            throw new TemplateException("Template not found in preview !!");
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("view",articleDetailVo);
        modelAndView.addObject("notPdf",true);
        modelAndView.setViewName(templateOptional.get().getTemplateValue());
        return modelAndView;
    }


    @Override
    public ModelAndView preview(int articleId) {
        ArticleDetailVO articleDetailVo = findArticleAOById(articleId);
        Optional<Template> templateOptional = templateRepository.findById(articleDetailVo.getTemplateId());
        if(!templateOptional.isPresent()){
            throw new TemplateException("Template not found in preview !!");
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("view",articleDetailVo);
        modelAndView.setViewName(templateOptional.get().getTemplateValue());
        return modelAndView;
    }





    @Override
    public Article deleteByArticleId(int id) {
        Optional<Article> optional = articleRepository.findById(id);
        if(optional.isPresent()){
            Article article = optional.get();
            String viewName = article.getViewName();
            if(viewName!=null){
                File file = new File(workDir+"/"+ CmsConst.STATIC_HTML_PATH+"/"+viewName);
                if(file.exists()){
                    file.delete();
                }
            }
            //重新生成这个文章所在分类的列表
            //1. 找到文章所有的分类
            List<Category> categories = categoryRepository.findCategoryByArticleId(article.getId());
            //TODO 没有使用JMS使生成
            categories.forEach(category -> {
                cmsService.generateCategoryArticleListByCategory(category);
            });
            log.debug(">>> delete comment");
            commentRepository.deleteByArticleId(id);
            log.debug(">>> delete article category");
            articleCategoryRepository.deleteByArticleId(id);
            log.debug(">>> delete article tags");
            articleTagsRepository.deleteByArticleId(id);
            log.debug("delete article");
            articleRepository.deleteById(id);
            return article;
        }
        return null;
    }

    @Override
    public ArticleDetailVO createOrUpdateArticleVo(Article article, Set<Integer> tagsIds,Set<Integer> categoryIds) {
        Article saveArticle = createOrUpdateArticle(article, tagsIds, categoryIds);
        // crate value object
        ArticleDetailVO articleVO = convert(saveArticle,tagsIds,categoryIds);
        return articleVO;
    }

    private Article createOrUpdateArticle(Article article, Set<Integer> tagsIds,Set<Integer> categoryIds) {
        article = super.createOrUpdate(article);
        String viewName = article.getViewName();
        if(viewName==null||"".equals(viewName)){
            viewName = CMSUtils.randomViewName();
            log.debug("!!! view name not found, use "+viewName);
            article.setViewName(viewName);
        }
        Article saveArticle = articleRepository.saveAndFlush(article);


        //TODO update delete option

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
        }

        if(!CollectionUtils.isEmpty(categoryIds)){
            //Get article category
            List<ArticleCategory> articleCategoryList = categoryIds.stream().map(categoryId -> {
                ArticleCategory articleCategory = new ArticleCategory();
                articleCategory.setCategoryId(categoryId);
                articleCategory.setArticleId(saveArticle.getId());
                return articleCategory;
            }).collect(Collectors.toList());
            //save article category
            articleCategoryRepository.saveAll(articleCategoryList);
        }
        return article;
    }


    private ArticleDetailVO convert(Article saveArticle, Set<Integer> tagsIds, Set<Integer> categoryIds) {
        ArticleDetailVO articleDetailVO = new ArticleDetailVO();
        BeanUtils.copyProperties(saveArticle,articleDetailVO);
        //find tags
        if(!CollectionUtils.isEmpty(tagsIds)){
            articleDetailVO.setTagIds(tagsIds);
            List<Tags> tags = tagsRepository.findAllById(tagsIds);

            articleDetailVO.setTags(tags);
        }
        if(!CollectionUtils.isEmpty(categoryIds)){
            articleDetailVO.setCategoryIds(categoryIds);
            // find article
            List<Category> categories = categoryRepository.findAllById(categoryIds);
            articleDetailVO.setCategories(categories);
        }
        return articleDetailVO;
    }


    /**
     * 转换tags和category 通过article Id
     * @param article
     * @return
     */
    private ArticleDetailVO convert(Article article) {
        ArticleDetailVO articleDetailVo = new ArticleDetailVO();
        BeanUtils.copyProperties(article,articleDetailVo);
        
        //find tags
        List<Tags> tags = tagsRepository.findTagsByArticleId(article.getId());
        articleDetailVo.setTags(tags);
        articleDetailVo.setTagIds( ServiceUtil.fetchProperty(tags, Tags::getId));
        // find article
        List<Category> categories = categoryRepository.findCategoryByArticleId(article.getId());
        articleDetailVo.setCategories(categories);
        articleDetailVo.setCategoryIds(ServiceUtil.fetchProperty(categories,Category::getId));
        return articleDetailVo;
    }


    @Override
    public  List<Integer>  updateAllArticleHtml(){
        List<Integer> allId = articleRepository.findAllId();
        allId.forEach(id->{
            ArticleDetailVO articleDetailVO = findArticleAOById(id);
            Optional<Template> optionalTemplate = templateRepository.findById(articleDetailVO.getTemplateId());
            if(optionalTemplate.isPresent()){
                    TemplateUtil.convertHtmlAndSave(articleDetailVO,optionalTemplate.get());
            }
            System.out.println("############################");
        });
        return allId;
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
    public Page<ArticleVO> convertToSimple(Page<Article> articlePage) {
        return  articlePage.map(article -> {
            ArticleVO articleVO = new ArticleVO();
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

        List<ArticleCategory> articleCategorys = articleCategoryRepository.findAllByArticleIdIn(articleIds);
        Set<Integer> categoryIds = ServiceUtil.fetchProperty(articleCategorys, ArticleCategory::getCategoryId);
        List<Category> categorys = categoryRepository.findAllById(categoryIds);
        Map<Integer, Category> categoryMap = ServiceUtil.convertToMap(categorys, Category::getId);
        Map<Integer,List<Category>> categoryListMap = new HashMap<>();
        articleCategorys.forEach(
                articleCategory -> {
                    categoryListMap.computeIfAbsent(articleCategory.getArticleId(),
                            categoryId->new LinkedList<>()).add(categoryMap.get(articleCategory.getCategoryId()));
                }
        );

        Page<ArticleVO> articleVOS = articlePage.map(article -> {
            ArticleVO articleVO = new ArticleVO();
            BeanUtils.copyProperties(article,articleVO);
            articleVO.setCategories(Optional.ofNullable(categoryListMap.get(article.getId()))
                    .orElseGet(LinkedList::new)
                    .stream()
                    .filter(Objects::nonNull)
                    .map(category -> {
                        CategoryDto categoryDto = new CategoryDto();
                        BeanUtils.copyProperties(category,categoryDto);
                        return categoryDto;
                    })
                    .collect(Collectors.toList()));
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
                Subquery<Article> articleSubQuery = query.subquery(Article.class);
                Root<ArticleCategory> postCategoryRoot = articleSubQuery.from(ArticleCategory.class);
                articleSubQuery.select(postCategoryRoot.get("articleId"));
                articleSubQuery.where(
                        criteriaBuilder.equal(root.get("id"), postCategoryRoot.get("articleId")),
                        criteriaBuilder.equal(postCategoryRoot.get("categoryId"), articleQuery.getCategoryId()));
                predicates.add(criteriaBuilder.exists(articleSubQuery));
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
    public void increaseLikes(int id) {
        int affectedRows = articleRepository.updateLikes(id);
    }


}
