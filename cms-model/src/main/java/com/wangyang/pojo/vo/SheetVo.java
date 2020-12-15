package com.wangyang.pojo.vo;

import com.wangyang.pojo.dto.SheetDto;
import lombok.Data;

@Data
public class SheetVo extends SheetDto {
    private String cssContent;
    private String jsContent;

}
