package com.wangyang.pojo.params;

import javax.validation.constraints.NotBlank;

public class CategoryParam {

    private String description;
    @NotBlank(message = "Category name can't empty!!")
    private String name;
//    @NotBlank(message = "parentId  can't empty!!")
    private Integer parentId;
    private String templateName;
    private String viewName;
    private Boolean haveHtml;
    private String picPath;
    private String path;
    private Integer order;
    private Boolean recommend=false;
    private String articleTemplateName;
    private Integer articleListSize=10;
    private Boolean isDesc=true;


    public Integer getArticleListSize() {
        return articleListSize;
    }

    public void setArticleListSize(Integer articleListSize) {
        this.articleListSize = articleListSize;
    }

    public Boolean getDesc() {
        return isDesc;
    }

    public void setDesc(Boolean desc) {
        isDesc = desc;
    }

    public String getArticleTemplateName() {
        return articleTemplateName;
    }

    public void setArticleTemplateName(String articleTemplateName) {
        this.articleTemplateName = articleTemplateName;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
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

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Boolean getRecommend() {
        return recommend;
    }

    public void setRecommend(Boolean recommend) {
        this.recommend = recommend;
    }
}
