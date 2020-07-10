package com.wangyang.cms.controller.user;


import com.google.common.base.Joiner;
import com.wangyang.cms.util.HtmlFileRender;
import com.wangyang.cms.util.HtmlFileRenderHandler;
import com.wangyang.cms.util.HtmlFileRenderObj;
import com.wangyang.common.utils.DocumentUtil;
import com.wangyang.common.utils.FileUtils;
import com.wangyang.common.utils.TemplateUtil;
import com.wangyang.data.service.IArticleService;
import com.wangyang.data.service.ICategoryService;
import com.wangyang.data.service.ITemplateService;
import com.wangyang.data.service.IUserService;
import com.wangyang.common.CmsConst;
import com.wangyang.model.pojo.dto.ArticleAndCategoryMindDto;
import com.wangyang.model.pojo.dto.CategoryArticleListDao;
import com.wangyang.model.pojo.entity.Article;
import com.wangyang.model.pojo.entity.Category;
import com.wangyang.model.pojo.entity.Template;
import com.wangyang.model.pojo.vo.ArticleDetailVO;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
@ResponseBody
public class ArticleDetailController {
    @Autowired
    IUserService userService;
    @Autowired
    ITemplateService templateService;

    @Autowired
    ICategoryService categoryService;

    @Autowired
    IArticleService articleService;
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
        return  HtmlFileRenderHandler.render(new HtmlFileRenderObj<ArticleDetailVO>() {
            @Override
            public ArticleDetailVO data() {
                Article article = articleService.findByViewName(viewName);
                ArticleDetailVO articleDetailVO = articleService.convert(article);
                return articleDetailVO;
            }


            @Override
            public Template template(ArticleDetailVO articleDetailVO) {
                return templateService.findByEnName(articleDetailVO.getTemplateName());
            }

            @Override
            public String path() {
                return "article";
            }

            @Override
            public String viewName() {
                return viewName;
            }
        });



//        String convert = FileUtils.convert("article/" + viewName+".html",request);
//        if(convert!=null){
//            return convert;
//        }
//        return "Page is not found!";
    }

    @GetMapping("/sheet/{viewName}")
    public String showSheet(@PathVariable("viewName") String viewName,HttpServletRequest request){
        String convert = FileUtils.convert("sheet/" + viewName,request);
        if(convert!=null){
            return convert;
        }
        return "Page is not found!";
    }








    @GetMapping("/articleList/{viewName}.html")
    public String showArticleList(@PathVariable("viewName") String viewName,HttpServletRequest request){
        return  HtmlFileRenderHandler.render(new HtmlFileRenderObj<CategoryArticleListDao>() {
            @Override
            public CategoryArticleListDao data() {
                Category category = categoryService.findByViewName(viewName);
                CategoryArticleListDao categoryArticle = articleService.findCategoryArticleBy(category, 0);
                return categoryArticle;
            }


            @Override
            public Template template(CategoryArticleListDao categoryArticleListDao) {
                return templateService.findByEnName(categoryArticleListDao.getCategory().getTemplateName());
            }

            @Override
            public String path() {
                return "articleList";
            }

            @Override
            public String viewName() {
                return viewName;
            }
        });

//
//        String convert = FileUtils.convert("articleList/" + viewName,request);
//        if(convert!=null){
//            return convert;
//        }
//        return "Page is not found!";
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
