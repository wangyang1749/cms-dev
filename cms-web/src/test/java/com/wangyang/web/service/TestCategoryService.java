package com.wangyang.web.service;

import com.wangyang.pojo.dto.CategoryDto;
import com.wangyang.pojo.entity.Category;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class TestCategoryService extends AbstractServiceTest{

    @Test
    public void testAddCategory(){
        Category category = categoryService.addOrUpdate(addCategory());
        Assert.assertNotNull(category);
    }
    @Test
    public void testFindAllId(){
//        List<Integer> list = categoryRepository.findAllId();

    }

    @Test
    public void listRecommend(){
        List<CategoryDto> list = categoryService.listRecommend();
        System.out.println(list);
    }

    @Test
    public void testList(){
        List<Category> list = categoryService.list();
        System.out.println(list);
    }

    @Test
    public void testAddOrRemoveToMenu(){
        Category category = categoryService.addOrUpdate(addCategory());
        categoryService.addOrRemoveToMenu(category.getId());
    }

    @Test
    public void testBaseCategory(){
//        BaseCategory category = baseCategoryRepository.findById(14).get();
//        System.out.println(category instanceof  Category);

    }

}
