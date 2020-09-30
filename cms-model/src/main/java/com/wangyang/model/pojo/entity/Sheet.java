package com.wangyang.model.pojo.entity;

import com.wangyang.model.pojo.entity.base.BaseArticle;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("1")
@Data
public class Sheet extends BaseArticle {
    private String cssContent;
    private String jsContent;

    @Column(columnDefinition = "bit(1) default false")
    private Boolean recommend=false;
    @Column(columnDefinition = "bit(1) default false")
    private Boolean existNav=false;

}
