package com.wangyang.cms.utils;

import com.wangyang.cms.expection.FileOperationException;
import com.wangyang.cms.expection.TemplateException;
import com.wangyang.cms.pojo.entity.Article;
import com.wangyang.cms.pojo.entity.Template;
import com.wangyang.cms.pojo.entity.TemplatePage;
import com.wangyang.cms.pojo.entity.base.BaseTemplate;
import com.wangyang.cms.pojo.support.CmsConst;
import com.wangyang.cms.pojo.vo.ArticleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class TemplateUtil {

    private static String workDir;

    private  static TemplateEngine templateEngine;

    @Value("${cms.workDir}")
    private void setWorkDir(String workDir) {
        this.workDir = workDir;
    }

    @Autowired
    private void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }


    public static String convertHtmlAndSave(Object object, BaseTemplate template){
        Context context = new Context();
        context.setVariable("view",object);
        return  convertHtml(template,context,getViewName(object),true);

    }
    public static String convertHtmlAndPreview(Object object, BaseTemplate template){
        Context context = new Context();
        context.setVariable("view",object);
        return  convertHtml(template,context,getViewName(object),false);
    }

    private static String getViewName(Object object){
        String viewName = null;
        try {
            viewName = (String)object.getClass().getMethod("getViewName").invoke(object);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return viewName;
    }



    public static String convertHtml(BaseTemplate baseTemplate, Context context,String viewName,boolean isSaveFile) {
        Assert.notNull(baseTemplate,"template can't null");
        String  html;
        if(baseTemplate instanceof TemplatePage){
            TemplatePage templatePage = (TemplatePage) baseTemplate;
            html = getHtml(templatePage.getTemplateValue(),context);

            if(isSaveFile){
                saveFile(templatePage.getPath(),templatePage.getViewName(),html);
            }
        }else{
            Template template = (Template)baseTemplate;
            html = getHtml(template.getTemplateValue(),context);

            if(isSaveFile){
                saveFile(template.getPath(),viewName,html);
            }
        }
        return html;

    }

    private static String getHtml(String templateValue,Context context) {
        if(templateValue==null||"".equals(templateValue)){
            throw new TemplateException("Template value can't empty!!");
        }
        String html;
        if(templateValue.startsWith("@")) {
            String templatePath = workDir + "/" + CmsConst.SYSTEM_TEMPLATE_PATH + "/" + templateValue + ".html";
            if (!Files.exists(Paths.get(templatePath))) {
                throw new TemplateException("Template does not exist!!");
            }
        }
        html = templateEngine.process(templateValue, context);
        return html;
    }

    private static String saveFile(String path,String viewName,String html) {
        if(path==null||"".equals(path)){
            path = workDir+"/"+ CmsConst.STATIC_HTML_PATH;
        }
        if(viewName==null||"".equals(viewName)){
            throw new TemplateException("Template  view name can't null in template page!!");
        }

        try(FileWriter write = new FileWriter(path+"/"+viewName+".html")) {
            write.write(html);
        } catch (IOException e) {
            throw new FileOperationException("Write html error!!");
        }
        return viewName+".html";
    }


}
