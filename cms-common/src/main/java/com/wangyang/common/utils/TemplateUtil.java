package com.wangyang.common.utils;


import com.wangyang.common.exception.FileOperationException;
import com.wangyang.common.exception.TemplateException;
import com.wangyang.common.thymeleaf.CmsDialect;
import com.wangyang.pojo.entity.Components;
import com.wangyang.pojo.entity.Template;
import com.wangyang.pojo.entity.base.BaseTemplate;
import com.wangyang.common.CmsConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class TemplateUtil {

    private static String workDir="";

    @Value("${cms.workDir}")
    public TemplateUtil setWorkDir(String workDir) {
        this.workDir = workDir;
        return this;
    }

    public static void deleteTemplateHtml(String oldName,String path){
        if(StringUtils.isEmpty(oldName)){
            return;
        }
        String filePath=workDir+"/"+ CmsConst.STATIC_HTML_PATH;
        if(path!=null){
            filePath=filePath+"/"+path;
        }
        File file = new File(filePath+"/"+oldName+".html");
        if(file.exists()){
            file.delete();
            log.info("### delete html file"+file.getPath());
        }
        File pdfFile = new File(filePath+"/"+oldName+".pdf");
        if(pdfFile.exists()){
            pdfFile.delete();
            log.info("### delete pdf file"+pdfFile.getPath());
        }
    }


    /**
     * 直接输入路径和视图名称生成对应的Html
     * @param path
     * @param viewName
     * @param object
     * @param template
     * @return
     */
    public static String convertHtmlAndSave(String path,String viewName,Object object, Template template){
        Assert.notNull(template,"template can't null");
        Map<String,Object> map = new HashMap<>();
        map.put("view",object);
        return convertHtmlAndSave(path,viewName,map,template);
    }

    public static String convertHtmlAndSave(String path,String viewName,Map<String,Object> map, Template template){
        Assert.notNull(template,"template can't null");
        map.put("template",template);
        map.put("isSave",true);
        Context context = new Context();
        context.setVariables(map);
        String html = getHtml(template.getTemplateValue(),context);
        saveFile(path,viewName,html);
        return html;
    }

    public static String convertHtmlAndPreview(Object object, BaseTemplate template){
        Assert.notNull(template,"template can't null");
        Map<String,Object> map;
        if(object instanceof  Map){
            map = (Map<String,Object> )object;
        }else {
            map = new HashMap<>();
            map.put("view",object);
            map.put("template",template);
        }
        map.put("isSave",true);
        Context context = new Context();
        context.setVariables(map);
        return  getHtml(template.getTemplateValue(),context);
//        saveFile(path,viewName,html);
//        return html;
    }

//    public static String convertHtmlAndSave(Map<String,Object> map, BaseTemplate template){
//        map.put("isSave",true);
//        Context context = new Context();
//        context.setVariables(map);
//        return  convertHtml(template,context,map,true);
//    }

    public static String convertHtmlAndSave(Object object, BaseTemplate template){
        Map<String,Object> map;
        if(object instanceof Map){
           map= (Map<String,Object> )object;
        }else {
            map = new HashMap<>();
            map.put("view",object);
            map.put("template",template);
        }
        map.put("isSave",true);
        Context context = new Context();
        context.setVariables(map);
        return  convertHtml(template,context,object,true);

    }


//    public static String convertHtmlAndPreviewByMap(Object object, BaseTemplate template){
//        Context context = new Context();
//        context.setVariable("view",object);
//        return  convertHtml(template,context,object,false);
//    }

    private static String getValue(Object object,String method){
        String viewName = null;
        try {
            Method[] methods = object.getClass().getDeclaredMethods();

            viewName = (String)object.getClass().getMethod(method).invoke(object);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return viewName;
    }



    public static String convertHtml(BaseTemplate baseTemplate, Context context,Object object,boolean isSaveFile) {
        Assert.notNull(baseTemplate,"template can't null");
        String  html;
        if(baseTemplate instanceof Components){
            Components templatePage = (Components) baseTemplate;
            html = getHtml(templatePage.getTemplateValue(),context);

            if(isSaveFile){
                saveFile(templatePage.getPath(),templatePage.getViewName(),html);
            }
        }else{
            Template template = (Template)baseTemplate;
            html = getHtml(template.getTemplateValue(),context);

            if(isSaveFile){
                saveFile(getValue(object,"getPath"),getValue(object,"getViewName"),html);
            }
        }
        return html;

    }

    public static String getHtml(String templateValue,Context context) {
        if(templateValue==null||"".equals(templateValue)){
            throw new TemplateException("Template value can't empty!!");
        }

        String html = getFileEngine().process(templateValue, context);
        return html;
    }

    /**
     *
     * @param needInclude 是否需要引入header
     * @see com.wangyang.common.thymeleaf.IncludeElementTagProcessor#doProcess(ITemplateContext, IProcessableElementTag, IElementTagStructureHandler) 
     * @return
     */
    public static ITemplateEngine getWebEngine() {
        TemplateEngine templateEngine = HtmlTemplateEngine.getWebInstance(workDir, ".html");
        return templateEngine;
    }
    /**
     *
     * @param needInclude 是否需要引入header
     * @see com.wangyang.common.thymeleaf.IncludeElementTagProcessor#doProcess(ITemplateContext, IProcessableElementTag, IElementTagStructureHandler)
     * @return
     */
    public static ITemplateEngine getFileEngine() {
        TemplateEngine templateEngine = HtmlTemplateEngine.getFileInstance(workDir, ".html");
        return templateEngine;
    }

    public static String saveFile(String path,String viewName,String html) {
        if(path==null||"".equals(path)){
            path = workDir+"/"+ CmsConst.STATIC_HTML_PATH;
        }else{
            path = workDir+"/"+ CmsConst.STATIC_HTML_PATH+"/"+path;
        }

        if(viewName==null||"".equals(viewName)){
            throw new TemplateException("Template  view name can't null in template page!!");
        }
        Path savePath = Paths.get(path);
        if (Files.notExists(savePath)){
            try {
                Files.createDirectories(savePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try(FileWriter write = new FileWriter(path+"/"+viewName+".html")) {
            write.write(html);
            log.info("### Write file["+path+"/"+viewName+".html] success!!");
        } catch (IOException e) {
            throw new FileOperationException("Write html error!!");
        }
        return viewName+".html";
    }

    public static boolean componentsExist(String viewName){
        String path = CmsConst.WORK_DIR+"/html/"+CmsConst.COMPONENTS_PATH+"/"+viewName+".html";
        File file = new File(path);
        return file.exists();
    }

}
