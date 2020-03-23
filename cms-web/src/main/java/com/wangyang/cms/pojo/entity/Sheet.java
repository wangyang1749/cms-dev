package com.wangyang.cms.pojo.entity;

import com.wangyang.cms.pojo.entity.base.BaseArticle;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("1")
public class Sheet extends BaseArticle {
    @Column(columnDefinition = "bit(1) default false")
    private Boolean recommend=false;
    @Column(columnDefinition = "bit(1) default false")
    private Boolean existNav=false;

    public Boolean getRecommend() {
        return recommend;
    }

    public void setRecommend(Boolean recommend) {
        this.recommend = recommend;
    }

    public Boolean getExistNav() {
        return existNav;
    }

    public void setExistNav(Boolean existNav) {
        this.existNav = existNav;
    }
}
