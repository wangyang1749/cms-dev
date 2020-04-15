package com.wangyang.cms.controller.api;

import com.wangyang.cms.pojo.dto.CategoryDto;
import com.wangyang.cms.pojo.entity.Category;
import com.wangyang.cms.pojo.params.CategoryParam;
import com.wangyang.cms.pojo.support.CmsConst;
import com.wangyang.cms.service.ICategoryService;
import com.wangyang.cms.service.IHtmlService;
import com.wangyang.cms.utils.ServiceUtil;
import com.wangyang.cms.utils.TemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

import static org.springframework.data.domain.Sort.Direction.DESC;

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
        return categoryService.listAllDto();
    }

    @PostMapping
    public Category add(@Valid @RequestBody CategoryParam categoryParam){
        Category category = new Category();

        BeanUtils.copyProperties(categoryParam,category);
        Category saveCategory = categoryService.addOrUpdate(category);
        //生成category列表Html
        htmlService.generateCategoryListHtml(saveCategory);
        if(saveCategory.getHaveHtml()){
            //生成文章第一页的列表
            htmlService.convertArticleListBy(saveCategory);
        }
        return saveCategory;
    }

    @GetMapping("/template/{categoryEnName}")
    public Page<CategoryDto> pageBy(@PathVariable("categoryEnName") String categoryEnName, @RequestParam(value = "page", defaultValue = "0")Integer page){
        return categoryService.pageBy(categoryEnName,page,20);
    }

    @PostMapping("/update/{categoryId}")
    public Category update(@Valid @RequestBody CategoryParam categoryParam,@PathVariable("categoryId") Integer categoryId){
        Category category = categoryService.findById(categoryId);
        TemplateUtil.deleteTemplateHtml(category.getViewName(), category.getPath());
        BeanUtils.copyProperties(categoryParam, category);
        Category updateCategory = categoryService.addOrUpdate(category);
        //更新Category列表
        htmlService.generateCategoryListHtml(updateCategory);
        if(updateCategory.getHaveHtml()){
            //生成文章第一页的列表
            htmlService.convertArticleListBy(category);
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
        htmlService.generateCategoryListHtml(category);
        return category;
    }
    @RequestMapping("/find/{id}")
    public Category findByID(@PathVariable("id") Integer id){
        return categoryService.findById(id);
    }

    @GetMapping("/updateAll")
    public Set<String> updateAllCategoryHtml(@RequestParam(value = "more", defaultValue = "false") Boolean more){
        List<Category> categories = categoryService.listAll();
        categories.forEach(category -> {
            if(more){
                if(category.getTemplateName()==null){
                    category.setTemplateName(CmsConst.DEFAULT_CATEGORY_TEMPLATE);
                }
                if(category.getArticleTemplateName()==null){
                    category.setArticleTemplateName(CmsConst.DEFAULT_ARTICLE_TEMPLATE);
                }
                if(category.getDesc()==null){
                    category.setDesc(true);
                }
                if(category.getArticleListSize()==null){
                    category.setArticleListSize(10);
                }
                category.setPath(CmsConst.CATEGORY_LIST_PATH);
                categoryService.save(category);
            }
            htmlService.convertArticleListBy(category);
        });

        return ServiceUtil.fetchProperty(categories,Category::getName);
    }

    @GetMapping("/updateHtml/{id}")
    public Category updateHtmlById(@PathVariable("id")  int id){
        Category category = categoryService.findById(id);
        if(category.getHaveHtml()){
            htmlService.convertArticleListBy(category);
            return category;
        }
        return category;
    }

    @GetMapping("/recommendOrCancel/{id}")
    public Category recommendOrCancelHome(@PathVariable("id") Integer id){
        Category category = categoryService.recommendOrCancelHome(id);
//        htmlService.generateHome();
        htmlService.convertArticleListBy(category);
        //只需要在推荐时刷新主页
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
            htmlService.convertArticleListBy(category);
            htmlService.generateCategoryListHtml(category);
        }else{
            TemplateUtil.deleteTemplateHtml(category.getViewName(),category.getPath());
            htmlService.generateCategoryListHtml(category);
        }
//        if(category.getRecommend()){
//            //有可能更改首页排序
//            htmlService.generateHome();
//
//        }
        return category;
    }

//    @GetMapping("/list/{id}")
//    public List<CategoryDto> listBaseCategory(@PathVariable("id") int id){
//        return categoryService.listCategoryDtoByParent(id);
//    }


//    @GetMapping("/listByParentId/{id}")
//    public List<CategoryDto> listByParentId(@PathVariable("id") Integer id){
//        return categoryService.listCategoryDtoByParent(id);
//    }


    @GetMapping("/generateHtml/{id}")
    public Category generateHtml(@PathVariable("id") Integer id){
        Category category = categoryService.findById(id);
        htmlService.convertArticleListBy(category);
        return category;
    }


}
