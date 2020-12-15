package com.wangyang.pojo.params;

import com.wangyang.pojo.enums.ArticleStatus;
import lombok.Data;

@Data
public class SheetParam {

    private ArticleStatus status ;//=ArticleStatus.PUBLISHED;
    private int userId;
    private String title;
    private String viewName;
    private String originalContent;
    private String formatContent;
    private String path;
    private Integer channelId;
    private String templateName;
    private String cssContent;
    private String jsContent;

}
