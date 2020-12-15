package com.wangyang.web.core.jms.producer;


import com.wangyang.pojo.vo.ArticleDetailVO;


public interface IProducerService {

    void sendMessage(ArticleDetailVO articleDetailVO);
}
