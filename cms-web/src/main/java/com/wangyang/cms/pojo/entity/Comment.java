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
    private Integer resourceId;
    //    @Column(columnDefinition = "int default 0")
    private CommentType commentType;

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public CommentType getCommentType() {
        return commentType;
    }

    public void setCommentType(CommentType commentType) {
        this.commentType = commentType;
    }
}
