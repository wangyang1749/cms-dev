package com.wangyang.cms.pojo.vo;

import com.wangyang.cms.pojo.dto.CategoryDto;

import java.util.List;

public class IndexVo {

    private List<CategoryDto> recommend;
    private List<CategoryDto> parent;

    public List<CategoryDto> getRecommend() {
        return recommend;
    }

    public void setRecommend(List<CategoryDto> recommend) {
        this.recommend = recommend;
    }

    public List<CategoryDto> getParent() {
        return parent;
    }

    public void setParent(List<CategoryDto> parent) {
        this.parent = parent;
    }
}
