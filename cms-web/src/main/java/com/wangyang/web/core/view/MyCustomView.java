package com.wangyang.web.core.view;

import com.wangyang.common.utils.TemplateUtil;
import org.springframework.web.servlet.View;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        Map<String,Object> map1 =  (Map<String,Object>)map;
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(),map1);

        ITemplateEngine templateEngine = TemplateUtil.getWebEngine();

        templateEngine.process(viewName,ctx,response.getWriter());
    }

    @Override
    public String getContentType() {
        //相当于response.setContextType()
        return "text/html;charset=utf-8";
    }
}
