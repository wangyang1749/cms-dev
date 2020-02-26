package com.wangyang.cms.pojo.entity.base;

import com.wangyang.cms.pojo.enums.ArticleStatus;

import javax.persistence.*;

@Entity(name = "BaseArticle")
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER, columnDefinition = "int default 0")
public class BaseArticle extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int templateId;
    @Column(name = "status", columnDefinition = "int default 1")
    private ArticleStatus status ;//=ArticleStatus.PUBLISHED;
    private int likes;
    private int visits;
    private int userId;
    private int commentNum;
    private Boolean haveHtml=true;

    private String title;

    @Column(name = "original_content", columnDefinition = "longtext not null")
    private String originalContent;

    @Column(name = "format_content", columnDefinition = "longtext not null")
    private String formatContent;
    private String summary;
    private String viewName;




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getFormatContent() {
        return formatContent;
    }

    public void setFormatContent(String formatContent) {
        this.formatContent = formatContent;
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
}

