package com.wangyang.web.controller;

import com.wangyang.web.util.HtmlFileRenderHandler;
import com.wangyang.web.util.HtmlFileRenderMap;
import com.wangyang.common.CmsConst;
import com.wangyang.common.utils.FileUtils;
import com.wangyang.service.service.IArticleService;
import com.wangyang.service.service.ICategoryService;
import com.wangyang.service.service.IHtmlService;
import com.wangyang.service.service.ITemplateService;
import com.wangyang.pojo.dto.ArticleAndCategoryMindDto;
import com.wangyang.pojo.dto.ArticleDto;
import com.wangyang.pojo.dto.CategoryArticleListDao;
import com.wangyang.pojo.entity.Article;
import com.wangyang.pojo.entity.Category;
import com.wangyang.pojo.entity.Template;
import com.wangyang.pojo.params.ArticleQuery;
import com.wangyang.common.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.springframework.data.domain.Sort.Direction.DESC;


@Controller
//@RequestMapping("/articleList")
public class ArticleListController {
    @Autowired
    IArticleService articleService;

    @Autowired
    ICategoryService categoryService;

    @Autowired
    ITemplateService templateService;

    @Autowired
    IHtmlService htmlService;

    @ResponseBody
    @RequestMapping(value = "/{categoryPath}/{categoryViewName}/page-{page}.html",produces = "text/html")
    public String articleListBy(HttpServletRequest request, @PathVariable("categoryPath") String categoryPath, @PathVariable("categoryViewName") String categoryViewName, @PathVariable("page") Integer page){
        File file = new File(CmsConst.WORK_DIR+"/html/"+categoryPath+"/"+categoryViewName+"/"+page+".html");
        String result = null;
        if(file.exists()){
            result = FileUtils.convert(file,request);
        }else {

            Category category = categoryService.findByViewName(categoryViewName);
            if (category!=null){
                String resultHtml = htmlService.convertArticleListBy(category,page);
               result =  FileUtils.convertByString(resultHtml,request);
            }
        }
        if (request!=null){
            return result;
        }
        return  "Page is not found!";
    }

    @GetMapping("/mind/{categoryId}.html")
    @ResponseBody
    public String listArticleMindDto(@PathVariable("categoryId") int categoryId){

//        Template template = templateService.findByEnName(CmsConst.ARTICLE_JS_MIND);
//        return FileUtils.convertByString(TemplateUtil.convertHtmlAndPreview(mindFormat,template));

        return  HtmlFileRenderHandler.render(new HtmlFileRenderMap() {
//            @Override
//            public boolean isDebug() {
//                return true;
//            }

            @Override
            public String path() {
                return "mind";
            }

            @Override
            public String viewName() {
                return String.valueOf(categoryId);
            }

            @Override
            public Map<String, Object> data() {
                ArticleAndCategoryMindDto articleAndCategoryMindDto = articleService.listArticleMindDto(categoryId);
                Category category = articleAndCategoryMindDto.getCategory();
                String mindFormat = articleService.jsMindFormat(articleAndCategoryMindDto);
                Map<String,Object> map = new HashMap<>();
                map.put("mind",mindFormat);
                map.put("category",category);
                return map;
            }

            @Override
            public Template template() {
                Template template = templateService.findByEnName(CmsConst.ARTICLE_JS_MIND);
                return template;
            }
        });
    }

    /**
     * 根据条件查询文章并且缓存，直到增删改文章
     * @param request
     * @param articleQuery
     * @param pageable
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/articleList",produces = "text/html")
    public String articleListBySort(HttpServletRequest request,  ArticleQuery articleQuery, @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){

        StringBuffer sb = new StringBuffer();
        sb.append("index");
        Map<String, String[]> params = request.getParameterMap();
        Set<String> keys = params.keySet();
        if(params!=null){
            params.forEach((k,v)->{
                sb.append("-"+k+"-"+v[0]);
            });
        }
        File file = new File(CmsConst.WORK_DIR+"/html/articleList/queryTemp/"+sb.toString()+".html");
        String result = null;
        if(file.exists()){
            result = FileUtils.convert(file,request);
        }else {
//            ArticleQuery articleQuery = new ArticleQuery();
//            Pageable pageable = PageRequest.of(0,1);
            Page<Article> articlePage = articleService.pageBy(pageable,articleQuery);
            Page<ArticleDto> articleDtoPage = articleService.convertToSimple(articlePage);
            String resultHtml;
            if(keys.contains("keyword")){
                resultHtml = htmlService.previewArticlePageBy(request,articleDtoPage);
            }else {
                resultHtml = htmlService.convertArticlePageBy(request,articleDtoPage, sb.toString());
            }

            result =  FileUtils.convertByString(resultHtml,request);
        }
        if (request!=null){
            return result;
        }

        return  "Page is not found!";

//        return "1111"+categoryViewName+page;
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void notFound(PropertyReferenceException ex) {
        System.out.println(ex.getMessage());
    }

    @RequestMapping(value = "/category/{categoryId}",produces = "text/html")
    public ModelAndView articleListByCategory(@PathVariable("categoryId") Integer categoryId,@RequestParam(value = "page", defaultValue = "1") Integer page){
        if(categoryId==null){
            return new ModelAndView("error");
        }
        if(page<=0) page=1;
        page = page-1;


        ModelAndView modelAndView = new ModelAndView();
        Category category = categoryService.findById(categoryId);
//
//        // 分页
        CategoryArticleListDao articleListByCategory = articleService.findCategoryArticleBy(category, page);
//        Template template = templateService.findById(category.getTemplateId());
        Template template = templateService.findByEnName(category.getTemplateName());
        modelAndView.setViewName(template.getTemplateValue());
        modelAndView.addObject("view", articleListByCategory);
        return modelAndView;

    }


    /**
     * 基于AJax的分页
     * @param categoryId

     * @return
     */
    @GetMapping("/categoryAjax/{categoryId}")
    @ResponseBody
    public Page<ArticleDto> articleListByCategoryAjax(@PathVariable("categoryId") Integer categoryId, Integer page){
        ArticleQuery articleQuery = new ArticleQuery();
        articleQuery.setCategoryId(categoryId);
        Page<ArticleDto> articles = articleService.pageDtoBy(categoryId,page);
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




//    @GetMapping("/option/like/{id}")
//    @ResponseBody
//    public  BaseResponse getLikesNumber(@PathVariable("id") int id){
//        int likes = articleService.increaseLikes(id);
//        Integer likesNumber = articleService.getLikesNumber(id);
//        if(likes!=0|| likesNumber!=null){
//            return BaseResponse.ok("点赞成功！",likesNumber);
//        }
//        return BaseResponse.error("点赞失败！");
//    }


}
