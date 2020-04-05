package com.wangyang.cms.controller.api;

import com.wangyang.cms.core.jms.producer.IProducerService;
import com.wangyang.cms.pojo.dto.ArticleDto;

import com.wangyang.cms.pojo.entity.Article;

import com.wangyang.cms.pojo.params.ArticleParams;
import com.wangyang.cms.pojo.params.ArticleQuery;
import com.wangyang.cms.pojo.support.BaseResponse;
import com.wangyang.cms.pojo.support.CmsConst;
import com.wangyang.cms.pojo.vo.ArticleDetailVO;
import com.wangyang.cms.pojo.vo.SheetDetailVo;
import com.wangyang.cms.repository.CategoryRepository;
import com.wangyang.cms.service.IArticleService;
import com.wangyang.cms.service.IHtmlService;
import com.wangyang.cms.service.IOptionService;
import com.wangyang.cms.utils.NodeJsUtil;
import com.wangyang.cms.utils.ServiceUtil;
import com.wangyang.cms.utils.TemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
    IOptionService optionService;

    @GetMapping
    public Page<? extends  ArticleDto> articleList(@PageableDefault(sort = {"id"},direction = DESC) Pageable pageable,
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
            if(articleDetailVO.getUpdateChannelFirstName()){
                htmlService.generateChannelListHtml();
            }
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


    @GetMapping("/preview/{articleId}")
    public String preview(@PathVariable("articleId")Integer articleId){
        return "11";
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


//    @GetMapping("/updateCategory/{articleId}")
//    public Article updateCategory(@PathVariable("articleId") Integer articleId,Integer categoryId){
//        Article article = articleService.findArticleById(articleId);
//        Integer  oldCategory = article.getCategoryId();
//
//        article.setCategoryId(categoryId);
//        articleService.save(article);
//
//        //更新之前存在分类,需要重新生成以便删除该文章
//        if(oldCategory!=null&&oldCategory!=categoryId){
//            htmlService.addOrRemoveArticleToCategoryListByCategoryId(oldCategory);
//        }
//        //更新新的分类
//        htmlService.addOrRemoveArticleToCategoryListByCategoryId(categoryId);
//
//        return article;
//    }
    @GetMapping("/updateAll")
    public Set<String> updateAllArticleHtml(@RequestParam(value = "more", defaultValue = "false") Boolean more){
        List<Article> articles = articleService.updateAllArticleHtml(more);
        articles.forEach(article->{
            //更新文章摘要
            if(more){
                article = articleService.createOrUpdate(article);
                articleService.generateSummary(article);
                article.setTemplateName(CmsConst.DEFAULT_ARTICLE_TEMPLATE);
                if(article.getLikes()==null){
                    article.setLikes(0);
                }
                if(article.getVisits()==null){
                    article.setVisits(0);
                }
                if(article.getCommentNum()==null){

                    article.setCommentNum(0);
                }
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
    public ArticleDetailVO updateCategory(@PathVariable("articleId") Integer articleId,Integer baseCategoryId){
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

    @GetMapping("/findListByCategoryId/{id}")
    public List<ArticleDto> findListByCategoryId(@PathVariable("id") Integer id){
        return articleService.listBy(id);
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
        //生成该文章之下的评论
        htmlService.generateCommentHtmlByArticleId(articleDetailVO.getId());
        //生成文章
        htmlService.conventHtml(articleDetailVO);
        return articleDetailVO;
    }


}
