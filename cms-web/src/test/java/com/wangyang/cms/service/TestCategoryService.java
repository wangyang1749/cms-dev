package com.wangyang.cms.service;

import com.wangyang.cms.pojo.dto.CategoryDto;
import com.wangyang.cms.pojo.entity.Category;
import com.wangyang.cms.pojo.entity.Channel;
import com.wangyang.cms.pojo.entity.base.BaseCategory;
import com.wangyang.cms.pojo.vo.CategoryVO;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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
