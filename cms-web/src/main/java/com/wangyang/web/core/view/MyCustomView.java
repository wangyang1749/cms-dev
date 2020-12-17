package com.wangyang.web.core.view;

import com.wangyang.common.CmsConst;
import com.wangyang.common.exception.ObjectException;
import com.wangyang.common.utils.TemplateUtil;
import com.wangyang.web.config.CmsConfig;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.View;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class MyCustomView implements View {

    private String viewName;
    public  MyCustomView(String viewName){
        this.viewName = viewName;
    }
    @Override
    public void render(Map<String, ?> map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        if(viewName.startsWith("redirect:")){
            String redirectPath = viewName.substring("redirect:".length());
            response.sendRedirect(redirectPath);
            return;
        }
        String viewNamePath = viewName.replace("_", File.separator);
        String path = CmsConst.WORK_DIR+ File.separator+viewNamePath+".html";
        Map<String,Object> map1 =  (Map<String,Object>)map;
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(),map1);
        ctx.setVariable("username",request.getAttribute("username"));
        ctx.setVariable("userId",request.getAttribute("userId"));
        ITemplateEngine templateEngine = TemplateUtil.getWebEngine();
        String[] pathArgs = viewName.split("_");
        if(!Paths.get(path).toFile().exists()&&!invokeGenerateHtml(pathArgs)){
            viewNamePath = "templates/error";
            ctx.setVariable("error","文件不存在！！！");;
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
        templateEngine.process(viewNamePath,ctx,response.getWriter());
    }

    /**
     * 文件不存在，查看GenerateHtml存是否存在生成html的方法，生成之后再用视图名称渲染
     * @see GenerateHtml
     * @param pathArgs
     */
    public boolean invokeGenerateHtml(String[] pathArgs){
        if(pathArgs.length<2){
            return false;
        }
        try {
            GenerateHtml generateHtml = CmsConfig.getBean(GenerateHtml.class);
            Method[] methods = generateHtml.getClass().getDeclaredMethods();
            for (Method method: methods){
                if(method.getName().equals(pathArgs[pathArgs.length-1])){
                    method.invoke(generateHtml,new Object[]{pathArgs});
                    return true;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public String getContentType() {
        //相当于response.setContextType()
        return "text/html;charset=utf-8";
    }
}
