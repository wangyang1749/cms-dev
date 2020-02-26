package com.wangyang.cms.pojo.vo;

import com.wangyang.cms.pojo.entity.Category;

import java.util.List;

public class CategoryVO extends Category {
    private List<CategoryVO> children;

    public List<CategoryVO> getChildren() {
        return children;
    }

    public void setChildren(List<CategoryVO> children) {
        this.children = children;
    }
}
