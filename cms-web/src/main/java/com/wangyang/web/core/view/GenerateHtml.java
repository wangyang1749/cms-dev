package com.wangyang.web.controller.user;

import com.wangyang.common.CmsConst;
import com.wangyang.common.utils.FileUtils;
import com.wangyang.pojo.entity.Category;
import com.wangyang.service.service.IArticleService;
import com.wangyang.service.service.ICategoryService;
import com.wangyang.service.service.IHtmlService;
import com.wangyang.service.service.ITemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.thymeleaf.ITemplateEngine;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

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

    public String page(List<String> aaaa){
//        File file = new File(CmsConst.WORK_DIR+"/html/"+page+".html");
        Category category = categoryService.findByViewName(aaaa.get(1));
        String resultHtml = htmlService.convertArticleListBy(category,Integer.parseInt(aaaa.get(2)));
        return resultHtml;
    }
}
