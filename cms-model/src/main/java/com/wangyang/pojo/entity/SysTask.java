package com.wangyang.pojo.entity;

import com.wangyang.pojo.entity.base.BaseEntity;
import com.wangyang.pojo.enums.ScheduleStatus;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity(name = "t_sys_task")
public class SysTask extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String jobName;
    private String description;
    private String cornExpression;
    private String beanClass;
    private String jobGroup;
    private ScheduleStatus scheduleStatus;
    private String methodName;
    private String args;
    private Integer userId;

}
