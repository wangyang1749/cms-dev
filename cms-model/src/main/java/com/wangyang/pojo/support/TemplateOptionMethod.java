package com.wangyang.pojo.support;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(value= ElementType.METHOD)
@Documented
@Inherited
public @interface TemplateOptionMethod {
    String name();
    String templateValue();
    String viewName();
    String event() default "";
    String path() default "null";
    boolean status() default true;

}
