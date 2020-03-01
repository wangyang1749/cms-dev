package com.wangyang.cms.pojo.entity;

import com.wangyang.cms.pojo.entity.base.BaseArticle;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("1")
public class Sheet extends BaseArticle {
}
