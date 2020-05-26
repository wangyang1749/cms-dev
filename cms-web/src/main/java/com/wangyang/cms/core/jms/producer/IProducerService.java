package com.wangyang.cms.core.jms.producer;


import com.wangyang.model.pojo.vo.ArticleDetailVO;


public interface IProducerService {

    void sendMessage(ArticleDetailVO articleDetailVO);
}
