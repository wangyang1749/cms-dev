package com.wangyang.cms.service;

import com.wangyang.cms.pojo.entity.Comment;
import com.wangyang.cms.pojo.enums.CommentType;
import com.wangyang.cms.pojo.vo.CommentVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


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

    Page<Comment> listByResourceId(int id, CommentType commentType);

    Page<Comment> listByResourceId(int id, CommentType commentType, Pageable pageable);

    Page<CommentVo> convertCommentVo(Page<Comment> commentPage);

    /**
     *
     * @param id comment id
     * @return
     */
    Comment findById(int id);


    Page<Comment> list(Pageable pageable);

}
