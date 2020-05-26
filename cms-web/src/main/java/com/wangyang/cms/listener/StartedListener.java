package com.wangyang.cms.listener;

import com.wangyang.data.service.IOptionService;
import com.wangyang.data.service.ISheetService;
import com.wangyang.data.service.ITagsService;
import com.wangyang.data.service.impl.PermissionServiceImpl;
import com.wangyang.model.pojo.dto.PermissionDto;
import com.wangyang.model.pojo.dto.TagsDto;
import com.wangyang.model.pojo.entity.Components;
import com.wangyang.model.pojo.entity.Option;
import com.wangyang.model.pojo.entity.Tags;
import com.wangyang.model.pojo.enums.PropertyEnum;
import com.wangyang.model.pojo.enums.TemplateType;
import com.wangyang.data.repository.ComponentsRepository;
import com.wangyang.data.repository.TemplateRepository;
import com.wangyang.data.cache.StringCacheStore;
import com.wangyang.model.pojo.entity.Template;
import com.wangyang.common.CmsConst;
import com.wangyang.model.pojo.support.TemplateOption;
import com.wangyang.model.pojo.support.TemplateOptionMethod;
import com.wangyang.common.utils.FileUtils;
import com.wangyang.common.utils.ServiceUtil;
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
    ITagsService tagsService;

    @Autowired
    IOptionService optionService;
//    @Autowired
//    IUserService userService;

    @Autowired
    ISheetService sheetService;
    @Autowired
    StringCacheStore stringCacheStore;
    @Autowired
    PermissionServiceImpl permissionService;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        initCms();
        initDatabase(applicationStartedEvent);

        log.info("加载用户权限！！");
        List<PermissionDto> permissionDtos = permissionService.listAll();
        permissionDtos.forEach(permissionDto -> {
            log.info(permissionDto.getUrl()+"需要权限"+permissionDto.getEnName());
        });
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

       List<Tags> tags = Arrays.asList(new Tags(CmsConst.TAGS_INFORMATION,CmsConst.TAGS_INFORMATION),new Tags(CmsConst.TAGS_RECOMMEND,CmsConst.TAGS_RECOMMEND));
       tags.forEach(tag->{
           Tags saveTag = tagsService.add(tag);
       });
//        log.info(">>> init user wangyang");
//        User user = new User();
//        user.setUsername("wangyang");
//        user.setPassword("123456");
//        userService.save(user);
        List<Option> options = new ArrayList<>();
        List<Template> templates = Arrays.asList(
                new Template("默认的文章模板",CmsConst.DEFAULT_ARTICLE_TEMPLATE,"templates/@article", TemplateType.ARTICLE,1),
                new Template("默认的文章栏目模板",CmsConst.DEFAULT_ARTICLE_CHANNEL_TEMPLATE, "templates/@articleChannel", TemplateType.ARTICLE,2),
                new Template("默认的图片文章模板",CmsConst.DEFAULT_ARTICLE_PICTURE_TEMPLATE, "templates/@articlePicture", TemplateType.ARTICLE,3),
                new Template("默认的pdf导出文章预览模板",CmsConst.DEFAULT_ARTICLE_PDF_TEMPLATE, "templates/@articlePDF", TemplateType.ARTICLE,3),

                //new Template("文章预览模板",CmsConst.DEFAULT_ARTICLE_PREVIEW_TEMPLATE, "templates/@articlePreview", TemplateType.ARTICLE,3),


                new Template("默认的分类模板",CmsConst.DEFAULT_CATEGORY_TEMPLATE,"templates/@category", TemplateType.CATEGORY,4),
                new Template("默认的栏目模板",CmsConst.DEFAULT_CHANNEL_TEMPLATE, "templates/@channel", TemplateType.CATEGORY,5),
                new Template("默认的图片分类模板",CmsConst.DEFAULT_PICTURE_TEMPLATE, "templates/@picture", TemplateType.CATEGORY,6),
                new Template("默认的幻灯片列表模板",CmsConst.DEFAULT_REVEAL_TEMPLATE, "templates/@reveal", TemplateType.CATEGORY,7),


                new Template("默认分类列表",CmsConst.DEFAULT_CATEGORY_LIST, "templates/@categoryList", TemplateType.CATEGORY_LIST,7),

                new Template("默认的页面模板",CmsConst.DEFAULT_SHEET_TEMPLATE, "templates/sheet/@sheet", TemplateType.SHEET,8),
                new Template("默认的评论模板",CmsConst.DEFAULT_COMMENT_TEMPLATE, "templates/@comment", TemplateType.COMMENT,9),

                new Template("基于AJAX分页的分类模板","CATEGORY_PAGE","templates/@categoryPage", TemplateType.CATEGORY,10),
                new Template("文章幻灯片模板","REVEAL","templates/@articleReveal", TemplateType.ARTICLE,11),

                new Template("文章列表(热门文章)",CmsConst.ARTICLE_LIST,"templates/@articleList", TemplateType.ARTICLE_LIST,12),
                new Template("文章列表分页",CmsConst.ARTICLE_PAGE,"templates/@articlePage", TemplateType.ARTICLE_LIST,12)

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
        componentsList.add( new Components("Carousel","components","templates/components/@carousel","carousel",CmsConst.ARTICLE_DATA,"",true));
        componentsList.add( new Components("myArticle","components","templates/components/@myArticle","myArticle",CmsConst.ARTICLE_DATA,"",true));
        componentsList.add( new Components("点赞最多","components","templates/components/@articleListAndLike","likeArticle",CmsConst.ARTICLE_DATA_SORT+"likes,DESC","",true));
        componentsList.add( new Components("热门文章","components","templates/components/@articleListAndVisit","hotArticle",CmsConst.ARTICLE_DATA_SORT+"visits,DESC","",true));
        componentsList.add( new Components("当下流行","components","templates/components/@articleListAndVisit","keyWordArticle",CmsConst.ARTICLE_DATA_KEYWORD+"R语言","",true));
        componentsList.add( new Components("最新文章","components","templates/components/@articleListAndVisit","newArticleIndex",CmsConst.ARTICLE_DATA_SORT+"createDate,DESC","",true));
        componentsList.add( new Components("推荐标签","components","templates/components/@articleListAndVisit","recommendArticle",CmsConst.ARTICLE_DATA_TAGS+"推荐","",true));
        componentsList.add( new Components("自定义组件","components","自定义HTML内容","myHtml","","",true));

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
