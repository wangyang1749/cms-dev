package com.wangyang.pojo.enums;

public enum  ArticleStatus implements ValueEnum<Integer> {

    /**
     * Published status.
     */
    PUBLISHED(0),

    /**
     * Draft status.
     */
    DRAFT(1),

    /**
     * Recycle status.
     */
    RECYCLE(2),

    /**
     * Intimate status
     */
    INTIMATE(3),

    MODIFY(4); //修改的


    private final int value;

    ArticleStatus(int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }


}
