package com.wangyang.model.pojo.dto;

import com.wangyang.model.pojo.entity.Category;
import lombok.Data;

import java.util.List;

@Data
public class ArticleAndCategoryMindDto {

    List<ArticleMindDto> list;
    Category category;
}
