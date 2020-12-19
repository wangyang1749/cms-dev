package com.wangyang.pojo.vo;

import com.wangyang.pojo.dto.CategoryDto;
import lombok.Data;

import javax.persistence.Column;
import java.util.List;

@Data
public class CategoryVO extends CategoryDto {
    private Boolean haveHtml;
    private Boolean recommend;
    private Boolean existNav;
    private Integer articleNumber;
    private String templateName;
    private String articleTemplateName;

    List<CategoryVO> childCategories;

}
