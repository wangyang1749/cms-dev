package com.wangyang.service.service.impl;

import com.wangyang.common.exception.ObjectException;
import com.wangyang.service.repository.SysTaskRepository;
import com.wangyang.service.service.ISysTaskService;
import com.wangyang.pojo.entity.SysTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SysTaskServiceImpl implements ISysTaskService {

    @Autowired
    SysTaskRepository sysTaskRepository;

    @Override
    public SysTask addOrUpdate(SysTask sysTask){
        return sysTaskRepository.save(sysTask);
    }

    @Override
    public SysTask findBy(int id){
        Optional<SysTask> sysTaskOptional = sysTaskRepository.findById(id);
        if(!sysTaskOptional.isPresent()){
            throw new ObjectException("SysTask is not found!!");
        }
        return sysTaskOptional.get();
    }


    @Override
    public SysTask deleteBy(int id){
        SysTask sysTask = findBy(id);
        sysTaskRepository.deleteById(sysTask.getId());
        return sysTask;
    }

    @Override
    public List<SysTask> list(){
        return sysTaskRepository.findAll();
    }
}
