package com.wangyang.cms.controller;

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

}
