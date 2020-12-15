package com.wangyang.pojo.entity;

import com.wangyang.pojo.entity.base.BaseEntity;
import com.wangyang.pojo.enums.AttachmentType;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Attachment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String path;
    private String fileKey;
    private String suffix;
    private Long size;
    private AttachmentType type;
    private String mediaType;
    private Integer width;
    private Integer height;
    private String thumbPath;
    private String latex;
    @Column( columnDefinition = "longtext")
    private String formatContent;
    @Column( columnDefinition = "longtext")
    private String originContent;
    private String renderType;
}
