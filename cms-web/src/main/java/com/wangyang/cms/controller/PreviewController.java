package com.wangyang.cms.controller;

import com.wangyang.cms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/preview")
public class PreviewController {
    @Autowired
    IArticleService articleService;
    @Autowired
    ICategoryService categoryService;
    @Autowired
    ISheetService sheetService;
    @Autowired
    IComponentsService componentsService;

    @GetMapping("/article/{articleId}")
    public ModelAndView previewArticle(@PathVariable("articleId")Integer articleId){
        return articleService.preview(articleId);
    }

    @GetMapping("/save/{articleId}")
    public ModelAndView previewSaveArticle(@PathVariable("articleId")Integer articleId){
        return articleService.previewForSave(articleId);
    }

    @GetMapping("/category/{id}")
    public ModelAndView previewCategory(@PathVariable("id") Integer id){
        return categoryService.preview(id);
    }
    @GetMapping("/sheet/{id}")
    public ModelAndView previewSheet(@PathVariable("id") Integer id){
        return sheetService.preview(id);
    }

    @GetMapping("/component/{id}")
    public ModelAndView previewComponent(@PathVariable("id") Integer id){
        return componentsService.preview(id);
    }

    @GetMapping("/pdf/{articleId}")
    public ModelAndView previewPdf(@PathVariable("articleId")Integer articleId){
        return articleService.previewPdf(articleId);
    }

}
