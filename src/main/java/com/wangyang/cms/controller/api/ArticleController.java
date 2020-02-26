package com.wangyang.cms.controller.api;

import com.wangyang.cms.pojo.dto.ArticleDto;
import com.wangyang.cms.pojo.entity.Article;
import com.wangyang.cms.pojo.params.ArticleParams;
import com.wangyang.cms.pojo.params.ArticleQuery;
import com.wangyang.cms.pojo.support.BaseResponse;
import com.wangyang.cms.pojo.vo.ArticleDetailVO;
import com.wangyang.cms.pojo.vo.ArticleVO;
import com.wangyang.cms.service.IArticleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Optional;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/article")
//@CrossOrigin
public class ArticleController {

    @Autowired
    IArticleService articleService;

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
    public ArticleDetailVO createArticle(@Valid @RequestBody ArticleParams articleParams){
        return  articleService.createArticle(articleParams, articleParams.getTagIds(), articleParams.getCategoryIds());
    }

    @GetMapping("/{articleId}/del")
    public void delArticle(@PathVariable("articleId")int aid){
        articleService.deleteByArticleId(aid);
    }

    @PostMapping("/update/{articleId}")
    public ArticleDetailVO updateArticle(@Valid @RequestBody ArticleParams articleParams,
                                         @PathVariable("articleId") Integer articleId){

        return articleService.updateArticle(articleId,articleParams,articleParams.getTagIds(),articleParams.getCategoryIds());
    }
    @GetMapping("/preview/{articleId}")
    public String preview(@PathVariable("articleId")Integer articleId){
        return "11";
    }
    @GetMapping("/find/{articleId}")
    public ArticleDto findById(@PathVariable("articleId") Integer articleId){
        return articleService.findArticleAOById(articleId);
    }
}
