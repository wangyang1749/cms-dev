package com.wangyang.schedule.util;

import com.wangyang.pojo.enums.ScheduleStatus;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ArticleJobAnnotation {
    String jobName();
    String cornExpression();
    String beanClass() default "com.wangyang.schedule.util.QuartzJobFactory";
    String jobGroup();
    ScheduleStatus scheduleStatus() default ScheduleStatus.NONE;
    String description() default "";

}
