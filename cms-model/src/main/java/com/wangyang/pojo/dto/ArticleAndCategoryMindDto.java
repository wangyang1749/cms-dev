package com.wangyang.pojo.dto;

import com.wangyang.pojo.entity.Category;
import lombok.Data;

import java.util.List;

@Data
public class ArticleAndCategoryMindDto {

    public List<ArticleMindDto> list;
    public Category category;
    public String linkPath;
}
