package com.wangyang.cms.controller.api;

import com.wangyang.cms.core.jms.CmsService;
import com.wangyang.cms.core.jms.producer.IProducerService;
import com.wangyang.cms.pojo.dto.ArticleDto;
import com.wangyang.cms.pojo.entity.Article;
import com.wangyang.cms.pojo.entity.Category;
import com.wangyang.cms.pojo.params.ArticleParams;
import com.wangyang.cms.pojo.params.ArticleQuery;
import com.wangyang.cms.pojo.support.BaseResponse;
import com.wangyang.cms.pojo.vo.ArticleDetailVO;
import com.wangyang.cms.pojo.vo.ArticleVO;
import com.wangyang.cms.pojo.vo.TagsCategoryArticleVo;
import com.wangyang.cms.repository.CategoryRepository;
import com.wangyang.cms.service.IArticleService;
import com.wangyang.cms.utils.NodeJsUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/article")
//@CrossOrigin
public class ArticleController {

    @Autowired
    IArticleService articleService;

    @Autowired
    IProducerService producerService;

    @Autowired
    CmsService cmsService;
    @Autowired
    CategoryRepository categoryRepository;
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

    @PostMapping
    public ArticleDetailVO createArticle(@RequestBody @Valid ArticleParams articleParams){
        ArticleDetailVO article = articleService.createArticle(articleParams, articleParams.getTagIds(), articleParams.getCategoryIds());
        if(article.getHaveHtml()){
            //使用JMS生成文章,文章列表
            producerService.sendMessage(article);
            producerService.commonTemplate("AC");
//            covertHtml(articleDetailVO);
        }
        return article;
    }


    @RequestMapping("/delete/{id}")
    public Article delete(@PathVariable("id") Integer id){
        return  articleService.deleteByArticleId(id);
    }

    @PostMapping("/form")
    public ArticleDetailVO createArticleByForm(@Valid  ArticleParams articleParams){
        ArticleDetailVO article = articleService.createArticle(articleParams, articleParams.getTagIds(), articleParams.getCategoryIds());
        if(article.getHaveHtml()){

            producerService.sendMessage(article);
            producerService.commonTemplate("AC");
//            covertHtml(articleDetailVO);
        }
        return article;
    }

    @GetMapping("/{articleId}/del")
    public void delArticle(@PathVariable("articleId")int aid){
        articleService.deleteByArticleId(aid);
    }

    @PostMapping("/update/{articleId}")
    public ArticleDetailVO updateArticle(@Valid @RequestBody ArticleParams articleParams,
                                         @PathVariable("articleId") Integer articleId){
        List<Category> oldCategory = categoryRepository.findCategoryByArticleId(articleId);

        ArticleDetailVO articleDetailVO = articleService.updateArticle(articleId, articleParams, articleParams.getTagIds(), articleParams.getCategoryIds());

        if(articleDetailVO.getHaveHtml()){
            cmsService.updateCategoryPage(oldCategory,articleDetailVO.getCategoryIds());
            //跟新文章分类, 还需要重新生成老的分类
            producerService.sendMessage(articleDetailVO);
            producerService.commonTemplate("AU");
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

    @GetMapping("/updateAll")
    public List<Integer> updateAllArticleHtml(){
        List<Integer> list = articleService.updateAllArticleHtml();
        return list;
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



}
