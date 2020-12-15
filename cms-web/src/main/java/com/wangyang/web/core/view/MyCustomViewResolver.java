package com.wangyang.web.core.view;

import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

@Component
public class MyCustomViewResolver implements ViewResolver, Ordered {
    //保存当前视图解析器的优先级
    private int order = Integer.MAX_VALUE;
    /**
     * 将ModelAndView 中视图名解析成View对象返回
     */
    @Override
    public View resolveViewName(String ViewName, Locale arg1) throws Exception {
        // TODO Auto-generated method stub
        //如果ModelAndView的视图名字是以download:或sql:开头的，那么创建一个 MyCustomView()视图对象
//        if(ViewName.startsWith("@cms")){
//            //返回视图对象，该视图对象为自定义的
//
//        }
        return new MyCustomView(ViewName);
//        return null;
    }

    @Override
    public int getOrder() {
        // TODO Auto-generated method stub
        return 1;
    }
    public void setOrder(Integer order){
        this.order = order;
    }
}
