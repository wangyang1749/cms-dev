package com.wangyang.web.config;

import org.springframework.context.annotation.Configuration;

@Configuration
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
