package com.wangyang.cms.pojo.entity;

import com.wangyang.cms.pojo.entity.base.BaseTemplate;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "1")
public class Components extends BaseTemplate {

    private String viewName;
    private String dataName;
    private boolean status;
    @Column(name = "template_event")
    private String event;
    private String path;



    public Components(){
    }

    public Components(String name, String path , String templateValue, String viewName, String dataName, String event, boolean status){
        this.path=path;
        super.setName(name);
        super.setTemplateValue(templateValue);
        this.setViewName(viewName);
        this.setDataName(dataName);
        this.setEvent(event);
        this.setStatus(status);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }
}
