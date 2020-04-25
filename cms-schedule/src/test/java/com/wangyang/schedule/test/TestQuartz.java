package com.wangyang.schedule.test;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class TestQuartz {

    public static void main(String[] args) throws SchedulerException, InterruptedException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

//       newT
        Trigger trigger= TriggerBuilder.newTrigger().withIdentity("trigger1", "group1")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(2)
                        .repeatForever()).build();
        JobDetail job = JobBuilder.newJob(HelloJob.class)
                .withIdentity("job1","group1")
                .usingJobData("name","quartz")
                .build();
        scheduler.scheduleJob(job,trigger);
        scheduler.start();
        Thread.sleep(10000);
        scheduler.shutdown();
    }


}

