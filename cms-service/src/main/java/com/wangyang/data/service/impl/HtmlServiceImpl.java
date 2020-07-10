package com.wangyang.data.service.impl;

import com.wangyang.common.utils.DocumentUtil;
import com.wangyang.common.utils.FileUtils;
import com.wangyang.common.utils.TemplateUtil;
import com.wangyang.data.ApplicationBean;
import com.wangyang.data.event.EntityCreatedEvent;
import com.wangyang.data.service.*;
import com.wangyang.model.pojo.dto.ArticleAndCategoryMindDto;
import com.wangyang.model.pojo.dto.ArticleDto;
import com.wangyang.model.pojo.dto.CategoryArticleListDao;
import com.wangyang.model.pojo.dto.CategoryDto;
import com.wangyang.model.pojo.enums.ArticleStatus;
import com.wangyang.model.pojo.vo.ArticleDetailVO;
import com.wangyang.model.pojo.entity.*;
import com.wangyang.common.CmsConst;
import com.wangyang.model.pojo.vo.CategoryVO;
import com.wangyang.model.pojo.vo.CommentVo;
import com.wangyang.data.repository.ArticleRepository;
import com.wangyang.data.repository.ComponentsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class HtmlServiceImpl implements IHtmlService {

    @Autowired
    ITemplateService templateService;
    @Autowired
    ICategoryService categoryService;

    @Autowired
    ComponentsRepository componentsRepository;
    @Autowired
    IArticleService articleService;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    IOptionService optionService;
    @Autowired
    ISheetService sheetService;

    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    IComponentsService componentsService;
    @Autowired
    ICommentService commentService;

    @Override
    @Async //异步执行
    public void conventHtml(ArticleDetailVO articleVO){
        if(articleVO.getStatus()== ArticleStatus.PUBLISHED){
            Category category = articleVO.getCategory();
//            EntityCreatedEvent<Category> createArticle = new EntityCreatedEvent<>(category);
//            publisher.publishEvent(createArticle);
            deleteTempFileByCategory(category);
            //生成文章列表，文章列表依赖分类列表
            convertArticleListBy(category);
            //判断评论文件是否存在
            if(!TemplateUtil.componentsExist(articleVO.getViewName())){
                generateCommentHtmlByArticleId(articleVO.getId());
            }
            //生成文章详情页面,依赖文章评论(在栏目页面文章详情依赖文章列表)
            covertHtml(articleVO);
            log.info("!!### generate "+articleVO.getViewName()+" html success!!");
            // 生成首页文章最新文章
            generateNewArticle();
        }
    }

    @Override
    public void deleteTempFileByCategory(Category category){
        FileUtils.removeCategoryPageTemp(category);
        //移除临时文章分类
        FileUtils.remove(CmsConst.WORK_DIR+"/html/articleList/queryTemp");
        FileUtils.remove(CmsConst.WORK_DIR+"/html/mind/"+category.getId()+".html");
    }

    /**
     * 从思维导图创建文章生成静态页面，之后统一生成文章首页列表
     * @param articleVO
     */
    @Override
    @Async //异步执行
    public void conventHtmlNoCategoryList(ArticleDetailVO articleVO){
        if(articleVO.getStatus()== ArticleStatus.PUBLISHED){

//            Category category = articleVO.getCategory();
            //生成文章列表，文章列表依赖分类列表
//            convertArticleListBy(category);

            //判断评论文件是否存在
            if(!TemplateUtil.componentsExist(articleVO.getViewName())){
                generateCommentHtmlByArticleId(articleVO.getId());
            }
            //生成文章详情页面,依赖文章评论(在栏目页面文章详情依赖文章列表)
            covertHtml(articleVO);
            log.info("!!### generate "+articleVO.getViewName()+" html success!!");


            // 生成首页文章最新文章
            generateNewArticle();
            //创建/更新 文章-删除文章分页的缓存文件
            FileUtils.removeCategoryPageTemp(articleVO.getCategory());
            //移除临时文章分类
            FileUtils.remove(CmsConst.WORK_DIR+"/html/articleList/queryTemp");
        }
    }


    @Override
    public void addOrRemoveArticleToCategoryListByCategoryId(int id) {
        Optional<Category> optionalCategory = categoryService.findOptionalById(id);
        if(optionalCategory.isPresent()){
            convertArticleListBy(optionalCategory.get());
        }
    }


    /**
     * 生成该栏目下文章列表, 只展示文章列表
     * @param category
     */
    @Override
    public CategoryArticleListDao convertArticleListBy(Category category) {
        //生成分类列表,用于首页文章列表右侧展示
        if(!TemplateUtil.componentsExist(category.getTemplateName())){
                generateCategoryListHtml();
        }
        log.debug("生成"+category.getName()+"分类下的第一个页面!");
        CategoryArticleListDao categoryArticle = articleService.findCategoryArticleBy(category, 0);

        Optional<Template> template = templateService.findOptionalByEnName(category.getTemplateName());
        if(template.isPresent()){
            String html = TemplateUtil.convertHtmlAndSave(categoryArticle, template.get());
            //生成文章列表组件,用于首页嵌入
            String content = DocumentUtil.getDivContent(html, "#components");
            if(StringUtils.isNotEmpty(content)){
                TemplateUtil.saveFile(CmsConst.COMPONENTS_PATH,category.getViewName(),content);
            }
        }
        return categoryArticle;
    }

    /**
     * 生成分页的缓存
     * @param category
     * @param page
     * @return
     */
    @Override
    public String convertArticleListBy(Category category, int page) {
        if(page<=0){
            return "Page is not exist!!";
        }
        CategoryArticleListDao categoryArticle = articleService.findCategoryArticleBy(category, page-1);
        Page<ArticleDto> articlePage = categoryArticle.getPage();
        if(page>articlePage.getTotalPages()){
            return "Page is not exist!!";
        }
        log.debug("生成"+category.getName()+"分类下的第["+page+"]个页面缓存!");
        Optional<Template> template = templateService.findOptionalByEnName(category.getTemplateName());
        if(template.isPresent()){
            String path = category.getPath()+"/"+category.getViewName();
            String viewName = String.valueOf(page);
            return TemplateUtil.convertHtmlAndSave(path,viewName,categoryArticle,template.get());
        }
        return null;
    }

    public String renderMindJs(int categoryId){
        ArticleAndCategoryMindDto articleAndCategoryMindDto = articleService.listArticleMindDto(categoryId);
        Category category = articleAndCategoryMindDto.getCategory();
        String mindFormat = articleService.jsMindFormat(articleAndCategoryMindDto);
        Template template = templateService.findByEnName(CmsConst.ARTICLE_JS_MIND);
        Map<String,Object> map = new HashMap<>();
        map.put("mind",mindFormat);
        map.put("category",category);
        String viewName = "";
        return  TemplateUtil.convertHtmlAndSave(category.getPath(),viewName,map,template);
    }



    /**
     * 生成单纯文章分页的缓存，没有分类
     * @return
     */
    @Override
    public String convertArticlePageBy(HttpServletRequest request, Page<ArticleDto> articleDtoPage, String viewName) {
//        log.debug("生成"+category.getName()+"分类下的第["+page+"]个页面缓存!");
        Optional<Template> template = templateService.findOptionalByEnName(CmsConst.ARTICLE_PAGE);
        if(template.isPresent()){
            Map<String,Object> map = new HashMap<>();
            map.put("view",articleDtoPage);
            map.put("request",request);
            String path = "articleList/queryTemp";
            return TemplateUtil.convertHtmlAndSave(path,viewName,map,template.get());
        }
        return null;
    }

    @Override
    public String previewArticlePageBy(HttpServletRequest request, Page<ArticleDto> articleDtoPage) {
//        log.debug("生成"+category.getName()+"分类下的第["+page+"]个页面缓存!");
        Optional<Template> template = templateService.findOptionalByEnName(CmsConst.ARTICLE_PAGE);
        if(template.isPresent()){
            Map<String,Object> map = new HashMap<>();
            map.put("view",articleDtoPage);
            map.put("request",request);
            return TemplateUtil.convertHtmlAndPreview(map,template.get());
        }
        return null;
    }



    public void generateNewArticle(){
        Components components = componentsService.findByViewName("newArticleIndex");
        Object data = componentsService.getModel(components);
        TemplateUtil.convertHtmlAndSave(data,components);
    }

    @Override
    public CategoryArticleListDao convertArticleListBy(int categoryId){
        Category category = categoryService.findById(categoryId);
        return convertArticleListBy(category);
    }

    /**
     * 生成文章详情页的静态页面
     * @param articleDetailVO
     * @return
     */
    private String covertHtml(ArticleDetailVO articleDetailVO) {
        Template template = templateService.findByEnName(articleDetailVO.getTemplateName());
        String html = TemplateUtil.convertHtmlAndSave(articleDetailVO, template);
        return html;
    }


    @Override
    public void convertArticleListBy(Sheet sheet) {
        Template template = templateService.findByEnName(sheet.getTemplateName());
        TemplateUtil.convertHtmlAndSave(sheet,template);
    }


    @Override
    public Components generateHome(){
        Components components = componentsService.findByDataName("articleJob.index");
        Object data = componentsService.getModel(components);
        TemplateUtil.convertHtmlAndSave(data,components);
        return components;
    }


    /**
     * 生成分类树的Html
     */
    @Override
    public void generateCategoryListHtml() {
        //获取该列表所在的组
        List<CategoryVO> categoryVOS = categoryService.listCategoryVo();
        Template template = templateService.findByEnName(CmsConst.DEFAULT_CATEGORY_LIST);
        TemplateUtil.convertHtmlAndSave(CmsConst.COMPONENTS_PATH,CmsConst.CATEGORY_MENU,categoryVOS,template);
    }


    @Override
    public void generateMenuListHtml() {
        Components components = componentsService.findByDataName("articleJob.listMenu");
        Object data = componentsService.getModel(components);
        TemplateUtil.convertHtmlAndSave(data,components);
    }


    @Override
    public void commonTemplate(String option){
        List<Components> templatePages = componentsRepository.findAll((Specification<Components>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaQuery.where(
                        criteriaBuilder.like(root.get("event"), "%"+option+"%"),
                        criteriaBuilder.isTrue(root.get("status"))
                ).getRestriction());

        templatePages.forEach(templatePage -> {
            Object data = getData(templatePage.getDataName());
            if(data!=null){
                TemplateUtil.convertHtmlAndSave(data,templatePage);
            }
        });
    }

    public Object getData(String name){
        try {
            String[] names = name.split("\\.");
            String className = names[0];
            String methodName = names[1];
            Object bean = ApplicationBean.getBean(className);
            Method method = bean.getClass().getMethod(methodName);
            return method.invoke(bean);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据文章Id生成该文章评论的Html
     * @param articleId
     */
    @Override
    public void generateCommentHtmlByArticleId(int articleId){
        Article article = articleService.findArticleById(articleId);
        //只有在文章打开评论时才能生成评论
        if(article.getOpenComment()){
            Page<CommentVo> commentVos = commentService.listVoBy(articleId);
            //获取文章评论的模板
            Template template = templateService.findByEnName(article.getCommentTemplateName());
            TemplateUtil.convertHtmlAndSave(CmsConst.COMPONENTS_PATH,article.getViewName(),commentVos,template);
        }

    }


}
