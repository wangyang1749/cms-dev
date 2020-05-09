package com.wangyang.common;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CmsConst {

    @Value("${cms.workDir}")
    private void setWorkDir(String workDir) {
        this.WORK_DIR = workDir;
    }
    public static  String WORK_DIR;


    public static final String SYSTEM_TEMPLATE_PATH="templates";
    public static final String SYSTEM_HTML_PATH="html";
    public static final String TEMPLATE_PATH="templates";
    public static final String STATIC_HTML_PATH="html";
    public static final String CONFIGURATION="application.properties";
    public final static String UPLOAD_SUB_DIR = "upload/";
    public final static String INIT_STATUS = "INIT_STATUS";
    public final static String COMPONENTS_PATH = "components";
    public final static String CATEGORY_LIST_PATH = "articleList";
    public final static String ARTICLE_DETAIL_PATH = "article";



    public final static String DEFAULT_ARTICLE_TEMPLATE = "DEFAULT_ARTICLE";
    public final static String DEFAULT_ARTICLE_CHANNEL_TEMPLATE = "DEFAULT_ARTICLE_CHANNEL";
    public final static String DEFAULT_ARTICLE_PICTURE_TEMPLATE = "DEFAULT_ARTICLE_PICTURE";

    public final static String DEFAULT_CHANNEL_TEMPLATE = "DEFAULT_CHANNEL";//分类下的文章列表
    public final static String DEFAULT_PICTURE_TEMPLATE = "DEFAULT_PICTURE";
    public final static String DEFAULT_CATEGORY_TEMPLATE = "DEFAULT_CATEGORY";
    public final static String DEFAULT_REVEAL_TEMPLATE = "DEFAULT_REVEAL";

    public final static String DEFAULT_SHEET_TEMPLATE = "DEFAULT_SHEET";
    public final static String DEFAULT_COMMENT_TEMPLATE = "DEFAULT_COMMENT";//评论

    // 依据模板类型分类列表
    public final static String DEFAULT_CATEGORY_LIST = "DEFAULT_CATEGORY_LIST";//分类列表



    public final static String ARTICLE_LIST = "ARTICLE_LIST";//分类列表
    public final static String ARTICLE_PAGE = "ARTICLE_PAGE";//分类列表

    public final static String TAGS_INFORMATION = "资讯";//分类列表
    public final static String TAGS_RECOMMEND = "推荐";//分类列表


    public static final String MARKDOWN_REVEAL_START = "<p>@=";
    public static final String MARKDOWN_REVEAL_END = "=@</p>";
    public static final String LABEL_SECTION_START = "<section>";
    public static final String LABEL_SECTION_END = "</section>";
}
