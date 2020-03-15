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
    private ArticleStatus status =ArticleStatus.PUBLISHED;
    private int userId;
    private String title;
    private String viewName;
    @Column(name = "original_content", columnDefinition = "longtext not null")
    private String originalContent;
    @Column(name = "format_content", columnDefinition = "longtext")
    private String formatContent;
    @Column(name = "toc_content", columnDefinition = "longtext")
    private String toc;

    private String path;

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
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

