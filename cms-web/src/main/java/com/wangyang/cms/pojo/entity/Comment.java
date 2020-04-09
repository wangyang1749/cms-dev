package com.wangyang.cms.pojo.entity;


import com.wangyang.cms.pojo.entity.base.BaseDiscuss;
import com.wangyang.cms.pojo.entity.base.BaseEntity;
import com.wangyang.cms.pojo.enums.CommentType;

import javax.persistence.*;

/**
 * 评论的数据结构
 */
@Entity
@DiscriminatorValue(value = "1")
public class Comment extends BaseDiscuss {
    @Column(columnDefinition = "int default 0")
    private Integer articleId;

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }
}
