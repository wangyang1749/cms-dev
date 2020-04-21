package com.wangyang.model.pojo.vo;

import com.wangyang.model.pojo.dto.CommentDto;
import com.wangyang.model.pojo.entity.User;

public class CommentVo extends CommentDto {
    private String content;
    private User user;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
