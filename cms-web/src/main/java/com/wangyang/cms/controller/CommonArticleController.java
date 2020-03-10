package com.wangyang.cms.controller;

import com.wangyang.cms.pojo.support.BaseResponse;
import com.wangyang.cms.pojo.vo.ArticleVO;
import com.wangyang.cms.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/article")
public class CommonArticleController {
    @Autowired
    IArticleService articleService;
    @GetMapping("/preview/{articleId}")
    public ModelAndView preview(@PathVariable("articleId")Integer articleId){
        return articleService.preview(articleId);
    }

    @GetMapping("/previewPdf/{articleId}")
    public ModelAndView previewPdf(@PathVariable("articleId")Integer articleId){
        return articleService.previewPdf(articleId);
    }

    @GetMapping("/download/{id}")
    public String downloadPdf(@PathVariable("id") Integer id){
        String generatePdf = articleService.generatePdf(id);
        return "redirect:/"+generatePdf;
    }

}
