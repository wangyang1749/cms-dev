package com.wangyang.cms.service.impl;

import com.wangyang.cms.expection.ObjectException;
import com.wangyang.cms.pojo.entity.Article;
import com.wangyang.cms.pojo.entity.Comment;
import com.wangyang.cms.pojo.enums.CommentType;
import com.wangyang.cms.pojo.vo.CommentVo;
import com.wangyang.cms.repository.ArticleRepository;
import com.wangyang.cms.repository.CommentRepository;
import com.wangyang.cms.service.ICommentService;
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
    ArticleRepository articleRepository;
    @Override
    public Comment add(Comment comment) {

        commentRepository.save(comment);

//        Optional<User> userOptional = userRepository.findById(uid);

//        Optional<Article> articleOptional = articleRepository.findById(aid);
        return comment;
    }

    @Override
    public void deleteById(int id) {
        commentRepository.deleteById(id);
    }

    @Override
    public Comment update(int id, Comment updateComment) {
        return null;
    }

    @Override
    public Page<Comment> listByResourceId(int id, CommentType commentType){
        //TODO 这里需要从数据库设置
        return  listByResourceId(id,commentType, PageRequest.of(0,100, Sort.by(Sort.Order.desc("id"))));
    }

    @Override
    public Page<Comment> listByResourceId(int id, CommentType commentType, Pageable pageable){
//        Comment comment = new Comment();
//        comment.setResourceId(id);
//        comment.setCommentType(commentType);
        Specification<Comment> specification = new Specification<Comment>() {
            @Override
            public Predicate toPredicate(Root<Comment> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("resourceId"),id),
                                            criteriaBuilder.equal(root.get("commentType"),commentType)).getRestriction();
            }
        };

        return commentRepository.findAll(specification,pageable);
    }

    @Override
    public Page<CommentVo> convertCommentVo(Page<Comment> commentPage){
        return commentPage.map(comment -> {
            CommentVo commentVo = new CommentVo();
            BeanUtils.copyProperties(comment,commentVo);
            return commentVo;
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
