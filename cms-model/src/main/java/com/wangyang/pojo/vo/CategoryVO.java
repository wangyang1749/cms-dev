package com.wangyang.pojo.vo;

import com.wangyang.pojo.dto.CategoryDto;
import lombok.Data;

import java.util.List;

@Data
public class CategoryVO extends CategoryDto {

    List<CategoryDto> childCategories;
}
