package com.wangyang.cms.service;

import com.wangyang.cms.pojo.entity.Option;

import java.util.List;

public interface IOptionService {
    void save(List<Option> options);
    String getValue(String key);
}
