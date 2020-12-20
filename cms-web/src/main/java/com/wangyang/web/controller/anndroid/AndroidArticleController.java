package com.wangyang.web.controller.anndroid;

import com.wangyang.service.service.IArticleService;
import com.wangyang.pojo.dto.ArticleDto;
import com.wangyang.pojo.entity.Article;
import com.google.common.base.Joiner;
import com.wangyang.pojo.params.ArticleQuery;
import com.wangyang.common.CmsConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.DESC;

//
@RestController
@RequestMapping("/android/article")
public class AndroidArticleController {

    @Autowired
    IArticleService articleService;

    @GetMapping("/load/{viewName}")
    public String getArticleDetail(@PathVariable("viewName") String viewName){
        String htmlPath =  "article/"+ viewName;
        return convert(htmlPath);
    }

    @GetMapping
    public Page<? extends ArticleDto> articleList(@PageableDefault(sort = {"id"},direction = DESC) Pageable pageable,
                                                  @RequestParam(value = "more", defaultValue = "true") Boolean more,
                                                  ArticleQuery articleQuery){
        Page<Article> articles = articleService.pagePublishBy(pageable,articleQuery);
        return articleService.convertToSimple(articles);
    }


    private String convert(String viewPath){
        String path = CmsConst.WORK_DIR+ "/html/" +viewPath;
        File file = new File(path);
        if(file.exists()){
            FileInputStream fileInputStream=null;
            try {
                fileInputStream = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
                List<String> list = reader.lines().collect(Collectors.toList());
                String content = Joiner.on("\n").join(list);
//                content = content.replace("<!--[","").replace("]-->","");
                return content;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }finally {
                if(fileInputStream!=null){
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
