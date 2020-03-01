package com.wangyang.cms.pojo.dto;

import com.wangyang.cms.pojo.dto.ArticleDto;
import com.wangyang.cms.pojo.entity.Category;
import org.springframework.data.domain.Page;

public class CategoryArticleListDao {

    private String viewName;
    private Page<ArticleDto> page;
    private Category category;
    private String path;

    public String getPath() {
        return category.getPath();
    }

    public void setPath(String path) {
        this.path = path;
    }



    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {

        this.viewName = viewName;
    }

    public Page<ArticleDto> getPage() {
        return page;
    }

    public void setPage(Page<ArticleDto> page) {
        this.page = page;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
