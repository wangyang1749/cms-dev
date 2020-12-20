package com.wangyang.pojo.entity;

import com.wangyang.pojo.entity.base.BaseArticle;
import lombok.Data;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = "0")
@Data
public class Article extends BaseArticle {


    @Column(name = "likes", columnDefinition = "int default 0")
    private Integer likes=0;
    @Column(name = "visits", columnDefinition = "int default 0")
    private Integer visits=0;
    @Column(name = "comment_num", columnDefinition = "int default 0")
    private Integer commentNum=0;
//    private Boolean haveHtml=true;
    private String summary;
    private String picPath;
    private String pdfPath;
    private Integer categoryId;
    @Column(name = "article_order", columnDefinition = "int default 0")
    private Integer order;
    @Column(name = "article_top",columnDefinition = "bit(1) default false")
    private Boolean top;
    private Integer parentId; // 父亲id 0是只没有父亲 此时指向category
    private Boolean expanded; // 节点是否展开
    private String direction; //节点的方向
}
