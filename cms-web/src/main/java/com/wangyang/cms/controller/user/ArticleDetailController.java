package com.wangyang.cms.controller.user;


import com.google.common.base.Joiner;
import com.wangyang.common.utils.FileUtils;
import com.wangyang.data.service.IUserService;
import com.wangyang.common.CmsConst;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
@ResponseBody
public class ArticleDetailController {
    @Autowired
    IUserService userService;

    @GetMapping("/")
    public String index(HttpServletRequest request){

//        String userName = userService.getCurrentUserName();
//        if(userName!=null){
//            System.out.println(userName);
//        }
        String convert = FileUtils.convert("index.html",request);
        if(convert!=null){
            return convert;
        }
        return "Page is not found!";
    }

    @GetMapping("/article/{viewName}.html")
    public String showArticle(@PathVariable("viewName") String viewName, HttpServletRequest request){
        String convert = FileUtils.convert("article/" + viewName+".html",request);
        if(convert!=null){
            return convert;
        }
        return "Page is not found!";
    }

    @GetMapping("/sheet/{viewName}")
    public String showSheet(@PathVariable("viewName") String viewName,HttpServletRequest request){
        String convert = FileUtils.convert("sheet/" + viewName,request);
        if(convert!=null){
            return convert;
        }
        return "Page is not found!";
    }

    @GetMapping("/articleList/{viewName}")
    public String showArticleList(@PathVariable("viewName") String viewName,HttpServletRequest request){
        String convert = FileUtils.convert("articleList/" + viewName,request);
        if(convert!=null){
            return convert;
        }
        return "Page is not found!";
    }






//    private String convert(String viewPath){
//        String path = CmsConst.WORK_DIR+"/html/"+viewPath;
//        File file = new File(path);
//        if(file.exists()){
//            FileInputStream fileInputStream=null;
//            try {
//                fileInputStream = new FileInputStream(file);
//                BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
//                List<String> list = reader.lines().collect(Collectors.toList());
//                String content = Joiner.on("\n").join(list);
//                content = content.replace("<!--[","").replace("]-->","");
//                return content;
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }finally {
//                if(fileInputStream!=null){
//                    try {
//                        fileInputStream.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//        return null;
//    }
//    @GetMapping("/{viewName}")
//    public ModelAndView show(@PathVariable("viewName") String viewName){
//        System.out.println(viewName);
////        ModelAndView modelAndView = new ModelAndView("html/article/"+viewName);
//        ModelAndView modelAndView = new ModelAndView("templates/user/articleDetail");
//        modelAndView.addObject("viewName",viewName);
//        return modelAndView;
//    }
}
