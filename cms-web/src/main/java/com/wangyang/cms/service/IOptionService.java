package com.wangyang.cms.service;

import com.wangyang.cms.pojo.entity.Option;
import com.wangyang.cms.pojo.enums.PropertyEnum;

import java.util.Collection;
import java.util.List;

public interface IOptionService {
    Option save(Option option);

    String getPropertyValue(PropertyEnum propertyEnum);

    String getValue(String key);

    List<Option> saveUpdateOptionList(Collection<Option> options);

    Option saveUpdateOption(Option updateOption);

    Option findByKey(String key);

    List<Option> list();
}
