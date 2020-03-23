package com.wangyang.cms.core.jms.consumer;

import com.wangyang.cms.core.jms.producer.DestinationConst;
import com.wangyang.cms.service.IHtmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;


@Component
public class ComponentsConsumerService {

    @Autowired
    IHtmlService htmlService;

    @JmsListener(destination = DestinationConst.ARTICLE_SHOW_LATEST_STRING)
    public void commonTemplate(String option){
        htmlService.commonTemplate(option);
    }
}
