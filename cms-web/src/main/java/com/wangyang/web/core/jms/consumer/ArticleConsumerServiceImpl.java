package com.wangyang.web.core.jms.consumer;

import com.wangyang.web.core.jms.producer.DestinationConst;
import com.wangyang.service.service.IHtmlService;
import com.wangyang.pojo.vo.ArticleDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class ArticleConsumerServiceImpl {

    @Autowired
    IHtmlService htmlService;

//    @JmsListener(destination = DestinationConst.ARTICLE_HTML_STRING)
    public void receiveArticleDetailVO(ArticleDetailVO articleVO){
        htmlService.conventHtml(articleVO);
    }

}
