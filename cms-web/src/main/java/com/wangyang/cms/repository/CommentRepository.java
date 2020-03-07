package com.wangyang.cms.repository;

import com.wangyang.cms.pojo.entity.ArticleCategory;
import com.wangyang.cms.pojo.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    List<Comment> deleteByArticleId(int id);

}
