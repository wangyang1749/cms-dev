package com.wangyang.cms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.ConnectionFactory;

//@Configuration
public class JmsConfig {

//    @Bean
//    public JmsListenerContainerFactory<?> topicListenerFactory(ConnectionFactory connectionFactory){
//        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
//        factory.setPubSubDomain(true);
//        factory.setConnectionFactory(connectionFactory);
//        //factory.setTaskExecutor(Executors.newFixedThreadPool(10));
//        //factory.setConcurrency("10");
//        return factory;
//    }
//    @Bean
//    public JmsListenerContainerFactory<?> queueListenerFactory(ConnectionFactory connectionFactory){
//        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
//        factory.setPubSubDomain(false);
//        factory.setConnectionFactory(connectionFactory);
//        return factory;
//    }



}
