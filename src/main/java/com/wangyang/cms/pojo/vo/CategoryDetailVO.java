package com.wangyang.cms.pojo.vo;

import com.wangyang.cms.pojo.dto.ArticleDto;
import com.wangyang.cms.pojo.dto.CategoryDto;

import java.util.List;

public class CategoryDetailVO extends CategoryDto {
    private String description;
    private int articleNumber;
    private Integer templateId;
    private Boolean haveHtml;
    private String viewName;
    private List<ArticleDto> articleVOList;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getArticleNumber() {
        return articleNumber;
    }

    public void setArticleNumber(int articleNumber) {
        this.articleNumber = articleNumber;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public Boolean getHaveHtml() {
        return haveHtml;
    }

    public void setHaveHtml(Boolean haveHtml) {
        this.haveHtml = haveHtml;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public List<ArticleDto> getArticleVOList() {
        return articleVOList;
    }

    public void setArticleVOList(List<ArticleDto> articleVOList) {
        this.articleVOList = articleVOList;
    }
}
