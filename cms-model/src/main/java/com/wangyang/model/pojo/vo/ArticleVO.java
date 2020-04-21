package com.wangyang.model.pojo.vo;

import com.wangyang.model.pojo.dto.ArticleDto;
import com.wangyang.model.pojo.dto.CategoryDto;
import com.wangyang.model.pojo.dto.TagsDto;

import java.util.List;

public class ArticleVO extends ArticleDto {

    private List<TagsDto> tags;
    private CategoryDto category;

    public List<TagsDto> getTags() {
        return tags;
    }

    public void setTags(List<TagsDto> tags) {
        this.tags = tags;
    }

    public CategoryDto getCategory() {
        return category;
    }

    public void setCategory(CategoryDto category) {
        this.category = category;
    }
}
