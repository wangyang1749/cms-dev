package com.wangyang.model.pojo.params;

import com.wangyang.model.pojo.dto.InputConverter;
import com.wangyang.model.pojo.entity.Article;
import com.wangyang.model.pojo.enums.ArticleStatus;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class ArticleParams  implements InputConverter<Article> {

    private String templateName;
    private ArticleStatus status =ArticleStatus.PUBLISHED;
    private Boolean haveHtml;

    @NotBlank(message = "文章标题不能为空!!")
    private String title;
    @NotBlank(message = "文章内容不能为空!!")
    private String originalContent;
    private String summary;
    private String viewName;
    private Set<Integer> tagIds;
    @NotNull(message = "文章类别不能为空!!")
    private Integer categoryId;
    @NotNull(message = "文章用户不能为空!!")
    private Integer userId;
    private String  path="article";
    private String picPath;

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public ArticleStatus getStatus() {
        return status;
    }

    public void setStatus(ArticleStatus status) {
        this.status = status;
    }

    public Boolean getHaveHtml() {
        return haveHtml;
    }

    public void setHaveHtml(Boolean haveHtml) {
        this.haveHtml = haveHtml;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalContent() {
        return originalContent;
    }

    public void setOriginalContent(String originalContent) {
        this.originalContent = originalContent;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Set<Integer> getTagIds() {
        return tagIds;
    }

    public void setTagIds(Set<Integer> tagIds) {
        this.tagIds = tagIds;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
}
