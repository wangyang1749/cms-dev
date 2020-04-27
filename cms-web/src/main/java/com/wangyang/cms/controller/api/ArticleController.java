package com.wangyang.cms.controller.api;

import com.wangyang.cms.core.jms.producer.IProducerService;
import com.wangyang.data.service.IArticleService;
import com.wangyang.data.service.ICategoryService;
import com.wangyang.data.service.IHtmlService;
import com.wangyang.model.pojo.dto.ArticleDto;
import com.wangyang.model.pojo.entity.Article;
import com.wangyang.model.pojo.vo.ArticleDetailVO;

import com.wangyang.model.pojo.entity.Category;
import com.wangyang.model.pojo.params.ArticleParams;
import com.wangyang.model.pojo.params.ArticleQuery;
import com.wangyang.common.BaseResponse;
import com.wangyang.common.CmsConst;
import com.wangyang.common.utils.ServiceUtil;
import com.wangyang.common.utils.TemplateUtil;
import com.wangyang.syscall.utils.NodeJsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;
import java.util.Set;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/article")
//@CrossOrigin
@Slf4j
public class ArticleController {

    @Autowired
    IArticleService articleService;

    @Autowired
    IProducerService producerService;

    @Autowired
    IHtmlService htmlService;

    @Autowired
    ICategoryService categoryService;

    @GetMapping
    public Page<? extends ArticleDto> articleList(@PageableDefault(sort = {"id"},direction = DESC) Pageable pageable,
                                                  @RequestParam(value = "more", defaultValue = "true") Boolean more,
                                                  ArticleQuery articleQuery){
        Page<Article> articles = articleService.articleList(articleQuery, pageable);
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




    @PostMapping
    public ArticleDetailVO createArticleDetailVO(@RequestBody @Valid ArticleParams articleParams){
        Article article = new Article();
        BeanUtils.copyProperties(articleParams,article);
        ArticleDetailVO articleDetailVO = articleService.createArticleDetailVo(article, articleParams.getTagIds());
        if(article.getHaveHtml()){
//            htmlService.receiveArticleDetailVO(articleDetailVO);
            //使用JMS生成文章,文章列表
            producerService.sendMessage(articleDetailVO);
//            producerService.commonTemplate("AC");
//            covertHtml(articleDetailVO);
//            if(articleDetailVO.getUpdateChannelFirstName()){
//                htmlService.generateChannelListHtml();
//            }
        }
        return articleDetailVO;
    }

    @PostMapping("/update/{articleId}")
    public ArticleDetailVO updateArticleDetailVO(@Valid @RequestBody ArticleParams articleParams,
                                         @PathVariable("articleId") Integer articleId){
        Article article = articleService.findArticleById(articleId);
        if(article.getSummary().equals(articleParams.getSummary())){
            //重新生成摘要
            articleParams.setSummary(null);
        }
        Integer  oldCategory = article.getCategoryId();
        BeanUtils.copyProperties(articleParams,article);
        ArticleDetailVO articleDetailVO = articleService.updateArticleDetailVo( article, articleParams.getTagIds());


        //有可能更新文章的视图名称
        TemplateUtil.deleteTemplateHtml(article.getViewName(),article.getPath());
        //更新文章分类, 还需要重新生成老的分类
        if(articleParams.getCategoryId()!=oldCategory&&oldCategory!=null){
            htmlService.addOrRemoveArticleToCategoryListByCategoryId(oldCategory);
        }
        if(articleDetailVO.getHaveHtml()){
//            htmlService.receiveArticleDetailVO(articleDetailVO);

            producerService.sendMessage(articleDetailVO);
//            producerService.commonTemplate("AU");
//            covertHtml(articleDetailVO);
        }
        return articleDetailVO;
    }

    /**
     * 只保存文章内容, 不为文章添加分类标签生成HTML
     * @param articleParams
     * @return
     */
    @PostMapping("/save")
    public Article saveArticle(@Valid @RequestBody ArticleParams articleParams){
        Article article = new Article();
        BeanUtils.copyProperties(articleParams,article);
        return  articleService.saveOrUpdateArticleDraft(article);
    }

    @PostMapping("/save/{id}")
    public Article updateArticle( @PathVariable("id") Integer id,@Valid @RequestBody ArticleParams articleParams){
        Article article = articleService.findArticleById(id);
        BeanUtils.copyProperties(articleParams,article);
        return  articleService.saveOrUpdateArticleDraft(article);
    }
    @RequestMapping("/delete/{id}")
    public Article delete(@PathVariable("id") Integer id){
        Article article = articleService.deleteByArticleId(id);
        //删除文章
        TemplateUtil.deleteTemplateHtml(article.getViewName(),article.getPath());
        //重新生成文章列表
        htmlService.addOrRemoveArticleToCategoryListByCategoryId(article.getCategoryId());
        return  article;
    }

    @PostMapping("/form")
    public ArticleDetailVO createArticleByForm(@Valid  ArticleParams articleParams){
        Article article  = new Article();
        BeanUtils.copyProperties(articleParams,article);
        ArticleDetailVO articleDetailVO = articleService.createArticleDetailVo(article, articleParams.getTagIds());
        if(article.getHaveHtml()){
//            htmlService.receiveArticleDetailVO(article);
            producerService.sendMessage(articleDetailVO);
//            producerService.commonTemplate("AC");
//            covertHtml(articleDetailVO);
        }
        return articleDetailVO;
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
        NodeJsUtil.execNodeJs("node","templates/nodejs/generatePdf.js",url,generatePath);
        return BaseResponse.ok("生成成功");
    }
    @GetMapping("/download/{id}")
    public String downloadPdf(@PathVariable("id") Integer id){
        String generatePdf = articleService.generatePdf(id);
        return generatePdf;
    }



    @GetMapping("/updateAll")
    public Set<String> updateAllArticleHtml(@RequestParam(value = "more", defaultValue = "false") Boolean more){
        List<Article> articles = articleService.updateAllArticleHtml(more);
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
                Category category = categoryService.findById(article.getCategoryId());
                article.setPath(CmsConst.ARTICLE_DETAIL_PATH);
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
        producerService.sendMessage(articleDetailVO);
        return articleDetailVO;
    }

    @GetMapping("/haveHtml/{id}")
    public Article haveHtml(@PathVariable("id") Integer id){
        Article article = articleService.haveHtml(id);

        if(article.getHaveHtml()){
            ArticleDetailVO articleDetailVO = articleService.convert(article);
            htmlService.conventHtml(articleDetailVO);
        }else {
            if(article.getCategoryId()!=null){
                htmlService.addOrRemoveArticleToCategoryListByCategoryId(article.getCategoryId());
            }
            TemplateUtil.deleteTemplateHtml(article.getViewName(),article.getPath());
        }
        return article;
    }

    /**
     * 更新文章分类
     * @param articleId
     * @param baseCategoryId
     * @return
     */
    @GetMapping("/updateCategory/{articleId}")
    public ArticleDetailVO updateCategory(@PathVariable("articleId") Integer articleId, Integer baseCategoryId){
        Article article = articleService.findArticleById(articleId);
        String  viewName = article.getViewName();
        String path = article.getPath();
        Integer categoryId=null;
        if(article.getCategoryId()!=null){
            categoryId = article.getCategoryId();
        }
        ArticleDetailVO articleDetailVO = articleService.updateCategory(article, baseCategoryId);
        //删除旧文章
        TemplateUtil.deleteTemplateHtml(viewName,path);
        //更新旧的文章列表
        if(categoryId!=null){
            htmlService.addOrRemoveArticleToCategoryListByCategoryId(categoryId);

        }

        //生成改变后文章
        htmlService.conventHtml(articleDetailVO);
        return articleDetailVO;
    }

    @GetMapping("/pageDtoBy/{categoryId}")
    public Page<ArticleDto> pageDtoBy(@PathVariable("categoryId") Integer categoryId,@RequestParam(value = "page", defaultValue = "1") Integer page){
        if(page<=0) page=1;
        page = page-1;
        Page<ArticleDto> articleDtoPage = articleService.pageDtoBy(categoryId, page);

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

}
