package com.wangyang.cms.pojo.entity;

import com.wangyang.cms.pojo.entity.base.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "Category")
public class Category extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Column(columnDefinition = "int default 0")
    private Integer parentId;
    private String description;
    @Column(columnDefinition = "int default 0")
    private Integer articleNumber;
//    private Integer templateId;
    private String templateName;
    @Column(columnDefinition = "bit(1) default true")
    private Boolean haveHtml=true;

    private String viewName;
    @Column(columnDefinition = "bit(1) default true")
    private Boolean status=true;
    private String picPath;
    private String path;
    @Column(name = "category_order",columnDefinition = "int default 1")
    private Integer order;
    @Column(columnDefinition = "bit(1) default false")
    private Boolean recommend=false;
    @Column(columnDefinition = "bit(1) default false")
    private Boolean existNav=false;
    private String articleTemplateName;


    private Integer articleListSize;
//    private Integer articleListPage=0;
    private Boolean isDesc;

//    private String firstArticle;
//    private String listViewName;//维护分类本身的列表

//    public String getListViewName() {
//        return listViewName;
//    }
//
//    public void setListViewName(String listViewName) {
//        this.listViewName = listViewName;
//    }


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

//    public String getFirstArticle() {
//        return firstArticle;
//    }
//
//    public void setFirstArticle(String firstArticle) {
//        this.firstArticle = firstArticle;
//    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getArticleNumber() {
        return articleNumber;
    }

    public void setArticleNumber(Integer articleNumber) {
        this.articleNumber = articleNumber;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
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

    public Boolean getExistNav() {
        return existNav;
    }

    public void setExistNav(Boolean existNav) {
        this.existNav = existNav;
    }
}
