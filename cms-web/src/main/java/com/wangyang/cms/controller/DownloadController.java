package com.wangyang.cms.controller;

import com.wangyang.cms.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/download")
public class DownloadController {
    @Autowired
    IArticleService articleService;

    @GetMapping("/articlePdf/{id}")
    public String downloadPdf(@PathVariable("id") Integer id){
        String generatePdf = articleService.generatePdf(id);
        return "redirect:/"+generatePdf;
    }

}
