package com.wangyang.schedule.util;

import com.wangyang.data.service.ISysTaskService;
import com.wangyang.model.pojo.entity.SysTask;
import com.wangyang.model.pojo.enums.ScheduleStatus;
import com.wangyang.schedule.exception.MyScheduleException;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class QuartzUtils {
    private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();

    @Autowired
    ISysTaskService sysTaskService;



    public SysTask findBy(int id){
        return sysTaskService.findBy(id);
    }

    public List<SysTask> list(){
        return sysTaskService.list();
    }

    public  SysTask startJob(Scheduler scheduler,SysTask sysTask) throws Exception {
        checkAlreadyStart(scheduler,sysTask.getJobName(),sysTask.getJobGroup());
        Class<?> clz = Class.forName(sysTask.getBeanClass());
        Class<? extends Job> jobClass =  (Class<? extends Job>)(clz.newInstance().getClass());
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(sysTask.getJobName(), sysTask.getJobGroup())
                .build();

        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(sysTask.getJobName(),sysTask.getJobGroup())
                .startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND))
                .withSchedule(CronScheduleBuilder.cronSchedule(sysTask.getCornExpression())).startNow().build();

        scheduler.scheduleJob(jobDetail,trigger);

        if(!scheduler.isShutdown()){
            scheduler.start();
        }
        return sysTask;

    }

    public  SysTask startJob(SysTask sysTask) throws Exception {
        Scheduler scheduler = schedulerFactory.getScheduler();
        return  startJob(scheduler,sysTask);
    }

    public  SysTask startJob(int id) throws Exception {
        SysTask sysTask = sysTaskService.findBy(id);
        startJob(sysTask);
        sysTask.setScheduleStatus(ScheduleStatus.RUNNING);
        return sysTaskService.addOrUpdate(sysTask);
    }

    public SysTask updateJobCron(int id,SysTask task) throws Exception {
        SysTask sysTask = sysTaskService.findBy(id);
        BeanUtils.copyProperties(task,sysTask,"id");

        Scheduler scheduler = schedulerFactory.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(sysTask.getJobName(), sysTask.getJobGroup());
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        if(trigger==null){
//            startJob(sysTask);
            throw new MyScheduleException("任务没有启动，不能更新！");

        }else {
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(sysTask.getCornExpression());
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            scheduler.rescheduleJob(triggerKey, trigger);
        }


        sysTask.setScheduleStatus(ScheduleStatus.RUNNING);
        return sysTaskService.addOrUpdate(sysTask);
    }
    public  SysTask addJob(SysTask sysTask) throws Exception {
            Scheduler scheduler = schedulerFactory.getScheduler();
            checkAlreadyStart(scheduler,sysTask.getJobName(),sysTask.getJobGroup());
            Class<?> clz = Class.forName(sysTask.getBeanClass());
            Class<? extends Job> jobClass =  (Class<? extends Job>)(clz.newInstance().getClass());
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(sysTask.getJobName(), sysTask.getJobGroup())
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(sysTask.getJobName(),sysTask.getJobGroup())
                    .startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND))
                    .withSchedule(CronScheduleBuilder.cronSchedule(sysTask.getCornExpression())).startNow().build();

            scheduler.scheduleJob(jobDetail,trigger);

            SysTask saveSysTask = sysTaskService.addOrUpdate(sysTask);
            if(!scheduler.isShutdown()){
                scheduler.start();
            }
            return saveSysTask;

    }


    public SysTask pauseJob(int  id) throws SchedulerException {
        SysTask sysTask = sysTaskService.findBy(id);

//        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        Scheduler scheduler = schedulerFactory.getScheduler();

        JobKey jobKey = JobKey.jobKey(sysTask.getJobName(), sysTask.getJobGroup());
        scheduler.pauseJob(jobKey);
        sysTask.setScheduleStatus(ScheduleStatus.PAUSE);
        return sysTaskService.addOrUpdate(sysTask);
    }

    public SysTask resumeJob(int  id) throws SchedulerException {
        SysTask sysTask = sysTaskService.findBy(id);
        Scheduler scheduler = schedulerFactory.getScheduler();
        JobKey jobKey = JobKey.jobKey(sysTask.getJobName(), sysTask.getJobGroup());
        scheduler.resumeJob(jobKey);

        sysTask.setScheduleStatus(ScheduleStatus.RUNNING);
        return sysTaskService.addOrUpdate(sysTask);
    }

    public SysTask removeJob(int id) throws SchedulerException {
        SysTask sysTask = sysTaskService.findBy(id);
//        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        Scheduler scheduler = schedulerFactory.getScheduler();
        JobKey jobKey = JobKey.jobKey(sysTask.getJobName(), sysTask.getJobGroup());
        scheduler.deleteJob(jobKey);
        sysTask.setScheduleStatus(ScheduleStatus.NONE);
        return sysTaskService.addOrUpdate(sysTask);
    }

    public SysTask runOnceJobNow(int id) throws SchedulerException {
        SysTask sysTask = sysTaskService.findBy(id);

        Scheduler scheduler = schedulerFactory.getScheduler();

        JobKey jobKey = JobKey.jobKey(sysTask.getJobName(), sysTask.getJobGroup());

        TriggerKey triggerKey = TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup());
        Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
        if(triggerState.equals(Trigger.TriggerState.NONE)){
            throw new MyScheduleException("任务没有启动，不能运行一次");
        }
        scheduler.triggerJob(jobKey);
        return  sysTask;
    }





    private void checkAlreadyStart(Scheduler scheduler,String jobName,String jobGroup) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        TriggerKey triggerKey = TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup());
        Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
        if(triggerState.equals(Trigger.TriggerState.NORMAL)){
            throw new MyScheduleException("任务已经运行！");
        }
    }
}
