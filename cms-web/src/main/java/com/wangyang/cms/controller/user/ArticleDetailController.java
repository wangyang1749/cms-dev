package com.wangyang.cms.controller.user;


import com.google.common.base.Joiner;
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
    private static  String pattern = "<!--#include file=\"(.*?)\"-->";
    private static String varPattern = "<!--\\{\\{(.*?)}}-->";
    // 创建 Pattern 对象
    private static Pattern r = Pattern.compile(pattern);
    private static Pattern rv = Pattern.compile(varPattern);

    @GetMapping("/")
    public String index(HttpServletRequest request){

//        String userName = userService.getCurrentUserName();
//        if(userName!=null){
//            System.out.println(userName);
//        }
        String convert = convert("index.html",request);
        if(convert!=null){
            return convert;
        }
        return "Page is not found!";
    }

    @GetMapping("/article/{viewName}")
    public String showArticle(@PathVariable("viewName") String viewName, HttpServletRequest request){
        String convert = convert("article/" + viewName,request);
        if(convert!=null){
            return convert;
        }
        return "Page is not found!";
    }

    @GetMapping("/sheet/{viewName}")
    public String showSheet(@PathVariable("viewName") String viewName,HttpServletRequest request){
        String convert = convert("sheet/" + viewName,request);
        if(convert!=null){
            return convert;
        }
        return "Page is not found!";
    }

    @GetMapping("/articleList/{viewName}")
    public String showArticleList(@PathVariable("viewName") String viewName,HttpServletRequest request){
        String convert = convert("articleList/" + viewName,request);
        if(convert!=null){
            return convert;
        }
        return "Page is not found!";
    }



    private String openFile(String path){
        FileInputStream fileInputStream=null;
        try {
            File file = new File(path);
            if(file.exists()){
                fileInputStream = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
                List<String> list = reader.lines().collect(Collectors.toList());
                String content = Joiner.on("\n").join(list);
                return content;
            }else {
                return "Page is not found!!";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "Page is not found!!";
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
    private String convert(String viewPath,HttpServletRequest request){
        String content = openFile(CmsConst.WORK_DIR+"/html/"+viewPath);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(content);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
//            System.out.println("Found value: " + m.group(0) );
//            System.out.println("Found value: " + m.group(1) );
            String components = m.group(1);
            String componentsContent = openFile( CmsConst.WORK_DIR+"/html/"+components);
            componentsContent = java.util.regex.Matcher.quoteReplacement(componentsContent);
            m.appendReplacement(sb,componentsContent);
        }

        m.appendTail(sb);
        String result = sb.toString();
        Matcher matcher = rv.matcher(result);
        while (matcher.find()){
            String attr = matcher.group(1);
//            System.out.println(attr);
            String attrS = (String) request.getAttribute(attr);
//            System.out.println(attrS);
            if(attrS!=null){
                result = matcher.replaceAll(attrS);
            }else {
                result = matcher.replaceAll("");
            }
        }
        return result;
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
