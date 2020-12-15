package com.wangyang.pojo.dto;

import com.wangyang.pojo.enums.ArticleStatus;

public class SheetDto {

    private Integer id;
    private ArticleStatus status =ArticleStatus.PUBLISHED;
    private Integer userId;
    private String title;
    private String viewName;
    private String templateName;
    private String path;
    private Boolean recommend=false;
    private Boolean existNav=false;

    public Boolean getRecommend() {
        return recommend;
    }

    public void setRecommend(Boolean recommend) {
        this.recommend = recommend;
    }

    public Boolean getExistNav() {
        return existNav;
    }

    public void setExistNav(Boolean existNav) {
        this.existNav = existNav;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArticleStatus getStatus() {
        return status;
    }

    public void setStatus(ArticleStatus status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }




}
