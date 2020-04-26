package com.wangyang.schedule.listener;

import com.wangyang.common.utils.ServiceUtil;
import com.wangyang.data.service.ISysTaskService;
import com.wangyang.model.pojo.entity.SysTask;
import com.wangyang.model.pojo.enums.ScheduleStatus;
import com.wangyang.model.pojo.support.TemplateOption;
import com.wangyang.schedule.util.ArticleJob;
import com.wangyang.schedule.util.ArticleJobAnnotation;
import com.wangyang.schedule.util.QuartzUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration
@Slf4j
public class ScheduleListener  implements ApplicationListener<ApplicationStartedEvent> {

    @Autowired
    private QuartzUtils quartzUtils;
    @Autowired
    private  ISysTaskService sysTaskService;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        log.info("初始化任务");
        List<SysTask> taskList = quartzUtils.list();

        initTask(taskList,applicationStartedEvent);
        taskList.forEach(task->{
            try {
                // 如果数据库是运行的, 程序启动运行
                if(task.getScheduleStatus().equals(ScheduleStatus.RUNNING)){
                    quartzUtils.startJob(task);
                    log.info("运行任务："+task.getJobName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initTask( List<SysTask> taskList,ApplicationStartedEvent applicationStartedEvent){

        List<SysTask> sysTasks  = new ArrayList<>();

        Method[] methods = ArticleJob.class.getDeclaredMethods();
        for (Method method : methods){
            if(method.isAnnotationPresent(ArticleJobAnnotation.class)){
                Annotation[] annotations = method.getAnnotations();
                for(Annotation annotation : annotations){
                    if(annotation instanceof  ArticleJobAnnotation){
                        ArticleJobAnnotation articleJobAnnotation = (ArticleJobAnnotation)annotation;
                        String methodName = method.getName();
//                        System.out.println(methodName);
                        SysTask sysTask = new SysTask();

                        sysTask.setMethodName(method.getName());
                        sysTask.setJobName(articleJobAnnotation.jobName());
                        sysTask.setJobGroup(articleJobAnnotation.jobGroup());
                        sysTask.setBeanClass(articleJobAnnotation.beanClass());
                        sysTask.setDescription(articleJobAnnotation.description());
                        sysTask.setScheduleStatus(articleJobAnnotation.scheduleStatus());
                        sysTask.setCornExpression(articleJobAnnotation.cornExpression());

                        sysTasks.add(sysTask);
                    }
                }
            }
        }

        Set<String> findJobMethods = ServiceUtil.fetchProperty(taskList, SysTask::getMethodName);
        Set<String> jobMethods = ServiceUtil.fetchProperty(sysTasks, SysTask::getMethodName);
        jobMethods.removeAll(findJobMethods);
        Map<String, SysTask> sysTaskMap = ServiceUtil.convertToMap(sysTasks, SysTask::getMethodName);
        jobMethods.forEach(jobMethod->{
            SysTask sysTask = sysTaskMap.get(jobMethod);
            sysTaskService.addOrUpdate(sysTask);
            log.info("添加任务"+jobMethod+"到数据库");
            try {
                if(sysTask.getScheduleStatus().equals(ScheduleStatus.RUNNING)){
                    quartzUtils.startJob(sysTask);
                    log.info("运行任务"+jobMethod);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
