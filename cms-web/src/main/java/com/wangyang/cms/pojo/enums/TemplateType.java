package com.wangyang.cms.pojo.enums;

public enum  TemplateType implements ValueEnum<Integer>  {
    ARTICLE(0), //文章模板
    CATEGORY(1), //分类模板
    SHEET(2), //单页模板
    CHANNEL(3), //栏目模板
    ARTICLE_CHANNEL(4),// 文章栏目模板
    COMMENT(5),
    CATEGORY_LIST(6);//评论模板

    private final int value;
    TemplateType(int value) {
        this.value = value;
    }


    @Override
    public Integer getValue() {
        return value;
    }
}
