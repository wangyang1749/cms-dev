package com.wangyang.cms.pojo.entity;

import com.wangyang.cms.pojo.entity.base.BaseArticle;
import com.wangyang.cms.pojo.entity.base.BaseEntity;
import com.wangyang.cms.pojo.enums.ArticleStatus;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@DiscriminatorValue(value = "0")
public class Article extends BaseArticle {


    @Column(name = "likes", columnDefinition = "int default 0")
    private Integer likes=0;
    @Column(name = "visits", columnDefinition = "int default 0")
    private Integer visits=0;
    @Column(name = "comment_num", columnDefinition = "int default 0")
    private Integer commentNum=0;
    private Boolean haveHtml=true;
    private String summary;
    private String picPath;
    private String pdfPath;
    private Integer categoryId;
//    @Column( columnDefinition = "int default 0")
//    private Integer templateId;



    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getVisits() {
        return visits;
    }

    public void setVisits(Integer visits) {
        this.visits = visits;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public Boolean getHaveHtml() {
        return haveHtml;
    }

    public void setHaveHtml(Boolean haveHtml) {
        this.haveHtml = haveHtml;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }


}
