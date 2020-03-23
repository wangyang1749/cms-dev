package com.wangyang.cms.controller.api;

import com.wangyang.cms.pojo.dto.BaseCategoryDto;
import com.wangyang.cms.pojo.dto.CategoryDto;
import com.wangyang.cms.pojo.entity.Category;
import com.wangyang.cms.pojo.entity.base.BaseCategory;
import com.wangyang.cms.pojo.params.CategoryParam;
import com.wangyang.cms.pojo.support.BaseResponse;
import com.wangyang.cms.pojo.support.CmsConst;
import com.wangyang.cms.service.ICategoryService;
import com.wangyang.cms.service.IHtmlService;
import com.wangyang.cms.utils.ServiceUtil;
import com.wangyang.cms.utils.TemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/category")
@Slf4j
public class CategoryController {

    @Autowired
    ICategoryService categoryService;



    @Autowired
    IHtmlService htmlService;

    @GetMapping
    public List<CategoryDto> list(){
        return categoryService.listAll();
    }

    @PostMapping
    public Category add(@Valid @RequestBody CategoryParam categoryParam){
        Category category = new Category();
        BeanUtils.copyProperties(categoryParam,category);
        Category saveCategory = categoryService.addOrUpdate(category);

        //生成category列表Html
        htmlService.generateCategoryListHtml();
        if(saveCategory.getHaveHtml()){
            //生成文章第一页的列表
            htmlService.convertHtml(saveCategory);

        }
        return saveCategory;
    }

    @PostMapping("/update/{categoryId}")
    public Category update(@Valid @RequestBody CategoryParam categoryParam,@PathVariable("categoryId") Integer categoryId){
        Category category = categoryService.findById(categoryId);
        TemplateUtil.deleteTemplateHtml(category.getViewName(), category.getPath());
        BeanUtils.copyProperties(categoryParam, category);
        Category updateCategory = categoryService.addOrUpdate(category);

        //更新Category列表
        htmlService.generateCategoryListHtml();
        if(updateCategory.getHaveHtml()){
            //生成文章第一页的列表
            htmlService.convertHtml(category);

        }
        return updateCategory;
    }

    @RequestMapping("/delete/{id}")
    public Category deleteById(@PathVariable("id") Integer id){
        Category category = categoryService.deleteById(id);
        //删除分离的列表页面
        TemplateUtil.deleteTemplateHtml(category.getViewName(), category.getPath());
        log.info("### delete category" + category.getName());
        //重新生成分类的列表
        htmlService.generateCategoryListHtml();
        return category;
    }
    @RequestMapping("/find/{id}")
    public Category findByID(@PathVariable("id") Integer id){
        return categoryService.findById(id);
    }

    @GetMapping("/updateAll")
    public Set<String> updateAllCategoryHtml(@RequestParam(value = "more", defaultValue = "false") Boolean more){
        List<Category> categories = categoryService.list();
        categories.forEach(category -> {
            if(more){
                category.setTemplateName(CmsConst.DEFAULT_CATEGORY_TEMPLATE);
                categoryService.save(category);
            }
            htmlService.convertHtml(category);
        });
        return ServiceUtil.fetchProperty(categories,Category::getName);
    }

    @GetMapping("/updateHtml/{id}")
    public Category updateHtmlById(@PathVariable("id")  int id){
        Category category = categoryService.findById(id);
        if(category.getHaveHtml()){
            htmlService.convertHtml(category);
            return category;
        }
        return category;
    }

    @GetMapping("/recommendOrCancel/{id}")
    public Category recommendOrCancelHome(@PathVariable("id") Integer id){
        Category category = categoryService.recommendOrCancelHome(id);
        htmlService.generateHome();
        return category;
    }
    @GetMapping("/addOrRemoveToMenu/{id}")
    public Category addOrRemoveToMenu(@PathVariable("id") int id){
        Category category = categoryService.addOrRemoveToMenu(id);
        htmlService.generateMenuListHtml();
        return category;
    }
    @GetMapping("/haveHtml/{id}")
    public Category haveHtml(@PathVariable("id") int id){
        Category category = categoryService.haveHtml(id);
        if(category.getHaveHtml()){
            htmlService.convertHtml(category);
            htmlService.generateCategoryListHtml();
        }else{
            TemplateUtil.deleteTemplateHtml(category.getViewName(),category.getPath());
            htmlService.generateCategoryListHtml();
        }
//        if(category.getRecommend()){
//            //有可能更改首页排序
//            htmlService.generateHome();
//
//        }
        return category;
    }

    @GetMapping("/listBaseCategory")
    public List<BaseCategoryDto> listBaseCategory(){
        return categoryService.listBaseCategory();
    }


    @GetMapping("/generateHtml/{id}")
    public Category generateHtml(@PathVariable("id") Integer id){
        Category category = categoryService.findById(id);
        htmlService.convertHtml(category);
        return category;
    }


}
