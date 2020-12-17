package com.wangyang.web.core.jms.producer;

import com.wangyang.pojo.vo.ArticleDetailVO;
import org.springframework.stereotype.Service;

@Service
public class ProducerServiceImpl implements IProducerService {
//    @Autowired
//    private JmsMessagingTemplate jmsTemplate;

    @Override
    public void sendMessage(ArticleDetailVO articleDetailVO) {
//        jmsTemplate.convertAndSend(DestinationConst.ARTICLE_HTML,articleDetailVO);
    }


}
