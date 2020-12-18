package com.wangyang.common;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class CmsConst {

    @Value("${cms.workDir}")
    private void setWorkDir(String workDir) {
        this.WORK_DIR = workDir;
    }
    public static  String WORK_DIR;

    @Value("${cms.isDebug}")
    private  void setsDebug(Boolean isDebug) {
        this.IS_DEBUG = isDebug;
    }
    public static  Boolean IS_DEBUG ;

    public static final String SYSTEM_TEMPLATE_PATH="templates";
    public static final String SYSTEM_HTML_PATH="html";
    public static final String TEMPLATE_PATH="templates";
    public static final String STATIC_HTML_PATH="html";
    public static final String CONFIGURATION="application.properties";
    public final static String UPLOAD_SUB_DIR = "upload/";
    public final static String INIT_STATUS = "INIT_STATUS";
    public final static String COMPONENTS_PATH = "html"+ File.separator+"components";
    public final static String CATEGORY_MENU = "categoryMenu";

    public final static String CATEGORY_LIST_PATH = "html"+File.separator+"articleList";
    public final static String ARTICLE_DETAIL_PATH = "html"+File.separator+"article";
    public final static String SHEET_PATH = "html"+File.separator+"sheet";


    public final static String DEFAULT_ARTICLE_PDF_TEMPLATE = "DEFAULT_ARTICLE_PDF_TEMPLATE";
//    public final static String DEFAULT_ARTICLE_PREVIEW_TEMPLATE = "DEFAULT_ARTICLE_PREVIEW_TEMPLATE";


    public final static String DEFAULT_ARTICLE_TEMPLATE = "DEFAULT_ARTICLE";
    public final static String DEFAULT_ARTICLE_CHANNEL_TEMPLATE = "DEFAULT_ARTICLE_CHANNEL";
    public final static String DEFAULT_ARTICLE_PICTURE_TEMPLATE = "DEFAULT_ARTICLE_PICTURE";

    public final static String DEFAULT_CHANNEL_TEMPLATE = "DEFAULT_CHANNEL";//分类下的文章列表
    public final static String DEFAULT_PICTURE_TEMPLATE = "DEFAULT_PICTURE";
    public final static String DEFAULT_CATEGORY_TEMPLATE = "DEFAULT_CATEGORY";
    public final static String DEFAULT_REVEAL_TEMPLATE = "DEFAULT_REVEAL";

    public final static String DEFAULT_SHEET_TEMPLATE = "DEFAULT_SHEET";
    public final static String CUSTOM_SHEET_TEMPLATE = "CUSTOM_SHEET";
    public final static String DEFAULT_COMMENT_TEMPLATE = "DEFAULT_COMMENT";//评论

    // 依据模板类型分类列表
    public final static String DEFAULT_CATEGORY_LIST = "DEFAULT_CATEGORY_LIST";//分类列表



    public final static String ARTICLE_LIST = "ARTICLE_LIST";//分类列表
    public final static String ARTICLE_RECOMMEND_LIST = "ARTICLE_RECOMMEND_LIST";//分类列表
    public final static String ARTICLE_TOP_LIST = "ARTICLE_RECOMMEND_LIST";//分类列表
    public final static String ARTICLE_PAGE = "ARTICLE_PAGE";//分类列表


    public final static String ARTICLE_JS_MIND = "ARTICLE_JS_MIND";//文章思维导图

    public final static String TAGS_INFORMATION = "资讯";//分类列表
    public final static String TAGS_RECOMMEND = "推荐";//分类列表


    public final static String ARTICLE_DATA = "@Article";//文章的Components标识
    public final static String ARTICLE_DATA_SORT = "@SortArticle:";//文章的Components标识
    public final static String ARTICLE_DATA_KEYWORD = "@KeywordArticle:";//文章的Components标识
    public final static String ARTICLE_DATA_TAGS = "@TagsArticle:";//文章的Components标识


    public static final String MARKDOWN_REVEAL_START = "<p>@=";
    public static final String MARKDOWN_REVEAL_END = "=@</p>";
    public static final String LABEL_SECTION_START = "<section>";
    public static final String LABEL_SECTION_END = "</section>";
}
