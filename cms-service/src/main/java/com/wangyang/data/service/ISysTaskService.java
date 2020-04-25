package com.wangyang.data.service;

import com.wangyang.model.pojo.entity.SysTask;

import java.util.List;

public interface ISysTaskService {
    SysTask addOrUpdate(SysTask sysTask);

    SysTask findBy(int id);

    SysTask deleteBy(int id);

    List<SysTask> list();
}
