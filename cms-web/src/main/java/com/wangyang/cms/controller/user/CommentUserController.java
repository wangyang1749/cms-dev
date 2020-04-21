package com.wangyang.cms.controller.user;

import com.wangyang.cms.pojo.entity.Comment;
import com.wangyang.cms.pojo.params.CommentParam;
import com.wangyang.cms.service.ICommentService;
import com.wangyang.cms.service.IHtmlService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/comment")
public class CommentUserController {
    @Autowired
    ICommentService commentService;

    @Autowired
    IHtmlService htmlService;

    @PostMapping
    public String add( CommentParam commentParam){
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentParam,comment);
        Comment saveComment = commentService.add(comment);

        return "redirect:";
    }
}
