package com.wangyang.service;

import com.wangyang.pojo.dto.ArticleDto;
import com.wangyang.pojo.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

/**
 * @author wangyang
 * @date 2020/12/20
 */
public class TestArticleService extends AbstractServiceTest {

//    @Test
    public void test(){
        Category category = new Category();
        category.setDesc(true);
        category.setId(1);
//        Page<ArticleDto> articleDtos = articleService.pageArticleDtoNoTopByCategoryAndPage(category, 1);
//        System.out.println(articleDtos);
    }

}
