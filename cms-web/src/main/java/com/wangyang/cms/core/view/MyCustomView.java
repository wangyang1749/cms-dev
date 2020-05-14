package com.wangyang.cms.core.view;

import com.wangyang.common.utils.FileUtils;
import com.wangyang.common.utils.TemplateUtil;
import com.wangyang.model.pojo.entity.base.BaseTemplate;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

public class MyCustomView implements View {


    @Override
    public void render(Map<String, ?> map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        Map<String,Object> objectMap = (Map<String,Object>)map;
        BaseTemplate baseTemplate = (BaseTemplate) map.get("template");
        String html = TemplateUtil.convertHtmlAndPreview(objectMap, baseTemplate);
        String convertHtml = FileUtils.convertByString(html, request);
        response.getWriter().write(convertHtml);
    }

    @Override
    public String getContentType() {
        //相当于response.setContextType()
        return "text/html;charset=utf-8";
    }
}
