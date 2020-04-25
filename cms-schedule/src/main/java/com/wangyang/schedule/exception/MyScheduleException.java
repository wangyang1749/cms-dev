package com.wangyang.schedule.exception;

import org.quartz.SchedulerException;

public class MyScheduleException extends SchedulerException {

    public MyScheduleException(String msg) {
        super(msg);
    }
}
