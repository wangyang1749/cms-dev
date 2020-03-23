package com.wangyang.cms.pojo.vo;

import com.wangyang.cms.pojo.dto.CategoryDto;
import com.wangyang.cms.pojo.entity.Category;

import java.util.List;

public class CategoryVO extends CategoryDto {
    private String description;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
