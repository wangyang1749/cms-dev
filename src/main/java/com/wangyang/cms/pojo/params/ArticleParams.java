package com.wangyang.cms.pojo.params;

import com.wangyang.cms.pojo.dto.InputConverter;
import com.wangyang.cms.pojo.entity.Article;
import com.wangyang.cms.pojo.enums.ArticleStatus;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.util.Set;

public class ArticleParams  implements InputConverter<Article> {

    private int templateId;
    private ArticleStatus status =ArticleStatus.PUBLISHED;
    private Boolean haveHtml=false;
    @NotBlank(message = "Article title can't be empty!!")
    private String title;
    private String originalContent;
    private String summary;
    private String viewName;
    private Set<Integer> tagIds;
    private Set<Integer> categoryIds;



    public Set<Integer> getTagIds() {
        return tagIds;
    }

    public void setTagIds(Set<Integer> tagIds) {
        this.tagIds = tagIds;
    }

    public Set<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(Set<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }

    @Override
    public Article convertTo() {
        return InputConverter.super.convertTo();
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }


    public ArticleStatus getStatus() {
        return status;
    }

    public void setStatus(ArticleStatus status) {
        this.status = status;
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

    public Boolean getHaveHtml() {
        return haveHtml;
    }

    public void setHaveHtml(Boolean haveHtml) {
        this.haveHtml = haveHtml;
    }
}
