package com.wangyang.model.pojo.vo;

import com.wangyang.model.pojo.dto.SheetDto;
import lombok.Data;

@Data
public class SheetVo extends SheetDto {
    private String cssContent;
    private String jsContent;

}
