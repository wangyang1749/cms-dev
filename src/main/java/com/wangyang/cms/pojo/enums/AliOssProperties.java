package com.wangyang.cms.pojo.enums;

public enum AliOssProperties {
    END_POINT(""),
    ACCESS_KEY(""),
    BUCKET_NAME(""),
    ACCESS_SECRET("");


    private final String value;
    AliOssProperties(String value){
        this.value=value;
    }
    public String getValue(){
        return value;
    }
}
