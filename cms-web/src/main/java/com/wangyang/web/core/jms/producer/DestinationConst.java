package com.wangyang.web.core.jms.producer;

import org.apache.activemq.command.ActiveMQQueue;

import javax.jms.Destination;

public class DestinationConst {
    public static final Destination ARTICLE_HTML = new ActiveMQQueue("ARTICLE_HTML");
    public static final String ARTICLE_HTML_STRING = "ARTICLE_HTML";
}
