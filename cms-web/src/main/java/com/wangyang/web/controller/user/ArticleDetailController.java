package com.wangyang.web.controller.user;


import com.wangyang.common.CmsConst;
import com.wangyang.pojo.dto.ArticleDto;
import com.wangyang.pojo.params.ArticleQuery;
import com.wangyang.service.service.IArticleService;
import com.wangyang.service.service.ITemplateService;
import com.wangyang.pojo.entity.Article;
import com.wangyang.pojo.entity.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Controller
public class ArticleDetailController {

    @Autowired
    IArticleService articleService;
    @Autowired
    ITemplateService templateService;

    @GetMapping("/")
    public String index(HttpServletRequest request){
        return "html/index";
    }
    @GetMapping("/html/{path}/{viewName}.html")
    public String showArticle(@PathVariable("path") String path, @PathVariable("viewName") String viewName) {
        return "html" + File.separator+path+File.separator+viewName;
    }
//    @GetMapping("/html_{path}_{method}_{arg1}_{arg2}.html")
    public String show(@PathVariable("path") String path, @PathVariable("viewName") String viewName) {
        return "html" + File.separator+path+File.separator+viewName;
    }
    @GetMapping("/html_{path}.html")
    public String showArticleFormat(@PathVariable("path") String path) {
        return "html" + File.separator+path;
    }

    @RequestMapping(value = "/articleList")
    public String articleListBySort(ArticleQuery articleQuery, @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable, Model model) {
        Page<Article> articlePage = articleService.pageBy(pageable, articleQuery);
        Page<ArticleDto> articleDtoPage = articleService.convertToSimple(articlePage);
        model.addAttribute("view",articleDtoPage);
        Template template = templateService.findOptionalByEnName(CmsConst.ARTICLE_PAGE).get();
        return template.getTemplateValue();
    }
}
