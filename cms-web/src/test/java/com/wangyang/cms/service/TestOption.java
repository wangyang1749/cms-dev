package com.wangyang.cms.service;

import com.wangyang.cms.pojo.entity.Option;
import com.wangyang.cms.pojo.support.CmsConst;
import com.wangyang.cms.repository.OptionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class TestOption
{
    @Autowired
    IOptionService optionService;

    @Autowired
    OptionRepository optionRepository;

    @Test
    public void test1(){
        String value = optionService.getValue(CmsConst.INIT_STATUS);
        System.out.println(value);
    }

    @Test
    public void test2(){
        Option option = new Option();
        option.setValue("7777777777777");
        option.setKey("aabbcc88");
        List<Option> options = Arrays.asList(option);
//        optionService.saveUpdateOptionList(options);
    }

    @Test
    public void test3(){
        Option option = new Option();
        option.setValue("7777777777777");
        option.setKey("aabbcc88555");
        option.setId(8);
//        Option save = optionRepository.save(option);
//        System.out.println(save.getId())

    }
}
