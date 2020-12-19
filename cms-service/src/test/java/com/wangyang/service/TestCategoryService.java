package com.wangyang.service;

import com.wangyang.pojo.vo.CategoryVO;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author wangyang
 * @date 2020/12/19
 */
public class TestCategoryService extends AbstractServiceTest {

    @Test
    public void testTreeList(){
        List<CategoryVO> categoryVo = categoryService.listAdminCategoryVo();

        System.out.println();
    }
}
