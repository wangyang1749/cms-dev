package com.wangyang.service.service;

import com.wangyang.pojo.entity.Option;
import com.wangyang.pojo.enums.PropertyEnum;

import java.util.Collection;
import java.util.List;

public interface IOptionService {
    Option save(Option option);


    String getPropertyStringValue(PropertyEnum propertyEnum);

    Integer getPropertyIntegerValue(PropertyEnum propertyEnum);

//    <T> T getPropertyValue(String key, Class<T> clz);

    String getValue(String key);

    List<Option> saveUpdateOptionList(Collection<Option> options);

    List<Option> saveAll(List<Option> options);

    Option saveUpdateOption(Option updateOption);

    Option findByKey(String key);

    List<Option> list();
}
