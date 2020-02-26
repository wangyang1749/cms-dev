package com.wangyang.cms.pojo.entity;

import com.wangyang.cms.pojo.entity.base.BaseEntity;
import com.wangyang.cms.pojo.entity.base.BaseTemplate;
import com.wangyang.cms.pojo.enums.TemplateType;

import javax.persistence.*;


@Entity
@DiscriminatorValue(value = "0")
public class Template extends BaseTemplate {



    @Column(name = "template_type", columnDefinition = "int")
    private TemplateType templateType;

    public Template(){}

    public Template(String name,String templateValue,TemplateType templateType){
        super.setName(name);
        super.setTemplateValue(templateValue);
        this.templateType = templateType;

    }
    public Template(String templateValue) {
        super.setTemplateValue(templateValue);
    }

    public void setTemplateType(TemplateType templateType) {
        this.templateType = templateType;
    }
}
