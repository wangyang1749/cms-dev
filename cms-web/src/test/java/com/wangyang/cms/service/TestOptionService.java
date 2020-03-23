package com.wangyang.cms.service;

import com.wangyang.cms.pojo.entity.Option;
import com.wangyang.cms.pojo.enums.PropertyEnum;
import com.wangyang.cms.pojo.support.CmsConst;
import com.wangyang.cms.repository.OptionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class TestOptionService extends AbstractServiceTest
{

    @Test
    public void test(){
        Integer value = optionService.getPropertyIntegerValue(PropertyEnum.CATEGORY_PAGE_SIZE);
        System.out.println(value);
    }
}
