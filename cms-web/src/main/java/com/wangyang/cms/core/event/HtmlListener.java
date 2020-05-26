package com.wangyang.cms.core.event;

import com.wangyang.model.pojo.entity.Article;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class HtmlListener {

    @Async
    public void asyn1()  {
        System.out.println("async run...");

    }

    @EventListener
    public void article(EntityCreatedEvent<Article> event){
        Article article = (Article) event.getSource();
        System.out.println("EventListener ");
    }
}
