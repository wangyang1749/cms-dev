package com.wangyang.cms.controller.api;

import com.wangyang.cms.pojo.dto.ArticleDto;
import com.wangyang.cms.pojo.dto.CategoryDto;
import com.wangyang.cms.pojo.entity.Category;
import com.wangyang.cms.pojo.params.CategoryParam;
import com.wangyang.cms.pojo.vo.CategoryDetailVO;
import com.wangyang.cms.service.ICategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    ICategoryService categoryService;



    @GetMapping
    public List<CategoryDto> list(){
        return categoryService.listAll();
    }

    @PostMapping
    public Category add(@Valid @RequestBody CategoryParam categoryParam){
        Category category = new Category();
        BeanUtils.copyProperties(categoryParam,category);
        return categoryService.add(category);
    }

    @PostMapping("/update/{categoryId}")
    public Category update(@Valid @RequestBody CategoryParam categoryParam,@PathVariable("categoryId") Integer categoryId){
        return categoryService.update(categoryId,categoryParam);
    }

    @RequestMapping("/delete/{id}")
    public void deleteById(@PathVariable("id") Integer id){
        categoryService.deleteById(id);
    }
    @RequestMapping("/find/{id}")
    public Category findByID(@PathVariable("id") Integer id){
        return categoryService.findById(id);
    }

    @GetMapping("/find/article/{id}")
    public List<ArticleDto> findArticleByCategoryId(@PathVariable("id") Integer id){
        return categoryService.findArticleById(id);
    }
    @GetMapping("/find/categoryDetail/{id}")
    public CategoryDetailVO findByCategoryDetail(@PathVariable("id") Integer id){
        return categoryService.findCategoryDetailVOByID(id);
    }
    @GetMapping("/tree_view")
    public List<Category> listAsTree(){
        return null;
    }
}
