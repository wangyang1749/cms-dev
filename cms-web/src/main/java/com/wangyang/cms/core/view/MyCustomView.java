package com.wangyang.cms.core.view;

import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

public class MyCustomView implements View {

    @Override
    public void render(Map<String, ?> map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("这是我自定义的视图");
        response.getWriter().write(UUID.randomUUID().toString());
    }

    @Override
    public String getContentType() {
        //相当于response.setContextType()
        return "text/html;charset=utf-8";
    }
}
