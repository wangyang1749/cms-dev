package com.wangyang.web.jms;

import com.wangyang.web.core.jms.producer.IProducerService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class ActivemqApplicationTests {


    @Autowired
    private IProducerService producerService;

    @Test
    public void contextLoads() {
//        Destination queueDestination = new ActiveMQQueue("test_html");
//        Destination topicDestination1 =new ActiveMQTopic("mytest.topic");
//        for(int i=0; i<10; i++){
//            producer.sendMessage(queueDestination, "myname is queue!!!");
//            producer.sendMessage(topicDestination1,"myName is topic!!!");
//        }

//        producerService.sendMessage(queueDestination,"12313");
    }

    @Test
    public void test2(){
//        producerService.commonTemplate(TemplatePageType.ARTICLE);
    }
}
