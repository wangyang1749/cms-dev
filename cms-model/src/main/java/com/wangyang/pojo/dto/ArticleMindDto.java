package com.wangyang.pojo.dto;

import lombok.Data;

@Data
public class ArticleMindDto {
    private Integer id;
    private String title;
    private String viewName;
    private String  path;
    private Integer parentId;
    private Boolean expanded;
    private String direction;
    private Integer order;

    public ArticleMindDto(Integer id, String title, String viewName, String path,  Integer parentId) {
        this.id = id;
        this.title = title;
        this.viewName = viewName;
        this.path = path;
        this.parentId = parentId;
    }

    public ArticleMindDto() {

    }
}
