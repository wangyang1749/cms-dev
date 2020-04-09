package com.wangyang.cms.service;

import com.wangyang.authorize.pojo.entity.User;
import com.wangyang.authorize.service.IUserService;
import com.wangyang.cms.controller.api.ArticleController;
import com.wangyang.cms.pojo.entity.Article;
import com.wangyang.cms.pojo.entity.Category;
import com.wangyang.cms.pojo.entity.Comment;
import com.wangyang.cms.pojo.enums.CommentType;
import com.wangyang.cms.pojo.params.ArticleParams;
import com.wangyang.cms.repository.ArticleRepository;
import com.wangyang.cms.repository.BaseCategoryRepository;
import com.wangyang.cms.repository.CategoryRepository;
import com.wangyang.cms.repository.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public abstract class AbstractServiceTest {
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    IOptionService optionService;

    @Autowired
    OptionRepository optionRepository;

    @Autowired
    ITemplateService templateService;
    @Autowired
    IArticleService articleService;

    @Autowired
    ICategoryService categoryService;

    @Autowired
    BaseCategoryRepository baseCategoryRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    IHtmlService htmlService;

    @Autowired
    IUserService userService;

    @Autowired
    ArticleController articleController;

    @Autowired
    ICommentService commentService;


    public Comment add(){
        User user = userService.add(addUser());

        Comment comment = new Comment();
        comment.setUserId(user.getId());
//        comment.setCommentType(CommentType.ARTICLE);
        comment.setContent("Test Comment");
        return comment;
    }

    public Category addParentCategory(){
        Category categoryParam = new Category();
        categoryParam.setName("TestCategory");
        categoryParam.setParentId(0);
        categoryParam.setPath("111111111111");
        return categoryService.addOrUpdate(categoryParam);
    }
    public Category addCategory(){

        Category categoryParam = new Category();
        categoryParam.setName("TestCategory");
        Category parentCategory = addParentCategory();
        categoryParam.setParentId(parentCategory.getId());
        return categoryParam;
    }
    public Category addCategory2(){
        Category categoryParam = new Category();
        categoryParam.setName("TestCategory2");
        Category parentCategory = addParentCategory();
        categoryParam.setParentId(parentCategory.getId());
        return categoryParam;
    }
    public User addUser(){
        User user = new User();
        user.setUsername("wangyang");
        return user;
    }
    public Article addArticle(){
        Category category = categoryService.addOrUpdate(addCategory());
        User user = userService.add(addUser());
        Article articleParams = new Article();
        articleParams.setTitle("Title");
        articleParams.setPath("article");
        //类别不能为空
        articleParams.setCategoryId(category.getId());
        //用户不能为空
        articleParams.setUserId(user.getId());
        articleParams.setOriginalContent("test_test");
        return articleParams;
    }
    public ArticleParams addArticleParam(){
        ArticleParams articleParams = new ArticleParams();
        articleParams.setTitle("Title");
        articleParams.setOriginalContent("test_test");
        return articleParams;
    }
    public Article updateArticle(){
        Article articleParams = new Article();
        articleParams.setTitle("updateTitle");
        articleParams.setOriginalContent("test_test");
        return articleParams;
    }
    public Set<Integer> tagIds(){
        Set<Integer> set = new HashSet<>();
        set.add(1);
        return set;
    }
    public Set<Integer> categoryIds(){
        Set<Integer> set = new HashSet<>();
        set.add(1);
        return set;
    }
}
