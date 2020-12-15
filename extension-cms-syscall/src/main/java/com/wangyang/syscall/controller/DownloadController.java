package com.wangyang.syscall.controller;


import com.wangyang.common.CmsConst;
import com.wangyang.common.exception.ArticleException;
import com.wangyang.service.service.IArticleService;
import com.wangyang.pojo.entity.Article;
import com.wangyang.pojo.enums.ArticleStatus;
import com.wangyang.syscall.utils.NodeJsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;

@Controller
@RequestMapping("/download")
public class DownloadController {

    @Autowired
    IArticleService articleService;


    @GetMapping("/pdf/{articleId}")
    public String  generatePdf(@PathVariable("articleId") Integer articleId) {
        Article article = articleService.findArticleById(articleId);
        if(article.getStatus()!= ArticleStatus.PUBLISHED){
            throw new ArticleException("文章没有发布不能生成PDF!!");
        }
        String pdfPath= article.getPath()+"/"+article.getViewName()+".pdf";
        String absolutePath = CmsConst.WORK_DIR +"/html/"+pdfPath;
        File file = new File(absolutePath);
        String result;
        if(file.exists()&&article.getPdfPath()!=null){
            result =  article.getPdfPath();
        }else {
            String url = "http://localhost:8080/preview/pdf/"+articleId;
            String node = NodeJsUtil.execNodeJs("node", CmsConst.WORK_DIR + "/templates/nodejs/generatePdf.js", url, CmsConst.WORK_DIR + "/html/" + pdfPath);
            article.setPdfPath(pdfPath);
            Article saveArticle = articleService.save(article);
            result =  saveArticle.getPdfPath();
        }
        return "redirect:/"+result;
    }
}
