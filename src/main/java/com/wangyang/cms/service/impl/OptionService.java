package com.wangyang.cms.service.impl;

import com.wangyang.cms.cache.StringCacheStore;
import com.wangyang.cms.pojo.entity.Option;
import com.wangyang.cms.repository.OptionRepository;
import com.wangyang.cms.service.IOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionService implements IOptionService {

    @Autowired
    OptionRepository optionRepository;

    @Autowired
    StringCacheStore stringCacheStore;

    @Override
    public void save(List<Option> options) {
        optionRepository.saveAll(options);
        options.forEach(option -> {
            stringCacheStore.setValue(option.getKey(),option.getValue());
        });
    }

    @Override
    public String getValue(String key) {
        return stringCacheStore.get(key).orElseGet(()->{
            System.out.println("########  no cache");
            Option option = optionRepository.findByKey(key);
            stringCacheStore.setValue(option.getKey(),option.getValue());
            return option.getValue();
        });
    }
}
