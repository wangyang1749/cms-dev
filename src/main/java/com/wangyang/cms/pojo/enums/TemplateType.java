package com.wangyang.cms.pojo.enums;

public enum  TemplateType {
    ARTICLE(0),
    CATEGORY(1);

    private int value;
    TemplateType(int value){

    }
    public Integer getValue() {
        return value;
    }
}
