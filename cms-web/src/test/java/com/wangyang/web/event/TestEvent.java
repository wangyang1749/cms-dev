package com.wangyang.web.event;


import com.wangyang.service.event.EntityCreatedEvent;
import com.wangyang.pojo.entity.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

@SpringBootTest
public class TestEvent {

    @Autowired
    private ApplicationEventPublisher publisher;


    @Test
    public void test1(){
        Article article = new Article();
        article.setTitle("123456");
        EntityCreatedEvent<Article>  entityCreatedEvent = new EntityCreatedEvent<>(article);
        publisher.publishEvent(entityCreatedEvent);
        System.out.println("sss");
    }
}
