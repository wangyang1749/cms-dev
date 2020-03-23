package com.wangyang.cms.service.impl;

import com.wangyang.cms.expection.ObjectException;
import com.wangyang.cms.pojo.dto.BaseCategoryDto;
import com.wangyang.cms.pojo.entity.base.BaseArticle;
import com.wangyang.cms.pojo.entity.base.BaseCategory;
import com.wangyang.cms.repository.BaseCategoryRepository;
import com.wangyang.cms.service.IBaseCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BaseCategoryServiceImpl<Category extends BaseCategory> implements IBaseCategoryService<Category> {

    @Autowired
    BaseCategoryRepository baseCategoryRepository;


    @Override
    public List<BaseCategoryDto> listBaseCategory(){
        return  baseCategoryRepository.findAll().stream().map(baseCategory -> {
            BaseCategoryDto baseCategoryDto = new BaseCategoryDto();
            BeanUtils.copyProperties(baseCategory,baseCategoryDto);
            return baseCategoryDto;
        }).collect(Collectors.toList());

    }

    @Override
    public Optional<BaseCategory> findBaseCategoryOptionalById(int id){
        return baseCategoryRepository.findById(id);
    }

    @Override
    public BaseCategory findBaseCategoryById(int id){
        Optional<BaseCategory> baseCategoryOptional = findBaseCategoryOptionalById(id);
        if(baseCategoryOptional.isPresent()){
            return baseCategoryOptional.get();
        }
        throw   new ObjectException("BaseCategory没有找到!!!");
    }
}
