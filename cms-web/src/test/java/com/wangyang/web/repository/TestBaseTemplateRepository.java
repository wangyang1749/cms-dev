package com.wangyang.web.repository;

import com.wangyang.service.repository.BaseTemplateRepository;
import com.wangyang.pojo.entity.base.BaseTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestBaseTemplateRepository {

    @Autowired
    BaseTemplateRepository baseTemplateRepository;

    @Test
    public void test(){
        List<BaseTemplate> all = baseTemplateRepository.findAll();

        System.out.println(all);
    }

}
