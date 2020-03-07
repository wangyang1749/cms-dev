package com.wangyang.cms.controller.api;

import com.wangyang.cms.pojo.dto.CategoryDto;
import com.wangyang.cms.pojo.entity.Category;
import com.wangyang.cms.pojo.params.CategoryParam;
import com.wangyang.cms.pojo.support.BaseResponse;
import com.wangyang.cms.pojo.dto.CategoryArticleListDao;
import com.wangyang.cms.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        return categoryService.add(categoryParam);
    }

    @PostMapping("/update/{categoryId}")
    public Category update(@Valid @RequestBody CategoryParam categoryParam,@PathVariable("categoryId") Integer categoryId){
        return categoryService.update(categoryId,categoryParam);
    }

    @RequestMapping("/delete/{id}")
    public BaseResponse deleteById(@PathVariable("id") Integer id){
        categoryService.deleteById(id);
        return BaseResponse.ok("Success delete category"+ id);
    }
    @RequestMapping("/find/{id}")
    public Category findByID(@PathVariable("id") Integer id){
        return categoryService.findById(id);
    }


    @GetMapping("/tree_view")
    public List<Category> listAsTree(){
        return null;
    }
}
