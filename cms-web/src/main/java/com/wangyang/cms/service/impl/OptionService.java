package com.wangyang.cms.service.impl;

import com.wangyang.cms.expection.ObjectException;
import com.wangyang.cms.expection.OptionException;
import com.wangyang.cms.pojo.entity.Option;
import com.wangyang.cms.pojo.enums.PropertyEnum;
import com.wangyang.cms.repository.OptionRepository;
import com.wangyang.cms.cache.StringCacheStore;
import com.wangyang.cms.service.IOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
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
    public String getPropertyStringValue(PropertyEnum propertyEnum) {
        String value = getValue(propertyEnum.name());
        if((value==null||"".equals(value))&&!propertyEnum.getNull()){
            throw  new OptionException("系统变量不能为空!!");
        }
        return value;
    }
    @Override
    public Integer getPropertyIntegerValue(PropertyEnum propertyEnum) {
        return Integer.parseInt(getValue(propertyEnum.name()));
    }

//    @Override
//    public <T> T getPropertyValue(String key){
////        return getValue(propertyEnum.name());
//        return clz.cast(getValue(key));
//    }

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
    public List<Option> saveAll(List<Option> options){
        return  optionRepository.saveAll(options);
    }



    @Override
    public Option saveUpdateOption(Option updateOption){
        Option option = optionRepository.findByKey(updateOption.getKey());
        if(option!=null){
            option.setValue(updateOption.getValue());
            option = optionRepository.save(option);
        }else {
            option = optionRepository.saveAndFlush(updateOption);
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
