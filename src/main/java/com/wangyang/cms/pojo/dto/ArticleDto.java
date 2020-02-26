package com.wangyang.cms.pojo.dto;

import com.wangyang.cms.pojo.entity.Category;
import com.wangyang.cms.pojo.entity.Tags;
import com.wangyang.cms.pojo.enums.ArticleStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

public class ArticleDto {
    private int id;
    private Date createDate;
    private Date updateDate;
    private int templateId;
    private ArticleStatus status;
    private int likes;
    private int visits;
    private int userId;
    private int commentNum;
    private Boolean haveHtml=false;

    private String title;
    private String viewName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
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

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }
}
