package com.wangyang.schedule.controller;

import com.wangyang.common.BaseResponse;
import com.wangyang.pojo.entity.SysTask;
import com.wangyang.schedule.util.QuartzUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    @Autowired
    QuartzUtils quartzUtils;

    @GetMapping("/find/{id}")
    public SysTask findBy(@PathVariable("id") Integer id){
        return quartzUtils.findBy(id);
    }

    @GetMapping
    public List<SysTask> list(){
        return quartzUtils.list();
    }

    @PostMapping("/addJob")
    public SysTask addJob(@RequestBody SysTask sysTask) throws Exception {

        SysTask saveSysTask = quartzUtils.addJob(sysTask);
        return saveSysTask;

    }

    @GetMapping("/pauseJob/{id}")
    public SysTask pauseJob(@PathVariable("id") Integer id) throws SchedulerException {

        SysTask updateSysTask = quartzUtils.pauseJob(id);
        return updateSysTask;

    }

    @GetMapping("/resumeJob/{id}")
    public SysTask resumeJob(@PathVariable("id") Integer id) throws SchedulerException {

        SysTask updateSysTask = quartzUtils.resumeJob(id);
        return updateSysTask;

    }

    @GetMapping("/startJob/{id}")
    public SysTask startJob(@PathVariable("id") Integer id) throws Exception {

            SysTask updateSysTask = quartzUtils.startJob(id);
            return updateSysTask;

    }

    @GetMapping("/removeJob/{id}")
    public SysTask removeJob(@PathVariable("id") Integer id) throws SchedulerException {

            SysTask deleteSysTask = quartzUtils.removeJob(id);
            return deleteSysTask;

    }

    @GetMapping("/runOnceJobNow/{id}")
    public BaseResponse runOnceJobNow(@PathVariable("id") Integer id) throws SchedulerException {

        SysTask updateSysTask = quartzUtils.runOnceJobNow(id);
        return BaseResponse.ok(updateSysTask);

    }


    @PostMapping("/updateJobCron/{id}")
    public SysTask updateJobCron(@PathVariable("id") int id,@RequestBody SysTask task) throws Exception {
        SysTask updateSysTask = quartzUtils.updateJobCron(id,task);
        return updateSysTask;
    }

}
