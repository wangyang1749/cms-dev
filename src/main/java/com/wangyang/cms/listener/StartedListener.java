package com.wangyang.cms.listener;

import com.wangyang.cms.pojo.entity.Option;
import com.wangyang.cms.pojo.entity.Template;
import com.wangyang.cms.pojo.entity.Components;
import com.wangyang.cms.pojo.entity.User;
import com.wangyang.cms.pojo.enums.TemplateType;
import com.wangyang.cms.pojo.params.SheetParam;
import com.wangyang.cms.pojo.support.CmsConst;
import com.wangyang.cms.pojo.support.TemplateOption;
import com.wangyang.cms.pojo.support.TemplateOptionMethod;
import com.wangyang.cms.service.*;
import com.wangyang.cms.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.file.*;
import java.util.*;

@Slf4j
@Configuration
public class StartedListener implements ApplicationListener<ApplicationStartedEvent> {

    @Value("${cms.workDir}")
    private String workDir;

    @Value("${spring.resources.static-locations}")
    private String staticResourceLocations;

    @Autowired
    IComponentsService componentsService;

    @Autowired
    ITemplateService templateService;

    @Autowired
    IOptionService optionService;
    @Autowired
    IUserService userService;

    @Autowired
    ISheetService sheetService;


    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        initCms();
        if(!isInit()){
            log.info("### init database!!!");
            initDatabase(applicationStartedEvent);
        }else {
            log.info("### database already init!!!");
        }

    }

    private boolean isInit() {
        String value = Optional.ofNullable(optionService.getValue(CmsConst.INIT_STATUS))
                .orElse("false");

        if(value.equals("true")){
            return true;
        }
        return false;
    }

    private void initDatabase(ApplicationStartedEvent applicationStartedEvent) {

        log.info(">>> init user wangyang");
        User user = new User();
        user.setUsername("wangyang");
        user.setPassword("123456");
        userService.save(user);

        List<Template> templates = Arrays.asList(
                new Template("DEFAULT INDEX","templates/@article", TemplateType.ARTICLE),
                new Template("DEFAULT CATEGORY","templates/@category", TemplateType.CATEGORY_INFO),
                new Template("REVEAL","templates/@reveal", TemplateType.ARTICLE),
                new Template("OTHER_SHEET","templates/sheet/@other", TemplateType.SHEET)

        );
        log.info("Template init: delete all template");
        templateService.deleteAll();

        log.info("Template init: add  template number of"+templates.size());
        templateService.saveAll(templates);


        sheetService.deleteAll();

        Template template = templateService.add(new Template(" DEFAULT SHEET", "templates/sheet/@index", TemplateType.SHEET));
        sheetService.add(new SheetParam(template.getId(),"Index","index",""));



        log.info("### Delete all [componentsService.deleteAll()]");
        componentsService.deleteAll();
        Map<String,Object> beans = applicationStartedEvent.getApplicationContext().getBeansWithAnnotation(TemplateOption.class);
        beans.forEach((k,v)->{
            Class<?> targetClass = AopUtils.getTargetClass(v);
            Method[] methods = targetClass.getDeclaredMethods();
            for (Method method: methods){
                if(method.isAnnotationPresent(TemplateOptionMethod.class)){
                    Annotation[] annotations = method.getDeclaredAnnotations();
                    for (Annotation annotation: annotations){
                        if(annotation instanceof TemplateOptionMethod){
                            TemplateOptionMethod tm = (TemplateOptionMethod) annotation;
                            String dataName = k+"."+method.getName();
                            Components components = new Components(tm.name(), tm.path(),tm.templateValue(), tm.viewName(), dataName, tm.event(), tm.status());
                            componentsService.add(components);
                        }
                    }

                }
            }
        });
        optionService.save(new Option(CmsConst.INIT_STATUS,"true"));
        log.info("###! all template init success!!");
    }

    private void initCms(){
        log.info("### WorkDir:"+workDir);
        log.info("### Static Resource Locations"+staticResourceLocations);
        log.info("### Template Resource Locations"+workDir+"/"+CmsConst.SYSTEM_TEMPLATE_PATH+"/");
        try {

            Path cmsDir = Paths.get(workDir);
            if(Files.notExists(cmsDir)){
                Files.createDirectories(cmsDir);
                log.info(">>> Not exist work directory, Create template directory "+cmsDir.toString());
                Path propertiesFile = new File(workDir+"/cms.properties").toPath();

                Path source = FileUtils.getJarResources(CmsConst.CONFIGURATION);
                Files.copy(source,propertiesFile);
                log.info(">>> copy configuration file ["+source.toString()+"] to ["+cmsDir.toString()+"]");
            }

            Path templatePath = Paths.get(workDir + "/" + CmsConst.TEMPLATE_PATH);
            if(Files.notExists(templatePath)){
                Files.createDirectories(templatePath);
                log.info(">>> Not exist template directory, Create template directory "+templatePath.toString());
                Path source = FileUtils.getJarResources(CmsConst.SYSTEM_TEMPLATE_PATH);
                FileUtils.copyFolder(source, templatePath);
                log.info(">>> copy ["+source.toString()+"] to ["+templatePath.toString()+"]");
            }

            Path htmlPath = Paths.get(workDir + "/" + CmsConst.STATIC_HTML_PATH);
            if(Files.notExists(htmlPath)){
                Files.createDirectories(htmlPath);
                log.info(">>> Not exits html directory, create html directory");
                Path source = FileUtils.getJarResources(CmsConst.SYSTEM_HTML_PATH);
                FileUtils.copyFolder(source, htmlPath);
                log.info(">>> copy ["+source.toString()+"] to ["+templatePath.toString()+"]");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
