package com.wangyang.pojo.params;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CommentParam {


    private Integer userId;
    @NotNull(message = "文章Id不能为空!")
    private Integer articleId;
//    private CommentType commentType;
//    @NotBlank(message = "用户昵称不能为空")
    private String username;
//    @NotBlank(message = "用户电子邮件不能为空")
    private String email;
    @NotBlank(message = "评论内容不能为空!")
    private String content;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
