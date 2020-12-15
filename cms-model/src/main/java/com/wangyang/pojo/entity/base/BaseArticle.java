package com.wangyang.pojo.entity.base;

import com.wangyang.pojo.enums.ArticleStatus;

import javax.persistence.*;

@Entity(name = "BaseArticle")
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER, columnDefinition = "int default 0")
public class BaseArticle extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "status", columnDefinition = "int default 1")
    private ArticleStatus status =ArticleStatus.PUBLISHED;
    private Integer userId;
    private String title;
    private String viewName;
    @Column(name = "original_content", columnDefinition = "longtext not null")
    private String originalContent;
    @Column(name = "format_content", columnDefinition = "longtext")
    private String formatContent;
    @Column(name = "toc_content", columnDefinition = "longtext")
    private String toc;
    private String templateName;
    private String commentTemplateName;
    //是否开启评论
    @Column(columnDefinition = "bit(1) default false")
    private Boolean openComment=false;

    private String path;


    public String getCommentTemplateName() {
        return commentTemplateName;
    }

    public void setCommentTemplateName(String commentTemplateName) {
        this.commentTemplateName = commentTemplateName;
    }

    public Boolean getOpenComment() {
        return openComment;
    }

    public void setOpenComment(Boolean openComment) {
        this.openComment = openComment;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getToc() {
        return toc;
    }

    public void setToc(String toc) {
        this.toc = toc;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }



    public ArticleStatus getStatus() {
        return status;
    }

    public void setStatus(ArticleStatus status) {
        this.status = status;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}

