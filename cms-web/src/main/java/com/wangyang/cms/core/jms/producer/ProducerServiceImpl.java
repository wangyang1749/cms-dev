package com.wangyang.cms.core.jms.producer;

import com.wangyang.model.pojo.vo.ArticleDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerServiceImpl implements IProducerService {
    @Autowired
    private JmsMessagingTemplate jmsTemplate;

    @Override
    public void sendMessage(ArticleDetailVO articleDetailVO) {
        jmsTemplate.convertAndSend(DestinationConst.ARTICLE_HTML,articleDetailVO);
    }


}
