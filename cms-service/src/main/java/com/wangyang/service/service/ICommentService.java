package com.wangyang.service.service;

import com.wangyang.pojo.entity.Comment;
import com.wangyang.pojo.dto.CommentDto;
import com.wangyang.pojo.vo.CommentVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ICommentService {




    /**
     *
     * @param uid user id
     * @param aid article id
     * @param comment
     * @return
     */
    Comment add(Comment comment);

    /**
     *
     * @param id comment id
     */
    void  deleteById(int id);

    /**
     *
     * @param id comment id
     * @param updateComment
     * @return
     */
    Comment update(int id, Comment updateComment);

    Page<CommentVo> listVoBy(int articleId);


    Page<CommentVo> pageVoBy(int articleId, Pageable pageable);

    Page<Comment> pageBy(int articleId, Pageable pageable);


    Page<CommentDto> pageDtoBy(int articleId, Pageable pageable);

    /**
     *
     * @param id comment id
     * @return
     */
    Comment findById(int id);


    Page<Comment> list(Pageable pageable);

}
