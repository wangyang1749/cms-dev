package com.wangyang;

import com.wangyang.common.thymeleaf.CmsDialect;
import com.wangyang.common.utils.HtmlTemplateEngine;
import com.wangyang.common.utils.TemplateUtil;
import org.junit.jupiter.api.Test;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Paths;
import java.util.Set;

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

        TemplateEngine templateEngine =  new TemplateEngine();
        templateEngine.addDialect(new CmsDialect(prefix));
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
        return Paths.get(this.getClass().getClassLoader().getResource("templates/test.html").getPath()).getParent().getParent().toString();
    }

    @Test
    public  void testHtml() {
        Context context = new Context();
        context.setVariable("name","zs");
//        String text = "<span th:text='${name}'>Sebastian</span>.";
        TemplateEngine templateEngine = templateEngine( getPath(), ".html");
//        TemplateEngine templateEngine = HtmlTemplateEngine.getWebInstance(getPath(),".html");
        String html = templateEngine.process("templates/test",context);
        System.out.println(html);
    }
}
