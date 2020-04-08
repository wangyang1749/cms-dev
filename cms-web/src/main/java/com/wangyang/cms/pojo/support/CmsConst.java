package com.wangyang.cms.pojo.support;

import com.wangyang.cms.pojo.enums.PropertyEnum;
import com.wangyang.cms.service.IOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

@Component
public class CmsConst {



    @Value("${cms.workDir}")
    private void setWorkDir(String workDir) {
        this.WORK_DIR = workDir;
    }
    public static String WORK_DIR;




    public static final String SYSTEM_TEMPLATE_PATH="templates";
    public static final String SYSTEM_HTML_PATH="html";
    public static final String TEMPLATE_PATH="templates";
    public static final String STATIC_HTML_PATH="html";
    public static final String CONFIGURATION="application.properties";
    public final static String UPLOAD_SUB_DIR = "upload/";
    public final static String INIT_STATUS = "INIT_STATUS";

    public final static String DEFAULT_ARTICLE_TEMPLATE = "DEFAULT_ARTICLE";
    public final static String DEFAULT_ARTICLE_CHANNEL_TEMPLATE = "DEFAULT_ARTICLE_CHANNEL";
    public final static String DEFAULT_PICTURE_TEMPLATE = "DEFAULT_PICTURE_TEMPLATE";
    public final static String DEFAULT_CHANNEL_TEMPLATE = "DEFAULT_CHANNEL_LIST";
    public final static String DEFAULT_PICTURE_LIST_TEMPLATE = "DEFAULT_PICTURE_LIST_TEMPLATE";
    public final static String DEFAULT_CATEGORY_TEMPLATE = "DEFAULT_CATEGORY";
    public final static String DEFAULT_SHEET_TEMPLATE = "DEFAULT_SHEET";
    public final static String DEFAULT_COMMENT_TEMPLATE = "DEFAULT_COMMENT";
    public final static String DEFAULT_CATEGORY_LIST = "DEFAULT_CATEGORY_LIST";


    public static final String MARKDOWN_REVEAL_START = "<p>@=";
    public static final String MARKDOWN_REVEAL_END = "=@</p>";
    public static final String LABEL_SECTION_START = "<section>";
    public static final String LABEL_SECTION_END = "</section>";
}
