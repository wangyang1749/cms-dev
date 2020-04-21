package com.wangyang.model.pojo.params;

import com.wangyang.model.pojo.enums.ArticleStatus;

public class ArticleQuery {
    /**
     * Keyword.
     */
    private String keyword;

    /**
     * Post status.
     */
    private ArticleStatus status;

    /**
     * Category id.
     */
    private Integer categoryId;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public ArticleStatus getStatus() {
        return status;
    }

    public void setStatus(ArticleStatus status) {
        this.status = status;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
