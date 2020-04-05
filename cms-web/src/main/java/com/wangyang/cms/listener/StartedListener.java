package com.wangyang.cms.listener;

import com.wangyang.authorize.pojo.entity.Permission;
import com.wangyang.authorize.pojo.entity.Role;
import com.wangyang.cms.cache.StringCacheStore;
import com.wangyang.cms.pojo.entity.Option;
import com.wangyang.cms.pojo.entity.Template;
import com.wangyang.cms.pojo.entity.Components;
import com.wangyang.cms.pojo.enums.PropertyEnum;
import com.wangyang.cms.pojo.enums.TemplateType;
import com.wangyang.cms.pojo.params.SheetParam;
import com.wangyang.cms.pojo.support.CmsConst;
import com.wangyang.cms.pojo.support.TemplateOption;
import com.wangyang.cms.pojo.support.TemplateOptionMethod;
import com.wangyang.cms.repository.ComponentsRepository;
import com.wangyang.cms.repository.TemplateRepository;
import com.wangyang.cms.service.*;
import com.wangyang.cms.utils.FileUtils;
import com.wangyang.cms.utils.ServiceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
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
    ComponentsRepository componentsRepository;

    @Autowired
    TemplateRepository templateRepository;

    @Autowired
    IOptionService optionService;
//    @Autowired
//    IUserService userService;

    @Autowired
    ISheetService sheetService;
    @Autowired
    StringCacheStore stringCacheStore;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        initCms();
        initDatabase(applicationStartedEvent);
//        if(!isInit()){
//            log.info("### init database!!!");
//            initDatabase(applicationStartedEvent);
//        }else {
//            log.info("### database already init!!!");
//        }

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

//        log.info(">>> init user wangyang");
//        User user = new User();
//        user.setUsername("wangyang");
//        user.setPassword("123456");
//        userService.save(user);
        List<Option> options = new ArrayList<>();
        List<Template> templates = Arrays.asList(
                new Template("默认的文章模板",CmsConst.DEFAULT_ARTICLE_TEMPLATE,"templates/@article", TemplateType.ARTICLE),
                new Template("默认的文章栏目模板",CmsConst.DEFAULT_ARTICLE_CHANNEL_TEMPLATE, "templates/@articleChannel", TemplateType.ARTICLE_CHANNEL),
                new Template("默认的分类模板",CmsConst.DEFAULT_CATEGORY_TEMPLATE,"templates/@category", TemplateType.CATEGORY),
                new Template("默认的栏目模板",CmsConst.DEFAULT_CHANNEL_TEMPLATE, "templates/@channelSheetList", TemplateType.CHANNEL),
                new Template("默认的页面模板",CmsConst.DEFAULT_SHEET_TEMPLATE, "templates/sheet/@sheet", TemplateType.SHEET),
                new Template("默认的评论模板",CmsConst.DEFAULT_COMMENT_TEMPLATE, "templates/@comment", TemplateType.COMMENT),
                new Template("基于AJAX分页的分类模板","CATEGORY_PAGE","templates/@categoryPage", TemplateType.CATEGORY),
                new Template("文章幻灯片模板","REVEAL","templates/@reveal", TemplateType.ARTICLE)

        );
        List<Template> findTemplates = templateRepository.findAll();
        Set<String> findTemplateName = ServiceUtil.fetchProperty(findTemplates, Template::getEnName);

        Set<String> templateNames = ServiceUtil.fetchProperty(templates, Template::getEnName);
        Map<String, Template> templateMap = ServiceUtil.convertToMap(templates, Template::getEnName);
        templateNames.removeAll(findTemplateName);
        if(!CollectionUtils.isEmpty(templateNames)){
            templateNames.forEach(name->{
                Template template = templateRepository.save(templateMap.get(name));
//                if(template.getName().equals("DEFAULT_ARTICLE")){
//                    options.add(new Option(PropertyEnum.DEFAULT_ARTICLE_TEMPLATE_ID.getValue(),String.valueOf(template.getId())));
//                }
                log.info("添加 Template ["+name+"] ");
            });
        }

        List<Components> componentsList = new ArrayList<>();
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
                            componentsList.add(components);
//                            componentsService.add(components);
                        }
                    }

                }
            }
        });
        List<Components> findComponents = componentsRepository.findAll();
        Set<String> findName = ServiceUtil.fetchProperty(findComponents, Components::getName);

        Set<String> componentsName = ServiceUtil.fetchProperty(componentsList, Components::getName);
        Map<String, Components> componentsMap = ServiceUtil.convertToMap(componentsList, Components::getName);
        componentsName.removeAll(findName);
        if(!CollectionUtils.isEmpty(componentsName)){
            componentsName.forEach(name->{
                componentsRepository.save(componentsMap.get(name));
                log.info("添加 Components ["+name+"] ");
            });
        }

        List<Option> findOption = optionService.list();
        findOption.forEach(option -> {
            stringCacheStore.setValue(option.getKey(),option.getValue());
        });
        Set<String> findOptionName = ServiceUtil.fetchProperty(findOption, Option::getKey);
        Set<String> optionsName = new HashSet<>();
        for (PropertyEnum propertyEnum: PropertyEnum.values()){
            optionsName.add(propertyEnum.name());
        }
        optionsName.removeAll(findOptionName);
        if(!CollectionUtils.isEmpty(optionsName)){
            optionsName.forEach(name->{
                options.add(new Option(name,PropertyEnum.valueOf(name).getDefaultValue(),PropertyEnum.valueOf(name).getName(),PropertyEnum.valueOf(name).getGroupId()));
            });

        }

        if(!CollectionUtils.isEmpty(options)){
            options.forEach(option -> {
                optionService.save(option);
                stringCacheStore.setValue(option.getKey(),option.getValue());
                log.info("添加 option key:"+option.getKey()+",value:"+option.getValue());
            });

        }


//        optionService.save(new Option(CmsConst.INIT_STATUS,"true"));
//        log.info("###! all template init success!!");
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
