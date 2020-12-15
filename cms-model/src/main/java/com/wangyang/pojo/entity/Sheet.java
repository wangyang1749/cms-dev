package com.wangyang.pojo.entity;

import com.wangyang.pojo.entity.base.BaseArticle;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("1")
@Data
public class Sheet extends BaseArticle {
    @Column(columnDefinition = "longtext")
    private String cssContent;
    @Column(columnDefinition = "longtext")
    private String jsContent;

    @Column(columnDefinition = "bit(1) default false")
    private Boolean recommend=false;
    @Column(columnDefinition = "bit(1) default false")
    private Boolean existNav=false;

}
