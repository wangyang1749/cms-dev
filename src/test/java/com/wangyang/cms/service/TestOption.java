package com.wangyang.cms.service;

import com.wangyang.cms.pojo.support.CmsConst;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestOption
{
    @Autowired
    IOptionService optionService;

    @Test
    public void test1(){
        String value = optionService.getValue(CmsConst.INIT_STATUS);
        System.out.println(value);
    }

}
