package com.wangyang.pojo.dto;

import com.wangyang.pojo.entity.User;
import com.wangyang.pojo.enums.ArticleStatus;
import lombok.Data;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

@Data
public class ArticleDto  implements Serializable {


    private Integer id;
    private Date createDate;
    private Date updateDate;
//    private Integer templateId;
    private String templateName;
    private ArticleStatus status;
    private Integer likes;
    private Integer visits;
    private Integer userId;
    private Integer commentNum;
//    private Boolean haveHtml=false;
    private String summary;
    private String title;
    private String viewName;
    private String  path;
    private String picPath;
    private String pdfPath;
    private String toc;
    private User user;
    private Integer categoryId;

    private String commentTemplateName;
    //是否开启评论
    private Boolean openComment;
    private Integer articleListSize;
    private Boolean isDesc;
    private Integer order;
    // 路径格式
    private String linkPath ;
    private Boolean top;

}
