package com.wangyang.pojo.dto;

import com.wangyang.pojo.entity.Category;
import lombok.Data;

import java.util.List;

@Data
public class ArticleAndCategoryMindDto {

    List<ArticleMindDto> list;
    Category category;
}
