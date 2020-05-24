package com.wangyang.model.pojo.entity;

import com.wangyang.model.pojo.entity.base.BaseEntity;
import com.wangyang.model.pojo.enums.AttachmentType;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

}
