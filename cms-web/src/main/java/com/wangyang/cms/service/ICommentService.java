package com.wangyang.cms.service;

import com.wangyang.cms.pojo.entity.Comment;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;

public interface ICommentService {
    /**
     *
     * @param uid user id
     * @param aid article id
     * @param comment
     * @return
     */
    Comment add(int uid,int aid,Comment comment);

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
    Comment update(int id,Comment updateComment);

    /**
     *
     * @param id comment id
     * @return
     */
    Comment findById(int id);

    /**
     *
     * @param id article id
     * @param pageable
     * @return
     */
    Page<Comment> listByArticleId(int id,Pageable pageable);

    Page<Comment> list(Pageable pageable);
}
