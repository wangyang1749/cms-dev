package com.wangyang.web.controller.api;

import com.wangyang.common.exception.ArticleException;
import com.wangyang.common.utils.*;
import com.wangyang.service.service.IArticleService;
import com.wangyang.service.service.ICategoryService;
import com.wangyang.service.service.IHtmlService;
import com.wangyang.pojo.dto.ArticleDto;
import com.wangyang.pojo.dto.MindJs;
import com.wangyang.pojo.entity.Article;
import com.wangyang.pojo.enums.ArticleStatus;
import com.wangyang.pojo.vo.ArticleDetailVO;

import com.wangyang.pojo.entity.Category;
import com.wangyang.pojo.params.ArticleParams;
import com.wangyang.pojo.params.ArticleQuery;
import com.wangyang.common.BaseResponse;
import com.wangyang.common.CmsConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.*;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/article")
//@CrossOrigin
@Slf4j
public class ArticleController {

    @Autowired
    IArticleService articleService;

//    @Autowired
//    IProducerService producerService;

    @Autowired
    IHtmlService htmlService;

    @Autowired
    ICategoryService categoryService;




    @PostMapping
    public ArticleDetailVO createArticleDetailVO(@RequestBody @Valid ArticleParams articleParams, HttpServletRequest request){
        int userId = (Integer)request.getAttribute("userId");
        Article article = new Article();
        BeanUtils.copyProperties(articleParams,article);
        article.setUserId(userId);
        ArticleDetailVO articleDetailVO = articleService.createArticleDetailVo(article, articleParams.getTagIds());
        htmlService.conventHtml(articleDetailVO);
        return articleDetailVO;
    }

    /**
     * 只保存文章内容, 不为文章添加分类标签生成HTML
     * @param articleParams
     * @return
     */
    @PostMapping("/save")
    public Article saveArticle(@Valid @RequestBody ArticleParams articleParams, HttpServletRequest request){
        int userId = (Integer)request.getAttribute("userId");
        Article article = new Article();
        BeanUtils.copyProperties(articleParams,article);
        article.setUserId(userId);
//        article.setStatus(ArticleStatus.DRAFT);
        return  articleService.saveArticleDraft(article);
    }

    @GetMapping("/simpleCreate/{categoryId}")
    public ArticleDetailVO createArticleDetailVO(@PathVariable("categoryId") Integer categoryId,
                                                 @RequestParam(required = true) String title, HttpServletRequest request){
        int userId = (Integer)request.getAttribute("userId");
        Article article = new Article();
        article.setCategoryId(categoryId);
        article.setTitle(title);
        article.setOriginalContent("# 开始写文章["+title+"]...");
        article.setUserId(userId);
        ArticleDetailVO articleDetailVO = articleService.createArticleDetailVo(article,null);

        htmlService.conventHtml(articleDetailVO);
        return articleDetailVO;
    }


/******************************************/
    /**
     *
     * @param id
     * @param articleParams
     * @param request
     * @return
     */
    @PostMapping("/save/{id}")
    public Article updateArticle(@PathVariable("id") Integer id,@Valid @RequestBody ArticleParams articleParams,HttpServletRequest request){
        int userId = (Integer)request.getAttribute("userId");
        Article article = articleService.findArticleById(id);
        checkUser(userId,article);

        if(article.getTitle().equals(articleParams.getTitle())&&article.getOriginalContent().equals(articleParams.getOriginalContent())&&article.getCategoryId().equals(articleParams.getCategoryId())){
            return article;
        }

        BeanUtils.copyProperties(articleParams,article,"picPath");
    //        Boolean haveHtml = Optional.ofNullable(article.getHaveHtml()).orElse(false);
        if(article.getStatus().equals(ArticleStatus.PUBLISHED)||article.getStatus().equals(ArticleStatus.MODIFY)){
            article.setStatus(ArticleStatus.MODIFY);
        }else if(article.getStatus().equals(ArticleStatus.INTIMATE)){
            article.setStatus(ArticleStatus.INTIMATE);
        }else {
            article.setStatus(ArticleStatus.DRAFT);
        }
        return  articleService.updateArticleDraft(article);
    }


    @PostMapping("/update/{articleId}")
    public ArticleDetailVO updateArticleDetailVO(@Valid @RequestBody ArticleParams articleParams,
                                         @PathVariable("articleId") Integer articleId,HttpServletRequest request){
        int userId = (Integer)request.getAttribute("userId");
        Article article = articleService.findArticleById(articleId);
        checkUser(userId,article);

        Integer  oldCategoryId = article.getCategoryId();
        BeanUtils.copyProperties(articleParams,article);
        ArticleDetailVO articleDetailVO = articleService.updateArticleDetailVo( article, articleParams.getTagIds());
        //有可能更新文章的视图名称
//        TemplateUtil.deleteTemplateHtml(article.getViewName(),article.getPath());

        //更新文章分类, 还需要重新生成老的分类
        if(articleParams.getCategoryId()!=oldCategoryId&&oldCategoryId!=null){
            Category oldCategory = categoryService.findById(oldCategoryId);
            articleDetailVO.setOldCategory(oldCategory);
            htmlService.convertArticleListBy(oldCategory);
        }
//        if(articleDetailVO.getHaveHtml()){
//
////            producerService.sendMessage(articleDetailVO);
//        }
        htmlService.conventHtml(articleDetailVO);
        log.info(article.getTitle()+"--->更新成功！");
        return articleDetailVO;
    }
    /**
     * 更新文章分类
     * @param articleId
     * @param baseCategoryId
     * @return
     */
    @GetMapping("/updateCategory/{articleId}")
    public ArticleDetailVO updateCategory(@PathVariable("articleId") Integer articleId, Integer baseCategoryId,HttpServletRequest request){
        int userId = (Integer)request.getAttribute("userId");
        Article article = articleService.findArticleById(articleId);
        checkUser(userId,article);
//        String  viewName = article.getViewName();
//        String path = article.getPath();

        Integer categoryId=null;
        if(article.getCategoryId()!=null){
            categoryId = article.getCategoryId();
        }
        ArticleDetailVO articleDetailVO = articleService.updateCategory(article, baseCategoryId);
        //删除旧文章
//        TemplateUtil.deleteTemplateHtml(viewName,path);
        //更新旧的文章列表
        if(categoryId!=null){
            Category oldCategory = categoryService.findById(categoryId);
            articleDetailVO.setOldCategory(oldCategory);
            htmlService.convertArticleListBy(oldCategory);
            // 删除分页的文章列表
//            FileUtils.removeCategoryPageTemp(oldCategory);
        }

        //生成改变后文章
        htmlService.conventHtml(articleDetailVO);
        // 删除分页的文章列表
//        FileUtils.removeCategoryPageTemp(articleDetailVO.getCategory());
//        FileUtils.remove(CmsConst.WORK_DIR+"/html/articleList/queryTemp");

        return articleDetailVO;
    }

    /**
     * 是否生成文章的html
     * @param id
     * @return
     */
//    @GetMapping("/haveHtml/{id}")
//    public Article haveHtml(@PathVariable("id") Integer id){
//        Article article = articleService.haveHtml(id);
//
//        if(article.getHaveHtml()){
//            ArticleDetailVO articleDetailVO = articleService.convert(article);
//            htmlService.conventHtml(articleDetailVO);
//        }else {
//            if(article.getCategoryId()!=null){
//                Category oldCategory = categoryService.findById(article.getCategoryId());
//                htmlService.convertArticleListBy(oldCategory);
////                htmlService.addOrRemoveArticleToCategoryListByCategoryId();
//            }
//            TemplateUtil.deleteTemplateHtml(article.getViewName(),article.getPath());
//        }
//        return article;
//    }


    @RequestMapping("/recycle/{id}")
    public ArticleDetailVO recycle(@PathVariable("id") Integer id){
        Article article = articleService.recycle(id);
        //删除文章
        TemplateUtil.deleteTemplateHtml(article.getViewName(),article.getPath());
        ArticleDetailVO articleDetailVO = articleService.convert(article);
        if(article.getStatus().equals(ArticleStatus.PUBLISHED)||article.getStatus().equals(ArticleStatus.MODIFY)){
            Category category = categoryService.findById(article.getCategoryId());
            //重新生成文章列表
            htmlService.convertArticleListBy(category);
            if(article.getTop()){
                htmlService.articleTopListByCategoryId(category.getId());
            }
            //删除的是最新文章
            htmlService.newArticleListHtml();
        }
        return  articleDetailVO;
    }



    @RequestMapping("/delete/{id}")
    public ArticleDetailVO delete(@PathVariable("id") Integer id){
        Article article = articleService.deleteByArticleId(id);
        //删除文章
        TemplateUtil.deleteTemplateHtml(article.getViewName(),article.getPath());
        ArticleDetailVO articleDetailVO = articleService.convert(article);
        if(article.getStatus().equals(ArticleStatus.PUBLISHED)||article.getStatus().equals(ArticleStatus.MODIFY)){
            Category category = categoryService.findById(article.getCategoryId());
            //重新生成文章列表
            htmlService.convertArticleListBy(category);
            // 删除分页的文章列表
//            FileUtils.removeCategoryPageTemp(category);
//            FileUtils.remove(CmsConst.WORK_DIR+"/html/articleList/queryTemp");
            if(article.getTop()){
                htmlService.articleTopListByCategoryId(category.getId());
            }
        }

        return  articleDetailVO;
    }



    @GetMapping("/find/{articleId}")
    public ArticleDto findById(@PathVariable("articleId") Integer articleId){
        return articleService.findArticleAOById(articleId);
    }


    @GetMapping("/generatePdf/{articleId}")
    public BaseResponse generatePdf(@PathVariable("articleId") Integer articleId) {
        Article article = articleService.findArticleById(articleId);
        String url = "http://localhost:8080/article/previewPdf/"+articleId;
        String generatePath = article.getPath()+"/"+article.getViewName()+".pdf";
//        NodeJsUtil.execNodeJs("node","templates/nodejs/generatePdf.js",url,generatePath);
        return BaseResponse.ok("生成成功");
    }
//    @GetMapping("/download/{id}")
//    public String downloadPdf(@PathVariable("id") Integer id){
//        String generatePdf = articleService.generatePdf(id);
//        return generatePdf;
//    }



    @GetMapping("/updateAll")
    public Set<String> updateAllArticleHtml(@RequestParam(value = "more", defaultValue = "false") Boolean more){
        List<Article> articles = articleService.listHaveHtml();
        articles.forEach(article->{
            //更新文章摘要
            if(more){
                article = articleService.createOrUpdate(article);
                articleService.generateSummary(article);
                if(article.getLikes()==null){
                    article.setLikes(0);
                }
                if(article.getVisits()==null){
                    article.setVisits(0);
                }
                if(article.getCommentNum()==null){
                    article.setCommentNum(0);
                }
                if(article.getOpenComment()==null){
                    article.setOpenComment(false);
                }
                if(article.getPicPath()==null){
                    article.setPicPath(ImageUtils.getImgSrc(article.getFormatContent()));
                }
                if(article.getParentId()==null){
                    article.setParentId(0);
                }
                if(article.getOrder()==null){
                    article.setOrder(0);
                }
                if(article.getDirection()==null){
                    article.setDirection("right");
                }
                if(article.getTop()==null){
                    article.setTop(false);
                }
                Category category = categoryService.findById(article.getCategoryId());
                article.setPath(CMSUtils.getArticlePath());
                article.setTemplateName(category.getArticleTemplateName());

                articleService.save(article);
                log.info("更新["+article.getTitle()+"]内容!!!");
            }
            ArticleDetailVO articleDetailVO = articleService.convert(article);
            htmlService.conventHtml(articleDetailVO);
            System.out.println("############################");
        });
        return  ServiceUtil.fetchProperty(articles,Article::getTitle);

    }
    @GetMapping("/updateHtml/{id}")
    public ArticleDetailVO updateHtmlById(@PathVariable("id") Integer id){
        Article article = articleService.findArticleById(id);
        ArticleDetailVO articleDetailVO = articleService.convert(article);
        htmlService.conventHtml(articleDetailVO);
//        producerService.sendMessage(articleDetailVO);
        return articleDetailVO;
    }




    @GetMapping("/pageDtoBy/{categoryId}")
    public Page<ArticleDto> pageDtoBy(@PathVariable("categoryId") Integer categoryId,@RequestParam(value = "page", defaultValue = "1") Integer page){
        if(page<=0) page=1;
        page = page-1;
        Page<ArticleDto> articleDtoPage = articleService.pageDtoByCategoryId(categoryId, page);

        return articleDtoPage;
    }

    @GetMapping("/updateOrderBy/{articleId}")
    public Article updateOrderBy(@PathVariable("articleId") Integer articleId,Integer order){
        Article article = articleService.updateOrder(articleId, order);
        htmlService.convertArticleListBy(article.getCategoryId());
        return article;
    }
    @GetMapping("/generateHtml/{id}")
    public ArticleDetailVO generateHtml(@PathVariable("id") Integer id){

//        TestStatic.test();
        Article article = articleService.findArticleById(id);
        ArticleDetailVO articleDetailVO = articleService.updateArticleDetailVo(article);
//        ArticleDetailVO articleDetailVO = articleService.convert(article);
        htmlService.conventHtml(articleDetailVO);
        return articleDetailVO;
    }


    @GetMapping("/openOrCloseComment/{id}")
    public ArticleDetailVO openOrCloseComment(@PathVariable("id")Integer id){
        Article article = articleService.openComment(id);
        ArticleDetailVO articleDetailVO = articleService.convert(article);
        if(articleDetailVO.getOpenComment()){
            //生成该文章之下的评论
            htmlService.generateCommentHtmlByArticleId(articleDetailVO.getId());
        }
        //生成文章
        htmlService.conventHtml(articleDetailVO);
        return articleDetailVO;
    }




    @GetMapping("/pageByTagId/{tagId}")
    public Page<ArticleDto> pageByTagId(@PathVariable("tagId") Integer tagId,@PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        return  articleService.pageByTagId(tagId,pageable);
    }

    @GetMapping("/query")
    public List<ArticleDto> listByTitle(String title){
        return articleService.listByTitle(title);
    }

    @GetMapping("/listByComponentsId/{componentsId}")
    public List<ArticleDto> listByComponentsId(@PathVariable("componentsId") Integer componentsId){
        return  articleService.listByComponentsId(componentsId);
    }

    @GetMapping("/listArticleMindDto/{categoryId}")
    public String listArticleMindDto(@PathVariable("categoryId") int categoryId){
        return  articleService.jsMindFormat( articleService.listArticleMindDto(categoryId));
    }


    @PostMapping("/saveMindJs/{categoryId}")
    public Category saveArticleMindJs(@PathVariable("categoryId") int categoryId
            ,@RequestBody  List<MindJs> mindJss
            ,HttpServletRequest request) {
        int userId = (Integer)request.getAttribute("userId");
        List<Article> articles = articleService.listArticleBy(categoryId);

        mindJss.remove(0);
        for (int i=0;i<mindJss.size();i++){
            mindJss.get(i).setOrder(mindJss.size()-i);
        }
        Map<String, MindJs> mindJsMap = ServiceUtil.convertToMap(mindJss, MindJs::getId);
        Map<String, Article> articleMap = ServiceUtil.convertToMap(articles, a -> String.valueOf(a.getId()));
        Map<String, MindJs> copyMindJsMap = new HashMap<>(mindJsMap);

        mindJsMap.keySet().removeAll(articleMap.keySet());
        articleMap.keySet().removeAll(copyMindJsMap.keySet());




        if(articleMap.size()!=0){
            //删除的节点
            articleMap.forEach((k,v)->{
//                v.setHaveHtml(false);
                v.setStatus(ArticleStatus.DRAFT);
                articleService.save(v);
                articles.removeIf(article -> article.getId()==v.getId());
            });

        }
        if(mindJsMap.size()!=0){
            mindJsMap.forEach((k,v)->{
                //新增节点

                Article article = new Article();
                article.setTitle(v.getTopic());
                article.setParentId(v.getParentid());
                article.setExpanded(v.getExpanded());
                article.setDirection(v.getDirection());
                article.setOrder(v.getOrder());
                article.setCategoryId(categoryId);
                article.setUserId(userId);
                article.setOriginalContent("开始创作文章["+v.getTopic()+"]...");
                ArticleDetailVO articleDetailVo = articleService.createArticleDetailVo(article, null);
                htmlService.conventHtmlNoCategoryList(articleDetailVo);
                BeanUtils.copyProperties(articleDetailVo,article);
                articles.add(mindJss.size()-articleDetailVo.getOrder(),article);
            });

        }
//        if(mindJsMap.size()!=0||articleMap.size()!=0){
//            articles.add();
//        }

        //整合顺序
        for(int i =0;i<mindJss.size();i++){
            MindJs mindJs = mindJss.get(i);
            Article article = articles.get(i);
            if(mindJsMap.containsKey(mindJs.getId())){
                continue;
            }
            int id = Integer.parseInt(mindJs.getId());;
            int dbId = article.getId();

            if(dbId==id){
                if(isChange(mindJs,article)||article.getOrder()!=mindJss.size()-i){
                    article.setOrder(mindJss.size()-i);
                    article.setParentId(mindJs.getParentid());
                    article.setTitle(mindJs.getTopic());
                    article.setExpanded(mindJs.getExpanded());
                    article.setDirection(mindJs.getDirection());

                    articleService.save(article);
                }

            }else {
                Article updateArticle = articleService.findArticleById(id);
                updateArticle.setOrder(mindJss.size()-i);
                updateArticle.setParentId(mindJs.getParentid());
                updateArticle.setTitle(mindJs.getTopic());
                updateArticle.setExpanded(mindJs.getExpanded());
                updateArticle.setDirection(mindJs.getDirection());
                articleService.save(updateArticle);
            }
        }

        Category category = categoryService.findById(categoryId);
        //TODO
//        htmlService.deleteTempFileByCategory(category);
        htmlService.convertArticleListBy(category);
        return category;
    }


    private boolean isChange(MindJs mindJs, Article article) {
        return  !mindJs.getTopic().equals(article.getTitle())||
                !mindJs.getParentid().equals(article.getParentId())||
                !mindJs.getExpanded().equals(article.getExpanded())||
                !(mindJs.getDirection()==null?true:mindJs.getDirection().equals(article.getDirection()));
    }

    /**
     * 置顶文章
     * @return
     */
    @GetMapping("/sendOrCancelTop/{id}")
    public Article sendOrCancelTop(@PathVariable("id")int id){
        Article article = articleService.sendOrCancelTop(id);

        htmlService.articleTopListByCategoryId(article.getCategoryId());
        return article;
    }

    public void checkUser(int userId,Article article){
        if(article.getUserId()!=userId){
            throw new ArticleException("您并非文章的发布者不能修改！");
        }
    }

    @GetMapping
    public Page<? extends ArticleDto> articleList(@PageableDefault(sort = {"id"},direction = DESC) Pageable pageable,
                                                  @RequestParam(value = "more", defaultValue = "true") Boolean more,
                                                  ArticleQuery articleQuery){
        Page<Article> articles = articleService.pageAllBy(pageable,articleQuery);
        if(more){
            return articleService.convertToListVo(articles);
        }
        return articleService.convertToSimple(articles);
    }

    @GetMapping("/findArticleDetail/{id}")
    public ArticleDetailVO findDetailArticleById(@PathVariable("id") Integer id){
        ArticleDetailVO articleDetailVO = articleService.findArticleAOById(id);
        return articleDetailVO;
    }

}
