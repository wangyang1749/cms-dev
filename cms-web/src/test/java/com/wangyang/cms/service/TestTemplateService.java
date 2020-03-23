package com.wangyang.cms.service;

import com.wangyang.cms.pojo.entity.Template;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class TestTemplateService extends AbstractServiceTest {

    @Test
    public void testFindByEnName(){
        Optional<Template> template = templateService.findOptionalByEnName("DEFAULT_CATEGORY");

        System.out.println(template.get());
    }
}
