package com.wangyang.cms.repository;

import com.wangyang.cms.pojo.entity.Article;
import com.wangyang.cms.pojo.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer>
    , JpaSpecificationExecutor<Comment> {
//    List<Comment> deleteByArticleId(int id);
}
