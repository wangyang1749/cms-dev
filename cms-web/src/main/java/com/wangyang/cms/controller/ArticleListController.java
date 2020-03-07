package com.wangyang.cms.controller;

import com.wangyang.cms.pojo.dto.CategoryArticleListDao;
import com.wangyang.cms.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/articleList")
public class ArticleListController {
    @Autowired
    ICategoryService categoryService;

    @GetMapping("/category/{categoryId}")
    public ModelAndView articleListByCategory(@PathVariable("categoryId") Integer categoryId, Integer page){
        if(page<=0) page=1;
        if(page==null||categoryId==null){
            return new ModelAndView("error");
        }
        page = page-1;
        return categoryService.getArticleListByCategory(categoryId,page);
    }
}
