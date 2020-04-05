package com.wangyang.cms.service.impl;

import com.wangyang.cms.pojo.entity.Discuss;
import com.wangyang.cms.repository.DiscussRepository;
import com.wangyang.cms.service.IDiscussService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DiscussServiceImpl implements IDiscussService {

    @Autowired
    DiscussRepository discussRepository;


    public Discuss add(Discuss discuss){
        return discussRepository.save(discuss);
    }

    public Page<Discuss> listAll(Pageable pageable){
        return discussRepository.findAll(pageable);
    }


}
