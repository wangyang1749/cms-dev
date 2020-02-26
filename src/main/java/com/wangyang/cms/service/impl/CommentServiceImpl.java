package com.wangyang.cms.service.impl;

import com.wangyang.cms.pojo.entity.Article;
import com.wangyang.cms.pojo.entity.Comment;
import com.wangyang.cms.pojo.entity.User;
import com.wangyang.cms.repository.ArticleRepository;
import com.wangyang.cms.repository.CommentRepository;
import com.wangyang.cms.repository.UserRepository;
import com.wangyang.cms.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.Optional;

@Service
public class CommentServiceImpl implements ICommentService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    ArticleRepository articleRepository;
    @Override
    public Comment add(int uid, int aid, Comment comment) {
        Optional<User> userOptional = userRepository.findById(uid);

        Optional<Article> articleOptional = articleRepository.findById(aid);

        return null;
    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public Comment update(int id, Comment updateComment) {
        return null;
    }

    @Override
    public Comment findById(int id) {
        return null;
    }

    @Override
    public Page<Comment> listByArticleId(int id, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Comment> list(Pageable pageable) {
        return null;
    }
}
