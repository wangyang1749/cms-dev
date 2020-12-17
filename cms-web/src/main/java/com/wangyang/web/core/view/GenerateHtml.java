package com.wangyang.web.core.view;

import com.wangyang.common.CmsConst;
import com.wangyang.common.utils.FileUtils;
import com.wangyang.common.utils.TemplateUtil;
import com.wangyang.pojo.dto.ArticleAndCategoryMindDto;
import com.wangyang.pojo.entity.Category;
import com.wangyang.pojo.entity.Template;
import com.wangyang.service.service.IArticleService;
import com.wangyang.service.service.ICategoryService;
import com.wangyang.service.service.IHtmlService;
import com.wangyang.service.service.ITemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.thymeleaf.ITemplateEngine;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangyang
 * @date 2020/12/15
 */
@Component
public class GenerateHtml {


    @Autowired
    ICategoryService categoryService;

    @Autowired
    IHtmlService htmlService;

    @Autowired
    IArticleService articleService;

    @Autowired
    ITemplateService templateService;
    /**
     * html_articleList_bioinfo_4_page.html
     * 生成category之下的article list
     * 在该category下增删改文章需要删除分页数据，以确保实时更新
     * @param args
     * @return
     */
    public String page(String[] args){
//        File file = new File(CmsConst.WORK_DIR+"/html/"+page+".html");
        Category category = categoryService.findByViewName(args[1]);
        String resultHtml = htmlService.convertArticleListBy(category,Integer.parseInt(args[2]));
        return resultHtml;
    }

    /**
     * html_articleList_bioinfo_4_mind
     * @param args
     * @return
     */
    public String mind(String[] args){
        ArticleAndCategoryMindDto articleAndCategoryMindDto = articleService.listArticleMindDto(Integer.parseInt(args[2]));
        Category category = articleAndCategoryMindDto.getCategory();
        String mindFormat = articleService.jsMindFormat(articleAndCategoryMindDto);
        Template template = templateService.findByEnName(CmsConst.ARTICLE_JS_MIND);
        Map<String,Object> map = new HashMap<>();
        map.put("mind",mindFormat);
        map.put("category",category);
        String path = category.getPath()+"/"+category.getViewName()+"/"+args[2];
        String htmlAndSave = TemplateUtil.convertHtmlAndSave(path, "mind", map, template);
        return htmlAndSave;
    }

}
