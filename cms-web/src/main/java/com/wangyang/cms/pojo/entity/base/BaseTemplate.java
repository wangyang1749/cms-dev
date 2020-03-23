package com.wangyang.cms.pojo.entity.base;

import javax.persistence.*;

@Entity(name = "BaseTemplate")
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER, columnDefinition = "int default 0")
public class BaseTemplate extends  BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String enName;
    private String description;

    @Column(name = "template_value", columnDefinition = "longtext ")
    private String templateValue;


    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getTemplateValue() {
        return templateValue;
    }

    public void setTemplateValue(String templateValue) {
        this.templateValue = templateValue;
    }
}
