package com.wangyang.cms.util;

import com.wangyang.cms.config.CmsConfig;
import com.wangyang.cms.pojo.entity.Menu;
import com.wangyang.cms.pojo.enums.AttachmentType;
import com.wangyang.cms.pojo.support.CmsConst;
import com.wangyang.cms.pojo.support.TemplateOption;
import com.wangyang.cms.pojo.support.TemplateOptionMethod;
import com.wangyang.cms.service.IMenuService;
import com.wangyang.cms.service.impl.ArticleServiceImpl;
import com.wangyang.cms.service.impl.MenuServiceImpl;
import com.wangyang.cms.utils.DocumentUtil;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.List;

@SpringBootTest
public class TestCmsUtil {

    @Autowired
    IMenuService menuService;

    @Test
    public void Test1(){


    }

    @Test
    public  void test2(){
        String html="<div id='aa'><a>444</a>1111111111111</div>";
        System.out.println(DocumentUtil.getDivContent(html,"#aa"));
    }

    @Test
    public void test3(){
//        Object menuServiceImpl = CmsConfig.getBean("menuServiceImpl");
//        IMenuService menuService = (MenuServiceImpl)menuServiceImpl;
//        System.out.println(menuService.list());
    }

    @Test
    public void test4(){
//        try {
//            Object menuServiceImpl = CmsConfig.getBean("menuServiceImpl");
//            Method method = menuServiceImpl.getClass().getMethod("list");
//
//            Object invoke = method.invoke(menuServiceImpl);
//
//            System.out.println(invoke);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void test5(){
//        Method[] methods = ArticleServiceImpl.class.getDeclaredMethods();
//        for (Method method: methods){
//            System.out.println(method.isAnnotationPresent(TemplateOptionMethod.class));
//        }
    }

    @Test
    public void test6(){
//        ArticleServiceImpl bean = CmsConfig.getBean(ArticleServiceImpl.class);
//        Class<?> targetClass = AopUtils.getTargetClass(bean);
//        for (Method method: targetClass.getDeclaredMethods()){
//            Annotation[] annotations = method.getDeclaredAnnotations();
//            for (Annotation annotation : annotations){
//                if(annotation instanceof TemplateOptionMethod){
//                   TemplateOptionMethod tm = ((TemplateOptionMethod) annotation);
//                    System.out.println(tm.name());
//                    System.out.println(tm.viewName());
//                    System.out.println(tm.templateValue());
//                }
//            }
//            System.out.println(method.isAnnotationPresent(TemplateOptionMethod.class));
//        }
    }

    @Test
    public void test7() throws Exception{
//        InetAddress address = InetAddress.getLocalHost();
//        System.out.println(address.getHostName());//主机名
//        System.out.println(address.getCanonicalHostName());//主机别名
//        System.out.println(address.getHostAddress());//获取IP地址
//        System.out.println("===============");


    }

    @Test
    public void test9(){
//        System.out.println(AttachmentType.valueOf("LOCAL"));
    }
}
