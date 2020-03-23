package com.wangyang.cms.pojo.entity;

import com.wangyang.cms.pojo.entity.base.BaseCategory;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@DiscriminatorValue(value = "1")
public class Channel extends BaseCategory implements Serializable {


    private String articleTemplateName;
    private String firstArticle;


    public String getArticleTemplateName() {
        return articleTemplateName;
    }

    public void setArticleTemplateName(String articleTemplateName) {
        this.articleTemplateName = articleTemplateName;
    }

    public String getFirstArticle() {
        return firstArticle;
    }

    public void setFirstArticle(String firstArticle) {
        this.firstArticle = firstArticle;
    }
}
