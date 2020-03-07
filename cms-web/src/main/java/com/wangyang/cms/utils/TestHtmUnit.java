package com.wangyang.cms.utils;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;


public class TestHtmUnit {


    public void test() throws Exception{
        WebClient webClient = new WebClient();
        HtmlPage page = webClient.getPage(this.getClass().getClassLoader().getResource("index.html").toURI().toString());
        webClient.waitForBackgroundJavaScript(1000);
        String svg = page.getElementById("container").asXml();
        System.out.println(svg);
    }
}
