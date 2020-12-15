package com.wangyang.web.controller.user;


import com.wangyang.web.util.HtmlFileRenderHandler;
import com.wangyang.web.util.HtmlFileRenderObj;
import com.wangyang.common.utils.FileUtils;
import com.wangyang.service.service.IArticleService;
import com.wangyang.service.service.ICategoryService;
import com.wangyang.service.service.ITemplateService;
import com.wangyang.service.service.IUserService;
import com.wangyang.pojo.dto.CategoryArticleListDao;
import com.wangyang.pojo.entity.Article;
import com.wangyang.pojo.entity.Category;
import com.wangyang.pojo.entity.Template;
import com.wangyang.pojo.vo.ArticleDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

@Controller
public class ArticleDetailController {
    @GetMapping("/")
    public String index(HttpServletRequest request){
        return  "html/index";
    }
    @GetMapping("/{path}/{viewName}.html")
    public String showArticle(@PathVariable("path") String path, @PathVariable("viewName") String viewName) {
        return "html"+ File.separator+path+File.separator+viewName;
    }
}
