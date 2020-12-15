package com.wangyang.web.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//import org.springframework.web.servlet.ViewResolver;
//import org.thymeleaf.spring5.SpringTemplateEngine;
//import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
//import org.thymeleaf.spring5.view.ThymeleafViewResolver;
//import org.thymeleaf.templateresolver.StringTemplateResolver;


@Configuration
@Slf4j
public class CmsConfig implements ApplicationContextAware {

//    @Bean
//    public ViewResolver myViewResolver(){
//        MyCustomViewResolver myCustomViewResolver = new MyCustomViewResolver();
//        myCustomViewResolver.setOrder(1);
//        return myCustomViewResolver;
//    }
    @Value("${cms.workDir}")
    private String workDir;

    private static ApplicationContext applicationContext;
    @Override
    public  void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }

    public static <T> T  getBean(Class<T> clazz) {
        return applicationContext != null?applicationContext.getBean(clazz):null;
    }
    public static Object  getBean(String name) {
        return applicationContext != null?applicationContext.getBean(name):null;
    }


//    @Bean
//    public TaskExecutor taskExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        // 设置核心线程数
//        executor.setCorePoolSize(5);
//        // 设置最大线程数
//        executor.setMaxPoolSize(10);
//        // 设置队列容量
//        executor.setQueueCapacity(20);
//        // 设置线程活跃时间（秒）
//        executor.setKeepAliveSeconds(60);
//        // 设置默认线程名称
//        executor.setThreadNamePrefix("hello-");
//        // 设置拒绝策略
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//        // 等待所有任务结束后再关闭线程池
//        executor.setWaitForTasksToCompleteOnShutdown(true);
//        return executor;
//    }


    /**
     * 路径特殊字符允许
     * @return
     */
    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> connector.setProperty("relaxedQueryChars", "^+|{}[]\\"));
        return factory;
    }

//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.addAllowedOrigin("*"); // e.g. http://domain1.com
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
//
//        source.registerCorsConfiguration("/api/**", config);
//        return new CorsFilter(source);
//    }
//    @Bean
//    public WebMvcConfigurer corsConfigurer()
//    {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**").
//                allowedOrigins("*"). //允许跨域的域名，可以用*表示允许任何域名使用
//                allowedMethods("*"). //允许任何方法（post、get等）
//                allowedHeaders("*"). //允许任何请求头
//                allowCredentials(true). //带上cookie信息
//                exposedHeaders(HttpHeaders.SET_COOKIE).maxAge(3600L);
//            }
//        };
//    }
//    @Bean
//    public ViewResolver thymeleafViewResolver(){
//        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
//        viewResolver.setTemplateEngine(templateEngine());
//        viewResolver.setCache(false);
//        viewResolver.setCharacterEncoding("UTF-8");
//        return viewResolver;
//    }


    /**
     * https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#chaining-template-resolvers
     * @return templateEngine
     */
//    @Bean
//    public SpringTemplateEngine templateEngine(){
//        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//        templateEngine.addDialect(new CmsDialect());
//
//
//        SpringResourceTemplateResolver springResourceTemplateResolver = new SpringResourceTemplateResolver();
//        springResourceTemplateResolver.setPrefix("file:"+workDir+"/");
//        springResourceTemplateResolver.setSuffix(".html");
//        springResourceTemplateResolver.setCacheable(false); //set cache
//        springResourceTemplateResolver.setCharacterEncoding("UTF-8");
//        springResourceTemplateResolver.setApplicationContext(this.applicationContext);
//        springResourceTemplateResolver.setOrder(Integer.valueOf(1));
//        springResourceTemplateResolver.getResolvablePatternSpec().addPattern("templates/*");
//        springResourceTemplateResolver.getResolvablePatternSpec().addPattern("html/*");
//
////
////        SpringResourceTemplateResolver springResourceTemplateResolver1 = new SpringResourceTemplateResolver();
////        springResourceTemplateResolver1.setPrefix("classpath:/");
//////        springResourceTemplateResolver.setPrefix("file:"+workDir+"/");
////        springResourceTemplateResolver1.setSuffix(".html");
////        springResourceTemplateResolver1.setCacheable(false); //set cache
////        springResourceTemplateResolver1.setCharacterEncoding("UTF-8");
////        springResourceTemplateResolver1.setApplicationContext(this.applicationContext);
////        springResourceTemplateResolver1.setOrder(Integer.valueOf(1));
////        springResourceTemplateResolver1.getResolvablePatternSpec().addPattern("templates/*");
////        springResourceTemplateResolver1.getResolvablePatternSpec().addPattern("html/*");
////
//
//        //字符串模板 SpringResourceTemplateResolver不能同时出现
//        StringTemplateResolver stringTemplateResolver = new StringTemplateResolver();
//        springResourceTemplateResolver.setCharacterEncoding("UTF-8");
//        // 设置字符串模板优先级
//        stringTemplateResolver.setOrder(Integer.valueOf(4));
//
//
//        // 添加字符串模板
//        templateEngine.addTemplateResolver(springResourceTemplateResolver);
////        templateEngine.addTemplateResolver(stringTemplateResolver);
//
//        return templateEngine;
//    }
}
