package com.wangyang.model.pojo.vo;

import com.wangyang.model.pojo.dto.CategoryDto;
import lombok.Data;

import java.util.List;

@Data
public class CategoryVO extends CategoryDto {

    List<CategoryDto> childCategories;
}
