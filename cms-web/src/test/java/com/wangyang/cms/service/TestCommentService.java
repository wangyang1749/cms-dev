package com.wangyang.cms.service;

import com.wangyang.cms.pojo.entity.Comment;
import org.junit.jupiter.api.Test;

public class TestCommentService extends AbstractServiceTest {

    @Test
    public void testAdd(){
        Comment comment = commentService.add(add());
        System.out.println(comment);
    }
}
