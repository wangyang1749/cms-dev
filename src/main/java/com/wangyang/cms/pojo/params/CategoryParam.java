package com.wangyang.cms.pojo.params;

import javax.validation.constraints.NotBlank;

public class CategoryParam {

    private String description;
    @NotBlank(message = "Category name can't empty!!")
    private String name;
    private int parentId;
    private Integer templateId=2;
    private String viewName;
    private Boolean haveHtml;

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

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }



    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }
}
