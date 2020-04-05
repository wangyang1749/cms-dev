package com.wangyang.cms.controller.api;

import com.wangyang.cms.pojo.entity.Comment;
import com.wangyang.cms.pojo.enums.CommentType;
import com.wangyang.cms.pojo.params.CommentParam;
import com.wangyang.cms.pojo.vo.CommentVo;
import com.wangyang.cms.service.ICommentService;
import com.wangyang.cms.service.IHtmlService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    ICommentService commentService;

    @Autowired
    IHtmlService htmlService;

    @PostMapping
    public Comment add(@RequestBody CommentParam commentParam){
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentParam,comment);
        comment.setCommentType(CommentType.ARTICLE);
        Comment saveComment = commentService.add(comment);
        /**
         * 根据一个评论生成单个文章下的评论列表
         */
        htmlService.generateCommentHtmlByComment(saveComment);
        return saveComment;
    }

    @GetMapping("/listArticle/{id}")
    public Page<CommentVo> listByResourceId(@PathVariable("id") Integer id, @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        Page<Comment> commentPage = commentService.listByResourceId(id, CommentType.ARTICLE, pageable);
        Page<CommentVo> commentVos = commentService.convertCommentVo(commentPage);
        return  commentVos;
    }

    @DeleteMapping("/deleteById/{id}")
    public Comment deleteById(@PathVariable("id") Integer id){
        Comment comment = commentService.findById(id);
        commentService.deleteById(comment.getId());
        htmlService.generateCommentHtmlByComment(comment);
        return comment;
    }

}
