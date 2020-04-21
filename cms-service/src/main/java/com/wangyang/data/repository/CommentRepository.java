package com.wangyang.data.repository;

import com.wangyang.model.pojo.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CommentRepository extends JpaRepository<Comment,Integer>
    , JpaSpecificationExecutor<Comment> {
//    List<Comment> deleteByArticleId(int id);
}
