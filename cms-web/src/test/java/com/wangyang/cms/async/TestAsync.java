package com.wangyang.cms.async;

import com.wangyang.data.event.HtmlListener;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestAsync {

    @Autowired
    HtmlListener htmlListener;

    @Test
    public void  test1(){
        System.out.println("before");
//        htmlListener.asyn1();
        System.out.println("after");
    }
}
