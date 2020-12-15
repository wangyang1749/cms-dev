package com.wangyang.web.controller.user;


import com.wangyang.service.service.IArticleService;
import com.wangyang.service.service.ICategoryService;
import com.wangyang.service.service.IUserService;
import com.wangyang.pojo.dto.ArticleAndCategoryMindDto;
import com.wangyang.pojo.dto.CategoryDto;
import com.wangyang.pojo.dto.UserDto;
import com.wangyang.pojo.entity.Article;
import com.wangyang.pojo.entity.Category;
import com.wangyang.pojo.params.ArticleQuery;
import com.wangyang.pojo.vo.ArticleDetailVO;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Controller
@RequestMapping("/user")
public class UserArticleController {
    @Autowired
    IUserService userService;

    @Autowired
    IArticleService articleService;

    @Autowired
    ICategoryService categoryService;



    @GetMapping("/write")
    public String writeArticle(HttpServletRequest request, Model model, @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        int userId = (Integer)request.getAttribute("userId");
//        Page<Article> articlePage = articleService.pageByUserId(userId, pageable);
//        model.addAttribute("view",articlePage);
//        System.out.println(userId);
        return "templates/user/write";
    }


    @GetMapping("/edit/{id}")
    public String editArticle(HttpServletRequest request,Model model,@PathVariable("id") Integer id){
        int userId = (Integer)request.getAttribute("userId");//在授权时将userId存入request
        Article article = articleService.findByIdAndUserId(id, userId);
        ArticleDetailVO articleDetailVO = articleService.conventToAddTags(article);
//        ArticleDetailVO articleDetailVO = articleService.convert(article);
        model.addAttribute("view",articleDetailVO);
        return "templates/user/write";
    }

    @GetMapping("/info")
    public String info(HttpServletRequest  request,Model model){
        int userId = (Integer)request.getAttribute("userId");
        UserDto userDto = userService.findUserDaoById(userId);
        model.addAttribute("view",userDto);
        return "templates/user/info";
    }


    @GetMapping("/mindJs/{categoryId}")
    public String mindJs(@PathVariable("categoryId") int categoryId,Model model){
        ArticleAndCategoryMindDto articleAndCategoryMindDto = articleService.listArticleMindDto(categoryId);
        Category category = articleAndCategoryMindDto.getCategory();
        String mindFormat = articleService.jsMindFormat(articleAndCategoryMindDto);
        model.addAttribute("mind",mindFormat);
        model.addAttribute("category",category);
        return "templates/user/mindJs";
    }


    @GetMapping("/delete/{id}")
    public String deleteArticle(HttpServletRequest request,@PathVariable("id") Integer id){
        int userId = (Integer)request.getAttribute("userId");
        return "redirect:/user/articleList";
    }

    @GetMapping("/articleList")
    public String articleList(HttpServletRequest request, Model model, ArticleQuery articleQuery, @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        int userId = (Integer)request.getAttribute("userId");

        Page<Article> articlePage = articleService.pageByUserId(userId, pageable,articleQuery);
//        model.addAttribute("view",articleService.convertToAddCategory(articlePage));
        model.addAttribute("view",articlePage);
        List<CategoryDto> categories = categoryService.listAllDto();
        model.addAttribute("categories",categories);


//        System.out.println(userId);
        return "templates/user/articleList";
    }

}
