package com.wangyang.cms.service;

import com.wangyang.cms.pojo.vo.CategoryVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import java.util.List;

@SpringBootTest
public class TestCategoryService {
    @Autowired
    ICategoryService categoryService;

    @Test
    public void testTree(){
        List<CategoryVO> categoryVOS = categoryService.listAsTree();
        System.out.println(categoryVOS);
    }
    @Test
    public void test(){
        categoryService.findCategoryByArticleId(3);
    }

    public void test2(){}
}
