package com.wangyang.cms.service;

import com.wangyang.cms.pojo.entity.Option;

import java.util.List;

public interface IOptionService {
    Option save(Option option);
    void save(List<Option> options);
    String getValue(String key);
}
