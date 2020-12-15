package com.wangyang.pojo.enums;

public enum  PropertyEnum {
    END_POINT("oss_ali_end_point","","END_POINT",0,false),
    END_POINT_PUBLIC("oss_ali_end_point_public","","END_POINT_PUBLIC",0,false),
    ACCESS_KEY("oss_ali_access_key","","ACCESS_KEY",0,false),
    ACCESS_SECRET("oss_ali_access_secret","","ACCESS_SECRET",0,false),
    BUCKET_NAME("oss_ali_bucket_name","","BUCKET_NAME",0,false),
    OSS_DOMAIN("oss_ali_domain","","OSS_DOMAIN",0,false),
    OSS_SOURCE("oss_ali_source","","OSS_SOURCE",0,false),
    OSS_STYLE_RULE("oss_ali_style_rule","","OSS_STYLE_RULE",0,true),
    OSS_THUMBNAIL_STYLE_RULE("oss_ali_thumbnail_style_rule","","OSS_THUMBNAIL_STYLE_RULE",0,true),
    ATTACHMENT_TYPE("attachment_type","","上传的附件类型",0,false),
//    DEFAULT_ARTICLE_TEMPLATE_ID("default_article_template_id","","DEFAULT_ARTICLE_TEMPLATE_ID",0),
    CATEGORY_PAGE_SIZE("category_page_size","10","分类的页面大小",1,false),
    CMS_TOKEN("cms_token","123456","API访问",1,false),
    ADMIN_ID("admin_id","1","管理员ID",1,false);


    private final String value;
    private final String defaultValue;
    private final String name;
    private Integer groupId;
    private Boolean isNull;
    PropertyEnum(String value,String defaultValue,String name,Integer groupId,Boolean isNull){
        this.value=value;
        this.defaultValue=defaultValue;
        this.name =name;
        this.groupId = groupId;
        this.isNull=isNull;

    }
    public String getValue(){
        return value;
    }

    public Boolean getNull() {
        return isNull;
    }

    public String getName(){
        return name;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public String getDefaultValue(){
        return defaultValue;
    }

//    public  <T> T getObj() {
//
////        try {
////            T obj = clz.newInstance();
////        } catch (InstantiationException e) {
////            e.printStackTrace();
////        } catch (IllegalAccessException e) {
////            e.printStackTrace();
////        }
//
//        return (T)defaultValue;
//    }
}
