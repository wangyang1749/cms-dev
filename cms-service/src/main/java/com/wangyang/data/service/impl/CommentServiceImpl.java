package com.wangyang.data.service.impl;

import com.wangyang.common.exception.ObjectException;
import com.wangyang.common.exception.OptionException;
import com.wangyang.data.service.IArticleService;
import com.wangyang.data.service.ICommentService;
import com.wangyang.data.service.IUserService;
import com.wangyang.model.pojo.entity.Article;
import com.wangyang.model.pojo.entity.Comment;
import com.wangyang.data.repository.CommentRepository;
import com.wangyang.model.pojo.entity.User;
import com.wangyang.model.pojo.dto.CommentDto;
import com.wangyang.model.pojo.vo.CommentVo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Optional;


@Service
public class CommentServiceImpl implements ICommentService {
//    @Autowired
//    UserRepository userRepository;
    @Autowired
CommentRepository commentRepository;
    @Autowired
    IArticleService articleService;
    @Autowired
    IUserService userService;
    @Override
    public Comment add(Comment comment) {
        if(StringUtils.isEmpty(comment.getUsername())&&comment.getUserId()!=null){
            Optional<User> user = userService.findOptionalBy(comment.getUserId());
            if(!user.isPresent()){
                throw new ObjectException("用户对象没有找到,不存在不能添加评论!");
            }
            comment.setUsername(user.get().getUsername());
            comment.setEmail(user.get().getEmail());
        }

        Article article = articleService.findArticleById(comment.getArticleId());
        if(!article.getOpenComment()){
            throw new OptionException("文章没有打开评论,不能添加!");
        }
        commentRepository.save(comment);
        articleService.updateCommentNum(comment.getArticleId(),1);
//        Optional<User> userOptional = userRepository.findById(uid);

//        Optional<Article> articleOptional = articleRepository.findById(aid);
        return comment;
    }

    @Override
    public void deleteById(int id) {
        Comment comment = findById(id);
        commentRepository.deleteById(id);
        articleService.updateCommentNum(comment.getArticleId(),-1);
    }

    @Override
    public Comment update(int id, Comment updateComment) {
        return null;
    }

    @Override
    public Page<CommentVo> listVoBy(int articleId){
        //TODO 这里需要从数据库设置
        Page<Comment> comments = pageBy(articleId, PageRequest.of(0, 100, Sort.by(Sort.Order.desc("id"))));

        return convertTo(comments);
    }

    @Override
    public Page<CommentVo> pageVoBy(int articleId, Pageable pageable){
        return convertTo(pageBy(articleId,pageable));
    }

    public Page<CommentVo> convertTo(Page<Comment> commentPage){
        return  commentPage.map(comment -> {
            CommentVo commentVo = new CommentVo();
            BeanUtils.copyProperties(comment,commentVo);
            return commentVo;
        });
    }


    @Override
    public Page<Comment> pageBy(int articleId, Pageable pageable){
//        Comment comment = new Comment();
//        comment.setResourceId(id);
//        comment.setCommentType(commentType);
        Specification<Comment> specification = new Specification<Comment>() {
            @Override
            public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("articleId"),articleId)
                                           ).getRestriction();
            }
        };

        return commentRepository.findAll(specification,pageable);
    }

    @Override
    public Page<CommentDto> pageDtoBy(int articleId, Pageable pageable) {
        return pageBy(articleId,pageable).map(comment -> {
            CommentDto commentDto = new CommentDto();
            BeanUtils.copyProperties(comment,commentDto);
            return commentDto;
        });
    }




    @Override
    public Comment findById(int id) {
        Optional<Comment> comment = commentRepository.findById(id);
        if(!comment.isPresent()){
            throw  new ObjectException("Comment 对象不存在！！");
        }
        return comment.get();
    }



    @Override
    public Page<Comment> list(Pageable pageable) {
        return null;
    }
}
