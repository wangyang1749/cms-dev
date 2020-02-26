package com.wangyang.cms.pojo.entity;

import com.wangyang.cms.pojo.entity.base.BaseTemplate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "1")
public class TemplatePage extends BaseTemplate {

    private String viewName;



    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }
}
