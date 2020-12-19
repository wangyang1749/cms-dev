package com.wangyang.pojo.dto;


import lombok.Data;

@Data
public class CategoryDto {

    private Integer id;
    private String name;
    private Integer parentId;
    private String viewName;
    private String path;
    private String linkPath;
    private Integer order;

}
