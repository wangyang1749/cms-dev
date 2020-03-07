package com.wangyang.cms.pojo.enums;

public enum  PropertyEnum {
    END_POINT("oss_ali_end_point"),
    ACCESS_KEY("oss_ali_access_key"),
    ACCESS_SECRET("oss_ali_access_secret"),
    BUCKET_NAME("oss_ali_bucket_name"),
    OSS_DOMAIN("oss_ali_domain"),
    OSS_SOURCE("oss_ali_source"),
    OSS_STYLE_RULE("oss_ali_style_rule"),
    OSS_THUMBNAIL_STYLE_RULE("oss_ali_thumbnail_style_rule"),

    ATTACHMENT_TYPE("attachment_type");



    private final String value;
    PropertyEnum(String value){
        this.value=value;
    }
    public String getValue(){
        return value;
    }
}
