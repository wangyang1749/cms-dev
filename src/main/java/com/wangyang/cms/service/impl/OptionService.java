package com.wangyang.cms.service.impl;

import com.wangyang.cms.cache.StringCacheStore;
import com.wangyang.cms.expection.ObjectException;
import com.wangyang.cms.pojo.entity.Option;
import com.wangyang.cms.pojo.enums.PropertyEnum;
import com.wangyang.cms.repository.OptionRepository;
import com.wangyang.cms.service.IOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OptionService implements IOptionService {

    @Autowired
    OptionRepository optionRepository;

    @Autowired
    StringCacheStore stringCacheStore;

    @Override
    public Option save(Option option) {
        return  optionRepository.save(option);
    }


    @Override
    public String getPropertyValue(PropertyEnum propertyEnum){
        return getValue(propertyEnum.getValue());
    }

    @Override
    public String getValue(String key) {
        return stringCacheStore.get(key).orElseGet(()->{
            System.out.println("########  no cache");
            Option option = optionRepository.findByKey(key);
            if (option==null){
                return null;
            }
            stringCacheStore.setValue(option.getKey(),option.getValue());
            return option.getValue();
        });
    }

    @Override
    public List<Option> saveUpdateOptionList(Collection<Option> options){
        return  options.stream().map(this::saveUpdateOption).collect(Collectors.toList());
    }

    @Override
    public Option saveUpdateOption(Option updateOption){
        Option option = optionRepository.findByKey(updateOption.getKey());
        if(option!=null){
            option.setValue(updateOption.getValue());
            option = optionRepository.save(option);
        }else {
            option = optionRepository.save(updateOption);
        }
        stringCacheStore.setValue(option.getKey(),option.getValue());
        return option;
    }

    @Override
    public Option findByKey(String key){
        Option option = optionRepository.findByKey(key);
        if(option==null){
            throw new ObjectException("Option not found!!");
        }
        return option;
    }

    @Override
    public List<Option> list(){
        return optionRepository.findAll();
    }
}
