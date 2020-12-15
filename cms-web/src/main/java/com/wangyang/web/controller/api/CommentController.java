package com.wangyang.web.controller.api;

import com.wangyang.service.service.ICommentService;
import com.wangyang.service.service.IHtmlService;
import com.wangyang.pojo.entity.Comment;
import com.wangyang.pojo.params.CommentLoginUserParam;
import com.wangyang.pojo.params.CommentParam;
import com.wangyang.pojo.vo.CommentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    ICommentService commentService;

    @Autowired
    IHtmlService htmlService;

    @PostMapping
    public Comment add(@RequestBody @Valid CommentParam commentParam){
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentParam,comment);
        Comment saveComment = commentService.add(comment);
        /**
         * 根据一个评论生成单个文章下的评论列表
         */
        htmlService.generateCommentHtmlByArticleId(comment.getArticleId());
        return saveComment;
    }
    @PostMapping("/addByLoginUser")
    public Comment addByLoginUser(@RequestBody @Valid CommentLoginUserParam commentLoginUserParam){
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentLoginUserParam,comment);
        Comment saveComment = commentService.add(comment);
        /**
         * 根据一个评论生成单个文章下的评论列表
         */
        htmlService.generateCommentHtmlByArticleId(comment.getArticleId());
        return saveComment;
    }



    @GetMapping("/listByArticleId/{id}")
    public Page<CommentVo> listByArticleId(@PathVariable("id") Integer id, @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){

        Page<CommentVo> commentDtos = commentService.pageVoBy(id,pageable);
        return  commentDtos;
    }

    @DeleteMapping("/deleteById/{id}")
    public Comment deleteById(@PathVariable("id") Integer id){
        Comment comment = commentService.findById(id);
        commentService.deleteById(comment.getId());
        htmlService.generateCommentHtmlByArticleId(comment.getArticleId());
        return comment;
    }

}
