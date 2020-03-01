package com.wangyang.cms.pojo.vo;

import com.wangyang.cms.pojo.dto.ArticleDto;
import com.wangyang.cms.pojo.dto.CategoryDto;
import com.wangyang.cms.pojo.dto.TagsDto;
import com.wangyang.cms.pojo.entity.Category;
import com.wangyang.cms.pojo.entity.Tags;

import java.util.List;

public class ArticleVO extends ArticleDto {

    private List<TagsDto> tags;
    private List<CategoryDto> categories;

    public List<TagsDto> getTags() {
        return tags;
    }

    public void setTags(List<TagsDto> tags) {
        this.tags = tags;
    }

    public List<CategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDto> categories) {
        this.categories = categories;
    }


}
