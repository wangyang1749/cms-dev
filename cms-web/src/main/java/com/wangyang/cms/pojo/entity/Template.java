package com.wangyang.cms.pojo.entity;

import com.wangyang.cms.pojo.entity.base.BaseTemplate;
import com.wangyang.cms.pojo.enums.TemplateType;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@DiscriminatorValue(value = "0")
public class Template extends BaseTemplate implements Serializable {



//    @Column(name = "template_type", columnDefinition = "int")
    private TemplateType templateType;

    public Template(){}

    public Template(String name,String enName,String templateValue,TemplateType templateType){
        super.setName(name);
        super.setEnName(enName);
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
