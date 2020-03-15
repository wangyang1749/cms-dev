package com.wangyang.cms.pojo.vo;

import com.wangyang.cms.pojo.entity.Category;
import com.wangyang.cms.pojo.entity.Tags;

import java.util.List;

public class TagsCategoryArticleVo {
    List<Tags> tags;
    List<Category> categories;
    ArticleDetailVO article;

    public List<Tags> getTags() {
        return tags;
    }

    public void setTags(List<Tags> tags) {
        this.tags = tags;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public ArticleDetailVO getArticle() {
        return article;
    }

    public void setArticle(ArticleDetailVO article) {
        this.article = article;
    }
}
