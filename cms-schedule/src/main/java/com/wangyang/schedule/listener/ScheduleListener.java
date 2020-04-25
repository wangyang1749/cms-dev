package com.wangyang.schedule.listener;

import com.wangyang.data.service.ISysTaskService;
import com.wangyang.model.pojo.entity.SysTask;
import com.wangyang.model.pojo.enums.ScheduleStatus;
import com.wangyang.schedule.util.QuartzUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
public class ScheduleListener  implements ApplicationListener<ApplicationStartedEvent> {

    @Autowired
    private QuartzUtils quartzUtils;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        log.info("初始化任务");
        List<SysTask> taskList = quartzUtils.list();
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

}
