package com.wangyang.web.controller.user;


import com.wangyang.common.BaseResponse;
import com.wangyang.common.CmsConst;
import com.wangyang.pojo.dto.ArticleDto;
import com.wangyang.pojo.params.ArticleQuery;
import com.wangyang.service.service.IArticleService;
import com.wangyang.service.service.ITemplateService;
import com.wangyang.pojo.entity.Article;
import com.wangyang.pojo.entity.Template;
import com.wangyang.service.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Controller
public class WebController {

    @Autowired
    IArticleService articleService;
    @Autowired
    ITemplateService templateService;
    @Autowired
    IUserService userService;


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



    /**
     * 没有登录用户的文章列表和搜索
     * @param articleQuery
     * @param pageable
     * @param model
     * @return
     */
    @RequestMapping(value = "/articleList")
    public String articleListBySort(ArticleQuery articleQuery, @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable, Model model) {
        Page<Article> articlePage = articleService.pagePublishBy(pageable, articleQuery);
        Page<ArticleDto> articleDtoPage = articleService.convertToSimple(articlePage);
        model.addAttribute("view",articleDtoPage);
        Template template = templateService.findOptionalByEnName(CmsConst.ARTICLE_PAGE).get();
        return template.getTemplateValue();
    }

    /**
     * 基于AJax的分页
     * @param categoryId

     * @return
     */
    @GetMapping("/categoryAjax/{categoryId}")
    @ResponseBody
    public Page<ArticleDto> articleListByCategoryAjax(@PathVariable("categoryId") Integer categoryId, Integer page){
        Page<ArticleDto> articles = articleService.pageDtoByCategoryId(categoryId,page);
        return articles;
    }

    @GetMapping("/option/increaseLikeCount/{id}")
    @ResponseBody
    public BaseResponse increaseLikes(@PathVariable("id") int id) {
        int likes = articleService.increaseLikes(id);
        Integer likesNumber = articleService.getLikesNumber(id);
        if(likes!=0&likesNumber!=null){
            return BaseResponse.ok("操作成功",likesNumber);
        }else {
            return BaseResponse.error("操作失败");
        }
    }

    @GetMapping("/option/getLikeCount/{id}")
    @ResponseBody
    public BaseResponse getLikesCount(@PathVariable("id") int id) {
        Integer likesNumber = articleService.getLikesNumber(id);
        return BaseResponse.ok("操作成功",likesNumber);
    }

    @GetMapping("/option/increaseViewCount/{id}")
    @ResponseBody
    public BaseResponse increaseVisitsCount(@PathVariable("id") int id) {
        int visits = articleService.increaseVisits(id);
        Integer visitsNumber = articleService.getVisitsNumber(id);
        if(visits!=0&visitsNumber!=null){
            return BaseResponse.ok("操作成功",visitsNumber);
        }else {
            return BaseResponse.error("操作失败");
        }
    }

    @GetMapping("/option/getVisitsCount/{id}")
    @ResponseBody
    public BaseResponse getVisitsCount(@PathVariable("id") int id) {
        Integer visitsNumber = articleService.getVisitsNumber(id);
        return BaseResponse.ok("操作成功",visitsNumber);
    }

    @RequestMapping("/admin")
    public String index(){
        return "redirect:admin/index.html";
    }
}
