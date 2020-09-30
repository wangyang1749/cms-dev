package com.wangyang.model.pojo.params;

import com.wangyang.model.pojo.enums.TemplateType;
import lombok.Data;



@Data
public class TemplateParam {
    private TemplateType templateType;
    private String name;
    private String enName;
    private String description;
    private Boolean status;
    private String templateValue;
    private Integer order;
    private Boolean isSystem;
}
