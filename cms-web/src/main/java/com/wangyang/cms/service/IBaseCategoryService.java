package com.wangyang.cms.service;

import com.wangyang.cms.pojo.dto.BaseCategoryDto;
import com.wangyang.cms.pojo.entity.base.BaseCategory;

import java.util.List;
import java.util.Optional;

public interface IBaseCategoryService<Category extends BaseCategory> {
    List<BaseCategoryDto> listBaseCategory();

    Optional<BaseCategory> findBaseCategoryOptionalById(int id);

    BaseCategory findBaseCategoryById(int id);
}
