package com.wangyang.cms.pojo.params;

import com.wangyang.cms.pojo.enums.ArticleStatus;

import javax.persistence.Column;

public class SheetParam {

    private int templateId;
    private ArticleStatus status ;//=ArticleStatus.PUBLISHED;
    private int userId;
    private String title;
    private String viewName;
    private String originalContent;
    private String formatContent;
    private String path;

    public SheetParam(){}
    public SheetParam(int templateId,String title,String viewName,String originalContent){
        this.templateId=templateId;
        this.title =title;
        this.viewName = viewName;
        this.originalContent =originalContent;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
