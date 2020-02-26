package com.wangyang.cms.pojo.entity;

import com.wangyang.cms.pojo.entity.base.BaseEntity;
import com.wangyang.cms.pojo.enums.AttachmentType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
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


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public AttachmentType getType() {
        return type;
    }

    public void setType(AttachmentType type) {
        this.type = type;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }
}
