package com.wangyang.cms.pojo.enums;

public enum  TemplateType {
    ARTICLE(0),
    CATEGORY_INFO(1),
    SHEET(2);
    private final int value;

    TemplateType(int value) {
        this.value = value;
    }
}
