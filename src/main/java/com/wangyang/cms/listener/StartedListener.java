package com.wangyang.cms.listener;

import com.wangyang.cms.pojo.support.CmsConst;
import com.wangyang.cms.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;

@Slf4j
@Configuration
public class StartedListener implements ApplicationListener<ApplicationStartedEvent> {

    @Value("${cms.workDir}")
    private String workDir;

    @Value("${spring.resources.static-locations}")
    private String staticResourceLocations;

//    @Value("${spring.thymeleaf.prefix}")
//    private String templateLocations;
    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        initCms();
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
