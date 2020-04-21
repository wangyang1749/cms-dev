package com.wangyang.cms.controller;


import com.google.common.base.Joiner;
import com.wangyang.authorize.service.IUserService;
import com.wangyang.common.CmsConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ArticleDetailController {
    @Autowired
    IUserService userService;

    @GetMapping("/")
    public String index(){

//        String userName = userService.getCurrentUserName();
//        if(userName!=null){
//            System.out.println(userName);
//        }
        String convert = convert("index.html");
        if(convert!=null){
            return convert;
        }
        return "Page is not found!";
    }

    @GetMapping("/article/{viewName}")
    public String showArticle(@PathVariable("viewName") String viewName){
        String convert = convert("article/" + viewName);
        if(convert!=null){
            return convert;
        }
        return "Page is not found!";
    }

    @GetMapping("/sheet/{viewName}")
    public String showSheet(@PathVariable("viewName") String viewName){
        String convert = convert("sheet/" + viewName);
        if(convert!=null){
            return convert;
        }
        return "Page is not found!";
    }

    @GetMapping("/articleList/{viewName}")
    public String showArticleList(@PathVariable("viewName") String viewName){
        String convert = convert("articleList/" + viewName);
        if(convert!=null){
            return convert;
        }
        return "Page is not found!";
    }

    private String convert(String viewPath){
        String path = CmsConst.WORK_DIR+"/html/"+viewPath;
        File file = new File(path);
        if(file.exists()){
            FileInputStream fileInputStream=null;
            try {
                fileInputStream = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
                List<String> list = reader.lines().collect(Collectors.toList());
                String content = Joiner.on("\n").join(list);
                content = content.replace("<!--[","").replace("]-->","");
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
//    @GetMapping("/{viewName}")
//    public ModelAndView show(@PathVariable("viewName") String viewName){
//        System.out.println(viewName);
////        ModelAndView modelAndView = new ModelAndView("html/article/"+viewName);
//        ModelAndView modelAndView = new ModelAndView("templates/user/articleDetail");
//        modelAndView.addObject("viewName",viewName);
//        return modelAndView;
//    }
}
