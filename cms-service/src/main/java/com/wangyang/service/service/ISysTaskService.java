package com.wangyang.service.service;

import com.wangyang.pojo.entity.SysTask;

import java.util.List;

public interface ISysTaskService {
    SysTask addOrUpdate(SysTask sysTask);

    SysTask findBy(int id);

    SysTask deleteBy(int id);

    List<SysTask> list();
}
