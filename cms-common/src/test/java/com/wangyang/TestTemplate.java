package com.wangyang;

import com.wangyang.common.thymeleaf.CmsWebDialect;
import org.junit.jupiter.api.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.io.File;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangyang
 * @date 2020/12/14
 */
public class TestTemplate {
    /**
     * https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#chaining-template-resolvers
     * @return templateEngine
     */
    public TemplateEngine templateEngine(String prefix,String suffix){
//        CmsFileDialect.createStandardProcessorsSet("cms");
//        CmsWebDialectTemp cmsWebDialectTemp = new CmsWebDialectTemp();
//        cmsFileDialect.set("cms");
        TemplateEngine templateEngine =  new TemplateEngine();
        templateEngine.addDialect(new CmsWebDialect());
//        standardDialect.
//        templateEngine.addDialect();
//        templateEngine.setDialect(new SpringStandardDialect());
        StringTemplateResolver stringTemplateResolver = new StringTemplateResolver();

        FileTemplateResolver fileTemplateResolver = new FileTemplateResolver();

        fileTemplateResolver.setOrder(Integer.valueOf(2));
        fileTemplateResolver.setPrefix(prefix+ File.separator);
        fileTemplateResolver.setSuffix(suffix);
        stringTemplateResolver.setOrder(Integer.valueOf(1));
        stringTemplateResolver.getResolvablePatternSpec().addPattern("str:");
        // 添加字符串模板
        templateEngine.addTemplateResolver(stringTemplateResolver);
        templateEngine.addTemplateResolver(fileTemplateResolver);
        return templateEngine;
    }

    public String getPath(){
        return Paths.get(this.getClass().getClassLoader().getResource("templates/test2.html").getPath()).getParent().getParent().toString();
    }

    @Test
    public  void testHtml() {
        Context context = new Context();
        context.setVariable("name","zs");
//        String text = "<span th:text='${name}'>Sebastian</span>.";
        TemplateEngine templateEngine = templateEngine( getPath(), ".html");
//        TemplateEngine templateEngine = HtmlTemplateEngine.getWebInstance(getPath(),".html");
        String html = templateEngine.process("templates/test2",context);
        System.out.println(html);
    }

    private static String varPattern = "(\\$\\{(.*?)})";
    private static Pattern rv = Pattern.compile(varPattern);
    private String str = "@{'~/html/components/'+${item.viewName}+'.html'}";
//    @Test
    public void test1(){

        Matcher matcher = rv.matcher(str);
        while (matcher.find()){
            String attr = matcher.group(1);
            String replaceAll = matcher.replaceAll("111");
//            matcher.replaceAll(attrS);
            System.out.println(replaceAll);
        }
        System.out.println(str);

    }

    private  String str2 = "~{'/'+${view.category.path}+'/top/'+${view.category.viewName}} ?:_";
    private static String varPattern2 = "\\~\\{(.*)}";
    private static Pattern rv2 = Pattern.compile(varPattern2);
//    @Test
    public void test2(){

        Matcher matcher = rv2.matcher(str2);
        while (matcher.find()){
            String attr = matcher.group(1);
//            String replaceAll = matcher.replaceAll("111");
//            matcher.replaceAll(attrS);
            System.out.println(attr);
        }
        System.out.println(str);

    }
}
