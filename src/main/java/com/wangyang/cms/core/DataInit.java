package com.wangyang.cms.core;

import com.wangyang.cms.pojo.entity.Template;
import com.wangyang.cms.pojo.entity.User;
import com.wangyang.cms.pojo.enums.TemplateType;
import com.wangyang.cms.service.ITemplateService;
import com.wangyang.cms.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class DataInit  implements CommandLineRunner {

    @Autowired
    IUserService userService;

    @Autowired
    ITemplateService templateService;

    @Override
    public void run(String... args) throws Exception {
        log.info("### load user");
        User findUser = userService.findByUserName("wangyang");
        if(findUser==null){
            log.info(">>> init user wangyang");
            User user = new User();
            user.setUsername("wangyang");
            user.setPassword("123456");
            userService.save(user);
            List<Template> templates = Arrays.asList(
                    new Template(1,"DEFAULT INDEX","@index", TemplateType.ARTICLE),
                    new Template(2,"DEFAULT CATEGORY","@category", TemplateType.CATEGORY),
                    new Template(3,"REVEAL","@reveal", TemplateType.ARTICLE)
                   );
            templateService.saveAll(templates);

        }
    }
}
